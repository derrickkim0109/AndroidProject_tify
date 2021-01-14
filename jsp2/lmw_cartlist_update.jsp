<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        

<%
	request.setCharacterEncoding("utf-8");
    int user_uSeqNo = Integer.parseInt(request.getParameter("user_uSeqNo"));
    int cLNo = Integer.parseInt(request.getParameter("cLNo"));
    int store_sSeqNo = Integer.parseInt(request.getParameter("store_sSeqNo"));
    int menu_mSeqNo = Integer.parseInt(request.getParameter("menu_mSeqNo"));
    int cLPrice = Integer.parseInt(request.getParameter("cLPrice"));
    int cLQuantity = Integer.parseInt(request.getParameter("cLQuantity"));
  
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
	
        String A = "update cartlist set cLPrice = ?, cLQuantity =? where user_uSeqNo = " + user_uSeqNo + " and ";
        String B = "cLNo = " + cLNo + " and store_sSeqNo = " + store_sSeqNo + " and menu_mSeqNo = " + menu_mSeqNo;

        ps = conn_mysql.prepareStatement(A+B);

        ps.setString(1, cLPrice);
        ps.setString(2, cLQuantity);
        
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