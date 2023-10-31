<%@ page isELIgnored = "false" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="da2i.payetesdettes.entities.User"%>

<% User user = (User) session.getAttribute("user"); %>

<!DOCTYPE html>
<html lang="fr">

	<%@ include file="/WEB-INF/jsp/header.jsp" %>

	<title>Index</title>

	<%@ include file="/WEB-INF/jsp/navbar.jsp" %>

	<h1>Vous êtes connecté en tant que ${ user.getLogin() }</h1>
	<h1>Votre id : ${ user.getId() }</h1>
	<h1>Votre email : ${ user.getEmail() }</h1>
	<h1>Votre mot de passe : ${ user.getPassword() }</h1>
	<h1>Vous êtes admin : ${ user.isAdmin() }</h1>
	<h1>Date d'inscription : <%= user.getRegistrationDate() %></h1>
	<h1>Date de dernière connexion : <%= user.getLastLoginDate() %></h1>
</html>