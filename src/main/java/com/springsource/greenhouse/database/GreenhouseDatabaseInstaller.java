package com.springsource.greenhouse.database;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.versioned.DatabaseChangeSetBuilder;
import org.springframework.jdbc.versioned.DatabaseVersion;
import org.springframework.jdbc.versioned.GenericDatabaseUpgrader;
import org.springframework.jdbc.versioned.DatabaseUpgrader;

public class GreenhouseDatabaseInstaller {
	
	private DatabaseUpgrader upgrader;

	private DatabaseVersion installVersion = DatabaseVersion.valueOf("1");
	
	@Autowired
	public GreenhouseDatabaseInstaller(DataSource dataSource) {		
		this.upgrader = initUpgrader(dataSource);
	}
	
	@PostConstruct
	public void run() {
		upgrader.run();		
	}
	
	private DatabaseUpgrader initUpgrader(DataSource dataSource) {
		GenericDatabaseUpgrader database = new GenericDatabaseUpgrader(dataSource);
		
		DatabaseChangeSetBuilder builder = new DatabaseChangeSetBuilder(installVersion);
		builder.addChange(databaseResource("install/Member.sql"));
		builder.addChange(databaseResource("install/Group.sql"));
		builder.addChange(databaseResource("install/Activity.sql"));
		builder.addChange(databaseResource("install/Event.sql"));
		builder.addChange(databaseResource("install/ConnectedApp.sql"));
		builder.addChange(databaseResource("install/ConnectedAccount.sql"));
		builder.addChange(databaseResource("install/Reset.sql"));
		database.addChangeSet(builder.getChangeSet());
		
		return database;
	}
	
	private Resource databaseResource(String resource) {
		return new ClassPathResource(resource, getClass());
	}
}