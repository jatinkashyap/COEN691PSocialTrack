package com.source.rest.impl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
@Path("/events/")
public class GetData {
	@GET
	@Produces(MediaType.APPLICATION_XML)
	 @Path("/{cityname}/location")
	 public LocationList getEvent( @PathParam ("cityname") String cityname) throws IOException { {
		 URL url;
		try {
			url = new URL("https://www.eventbriteapi.com/v3/events/search/?&start_date.keyword=next_week&venue.city="+cityname+"&token=xxxxxxxxxxxx");
			  HttpURLConnection con =
				      (HttpURLConnection) url.openConnection();
			  con.setConnectTimeout(100000); 
			  if (con.getResponseCode() != 200) 
			 	{
				 	throw new IOException(con.getResponseMessage());
			 	}
			 
			 BufferedReader rd = new BufferedReader( new InputStreamReader(con.getInputStream())); 
			 String url1 = null;
			 JSONArray description = null;
			 StringBuilder sb = new StringBuilder(); 
			 String line; 
			 while ((line =rd.readLine()) != null)
			 	{ 
				 	sb.append(line); 
			 	} 
			 rd.close();
			 con.disconnect();
			 JSONObject jsonObject = null;
			 
			 try 
			 {
				 ArrayList al = new ArrayList();
				 jsonObject = new JSONObject(sb.toString()); 
				 JSONArray jArray3 = jsonObject.getJSONArray("events"); 
				 for(int i = 0; i < jArray3.length(); i++) 
				 	{
					 	JSONObject object3 = jArray3.getJSONObject(i);
					 	url1 = object3.getString("url"); 
					 	al.add(url1);
					 	
				 	}
				 LocationList responseList = new LocationList();
				 responseList.setList(al);
				 return responseList;
			 } 
			 catch (JSONException e)
			 { 
				 // TODO Auto-generated catch block 
				 e.printStackTrace();
			 }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
	return null;
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	 @Path("/{username}/{city}")
	 public String getLocation( @PathParam ("username") String username,@PathParam ("city") String city) throws IOException { {
		String[] cities = city.split("&"); 
		 try 
		{
			  Class.forName("com.mysql.jdbc.GoogleDriver");  
			  Connection myConn=(Connection) DriverManager.getConnection("jdbc:google:mysql://cloudclassproject1988:demo?user=xxxx"); 
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
	 return null; 
}
}
