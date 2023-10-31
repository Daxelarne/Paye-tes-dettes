<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="da2i.payetesdettes.entities.User"%>
<%@ page import="da2i.payetesdettes.entities.Event"%>

<% Event event = (Event) request.getAttribute("event"); %>

<!DOCTYPE html>
<html lang="fr">

<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>${ event.getTitle() }</title>

<%@ include file="/WEB-INF/jsp/navbar.jsp" %>

<body>
	<section class="container">
	<a class="btn btn-success" href="/event/details/<%= event.getId() %>">Retour à l'évènement</a>
	
			<br/><br/>
			<% String error = request.getParameter("error");
			   String successAddingByEmail = request.getParameter("successAddingByEmail");
			if ("failedToAdd".equals(error)) { %>
				<div class="alert alert-danger" role="alert">
			  		Cet utilisateur n'a pas pu être ajouté. Vérifiez l'orthographe de son adresse e-mail et réessayez.
				</div>
			<% } else if (successAddingByEmail != null) {%>
				<div class="alert alert-success" role="alert">
			  		L'utilisateur <%= successAddingByEmail %> a bien été ajouté parmis les membres de cet évènement.
				</div>
			<% } %>
			
			
			<div class="mb-3 ">
				<h4>Ajoutez une nouvelle personne grâce à son adresse e-mail</h4>
				<div class="row">
					<div class="col-sm-2">
						<input type="text" class="form-control" id="userEmailInput" name="userEmail" placeholder="Adresse e-mail">
					</div>
					<div class="col-sm-2">
						<a class="btn btn-primary" onClick="getUrlAdd()">Ajouter</a>
					</div>
				</div>
			</div>
			
			<script>
				function getUrlAdd() {
					let baseUrl = '/event/add/<%= event.getId() %>/'
					let value = document.getElementById("userEmailInput").value
				  	window.location.href = baseUrl + value;
				}
			</script>
			
			<hr/>

			<div class="mb-3 ">
				<h3>Membres :<br/></h3>
				<p>
				<% for (User participant : event.getParticipants()) {
					out.print("<b>"+participant.getLogin()+"</b>");
					if (event.getOwner().equals(participant)) {%>
						(propriétaire de l'évènement - ne peut pas être viré ou rétrogradé)
					<% } else { %>
						 <% if (event.getAdministrators().contains(participant)) { %>
							<a class="btn btn-warning" href="/event/demote/<%= event.getId() %>/<%= participant.getId() %>">Rétrograder membre</a>
						<% } else { %>
							<a class="btn btn-success" href="/event/promote/<%= event.getId() %>/<%= participant.getId() %>">Promouvoir administrateur</a>
					    <%}%>
				    <a class="btn btn-danger" href="/event/kick/<%= event.getId() %>/<%= participant.getId() %>">Virer</a>
				    <% } %>
				    <br/><br/>
				<%}%></p>
			</div>
	</section>
</body>
</html>