package com.security.contests.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class MyDataBaseConfiguration {

	@Autowired
	private DataSource dataSource;

	public void createDefaultDB() {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8",
				new ClassPathResource("data.sql"));
		resourceDatabasePopulator.execute(dataSource);
	}
}
