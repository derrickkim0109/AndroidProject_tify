<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    int user_uNo = Integer.parseInt(request.getParameter("user_uNo"));
	
//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select cCardNo, cCardCompany, cYear, cMM, cNo from tify.creditcard where cDeleteDate IS NULL and user_uNo = " + user_uNo;
    int count = 0;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); // 
%>
		{ 
  			"card_info"  : [ 
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
			
            "cCardNo" : "<%=rs.getString(1) %>",
            "cCardCompany" : "<%=rs.getString(2) %>",
            "cYear" : "<%=rs.getString(3) %>",
            "cMM" : "<%=rs.getString(4) %>",
            "cNo" : "<%=rs.getInt(5) %>"
            
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