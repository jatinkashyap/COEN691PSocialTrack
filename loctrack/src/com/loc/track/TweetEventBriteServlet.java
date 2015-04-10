package com.loc.track;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import twitter4j.AccountSettings;
import twitter4j.GeoLocation;
import twitter4j.Location;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.google.appengine.api.search.Document;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.mysql.jdbc.Connection;


/**
 * Main logic of the application lies inside this class
 * Class calls Twitter Api to get the Users Account settings and Checks for the geo-location status.
 * If geo-location is Enabled then it checks for the tweet locations.
 * And using these locations calls eventbrite Api to get events in the nearest locations.
 * If geo-location is disabled, the application uses the profile location and gives events nearest to that location.
 */

public class TweetEventBriteServlet extends HttpServlet{
	private static final long serialVersionUID = -2347907323343494157L;
	static String cityName;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.setContentType("text/html");
		Twitter tw = (Twitter) request.getSession().getAttribute("twitter");
		
		//Setting the requestToken and oAuthAccessToken as the session variables.
		
		RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
		AccessToken oAuthAccessToken = (AccessToken) request.getSession().getAttribute("oAuthToken");
		tw.setOAuthAccessToken(oAuthAccessToken);
		AccountSettings settings = null;
		
		List<Status> stat1 = null;
		try {
			settings = tw.getAccountSettings();
			String username=tw.getScreenName();
			response.getWriter().println(settings.getScreenName());
			boolean geoEnabled = settings.isGeoEnabled();
			String UserScreenName = tw.getScreenName();
			//Check geo-location Status. 
			if(geoEnabled)
			{
				stat1 = tw.getUserTimeline();
				if(stat1!=null)
				{
					//Iterate through each status and get geo-location of each status
					for(Status stat_1 : stat1)
						{
						if(stat_1.getPlace()!=null)
						{
							cityName = stat_1.getPlace().getFullName().split(",")[0];
							//Since EventBrite doesnot readily take special characters, renaming the location only in case of montreal.
							if(cityName.equals("Montréal"))
								{
									cityName = "montreal";
								}
							try { 
								//Each location and User is saved in the cloud database.
								URL connection = new URL("http://1-dot-cloudclassproject1988.appspot.com/socialtrack/events/"+UserScreenName+"/"+cityName+"");
								 HttpURLConnection con = (HttpURLConnection)connection.openConnection();
								 con.setConnectTimeout(100000);
								 if (con.getResponseCode() != 200) 
								 	{
									 	throw new IOException(con.getResponseMessage());
								 	}
								 
								}
								  
							catch(Exception exe) 
								{
									exe.printStackTrace(); 
								}
								//EventbriteApi is called to get all the events nearest to the location.
					 			 URL connection = new URL("http://1-dot-cloudclassproject1988.appspot.com/socialtrack/events/"+cityName+"/location");
								 HttpURLConnection con = (HttpURLConnection)connection.openConnection();
								 con.setConnectTimeout(100000);
								 if (con.getResponseCode() != 200) 
								 	{
									 	throw new IOException(con.getResponseMessage());
								 	}
								 
								 BufferedReader rd = new BufferedReader( new InputStreamReader(con.getInputStream())); 
								 String url1 = null;
								 StringBuilder sb = new StringBuilder(); 
								 String line = null; 
								 while ((line =rd.readLine()) != null)
								 	{ 
									 	sb.append(line); 
								 	} 
								 rd.close();
								 con.disconnect();
								 JSONObject jsonObject = null;
						  
								 try 
								 {
			
									  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
									    DocumentBuilder builder = factory.newDocumentBuilder();
									    InputSource is = new InputSource(new StringReader(sb.toString()));
									    org.w3c.dom.Document doc = builder.parse(is);
									    NodeList nList = doc.getElementsByTagName("list");
									    for (int temp = 0; temp < nList.getLength(); temp++) {

									    	Element el = (Element)nList.item(temp);
											String url = el.getFirstChild().getNodeValue();
											response.getWriter().println( "<a href =" + url +"> " + url + "</a><br>");
										}
									    
								 } 
								 catch (Exception e)
								 { 
									 // TODO Auto-generated catch block 
									 e.printStackTrace();
								 }
							}

						}
				}
				else
				{
					//If geo-location is enabled but tweets doesnt have any location attached to it, Take profile location.
					settings = tw.getAccountSettings();
					long id = tw.getId();
					User user = tw.showUser(id);
					cityName = user.getLocation();
					String userScreenName = tw.getScreenName();
					try { 
						//Each location and User is saved in the cloud database.
						URL connection = new URL("http://1-dot-cloudclassproject1988.appspot.com/socialtrack/events/"+userScreenName+"/"+cityName+"");
						 HttpURLConnection con = (HttpURLConnection)connection.openConnection();
						 con.setConnectTimeout(100000);
						 if (con.getResponseCode() != 200) 
						 	{
							 	throw new IOException(con.getResponseMessage());
						 	}
						 
						  }
						  
						  catch(Exception exe) 
						  {
							  exe.printStackTrace(); 
							  
						  }
						//EventbriteApi is called to get all the events nearest to the location.
						URL connection = new URL("http://1-dot-cloudclassproject1988.appspot.com/socialtrack/events/"+cityName+"/location");
						 HttpURLConnection con = (HttpURLConnection)connection.openConnection();
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

							  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
							    DocumentBuilder builder = factory.newDocumentBuilder();
							    InputSource is = new InputSource(new StringReader(sb.toString()));
							    org.w3c.dom.Document doc = builder.parse(is);
							    NodeList nList = doc.getElementsByTagName("list");
							    for (int temp = 0; temp < nList.getLength(); temp++) {

							    	Element el = (Element)nList.item(temp);
									String url = el.getFirstChild().getNodeValue();
									response.getWriter().println( "<a href =" + url +"> " + url + "</a><br>");
								}
						 }
							 
						 catch (Exception e)
						 { 
							 // TODO Auto-generated catch block 
							 e.printStackTrace();
						 }

				
				}
			 
			}
			else
			{
				settings = tw.getAccountSettings();
				long id = tw.getId();
				User user = tw.showUser(id);
				String userScreeenName = tw.getScreenName();
				cityName = user.getLocation();
				try { 
					//Each location and User is saved in the cloud database.
					URL connection = new URL("http://1-dot-cloudclassproject1988.appspot.com/resources/hr/"+userScreeenName+"/"+cityName+"");
					 HttpURLConnection con = (HttpURLConnection)connection.openConnection();
					 con.setConnectTimeout(100000);
					 if (con.getResponseCode() != 200) 
					 	{
						 	throw new IOException(con.getResponseMessage());
					 	}
					 
					  }
					  
					  catch(Exception exe) 
					  {
						  exe.printStackTrace(); 
						  }
					 //EventbriteApi is called to get all the events nearest to the location.
					 URL connection = new URL("http://1-dot-cloudclassproject1988.appspot.com/socialtrack/events/"+cityName+"/location");
					 HttpURLConnection con = (HttpURLConnection)connection.openConnection();
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
					 Map<Integer, ArrayList> map = new HashMap<Integer, ArrayList>();
						
						
					 
					 try 
					 {

						  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						    DocumentBuilder builder = factory.newDocumentBuilder();
						    InputSource is = new InputSource(new StringReader(sb.toString()));
						    org.w3c.dom.Document doc = builder.parse(is);
						    NodeList nList = doc.getElementsByTagName("list");
						    for (int temp = 0; temp < nList.getLength(); temp++) {

						    	Element el = (Element)nList.item(temp);
								String url = el.getFirstChild().getNodeValue();
								response.getWriter().println( "<a href =" + url +"> " + url + "</a><br>");
							}
					 } 
					 catch (Exception e)
					 { 
						 // TODO Auto-generated catch block 
						 e.printStackTrace();
					 }
					
				}
		}
		catch (TwitterException e1) 
		{
			throw new ServletException(e1);
		}
		
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		 
		  
		
		

	}
}
