package com.source.rest.impl;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
@Path("/hr/")
public class Employee {
 @GET
 @Produces("text/plain")
 @Path("/employee")
 public String getEmployee() {
        return "Hello World!";
    }
}