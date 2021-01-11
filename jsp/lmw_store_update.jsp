<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	int result = 0; // 수정 확인 

	PreparedStatement ps = null;

	try{
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection conn_mysql = DriverManager.getConnection(url_mysql,id_mysql,pw_mysql);
	    Statement stmt_mysql = conn_mysql.createStatement();
	
        String A = "update tify.store set sName = '" + sName + "', sTelNo = '" + sTelNo + "', sRunningTime = '" + sRunningTime + "', sAddress = '" + sAddress + "', sImage = '" + sImage + "', sPackaging = " + sPackaging + ", sComment = '" + sComment;
        String B = "' where storekeeper_skSeqNo = " + storekeeper_skSeqNo;

        ps = conn_mysql.prepareStatement(A+B);

        
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