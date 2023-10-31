<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored = "false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page import="da2i.payetesdettes.entities.User"%>
<%@ page import="da2i.payetesdettes.entities.Event, java.io.File"%>
<%@ page import="java.util.List,java.text.*"%>

<% User user = (User) session.getAttribute("user"); %>
<% Event newEvent = (Event) request.getAttribute("event"); %>
<% List<Event> myEventListAsOwner = (List<Event>) request.getAttribute("myEventListAsOwner"); %>
<% List<Event> myEventListAsParticipant = (List<Event>) request.getAttribute("myEventListAsParticipant"); %>
<% List<Event> publicEventList= (List<Event>) request.getAttribute("publicEventList"); %>
<!DOCTYPE html>
<html>
	<%@ include file="/WEB-INF/jsp/header.jsp" %>

	<title>Évènements</title>
	
	<%@ include file="/WEB-INF/jsp/navbar.jsp" %>
		
		<!-- Modal form ajout d'évènement -->
		<div class="modal fade" id="createEventModal" tabindex="-1" aria-labelledby="createEventModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="createEventModalLabel">Créer un évènement</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <form:form method="POST" action="/event/new" modelAttribute="event">
		      <div class="modal-body">
		      
					<!-- Form d'ajout d'évènement -->
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Titre de l'évènement</label>
						<form:input class="form-control" type="text" path="title" required="true"/>
					</div>
					
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Description</label>
						<form:textarea class="form-control" type="text" path="description"></form:textarea>
					</div>
					
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Visibilité</label>
						<form:select class="form-control" path="visibility">
							<form:options items="${visibilityEnum}" />
						</form:select>
					</div>
					
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">État</label>
						<form:select class="form-control" path="state">
							<form:options items="${stateEnum}" />
						</form:select>
					</div>
					
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Date de début</label>
						<form:input class="form-control" type="date" id="startDate" path="startDate" value="<%= newEvent.getStartDate() %>" required="true" />
					</div>
					
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Date de fin</label>
						<form:input class="form-control" type="date" id="endDate" path="endDate" value="<%= newEvent.getEndDate() %>" required="true" />
					</div>
		      
			      </div>
			      <div class="modal-footer">
			        <button type="submit" class="btn btn-primary" data-bs-dismiss="modal">Créer</button>
			        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
			      </div>
		      </form:form>
		    </div>
		  </div>
		</div>
		
		<button onclick="deployModal()" class="btn btn-primary mx-2" style="float: right;"><i class="fa-solid fa-plus"></i> Créer un évènement</button>
		
		<div class="m-1">
		    <ul class="nav nav-tabs" id="myTab">
		        <li class="nav-item">
		            <a href="#owner" class="nav-link active" data-bs-toggle="tab">Mes évènements</a>
		        </li>
		        <li class="nav-item">
		            <a href="#participant" class="nav-link" data-bs-toggle="tab">Les évènements auxquelles je participe</a>
		        </li>
		        <li class="nav-item">
		            <a href="#public" class="nav-link" data-bs-toggle="tab">Les évènements publics</a>
		        </li>
		    </ul>
		    <div class="tab-content">
		        <div class="tab-pane fade show active" id="owner">
					<div class="container">
						 <div class="row justify-content-center">
						 	<% if (myEventListAsOwner.size() == 0) { %>
						 		<h1 class="text-center mt-5">Aucun évènement</h1>
						 	<% } %>
							<% for(Event event : myEventListAsOwner) { %>
						    <div class="col-sm-3 mx-2 mt-3">
						      <div class="card w-100">
								  <%
						      		String imgPath = "eventImg/"+event.getId()+"/"+event.getDefaultImg();
						      		if(event.getDefaultImg()!=null) {
								  %>
								  <img class="card-img-top" src="<%=imgPath %>" alt="Event image">
								  <% } else { %>
								  		<img class="card-img-top" src="http://www.ipsgroup.fr/wp-content/uploads/2013/12/default_image_01-1024x1024-570x321.png" alt="Event image">
								  <% } %>
								  <div class="card-body">
								    <h5 class="card-title"><%= event.getTitle() %></h5>
								    <p class="card-text"><%= event.getDescription() %></p>
								    <a href="/event/details/<%= event.getId() %>" class="btn btn-primary">Voir le détail</a>
								  </div>
								</div>
						    </div>
							<% } %>
						</div>
					</div>
		        </div>
		        <div class="tab-pane fade" id="participant">
					<div class="container">
						 <div class="row justify-content-center">
						 	<% if (myEventListAsParticipant.size() == 0) { %>
						 		<h1 class="text-center mt-5">Aucun évènement</h1>
						 	<% } %>
							<% for(Event event : myEventListAsParticipant) { %>
						    <div class="col-sm-3 mx-2 mt-3">
						      <div class="card w-100">
								  <% 
						      		String imgPath = "eventImg/"+event.getId()+"/"+event.getDefaultImg();
						      		if(event.getDefaultImg()!=null) {
						      				
						      			%>
						      			
						      			<img class="card-img-top" src="<%=imgPath %>" alt="Event image">
						      			
						      		<% } else { %>
						      			
								  		<img class="card-img-top" src="http://www.ipsgroup.fr/wp-content/uploads/2013/12/default_image_01-1024x1024-570x321.png" alt="Event image">
								 	<% } %>
								  <div class="card-body">
								    <h5 class="card-title"><%= event.getTitle() %></h5>
								    <p class="card-text"><%= event.getDescription() %></p>
								    <a href="/event/details/<%= event.getId() %>" class="btn btn-primary">Voir le détail</a>
								  </div>
								</div>
						    </div>
							<% } %>
							
								 
						</div>
					</div>
				</div>
		        <div class="tab-pane fade" id="public">
		        	<div class="container">
						 <div class="row justify-content-center">
						 	<% if (publicEventList.size() == 0) { %>
						 		<h1 class="text-center mt-5">Aucun évènement</h1>
						 	<% } %>
							<% for(Event event : publicEventList) { %>
						    <div class="col-sm-3 mx-2 mt-3">
						      <div class="card w-100">
								  <% 
						      		String imgPath = "eventImg/"+event.getId()+"/"+event.getDefaultImg();
						      		if(event.getDefaultImg()!=null) {
						      				
						      			%>
						      			
						      			<img class="card-img-top" src="<%=imgPath %>" alt="Event image">
						      			
						      		<% } else { %>
						      			
								  		<img class="card-img-top" src="http://www.ipsgroup.fr/wp-content/uploads/2013/12/default_image_01-1024x1024-570x321.png" alt="Event image">
								 	<% } %>
								  <div class="card-body">
								    <h5 class="card-title"><%= event.getTitle() %></h5>
								    <p class="card-text"><%= event.getDescription() %></p>
								    <a href="/event/details/<%= event.getId() %>" class="btn btn-primary">Voir le détail</a>
								  </div>
								</div>
						    </div>
							<% } %>
						</div>
					</div>
		        </div>
		    </div>
		</div>
		
		<script>
			function deployModal() {
				$("#createEventModal").modal("show");
			}
		</script>
		
	
	</body>
</html>