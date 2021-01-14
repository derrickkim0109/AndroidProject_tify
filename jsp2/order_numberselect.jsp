<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
    String pw_mysql = "qwer1234";

	int user_uNo = Integer.parseInt(request.getParameter("user_uNo"));

    String WhereDefault = "SELECT oNo, storekeeper_skSeqNo, store_sName FROM tify.order where user_uNo = ? order by oNo desc limit 1";
    int count = 0;


	PreparedStatement ps = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();
        
	    ps = conn_mysql.prepareStatement(WhereDefault);
        ps.setInt(1, user_uNo);


        ResultSet rs = ps.executeQuery(); 
%>
		{ 
  			"order_info"  : [ 
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
            "oNo" : "<%=rs.getInt(1) %>",   
			"store_sSeqno" : "<%=rs.getInt(2) %>", 
			"store_sName" : "<%=rs.getString(3) %>"  
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