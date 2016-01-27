package com.gm.mgmt.price.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.error.DocumentDoesNotExistException;


@RestController
@RequestMapping("/price")
public class PriceService {

    private final CouchbaseManager couchbaseService;

    @Autowired
    public PriceService(CouchbaseManager couchbaseService) {
        this.couchbaseService = couchbaseService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{priceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPrice(@PathVariable String priceId) {
        JsonDocument doc = couchbaseService.read(priceId);
        if (doc != null) {
            return new ResponseEntity<String>(doc.content().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPrice(@RequestBody Map<String, Object> priceData) {
        String id = "";
        try {
        	JsonObject price = parsePrice(priceData);
            id = "price-" + price.getString("priceId");
            JsonDocument doc = CouchbaseManager.createDocument(id, price);
            couchbaseService.create(doc);
            return new ResponseEntity<String>(id, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } catch (DocumentAlreadyExistsException e) {
            return new ResponseEntity<String>("Id " + id + " already exist", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{priceId}")
    public ResponseEntity<String> deletePrice(@PathVariable String priceId) {
        JsonDocument deleted = couchbaseService.delete(priceId);
        return new ResponseEntity<String>(""+deleted.cas(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{priceId}", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    public ResponseEntity<String> updatePrice(@PathVariable String priceId, @RequestBody Map<String, Object> priceData) {
        try {
            JsonObject price = parsePrice(priceData);
            couchbaseService.update(CouchbaseManager.createDocument(priceId, price));
            return new ResponseEntity<String>(priceId, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } catch (DocumentDoesNotExistException e) {
            return new ResponseEntity<String>("Id " + priceId + " does not exist", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private JsonObject parsePrice(Map<String, Object> priceData) {
        JsonObject price = JsonObject.create();
        for (Map.Entry<String, Object> entry : priceData.entrySet()) {
             price.put(entry.getKey(), entry.getValue());
        }
        return price;
     }
}
