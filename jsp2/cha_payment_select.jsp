<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
    String pw_mysql = "qwer1234";

    String oNo = request.getParameter("oNo");
    String WhereDefault = "select * from tify.order where oNo =" + oNo;
    int count = 0;


    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); 
%>
		{ 
  			"payment_select"  : [ 
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
            "oNo" : "<%=rs.getInt(2) %>",
            "storekeeper_skSeqNo" : "<%=rs.getInt(3) %>",
            "store_sName" : "<%=rs.getString(4) %>",
            "oInsertDate" : "<%=rs.getString(5) %>",
            "oDeleteDate" : "<%=rs.getString(6) %>",
            "oSum" : "<%=rs.getString(7) %>",
            "oCardName" : "<%=rs.getString(8) %>",
            "oCardNo" : "<%=rs.getString(9) %>"
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