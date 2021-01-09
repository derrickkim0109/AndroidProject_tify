<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    int user_uSeqNo = Integer.parseInt(request.getParameter("user_uSeqNo"));
	
//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select * from cartlist where user_uSeqNo = " + user_uSeqNo  + " order by cLNo desc";
    int count = 0;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); // 
%>
		{ 
  			"cartList"  : [ 
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
			"cLNo" : "<%=rs.getInt(2) %>", 
			"store_sSeqNo" : "<%=rs.getInt(3) %>",   
            "store_sName" : "<%=rs.getString(4) %>", 
			"menu_mName" : "<%=rs.getString(5) %>", 
            "cLPrice" : "<%=rs.getInt(6) %>",
            "cLQuantity" : "<%=rs.getInt(7) %>",
            "cLImage" : "<%=rs.getString(8) %>",
            "cLSizeUp" : "<%=rs.getInt(9) %>",
            "cLAddShot" : "<%=rs.getInt(10) %>",
            "cLRequest" : "<%=rs.getString(11) %>"
           
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