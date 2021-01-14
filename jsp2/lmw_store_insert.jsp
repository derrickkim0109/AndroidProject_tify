<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	
    int storekeeper_skSeqNo = Integer.parseInt(request.getParameter("storekeeper_skSeqNo"));
	String sName = request.getParameter("sName");
    String sTelNo = request.getParameter("sTelNo");
    String sRunningTime = request.getParameter("sRunningTime");
    String sAddress = request.getParameter("sAddress");
    String sImage = request.getParameter("sImage");
    int sPackaging = Integer.parseInt(request.getParameter("sPackaging"));
	String sComment = request.getParameter("sComment");
	

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
	
		String A = "insert into tify.store(storekeeper_skSeqNo, sName, sTelNo, sRunningTime, sAddress, sImage, sPackaging, sComment) values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	    ps = conn_mysql.prepareStatement(A);
		ps.setInt(1, storekeeper_skSeqNo);
		ps.setString(2, sName);
        ps.setString(3, sTelNo);
        ps.setString(4, sRunningTime);
        ps.setString(5, sAddress);
        ps.setString(6, sImage);
        ps.setInt(7, sPackaging);
        ps.setString(8, sComment);
    
	

		
	
	    
	    
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