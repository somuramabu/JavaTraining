package com.gm.product.browse.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.couchbase.client.java.view.ViewResult;
import com.gm.enitity.product.Product;
import com.gm.product.browse.config.ProductConfiguration;

import rx.Observable;



@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductService {
	
	private CouchbaseManager service = null; 
	public ProductService(ProductConfiguration config){
		   service = new CouchbaseManager(config);
	}
	
	/*@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createProduct(Product product){
		 String id = "";
	        try {
	        	id = "product-" + product.getId();
	        	product.setId(id);
	        	JsonObject productJson = parseObject(product);
	            JsonDocument doc = service.createDocument(id, productJson);
	            service.create(doc);
	            return Response.created(null).build();
	        } catch (IllegalArgumentException e) {
	            return Response.status(Status.BAD_REQUEST).build();
	        } catch (DocumentAlreadyExistsException e) {
	        	 return Response.status(Status.CONFLICT).build();
	        } catch (Exception e) {
	        	return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	        }
		
	}*/
	
	/**
	 * This method create product in async manner and blocks method return until data persist into Couchbase by using BlockingObservable  
	 * @param product
	 * @return
	 */
	/*@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAsyncProduct(Product product){
		 String id = "";
	        try {
	        	id = "product-" + product.getId();
	        	product.setId(id);
	        	JsonObject productJson = parseObject(product);
	            JsonDocument doc = service.createDocument(id, productJson);
	            JsonDocument returnDoc = service.createAsync(doc).toBlocking().single();
	            return Response.created(null).build();
	        } catch (IllegalArgumentException e) {
	            return Response.status(Status.BAD_REQUEST).build();
	        } catch (DocumentAlreadyExistsException e) {
	        	 return Response.status(Status.CONFLICT).build();
	        } catch (Exception e) {
	        	return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	        }
		
	}*/
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAsyncProduct(Product product){
		 String id = "";
	        try {
	        	id = "product-" + product.getId();
	        	product.setId(id);
	        	JsonObject productJson = parseObject(product);
	            JsonDocument doc = service.createDocument(id, productJson);
	            Observable<JsonDocument> returnObservable = service.createAsync(doc);
	            returnObservable.forEach(jsonDoc->{
	            		System.out.println("---------------Data paersited in couchbase async manner"+jsonDoc.toString());
	            });
	            return Response.created(null).build();
	        } catch (IllegalArgumentException e) {
	            return Response.status(Status.BAD_REQUEST).build();
	        } catch (DocumentAlreadyExistsException e) {
	        	 return Response.status(Status.CONFLICT).build();
	        } catch (Exception e) {
	        	return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	        }
		
	}
	@GET
	@Path("/{id}")
	public Response getProduct(@PathParam("id") String productId) {
		// retrieve information about the reward with the provided id
		JsonDocument doc = service.read(productId);
		if (doc != null) {
	            return Response.ok(doc.content().toString()).build();
	    } else {
	            return Response.status(Status.NOT_FOUND).build();
	    }
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProduct(@PathParam("id") String productId,Product product) {
		try {
			JsonObject productJson = parseObject(product);
			service.update(CouchbaseManager.createDocument(productId, productJson));
			return Response.ok(productId).build();
           
        } catch (IllegalArgumentException e) {
        	return Response.status(Status.BAD_REQUEST).build();
        } catch (DocumentDoesNotExistException e) {
        	return Response.status(Status.NOT_FOUND).build();
        } catch (Exception e) {
        	return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
		
	}

	@DELETE
	@Path("/{id}")
	public Response deleteProdcut(@PathParam("id") String productId) {
		JsonDocument deleted = service.delete(productId);
        return Response.status(Status.OK).build();
	}
	
	@GET
	public Response getProductsByType(@QueryParam("type") String productType) {
		ViewResult result = service.findAllByCriteria("dev_product","ProductByType",productType,0,10);
		if (!result.success()) {
            return  Response.status(Status.INTERNAL_SERVER_ERROR).build();
        } else {
            JsonArray keys = JsonArray.create();
            result.allRows().stream().forEach(row->{
            	keys.add(row.document().content());
            });
        	/*Iterator<ViewRow> iter = result.rows();
            while(iter.hasNext()) {
                ViewRow row = iter.next();
                JsonObject product = JsonObject.create();
                keys.add(row.document().content());
            }*/
            if (keys.isEmpty()) {
	            return Response.ok(Status.NOT_FOUND).build();
            }
            else{
            	return Response.ok(keys.toString()).build();
            }
        }
	}
	
	/**
	 * http://localhost:8080/product/batch/ids;id1=product-21;id2=product-22;id3=product-23;id4=product-24;id5=product-25;id6=product-26;id7=product-27;id8=product-28;id9=product-29;id10=product-30
	 * @param prodcutSeg
	 * @return
	 */
	@GET
	@Path("/batch/{ids}")
	public Response getProducts(@PathParam("ids") PathSegment prodcutSeg) {
		
		System.out.println(prodcutSeg);
		MultivaluedMap<String, String> map = prodcutSeg.getMatrixParameters();
		System.out.println(map.size());
		List<String> productIDList = new ArrayList<String>();
		map.values().stream().forEach(valueList->productIDList.add(valueList.get(0)));
		System.out.println("----------"+productIDList);
		List<JsonDocument> docList = service.batchGet(productIDList);
		JsonArray keys = JsonArray.create();
		docList.forEach(jsonDoc->keys.add(jsonDoc.content()));
		System.out.println(docList.toString());
		if (docList != null && !docList.isEmpty()) {
	            return Response.ok(keys.toString()).build();
	    } else {
	            return Response.status(Status.NOT_FOUND).build();
	    }
	}
	
	 private JsonObject parseObject(Map<String, Object> dataMap) {
	        JsonObject dataJsonObj = JsonObject.create();
	        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
	        	dataJsonObj.put(entry.getKey(), entry.getValue());
	        }
	        return dataJsonObj;
	 }
	 
	 // I have used reflection just to practice lamda expressions. Performance can be fine tuned by using other approaches. 
	 private JsonObject parseObject(Product product) {
	        JsonObject dataJsonObj = JsonObject.create();
	        Field fields[] =product.getClass().getDeclaredFields();
	        Arrays.asList(fields).stream().forEach(filed->{
	        	String getMethodName = "get"+ Character.toUpperCase(filed.getName().charAt(0)) + filed.getName().substring(1);
	        	Object val = null;
				try {
					val = product.getClass().getMethod(getMethodName).invoke(product);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	dataJsonObj.put(filed.getName(), val);
	        	}
	        );
	        return dataJsonObj;
	  }
}
