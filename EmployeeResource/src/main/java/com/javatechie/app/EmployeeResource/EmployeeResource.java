//Develop Restfull web services using Vertx
package com.javatechie.app.EmployeeResource;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Hello world!
 *
 */
public class EmployeeResource 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        List<Employee> employees=new ArrayList<>();
        //create Vertx
        Vertx vertx = Vertx.vertx();
        //create Http Server
        
        HttpServer server =vertx.createHttpServer();
        
        //for redirect our Url and url mapping
        
        Router router = Router.router(vertx);
        
        /**
         * @POST Method
         *
         */
        Route posthandler=router.post("/addEmployee").handler(BodyHandler.create()).handler(routingContext ->{
        	
        	final Employee employee=Json.decodeValue(routingContext.getBody(), Employee.class);
        	//Create HTTP server response
        	HttpServerResponse serverResponse=routingContext.response();
        	serverResponse.setChunked(true);
        	//now add this emplyee in our list
        	employees.add(employee);
        	serverResponse.end(employees.size()+" Employee added successfully..!!!");
        	
        });
        
        
        /**
         * @GET Method
         *
         */
        
        Route getHandleRoute=router.get("/getEmployee").produces("*/Json").handler(routingContext -> {
        	//get the response from routing context
        	routingContext.response().setChunked(true).end(Json.encodePrettily(employees));
        	
        });
        
        /**
         * @GET Method with filter based on Name
         *
         */
        Route getFilterHandler =router.get("/getEmployee/:name").produces("*/Json").handler(routingContext -> {
        	//get the response from routing context single name based
        	String name=routingContext.request().getParam("name");
        	routingContext.response().setChunked(true).end(Json.encodePrettily(employees.stream().filter(emp->emp.getName().equals(name)).findFirst().get()));
        	
        });
        
        server.requestHandler(router::accept).listen(9090);
        
    }
}
