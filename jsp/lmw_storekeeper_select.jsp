<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    String skId = request.getParameter("skId");
    String skPw = request.getParameter("skPw");


//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select count(*)as cnt, skSeqNo, skStatus from tify.storekeeper where skId = '" + skId + "' and skPw = '" + skPw + "'";
    int count = 0;


    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();



        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); // 


%>
		{ 
  			"storekeeper"  : [ 
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

			"cnt" : "<%=rs.getInt(1) %>",
            "skSeqNo" : "<%=rs.getInt(2) %>",
            "skStatus" : "<%=rs.getInt(3) %>"
            
     
      
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