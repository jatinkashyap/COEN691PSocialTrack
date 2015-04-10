package com.source.rest.impl;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mysql.jdbc.Connection;

@Path("/post/")
public class PostData {
	@POST
	 @Path("/{username}/{city}")
	 public String getLocation( @PathParam ("username") String username,@PathParam ("city") String city) throws IOException { {
		String[] cities = city.split("&"); 
		
		 try 
		{
			  Class.forName("com.mysql.jdbc.GoogleDriver");  
			  Connection myConn=(Connection) DriverManager.getConnection("jdbc:google:mysql://cloudclassproject1988:demo?user=root"); 
			  for(int i=0; i<cities.length;i++)
				  {
				  Statement myStmt=myConn.createStatement();
				  String sql ="Insert into Users.user (name,location) VALUES('"+username+"','"+cities[i]+"');"; 
				  myStmt.execute(sql); 
				  }
		} 
		catch (Exception e) {
			
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} 
		 
	 }
	 return city;
	 }

}
