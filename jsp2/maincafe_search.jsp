<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("utf-8");
   String search = request.getParameter("search");


//-----
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
 	String pw_mysql = "qwer1234";
    String WhereDefault = "select s.sName,s.sTelNo,s.sRunningTime,s.sAddress,s.sImage,s.sPackaging,s.sComment,s.storekeeper_skSeqNo,";
    String WhereDefault2 = "(select count(b.bNo) from bookmark as b where b.storekeeper_skSeqNo =s.storekeeper_skSeqNo) as likeCount,";
    String WhereDefault3= "(select count(r.rNo) from review as r where r.storekeeper_skSeqNo =s.storekeeper_skSeqNo) as reviewCount,";
    String WhereDefault4="(select sk.skStatus from storekeeper as sk where s.storekeeper_skSeqNo = sk.skSeqNo) as skStatus from store as s where s.sName like'%"+search+"%';";
    int count = 0;
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();
        ResultSet rs = stmt_mysql.executeQuery(WhereDefault+WhereDefault2+WhereDefault3+WhereDefault4); // 
%>
		{ 
  			"cafe_info"  : [ 
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
			"sName" : "<%=rs.getString(1) %>",
            "sTelNo" : "<%=rs.getString(2) %>",
            "sRunningTime" : "<%=rs.getString(3) %>",
            "sAddress" : "<%=rs.getString(4) %>",
            "sImage" : "<%=rs.getString(5) %>",
            "sPackaging" : "<%=rs.getString(6) %>",
            "sComment" : "<%=rs.getString(7) %>",
            "storekeeper_skSeqNo" : "<%=rs.getString(8) %>",
            "likeCount" : "<%=rs.getString(9) %>",
            "reviewCount" : "<%=rs.getString(10) %>",
            "skStatus" : "<%=rs.getString(11) %>"
            
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