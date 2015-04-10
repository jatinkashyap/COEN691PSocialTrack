package com.loc.track;

import java.io.BufferedReader;

import twitter4j.TwitterException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.AccountSettings;
import twitter4j.Location;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterBase;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * This class is used to call the twitter OAuth functionality. It uses the Application ConsumerKey
 * ConsumerSecret. and redirects user to secure login page for Twitter Authentication.
 * It uses Twitter4j external library to call twitter Api using REST.
 */
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String cityName;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("IjIaxN9tCgl8KdCmHNxqAFkzr")
				.setOAuthConsumerSecret(
						"Jkvq8X4FJvot0yWaZnquUFpZzmZdjKoG76MGGFNwxioAyfFED0");


		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter4j.Twitter tw = tf.getInstance();
		
		RequestToken requestToken = null;
		try {
			requestToken = tw.getOAuthRequestToken();
			request.getSession().setAttribute("twitter", tw);
			request.getSession().setAttribute("requestToken", requestToken);
			response.sendRedirect(requestToken.getAuthenticationURL());
		} catch (TwitterException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
