package com.recommender.mahout;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBasedRecommenderIntro {

	private DataModel dataModel;
	private UserSimilarity userSimilarity;
	private static final Logger LOG = LoggerFactory
			.getLogger(UserBasedRecommenderIntro.class);

	public UserBasedRecommenderIntro() {
		
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



	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public void setUserSimilarity(UserSimilarity userSimilarity) {
		this.userSimilarity = userSimilarity;
	}
	
	
}
