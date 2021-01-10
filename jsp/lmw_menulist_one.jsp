<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    int mNo =  Integer.parseInt(request.getParameter("mNo"));
	
//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select * from menu where mNo=" + mNo;
    int count = 0;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); // 
%>
		{ 
  			"menuList"  : [ 
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
            "mNo" : "<%=rs.getInt(2) %>",     
			"mName" : "<%=rs.getString(3) %>",   
			"mPrice" : "<%=rs.getInt(4) %>", 
            "mSizeUp" : "<%=rs.getInt(5) %>",
            "mShut" : "<%=rs.getInt(6) %>",
            "mImage" : "<%=rs.getString(7) %>",
            "mType" : "<%=rs.getInt(8) %>",
            "mComment" : "<%=rs.getString(9) %>"
           
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