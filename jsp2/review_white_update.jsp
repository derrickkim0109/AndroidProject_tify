<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        

<%
	request.setCharacterEncoding("utf-8");
    String rContent = request.getParameter("rContent");
    String rImage = request.getParameter("rImage");
    int user_uNo = Integer.parseInt(request.getParameter("user_uNo"));
    int storekeeper_skSeqNo = Integer.parseInt(request.getParameter("storekeeper_skSeqNo"));

//------

	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";
	int result = 0; // 수정 확인 

	PreparedStatement ps = null;

	try{
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
	    Statement stmt_mysql = conn_mysql.createStatement();
	
        String A = "insert into tify.review(rContent,rImage,user_uNo,storekeeper_skSeqNo,rInsertDate) value (?, ?, ?, ?, now())";


        ps = conn_mysql.prepareStatement(A);

        ps.setString(1, rContent);
        ps.setString(2, rImage);
        ps.setInt(3, user_uNo);
        ps.setInt(4, storekeeper_skSeqNo);

        result = ps.executeUpdate();
        
%>
		{
			"result" : "<%=result%>"
		}

<%	
	
	    conn_mysql.close();

	} 

	catch (Exception e){
%>
		{
			"result" : "<%=result%>"
		}
<%			
	    e.printStackTrace();

	}

%>