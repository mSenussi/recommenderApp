package com.recommender.mahout;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBasedRecommender {

	private DataModel dataModel;
	private UserSimilarity userSimilarity;
	private static final Logger LOG = LoggerFactory
			.getLogger(UserBasedRecommender.class);

	public UserBasedRecommender(File userPreferencesFile) {
		try {
			this.dataModel = new FileDataModel(userPreferencesFile);
			this.userSimilarity = new PearsonCorrelationSimilarity(dataModel);
		} catch (TasteException e) {
			LOG.error(e.getMessage() + "\n" + e.getStackTrace());
		} catch (IOException e) {
			LOG.error(e.getMessage() + "\n" + e.getStackTrace());
		}
	}

	public List<RecommendedItem> getRecommendation(long userId,
			int maxRecommendations) throws TasteException {

		long start = System.currentTimeMillis();
		UserNeighborhood neighbourhood = new NearestNUserNeighborhood(2,
				userSimilarity, dataModel);

		Recommender recommender = new GenericUserBasedRecommender(dataModel,
				neighbourhood, userSimilarity);
		List<RecommendedItem> recommendedItems = recommender.recommend(userId,
				maxRecommendations);
		long stop = System.currentTimeMillis();
		LOG.info("[ Took:" + (stop - start) + " millis ]");
		return recommendedItems;
	}
}
