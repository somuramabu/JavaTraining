package com.gm.mgmt.price;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceConfiguration {
    @Value("${couchbase.nodes}")
    private String[] couchbaseNodes;

    @Value("${couchbase.bucket}")
    private String couchbaseBucket;

    @Value("${couchbase.password}")
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
