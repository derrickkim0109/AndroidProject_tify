<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    int storekeeper_skSeqNo = Integer.parseInt(request.getParameter("storekeeper_skSeqNo"));
	
//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select * from store where storekeeper_skSeqNo = " + storekeeper_skSeqNo;
    int count = 0;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); // 
%>
		{ 
  			"store"  : [ 
<%
        while (rs.next()) {
            if (count == 0) {

            }else{
%>
            , 
<%
            }
%>            
			{
			
            "sName" : "<%=rs.getString(2) %>",
            "sTelNo" : "<%=rs.getString(3) %>",
            "sRunningTime" : "<%=rs.getString(4) %>",
            "sAddress" : "<%=rs.getString(5) %>",
            "sImage" : "<%=rs.getString(6) %>",
            "sPackaging" : "<%=rs.getInt(7) %>",
            "sComment" : "<%=rs.getString(8) %>"

            
			}

<%		
        count++;
        }
%>
		  ] 
		} 
<%		
        conn_mysql.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
	
%>