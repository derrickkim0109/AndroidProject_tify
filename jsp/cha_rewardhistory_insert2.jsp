<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        

<%
	request.setCharacterEncoding("utf-8");
    String rhContent = request.getParameter("rhContent");
    int user_uNo = Integer.parseInt(request.getParameter("user_uNo"));
    int rhChoice = Integer.parseInt(request.getParameter("rhChoice"));
    String rhPointHow = request.getParameter("rhPointHow");
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
	
        String A = "insert into tify.rewardhistory(rhContent,user_uNo,rhChoice,rhPointHow,rhDay) value (?, ?, ?, ?,now())";


        ps = conn_mysql.prepareStatement(A);

        ps.setString(1, rhContent);
        ps.setInt(2, user_uNo);
        ps.setInt(3, rhChoice);
        ps.setString(4, rhPointHow);

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