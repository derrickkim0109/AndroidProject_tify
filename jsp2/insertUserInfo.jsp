<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	
 
    String uEmail = request.getParameter("uEmail");
    String uNickName = request.getParameter("uNickName");
    String uTelNo = request.getParameter("uTelNo");
    String uImage = request.getParameter("uImage");
    String uPayPassword = request.getParameter("uPayPassword");
	

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
	
		String A = "insert into tify.user(uEmail,uNickName,uTelNo,uImage,uPayPassword,uInsertDate) values (?,?,?,?,?,now())";
	
	    ps = conn_mysql.prepareStatement(A);
	
        ps.setString(1,uEmail);
        ps.setString(2,uNickName);
        ps.setString(3,uTelNo);
        ps.setString(4,uImage);
        ps.setString(5,uPayPassword);
  
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