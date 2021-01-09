<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	
    int user_uSeqNo = Integer.parseInt(request.getParameter("user_uSeqNo"));
    int store_sSeqNo = Integer.parseInt(request.getParameter("store_sSeqNo"));
	String store_sName = request.getParameter("store_sName");
    String menu_mName = request.getParameter("menu_mName");
    int cLPrice = Integer.parseInt(request.getParameter("cLPrice"));
    int cLQuantity = Integer.parseInt(request.getParameter("cLQuantity"));
    String cLImage = request.getParameter("cLImage");
    int cLSizeUp = Integer.parseInt(request.getParameter("cLSizeUp"));
    int cLAddShot = Integer.parseInt(request.getParameter("cLAddShot"));
    String cLRequest = request.getParameter("cLRequest");
    
    


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
	
		String A = "insert into tify.cartlist(user_uSeqNo, storekeeper_skSeqNo, store_sName, menu_mName, cLPrice, cLQuantity, cLImage, cLSizeUp, cLAddShot, cLRequest) value (?, ?, ?, ? ,? ,?, ?, ?, ?, ?)";
		
	    ps = conn_mysql.prepareStatement(A);
        ps.setInt(1, user_uSeqNo);
        ps.setInt(2, store_sSeqNo);
		ps.setString(3, store_sName);
        ps.setString(4, menu_mName);
        ps.setInt(5, cLPrice);
        ps.setInt(6, cLQuantity);
        ps.setString(7, cLImage);
        ps.setInt(8, cLSizeUp);
        ps.setInt(9, cLAddShot);
        ps.setString(10, cLRequest);

        
		
	    
	    
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