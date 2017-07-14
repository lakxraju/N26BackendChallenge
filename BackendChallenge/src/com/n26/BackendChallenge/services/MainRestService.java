package com.n26.BackendChallenge.services;

import java.time.Instant;
import java.util.concurrent.PriorityBlockingQueue;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/concurrentApp")
@Singleton
public class MainRestService {
	
	
	/**
	 * Variable to store transactions done in last 60 seconds 
	 * It is a custom implementation(Lines 112 to 244 in N26PriorityBlockingQueue.java) of PriorityBlockingQueue and should not be used for objects which are not instance of TransactionBean
	 * 
	 * Note: This REST service can be implemented in EJB singleton as well. Didn't really have time to use it.  
	 * */
	N26PriorityBlockingQueue<TransactionBean> last60SecondsQueue = new N26PriorityBlockingQueue<TransactionBean>();
	
	
	
	/**
	 * Ping method to test the working of the web server
	 * 
	 * Ex URL : http://localhost:8080/BackendChallenge/concurrentApp/ping/{name}
	 * 
	 * */
    @GET
    @Path("/ping/{name}")
    @Produces("application/json") 
    public Response sayHello(@PathParam("name") String msg) {
    	if(msg==null){
    		return Response.status(400).build();
    	}
        String output = "Hello, " + msg + "!";
        return Response.status(200).entity(output).build();
    }
    
    
    
    /**
     * Stores the incoming transactions if the timestamp is not older than 60 seconds
     * 
     * Ex. URL: http://localhost:8080/BackendChallenge/concurrentApp/transactions
     * 
     * Expected Request Body:  
     * Type: application/json
     * Structure: {
	 *				"amount": 12.3,
	 *				"timestamp": 1478192204000
	 *			  }
	 *
	 * Note : 'timestamp' is linux epoch timestamp in millis 
     * */
    @POST
    @Path("/transactions")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response storeTransactions(TransactionBean txnBean){
    	if((txnBean.getAmount()==null)||(txnBean.getTimestamp()==null)){
    		return Response.status(400).build();
    	}
    	if(Instant.now().toEpochMilli() - txnBean.getTimestamp() > 60000){
    		return Response.status(204).build();
    	}
    	else{
    		System.out.println("Current Length:" + last60SecondsQueue.size());
    		last60SecondsQueue.pruneBucket(Instant.now().toEpochMilli());
    		System.out.println("Current Length after prune:" + last60SecondsQueue.size());
    		last60SecondsQueue.addToAllDS(txnBean);
    		System.out.println("Current after addition:" + last60SecondsQueue.size());
    		return Response.status(201).entity(new String(Integer.toString(last60SecondsQueue.size()))).build();
    	}
    }
    
    /**
     * Returns statistics of transactions done in last 60 seconds. 
     * 
     * Structure of returned JSON:
     * {
	 *	"sum": 1000,
	 *	"avg": 100,
	 *	"max": 200,
	 *	"min": 50,
	 *	"count": 10
	 *	}
     * 
     * */
    @GET
    @Path("/statistics")
    @Produces("application/json")
    public Response getStatistics(){
    	StatisticsBean returnBean = new StatisticsBean();
    	if(last60SecondsQueue.isEmpty()){
    		returnBean.setAverage(0.0);
        	returnBean.setCount((long)0);
        	returnBean.setMin(0.0);
        	returnBean.setMax(0.0);
        	returnBean.setSum(0.0);
        	return Response.status(200).entity(returnBean).build();
    	}
    	last60SecondsQueue.pruneBucket(Instant.now().toEpochMilli());
    	returnBean.setAverage((last60SecondsQueue.getAverage()==Double.MIN_VALUE)?0.0:last60SecondsQueue.getAverage());
    	returnBean.setCount((long)last60SecondsQueue.size());
    	returnBean.setMin((last60SecondsQueue.getMin() == Double.MAX_VALUE)?0.0:last60SecondsQueue.getMin());
    	returnBean.setMax((last60SecondsQueue.getMax()==Double.MIN_VALUE)?0.0:last60SecondsQueue.getMax());
    	returnBean.setSum(last60SecondsQueue.getSum());
		return Response.status(200).entity(returnBean).build();
    }
}
