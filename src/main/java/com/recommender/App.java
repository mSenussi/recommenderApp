package com.recommender;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recommender.mahout.UserBasedRecommender;

/**
 * Hello world!
 * 
 */
public class App {

	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		File userPreferencesFile;
		try {

			userPreferencesFile = new File(
					"src/main/resources/data/dataset.txt");

			// Utils.generateUserDataPrefFile(userPreferencesFile);

			UserBasedRecommender userBasedRecommender = new UserBasedRecommender(
					userPreferencesFile);
			int uId = 1;
			List<RecommendedItem> recommendations = userBasedRecommender
					.getRecommendation(uId, 3);
			LOG.info("[Recommendations for User #" + uId
					+ " Total Recom(s) #"+recommendations.size()+"]");
			for (RecommendedItem recommendation : recommendations) {
				System.out.println(recommendation);
			}

		} catch (Exception e) {
			LOG.error(e.getMessage() + "\n--------\n" + e.getStackTrace());
		}

	}
}
