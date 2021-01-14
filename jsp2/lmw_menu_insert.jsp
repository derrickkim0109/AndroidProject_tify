<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	
    int storekeeper_skSeqNo = Integer.parseInt(request.getParameter("storekeeper_skSeqNo"));
	String mName = request.getParameter("mName");
    int mPrice = Integer.parseInt(request.getParameter("mPrice"));
    int mSizeUp = Integer.parseInt(request.getParameter("mSizeUp"));
    int mShut = Integer.parseInt(request.getParameter("mShut"));
	String mImage = request.getParameter("mImage");
    int mType = Integer.parseInt(request.getParameter("mType"));
    String mComment = request.getParameter("mComment");
	

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
	
		String A = "insert into tify.menu(storekeeper_skSeqNo, mName, mPrice, mSizeUp, mShut, mImage, mType, mComment) values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	    ps = conn_mysql.prepareStatement(A);
		ps.setInt(1, storekeeper_skSeqNo);
		ps.setString(2, mName);
        ps.setInt(3, mPrice);
        ps.setInt(4, mSizeUp);
        ps.setInt(5, mShut);
        ps.setString(6, mImage);
        ps.setInt(7, mType);
        ps.setString(8, mComment);
    
	

		
	
	    
	    
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