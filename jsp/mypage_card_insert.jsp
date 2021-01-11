<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	
    int user_uNo = Integer.parseInt(request.getParameter("user_uNo"));

	String cCardNo = request.getParameter("cCardNo");
	String cYMM = request.getParameter("cYMM");
    String cPassword = request.getParameter("cPassword");
    String cBirthday = request.getParameter("cBirthday");
    String cInfo = request.getParameter("cInfo");
    String cCardCompany = request.getParameter("cCardCompany");

	

//------
	String url_mysql = "jdbc:mysql://localhost/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
	String id_mysql = "root";
	String pw_mysql = "qwer1234";
    int result = 0; // 입력 확인 

	PreparedStatement ps = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
	    Statement stmt_mysql = conn_mysql.createStatement();
	
		String A = "insert into tify.creditcard(user_uNo, cCardNo, cYMM, cPassword, cBirthday, cInfo, cCardCompany) values (?, ?, ?, ?, ?, ?, ?)";
	
	    ps = conn_mysql.prepareStatement(A);
		ps.setInt(1, user_uNo);
		ps.setString(2, cCardNo);
        ps.setString(3, cYMM);
        ps.setString(4, cPassword);
        ps.setString(5, cBirthday);
        ps.setString(6, cInfo);
		ps.setString(7, cCardCompany);
	

		
	
	    
	    
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
%>
		{
			"result" : "<%=result%>"
		}
<%	
        e.printStackTrace();
	}
	
%>