package com.recommender.mahout;

import java.io.File;
import java.text.DecimalFormat;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluatorInto {

	private static final Logger LOG = LoggerFactory
			.getLogger(EvaluatorInto.class);

	
	
	
	public static double evalAvarage(DataModel model, double trainingPercentage, double evalPercentage) throws TasteException {
		
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		
		RecommenderBuilder recommenderBuilder = new  RecommenderBuilder() {
			
			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {

				UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
				UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2, userSimilarity, dataModel);
				
				
				return new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
			}
		};
		long start = System.currentTimeMillis();
		double score = evaluator.evaluate(recommenderBuilder, null, model, trainingPercentage, evalPercentage);
		long stop = System.currentTimeMillis();
		LOG.info(" Took: [" + (stop - start) + " millis | "+(stop - start)/1000+" Sec ]");
		return score;
	}
	
    public static double evalRMS(DataModel model, double trainingPercentage, double evalPercentage) throws TasteException{
    	
    	RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
    	RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
			
			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
				
				return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
			}
		};
    	
		long start = System.currentTimeMillis();
		double score = evaluator.evaluate(recommenderBuilder, null, model, trainingPercentage, evalPercentage);
		long stop = System.currentTimeMillis();
		LOG.info(" Took: [" + (stop - start) + " millis | "+(stop - start)/1000+" Sec ]");
		return score;
	}
	
	
    public static IRStatistics evalIRStats(DataModel model) throws TasteException {
    	
    	RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
    	RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
			
			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
				return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
			}
		};
		long start = System.currentTimeMillis();
		IRStatistics irStatistics = evaluator.evaluate(recommenderBuilder, null, model, null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.2);
		long stop = System.currentTimeMillis();
		LOG.info(" Took: [" + (stop - start) + " millis | "+(stop - start)/1000+" Sec ]");
		return irStatistics;
	}
    
    public static void main(String[] args) {

		File userPreferencesFile;
		try {

			userPreferencesFile = new File(
					"src/main/resources/data/dataset.txt");

			DataModel model =  new FileDataModel(userPreferencesFile);
			
//			double avarEvaScore = evalAvarage(model, 0.8, 0.3);
//			double rMSEvaScore = evalRMS(model, 0.8, 0.3);
//			System.out.println("AverageAbsoluteDifference: " + new DecimalFormat("##.##").format(avarEvaScore));
//		    System.out.println("RMSDifference: " + new DecimalFormat("##.##").format(rMSEvaScore));
		    
			IRStatistics iRStats = evalIRStats(model);
			System.out.println(new DecimalFormat("##.##").format(iRStats.getRecall()));

		} catch (Exception e) {
			LOG.error(e.getMessage() + "\n--------\n" + e.getStackTrace());
		}

	}

}
