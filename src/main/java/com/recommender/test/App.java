package com.recommender.test;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.example.grouplens.GroupLensDataModel;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recommender.mahout.ItemBasedRecommenderIntro;
import com.recommender.mahout.UserBasedRecommenderIntro;

/**
 * Hello world!
 * 
 */
public class App {

	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		final int UID = 1, MAX_RECOMMENDATIONS = 3;
		List<RecommendedItem> recommendations = null;
		File userPreferencesFile;
		try {

			userPreferencesFile = new File("src/main/resources/data/gl/ratings.dat");

			DataModel dataModel = new GroupLensDataModel(userPreferencesFile);

			// ----------------- user-based ----------------------//
			UserBasedRecommenderIntro userBasedRecommender = new UserBasedRecommenderIntro();
			userBasedRecommender.setDataModel(dataModel);
			userBasedRecommender
					.setUserSimilarity(new PearsonCorrelationSimilarity(
							dataModel));

			recommendations = userBasedRecommender.getRecommendation(UID, MAX_RECOMMENDATIONS);
			LOG.info("[Recommendations for User #" + UID + " Total Recom(s) #"
					+ recommendations.size() + "]");
			for (RecommendedItem recommendation : recommendations) {
				System.out.println(recommendation);
			}
			// ----------------- item-based ----------------------//
			ItemBasedRecommenderIntro itemBasedRecommenderIntro = new ItemBasedRecommenderIntro();
			itemBasedRecommenderIntro.setDataModel(dataModel);
			itemBasedRecommenderIntro.setItemSimilarity(new PearsonCorrelationSimilarity(dataModel));
			
			recommendations = itemBasedRecommenderIntro.getRecommendation(UID, MAX_RECOMMENDATIONS);

			LOG.info("[Recommendations for User #" + UID + " Total Recom(s) #"
					+ recommendations.size() + "]");
			for (RecommendedItem recommendation : recommendations) {
				System.out.println(recommendation);
			}

		} catch (Exception e) {
			LOG.error(e.getMessage() + "\n--------\n" + e.getStackTrace());
		}

	}
}
