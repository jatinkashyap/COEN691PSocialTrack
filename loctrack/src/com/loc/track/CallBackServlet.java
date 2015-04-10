package com.loc.track;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * This class is used as callBack class to the application.
 * After the user Authentication is successfully completed by the oAuth, user is re-directed to this class of the application.
 * User oAuthToken is given back by the twitter Oauth.
 *
 */
public class CallBackServlet extends HttpServlet {
    private static final long serialVersionUID = 1657390011452788111L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        try {
		AccessToken oAuthAccessToken = 	twitter.getOAuthAccessToken(requestToken, verifier);
		request.getSession().setAttribute("oAuthToken", oAuthAccessToken);
		response.getWriter().println(oAuthAccessToken);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        response.sendRedirect(request.getContextPath() + "/"+"TweetEventBriteServlet");
        response.getWriter().println(verifier+"hello");
    }
}