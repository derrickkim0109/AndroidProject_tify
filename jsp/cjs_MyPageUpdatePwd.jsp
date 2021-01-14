<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        

<%
	request.setCharacterEncoding("utf-8");
    int uNo = Integer.parseInt(request.getParameter("uNo"));
    String uPayPassword = request.getParameter("uPayPassword");

//------

	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";
	int result = 0; // 수정 확인 

	PreparedStatement ps = null;

	try{
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
	    Statement stmt_mysql = conn_mysql.createStatement();
	
        String A = "UPDATE user set uPayPassword = ? where uNo =?";
        ps = conn_mysql.prepareStatement(A);
        ps.setString(1, uPayPassword);
        ps.setInt(2, uNo);
        result = ps.executeUpdate();
%>
		{
			"result" : "<%=result%>"
		}

<%	
	
	    conn_mysql.close();

	} 

	catch (Exception e){
%>
		{
			"result" : "<%=result%>"
		}
<%			
	    e.printStackTrace();

	}

%>