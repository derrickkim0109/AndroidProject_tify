<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

	
<%@ page 
import="com.oreilly.servlet.MultipartRequest" 
import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"
import="java.util.*" 
import="java.io.*"
import="java.text.SimpleDateFormat"
%> 
 
 <% 
 String realPath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\ROOT\\tify"; // 저장할 디렉토리 (절대경로)

 

  int sizeLimit = 5 * 1024 * 1024;

  String now = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());  //현재시간

  // 5메가까지 제한 넘어서면 예외발생 

  try { 
	  MultipartRequest multi = new MultipartRequest(request, realPath, sizeLimit, new DefaultFileRenamePolicy());
	   /* Enumeration formNames = multi.getFileNames(); // 폼의 이름 반환 
	   String formName = (String) formNames.nextElement(); // 자료가 많을 경우엔 while 문을 사용 
	   String fileName = multi.getFilesystemName(formName); // 파일의 이름 얻기 */ 
	   /* multi.getFile("images"); */ 
		
	   
		
	   } catch (Exception e) {
		    out.print(e); out.print("예외 상황 발생..! ");
		}



%>
