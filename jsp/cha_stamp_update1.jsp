<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
    String pw_mysql = "qwer1234";

    String oNo = request.getParameter("oNo");
    String WhereDefault = "SELECT SUM(olQuantity) FROM tify.orderlist where order_oNo="+oNo;
    int count = 0;


    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); 

%>

<%
        while (rs.next()) {
        
            if (count == 0) {

            }else{
%>

<%
            }
%>           
    
			{

			"stamp_count" : "<%=rs.getInt(1) %>"
        
            
     
      
			}

<%		
        count++;
        }
%>

<%		
        conn_mysql.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
	
%>