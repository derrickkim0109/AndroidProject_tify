<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    int user_uNo = Integer.parseInt(request.getParameter("user_uNo"));
    int order_oNo = Integer.parseInt(request.getParameter("order_oNo"));
//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select * from tify.orderlist where user_uNo = " + user_uNo + " and order_oNo = " + order_oNo + " order by olSeqNo desc";
    int count = 0;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); // 
%>
		{ 
  			"orderList"  : [ 
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
            "store_sSeqNo" : "<%=rs.getInt(3) %>",
            "store_sName" : "<%=rs.getString(4) %>", 
            "menu_mName" : "<%=rs.getString(5) %>",    
			"olSeqNo" : "<%=rs.getInt(6) %>", 
            "olSizeUp" : "<%=rs.getInt(7) %>",
            "olAddShot" : "<%=rs.getInt(8) %>",
            "olRequest" : "<%=rs.getString(9) %>",
            "olPrice" : "<%=rs.getInt(10) %>",
            "olQuantity" : "<%=rs.getInt(11) %>"
          
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