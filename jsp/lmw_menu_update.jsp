<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	int mNo = Integer.parseInt(request.getParameter("mNo"));
	String mName = request.getParameter("mName");
    int mPrice = Integer.parseInt(request.getParameter("mPrice"));
    int mSizeUp = Integer.parseInt(request.getParameter("mSizeUp"));
    int mShut = Integer.parseInt(request.getParameter("mShut"));
	String mImage = request.getParameter("mImage");
    int mType = Integer.parseInt(request.getParameter("mType"));
    String mComment = request.getParameter("mComment");
	

//------
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";
	int result = 0; // 수정 확인 

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
	    Statement stmt_mysql = conn_mysql.createStatement();
	
        String A = "update tify.menu set mName = '" + mName + "', mPrice = " + mPrice + ", mSizeUp = " + mSizeUp + ", mShut = " + mShut + ", mImage = '" + mImage + "', mType = " + mType + ", mComment = '" + mComment + "' where mNo = " + mNo;

	    ps = conn_mysql.prepareStatement(A);

        
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