<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    String usertel = request.getParameter("usertel");
  
//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "SELECT count(*) from user where uTelNo = ?";
    int count = 0;

	PreparedStatement preparedStatement = null; //검색하는거 pre이거있어야됨
	ResultSet resultSet = null;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        preparedStatement = connection.prepareStatement(WhereDefault);
		preparedStatement.setString(1, usertel);
        resultSet = preparedStatement.executeQuery();


%>
		{ 
  			"user_info"  : [ 
<%
        while (resultSet.next()) {
            if (count == 0) {

            }else{
%>
            , 
<%
            }
%>            
			{
            "userTelCount" : "<%=resultSet.getInt(1) %>"
			}

<%		
        count++;
        }
%>
		  ] 
		} 
<%		
        connection.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
	
%>