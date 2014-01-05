package com.recommender.mahout;

import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public class RecommenderRepresentaionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		FastByIDMap<PreferenceArray> prefs = new FastByIDMap<PreferenceArray>();

		PreferenceArray userPref = new GenericUserPreferenceArray(2);
		userPref.setUserID(0, 1L);
		userPref.setItemID(0, 101L);
		userPref.setValue(0, 3.0f);

		userPref.setItemID(1, 102L);
		userPref.setValue(1, 1.3f);

		prefs.put(0, userPref);

		userPref = new GenericUserPreferenceArray(3);
		userPref.setUserID(1, 2L);
		userPref.setItemID(0, 101L);
		userPref.setValue(0, 1.0f);

		userPref.setItemID(1, 105L);
		userPref.setValue(1, 3.3f);

		userPref.setItemID(2, 102L);
		userPref.setValue(2, 3.3f);

		prefs.put(1, userPref);

		DataModel model = new GenericDataModel(prefs);

		System.out.println(prefs);

	}

}
