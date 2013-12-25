package com.recommender.mahout;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluatorInto {

	private static final Logger LOG = LoggerFactory
			.getLogger(EvaluatorInto.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		File userPreferencesFile;
		try {

			userPreferencesFile = new File(
					"src/main/resources/data/dataset.txt");

			DataModel model =  new FileDataModel(userPreferencesFile);
			RecommenderEvaluator evaluator =
				      new AverageAbsoluteDifferenceRecommenderEvaluator();
			
			RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
				@Override
				public Recommender buildRecommender(DataModel model)
						throws TasteException {
					
					UserSimilarity similarity = new PearsonCorrelationSimilarity (model);
					 UserNeighborhood neighborhood =
					 new NearestNUserNeighborhood (2, similarity, model);
					 
					 return  new GenericUserBasedRecommender (model, neighborhood, similarity);
					
				}
			};
						
			long start = System.currentTimeMillis();
			// Use 70% of the data to train; test using the other 30%.
		    double score = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 0.2);
		    long stop = System.currentTimeMillis();
		    System.out.println(score);
		    LOG.info(" Took: [" + (stop - start) + " millis | "+(stop - start)/1000+" Sec ]");

		} catch (Exception e) {
			LOG.error(e.getMessage() + "\n--------\n" + e.getStackTrace());
		}

	}

}
