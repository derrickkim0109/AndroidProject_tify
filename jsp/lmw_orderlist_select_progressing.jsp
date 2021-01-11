<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    int skSeqNo = Integer.parseInt(request.getParameter("skSeqNo"));
//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select ol.*, o.oInsertDate, o.oStatus from tify.orderlist as ol, tify.order as o where o.oDeleteDate IS NULL and (ol.storekeeper_skSeqNo = " + skSeqNo + " and (o.oStatus = 1 or o.oStatus = 2) and o.oNo = ol.order_oNo) order by ol.order_oNo desc";
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
            "order_oNo" : "<%=rs.getInt(2) %>", 
            "store_sName" : "<%=rs.getString(4) %>", 
            "menu_mName" : "<%=rs.getString(5) %>",    
			"olSeqNo" : "<%=rs.getInt(6) %>", 
            "olSizeUp" : "<%=rs.getInt(7) %>",
            "olAddShot" : "<%=rs.getInt(8) %>",
            "olRequest" : "<%=rs.getString(9) %>",
            "olPrice" : "<%=rs.getInt(10) %>",
            "olQuantity" : "<%=rs.getInt(11) %>",
            "oInsertDate" : "<%=rs.getString(12) %>",
            "oStatus" : "<%=rs.getInt(13) %>"
          
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