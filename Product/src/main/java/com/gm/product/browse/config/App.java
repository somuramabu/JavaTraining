package com.gm.product.browse.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gm.product.browse.services.ProductService;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class App extends Application<ProductConfiguration> {
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	@Override
	public void initialize(Bootstrap<ProductConfiguration> b) {
	}

	@Override
	public void run(ProductConfiguration config, Environment e)
			throws Exception {
		LOGGER.info("Method App#run() called");
		System.out.println("Hello world, by Dropwizard!");
		System.out.println("Coucbase Bucket : " + config.getCouchbaseBucket());
		e.jersey().register(new ProductService(config));
	}

	public static void main(String[] args) throws Exception {
		new App().run(args);
	}
}