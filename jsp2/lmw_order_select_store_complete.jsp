<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
    int skSeqNo = Integer.parseInt(request.getParameter("skSeqNo"));

    // 3,4(픽업완료,주문취소)인 주문 내역 => CompleteFragment

	
//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select * from tify.order where (oStatus = 3 or oStatus = 4 or oStatus = 5) and store_skSeqNo = " + skSeqNo + " order by oNo desc";
    int count = 0;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); // 
%>
		{ 
  			"order"  : [ 
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
            "store_sSeqNo" : "<%=rs.getInt(3) %>",
            "store_sName" : "<%=rs.getString(4) %>",
            "oInsertDate" : "<%=rs.getString(5) %>",    
			"oDeleteDate" : "<%=rs.getString(6) %>", 
            "oSum" : "<%=rs.getInt(7) %>",
            "oCardName" : "<%=rs.getString(8) %>",
            "oCardNo" : "<%=rs.getInt(9) %>",
            "oReview" : "<%=rs.getInt(10) %>",
            "oStatus" : "<%=rs.getInt(11) %>"
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