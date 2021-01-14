<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>        
<%
	request.setCharacterEncoding("utf-8");
	
    int user_uNo = Integer.parseInt(request.getParameter("user_uNo"));
    int order_oNo = Integer.parseInt(request.getParameter("order_oNo"));
    int store_sSeqNo = Integer.parseInt(request.getParameter("store_sSeqNo"));
	String store_sName = request.getParameter("store_sName");
    String menu_mName = request.getParameter("menu_mName");
    int olSizeUp = Integer.parseInt(request.getParameter("olSizeUp"));
    int olAddShot = Integer.parseInt(request.getParameter("olAddShot"));
    String olRequest = request.getParameter("olRequest");
    int olPrice = Integer.parseInt(request.getParameter("olPrice"));
    int olQuantity = Integer.parseInt(request.getParameter("olQuantity"));
    


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
	
		String A = "insert into orderlist(user_uNo, order_oNo, storekeeper_skSeqNo, store_sName, menu_mName, olSizeUp, olAddShot, olRequest, olPrice, olQuantity) value (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
	    ps = conn_mysql.prepareStatement(A);
        ps.setInt(1, user_uNo);
        ps.setInt(2, order_oNo);
        ps.setInt(3, store_sSeqNo);
		ps.setString(4, store_sName);
        ps.setString(5, menu_mName);
        ps.setInt(6, olSizeUp);
        ps.setInt(7, olAddShot);
        ps.setString(8, olRequest);
        ps.setInt(9, olPrice);
        ps.setInt(10, olQuantity);

        
		
	    
	    
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