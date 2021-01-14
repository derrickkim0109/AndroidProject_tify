<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%	
   request.setCharacterEncoding("utf-8");
	int user_uNo = Integer.parseInt(request.getParameter("user_uNo"));

    int storekeeper_skSeqNo = Integer.parseInt(request.getParameter("storekeeper_skSeqNo"));
	String store_sName = request.getParameter("store_sName");
	int oSum = Integer.parseInt(request.getParameter("oSum"));
	String oCardName = request.getParameter("oCardName");
	int oCardNo = Integer.parseInt(request.getParameter("oCardNo"));



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
	
		String A = "insert into tify.order (user_uNo, storekeeper_skSeqNo, store_sName, oSum, oCardName, oCardNo, oInsertDate) values (?, ?, ?, ?, ?, ?, now())";
		
	    ps = conn_mysql.prepareStatement(A);
		
        ps.setInt(1, user_uNo);
        ps.setInt(2, storekeeper_skSeqNo);
        ps.setString(3, store_sName);
		ps.setInt(4, oSum);
        ps.setString(5, oCardName);
        ps.setInt(6, oCardNo);
    


	

		
	
	    
	    
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