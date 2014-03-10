package com.recommender.mahout;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemBasedRecommenderIntro {

	private DataModel dataModel;
	private ItemSimilarity itemSimilarity;
	private static final Logger LOG = LoggerFactory
			.getLogger(ItemBasedRecommenderIntro.class);

	public ItemBasedRecommenderIntro() {

	}

	public List<RecommendedItem> getRecommendation(long userId,
			int maxRecommendations) throws TasteException {

		long start = System.currentTimeMillis();

		Recommender recommender = new GenericItemBasedRecommender(dataModel,
				itemSimilarity);
		List<RecommendedItem> recommendedItems = recommender.recommend(userId,
				maxRecommendations);

		long stop = System.currentTimeMillis();
		LOG.info("[ Took:" + (stop - start) + " millis ]");
		LOG.info("[Recommendations for User #" + 2 + " Total Recom(s) #"
				+ recommendedItems.size() + "]");
		return recommendedItems;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public void setItemSimilarity(ItemSimilarity itemSimilarity) {
		this.itemSimilarity = itemSimilarity;
	}

}
