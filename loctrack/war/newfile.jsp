<%@ page language="java" contentType="text/html"%>
<%@page import="org.json.simple.JSONObject"%>
<%@ page import ="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
	<title>Social Track</title>
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<script type="text/javascript" src="js/twitter-login.js"></script>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
	<script src="http://code.jquery.com/jquery-2.1.3.js"></script>
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,800,300,600,700' rel='stylesheet' type='text/css'>
	<script src="http://code.jquery.com/ui/1.11.3/jquery-ui.js"></script>
	<script type="text/javascript" charset="utf8" src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Events</title>	
</head>
<body background="images/sunshine.jpg">
<center>
	<br/>
	<h1 style="font-family:'Open Sans';font-weight:300;color:#fff; font-size:72px; ">Events Table </h1>
	<br/>
	<%
	Map<Integer, ArrayList> map = (HashMap<Integer, ArrayList>)request.getAttribute("jsonObject");
	Set<Integer> keySet = map.keySet();
	for(int i : keySet) {
		ArrayList al = (ArrayList)map.get(i);
		String url=(String)al.get(0);
		String text=(String)al.get(1);
	%>
	    <a href='<%=url%>'><%=text%></a> <br/>
	<%
	}
	%>	
</center>

<br>
</body>
</html>