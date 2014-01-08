package com.recommender.mahout;

import java.io.File;
import java.text.DecimalFormat;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.example.grouplens.GroupLensDataModel;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluatorInto {

	private static final Logger LOG = LoggerFactory
			.getLogger(EvaluatorInto.class);

	/** ---------------- Eval Methods ----------- **/
	public static double evalAvarage(DataModel model,
			double trainingPercentage, double evalPercentage)
			throws TasteException {

		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();

		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {

				// UserSimilarity userSimilarity = new
				// PearsonCorrelationSimilarity(dataModel, Weighting.WEIGHTED);
				// UserSimilarity userSimilarity = new
				// EuclideanDistanceSimilarity(dataModel);
				// UserSimilarity userSimilarity = new CachingUserSimilarity(
				// new SpearmanCorrelationSimilarity(dataModel), dataModel);
				// UserSimilarity userSimilarity = new
				// TanimotoCoefficientSimilarity(dataModel);
				UserSimilarity userSimilarity = new LogLikelihoodSimilarity(
						dataModel);

				UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(
						2, userSimilarity, dataModel);

				return new GenericUserBasedRecommender(dataModel,
						userNeighborhood, userSimilarity);
			}
		};
		long start = System.currentTimeMillis();
		double score = evaluator.evaluate(recommenderBuilder, null, model,
				trainingPercentage, evalPercentage);
		long stop = System.currentTimeMillis();
		LOG.info(" Took: [" + (stop - start) + " millis | " + (stop - start)
				/ 1000 + " Sec ]");
		return score;
	}

	public static double evalRMS(DataModel model, double trainingPercentage,
			double evalPercentage) throws TasteException {

		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(
						dataModel);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2,
						similarity, dataModel);

				return new GenericUserBasedRecommender(dataModel, neighborhood,
						similarity);
			}
		};

		long start = System.currentTimeMillis();
		double score = evaluator.evaluate(recommenderBuilder, null, model,
				trainingPercentage, evalPercentage);
		long stop = System.currentTimeMillis();
		LOG.info(" Took: [" + (stop - start) + " millis | " + (stop - start)
				/ 1000 + " Sec ]");
		return score;
	}

	public static IRStatistics evalIRStats(DataModel model, int at,
			double evalPercentage) throws TasteException {

		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(
						dataModel);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2,
						similarity, dataModel);
				return new GenericUserBasedRecommender(dataModel, neighborhood,
						similarity);
			}
		};
		long start = System.currentTimeMillis();
		IRStatistics irStatistics = evaluator.evaluate(recommenderBuilder,
				null, model, null, at,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,
				evalPercentage);
		long stop = System.currentTimeMillis();
		LOG.info(" Took: [" + (stop - start) + " millis | " + (stop - start)
				/ 1000 + " Sec ]");
		return irStatistics;
	}

	public static double evalRMSWithThresholdUserNeighborhood(DataModel model,
			double trainingPercentage, double evalPercentage,
			final double threshold) throws TasteException {

		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(
						dataModel);
				ThresholdUserNeighborhood neighborhood = new ThresholdUserNeighborhood(
						0.9, similarity, dataModel);

				return new GenericUserBasedRecommender(dataModel, neighborhood,
						similarity);
			}
		};

		long start = System.currentTimeMillis();
		double score = evaluator.evaluate(recommenderBuilder, null, model,
				trainingPercentage, evalPercentage);
		long stop = System.currentTimeMillis();
		LOG.info(" Took: [" + (stop - start) + " millis | " + (stop - start)
				/ 1000 + " Sec ]");
		return score;
	}

	
	/** ----------------END: Eval Methods ----------- **/

	public static void main(String[] args) {

		File userPreferencesFile, testFile, groupLensFileK, groupLensFileM;
		try {

			userPreferencesFile = new File(
					"src/main/resources/data/dataset.txt");
			testFile = new File("src/main/resources/data/introDataset.txt");
			groupLensFileK = new File("src/main/resources/data/gl/ua.base");
			groupLensFileM = new File("src/main/resources/data/gl/ratings.dat");

			DataModel model = new FileDataModel(userPreferencesFile);
			DataModel testModel = new FileDataModel(testFile);
			DataModel gLModelK = new FileDataModel(groupLensFileK);
			DataModel gLModelM = new GroupLensDataModel(groupLensFileM);

			double avarEvaScore = evalAvarage(model, 0.8, 0.3);
			// double rMSEvaScore = evalRMS(model, 0.8, 0.3);
			System.out.println("AverageAbsoluteDifference: "
					+ new DecimalFormat("##.##").format(avarEvaScore));
			// System.out.println("RMSDifference: " + new
			// DecimalFormat("##.##").format(rMSEvaScore));

			// IRStatistics iRStats = evalIRStats(gLModelK, 5, 0.02);
			//
			// System.out.println(iRStats.getPrecision());
			// System.out.println(iRStats.getRecall());

			// double rMSEvaScoreWithThresholdUserNeighborhood =
			// evalRMSWithThresholdUserNeighborhood(gLModelM, 0.95, 0.05, 0.7);
			// System.out.println("rMSEvaScoreWithThresholdUserNeighborhood: " +
			// new
			// DecimalFormat("##.##").format(rMSEvaScoreWithThresholdUserNeighborhood));

		} catch (Exception e) {
			LOG.error(e.getMessage() + "\n--------\n" + e);
		}

	}

}
