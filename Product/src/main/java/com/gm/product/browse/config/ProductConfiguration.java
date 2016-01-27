package com.gm.product.browse.config;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductConfiguration extends Configuration {
	@JsonProperty("couchbase.nodes")
	private String[] couchbaseNodes;

	@JsonProperty("couchbase.bucket")
	private String couchbaseBucket;

	@JsonProperty("couchbase.password")
	private String couchbasePassword;

	public String[] getCouchbaseNodes() {
		return couchbaseNodes;
	}

	public String getCouchbaseBucket() {
		return couchbaseBucket;
	}

	public String getCouchbasePassword() {
		return couchbasePassword;
	}
}
