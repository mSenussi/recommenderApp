package com.recommender.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DB {

	private Connection connection;

	private static final String DB = "recommender";
	private static final String USER = "mahout";
	private static final String PASS = "mahout";
	private static final Logger LOG = LoggerFactory.getLogger(DB.class);

	public DB() {
		connection = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			LOG.error("Error: Registering Driver to DB :" + e.getMessage()
					+ "\n" + e.getStackTrace());
		}
	}

	public Connection connect() {
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/" + DB, USER, PASS);
		} catch (SQLException e) {
			LOG.error("Error: connecting to DB :" + e.getMessage() + "\n"
					+ e.getStackTrace());

		}
		return connection;
	}

}
