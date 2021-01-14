<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
    String pw_mysql = "qwer1234";
    
    String uNo = request.getParameter("user_uNo");
    
    String WhereDefault = "SELECT l.order_oNo, o.oInsertDate, o.store_sName, Sum(l.olQuantity) from tify.order o, orderlist l where l.order_oNo = o.oNo and l.user_uNo="+uNo+" Group by l.order_oNo";
    
    int count = 0;
    

    
    try {
       
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
       
        Statement stmt_mysql = conn_mysql.createStatement();
       
        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); 
        
%>
		{ 
  			"order_history"  : [ 
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
            "user_uNo" : "<%=rs.getInt(1) %>", 
            "oInsertDate" : "<%=rs.getString(2) %>",
            "store_sName" : "<%=rs.getString(3) %>",
            "total" : "<%=rs.getInt(4) %>"
	
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