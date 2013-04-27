package me.twocoffee.common;

import java.util.List;

import javax.annotation.PostConstruct;

import com.google.code.morphia.DatastoreImpl;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class MongoDataStore extends DatastoreImpl {
	private int connectionsPerHost;
	private List<String> modelPackages;
	private Morphia morphia = null;
	private int threadsAllowedToBlockForConnectionMultiplier;

	public MongoDataStore(Morphia morphia, Mongo mongo) {
		super(morphia, mongo);
		this.morphia = morphia;
	}

	public MongoDataStore(Morphia morphia, Mongo mongo, String dbName) {
		super(morphia, mongo, dbName);
		this.morphia = morphia;
	}

	public MongoDataStore(Morphia morphia, Mongo mongo, String dbName,
			String username, char[] password) {
		super(morphia, mongo, dbName, username, password);
		this.morphia = morphia;
	}

	public int getConnectionsPerHost() {
		return connectionsPerHost;
	}

	public int getThreadsAllowedToBlockForConnectionMultiplier() {
		return threadsAllowedToBlockForConnectionMultiplier;
	}
	
	public void init() {
		this.mongo.getMongoOptions().autoConnectRetry = true;
		this.mongo.getMongoOptions().connectionsPerHost = getConnectionsPerHost();
		this.mongo.getMongoOptions().threadsAllowedToBlockForConnectionMultiplier = getThreadsAllowedToBlockForConnectionMultiplier();
		if (this.modelPackages != null && !this.modelPackages.isEmpty()) {
			for (String p : modelPackages) {
				this.morphia.mapPackage(p);
			}
		}

		this.ensureIndexes();
		this.ensureCaps();
	}

	public void setConnectionsPerHost(int connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
	}

	public void setModelPackages(List<String> mp) {
		this.modelPackages = mp;
	}

	public void setThreadsAllowedToBlockForConnectionMultiplier(
			int threadsAllowedToBlockForConnectionMultiplier) {
		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
	}
}
