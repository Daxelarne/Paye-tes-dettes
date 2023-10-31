<%@ page isELIgnored = "false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="da2i.payetesdettes.entities.User, java.io.File"%>
<% User user = (User) session.getAttribute("user"); %>
<% User publicUser = (User) request.getAttribute("user"); %>

    
	<%@ include file="/WEB-INF/jsp/header.jsp" %>

	<title>Profil de ${ user.getLogin() }</title>
	
	<%@ include file="/WEB-INF/jsp/navbar.jsp" %>

<body>
	<section class="container">
	
		<h2>Profil public de ${ user.getLogin() }</h2>
		
		<br/>
				<%
					//Recuperation de l'image du client, default_profile.png si aucune de dispo
					File userDir = new File("src/main/webapp/profileImg/"+publicUser.getId());
					String profilImagePath = "../img/default_profile.png";
				if(userDir.listFiles() != null && userDir.listFiles().length>0) {
					System.out.println(""+publicUser.getId());
					profilImagePath="../profileImg/"+publicUser.getId()+"/"+userDir.listFiles()[0].getName();
				}
				%>
				<div>
					<img src="<%=profilImagePath%>" class="rounded float-left" alt="Photo de profil" width=200 style="padding: 5px; background-color: #ced4da;"/>
				</div>
				<br/>
				<hr/>
				<ul>
					<li>Inscrit depuis le ${user.getRegistrationDate()}</li>
					<li>Dernière connexion le ${user.getLastLoginDate	()}</li>
				</ul>
	</section>
</body>
</html>