package com.gm.mgmt.price.services;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.gm.mgmt.price.PriceConfiguration;

import rx.Observable;


@Service
public class CouchbaseManager {

    private final Bucket bucket;
    private final Cluster cluster;

    @Autowired
    public CouchbaseManager(final PriceConfiguration config) {
        //connect to the cluster and open the configured bucket
        this.cluster = CouchbaseCluster.create(config.getCouchbaseNodes());
        this.bucket = cluster.openBucket(config.getCouchbaseBucket(), config.getCouchbasePassword());
    }

    @PreDestroy
    public void preDestroy() {
        if (this.cluster != null) {
            this.cluster.disconnect();
        }
    }

    /**
     * Prepare a new JsonDocument with some JSON content
     */
    public static JsonDocument createDocument(String id, JsonObject content) {
        return JsonDocument.create(id, content);
    }
    
    /**
     * CREATE the document in database asynchronously
     * @return the created document, with up to date metadata
     */
    public Observable<JsonDocument> createAsync(JsonDocument doc) {
        return bucket.async().insert(doc);
    }

    /**
     * CREATE the document in database
     * @return the created document, with up to date metadata
     */
    public JsonDocument create(JsonDocument doc) {
        return bucket.insert(doc);
    }

    
    /**
     * READ the document from database
     */
    public JsonDocument read(String id) {
        return bucket.get(id);
    }

    /**
     * UPDATE the document in database
     * @return the updated document, with up to date metadata
     */
    public JsonDocument update(JsonDocument doc) {
        return bucket.replace(doc);
    }

    /**
     * DELETE the document from database
     * @return the deleted document, with only metadata (since content has been deleted)
     */
    public JsonDocument delete(String id) {
        return bucket.remove(id);
    }
}
