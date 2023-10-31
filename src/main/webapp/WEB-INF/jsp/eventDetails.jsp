<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="da2i.payetesdettes.entities.User"%>
<%@ page import="da2i.payetesdettes.entities.Event"%>
<%@ page import="da2i.payetesdettes.entities.Transaction"%>
<%@ page import="da2i.payetesdettes.utils.DisplayUtil"%>
<%@ page import="java.time.LocalDateTime, java.io.File"%>
<%@ page import="da2i.payetesdettes.entities.SuggestedTransaction" %>
<%@ page import="da2i.payetesdettes.entities.Comment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList, java.time.format.DateTimeFormatter" %>

<% Event event = (Event) request.getAttribute("event"); %>
<% User user = (User) session.getAttribute("user"); %>
<% DisplayUtil displayUtil = new DisplayUtil<User>(); %>

<% List<SuggestedTransaction> suggestedTransactions = (List<SuggestedTransaction>) request.getAttribute("suggestedTransactions");
if (suggestedTransactions == null) suggestedTransactions = new ArrayList<>();


List<Comment> comments = event.getComments(); 

DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm ");
%>

<!DOCTYPE html>
<html lang="fr">
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>${ event.getTitle() }</title>

<%@ include file="/WEB-INF/jsp/navbar.jsp" %>

<body>
	<section class="container">
		<a class="btn btn-success" href="/event"><i class="fa-solid fa-arrow-left"></i> Retour</a>
		
		<h2 style="display:inline-block; transform: translateY(8px); padding-left: 15px;">   ${ event.getTitle() }</h2>
		<hr/>
			<% if (event.getParticipants().contains(user)) { %>
				<a class="btn btn-danger" href="/event/leave/<%= event.getId() %>" style="float: right;">Quitter l'évènement</a>
			<% } else { %>
				<a class="btn btn-primary" href="/event/join/<%= event.getId() %>" style="float: right;">Rejoindre l'évènement</a>
			<% } %>
			<div class="mb-3 ">
				
				
				<p><b>Description :</b><br/>
				${ event.getDescription() }</p>
				
				<p><b>Propriétaire :</b><br/>
				${ event.getOwner().getLogin() }</p>
				
				<p><b>Visibilité :</b><br/>
				${ event.getVisibility() }</p>
				
				<p><b>État :</b><br/>
				${ event.getState() }</p>
				
				<p><b>Participants :</b><br/>
				
				<% for (User participant : event.getParticipants()) {
					if(participant.equals(user)) {
						out.print("<a href=/profile>"+participant.getLogin()+"</a> | solde : "+event.getBalance().get(participant)+"€<br/>");
					} else {
						out.print("<a href=/profile/"+participant.getId()+">"+participant.getLogin()+"</a> | solde : "+event.getBalance().get(participant)+"€<br/>");
					}
					
				}%>
				</p>
				
				
				
				
				 
				<div class="row">
					<div class="col-sm-2">
						<p><b>Date de création :</b><br/>
						<%= event.getCreationDate() %></p>
					</div>
					<div class="col-sm-2">
						<p><b>Date de début :</b><br/>
						<%= event.getStartDateString() %></p>
					</div>
					<div class="col-sm-2">
						<p><b>Date de fin :</b><br/>
						<%= event.getEndDateString() %></p>
					</div>
				</div>
				
			</div>
				 <hr>
				 <div class="accordion" id="accordionExample">
					  <div class="accordion-item">
					    <h2 class="accordion-header" id="headingOne">
					      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
					        Transactions
					      </button>
					    </h2>
					    <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
					      <div class="accordion-body">
					      	<% if (event.getParticipants().contains(user)) { %>
					      		<a class="btn btn-primary" onClick="showNewTransactionModal()" style="float: right;">Nouvelle transaction</a>
					      		<br/><br/>
					      		<hr>
					      	<% } 
					      	   if (event.getTransactions().isEmpty()) { %>
					      		<h4 class="text-center"> Aucune transaction</h4>
					      	<% } else { 
						      		for (Transaction transaction : event.getTransactions()) {
					      				out.print(transaction.getDateString()+" <br/>");
					      				out.print("Montant : "+transaction.getAmount()+ "€ <br/>");
					      				out.print("Raison : "+transaction.getReason()+"<br/>");
					      				out.print("Expéditeur : "+transaction.getSender()+" | Receveurs : "+ displayUtil.displayList(transaction.getReceivers()));
					      				out.print("<hr>");
					      			}
					      	} %>
					      </div>
					    </div>
					  </div>
					   <div class="accordion-item">
						 <h2 class="accordion-header" id="headingSuggestedTransac">
							 <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseSuggestedTransac" aria-expanded="false" aria-controls="collapseSuggestedTransac">
								 Transactions suggérées
							 </button>
						 </h2>
						 <div id="collapseSuggestedTransac" class="accordion-collapse collapse" aria-labelledby="headingSuggestedTransac" data-bs-parent="#accordionExample">
							 <div class="accordion-body">
								 <p>Pour équilibrer toutes les dépenses, nous vous suggérons d'effectuer les transactions suivantes : </p>
								 <hr>
								 <% if (suggestedTransactions.isEmpty()) { %>
								 <h4 class="text-center"> Aucune transaction à faire</h4>
								 <% } else {
									 for (SuggestedTransaction suggestedTransaction : suggestedTransactions) {
										 out.print(suggestedTransaction.toString()+" <br/>");
									 }
								 } %>
							 </div>
						 </div>
					 </div>
					  <div class="accordion-item">
					    <h2 class="accordion-header" id="headingTwo">
					      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
					        Commentaires
					      </button>
					    </h2>
					    <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#accordionExample">
					      <div class="accordion-body">
					       		<form:form method="POST" action="/comment/new/${ event.getId() }" modelAttribute="comment">
							      <div class="row g-3 align-items-center" style="float: right;">
							      
							      		<div class="col-auto">
							      			<label class="form-label">Commentaire</label>
							      		</div>
							      
										<div class="col-auto">
											<form:input type="text" path="event" value="${event.getId()}" hidden="true"/>
								      		<form:input type="text" path="sender" value="${user.getId()}" hidden="true"/>
											
											<form:input class="form-control" type="text" path="message" size="40" required="true" />
											<form:input type="text" path="date" value="${LocalDateTime.now()}" hidden="true" />
										</div>
										
										<div class="col-auto" style="float: right">
											<button type="submit" class="btn btn-primary" >Poster mon commentaire</button>
										</div>
								   </div>
							      </form:form>
					       		
					       		
					       		
					       		<br/><br/>
					       		<hr/>
					       		<div class="container">
					       		<%if(comments.size()==0){ %>
					       			<h4 class="text-center"> Aucun commentaire à afficher</h4>
					       		<% } else {%>
					       			<%for(Comment c : comments) {
					       				if(c.getSender().equals(user)) {
					       					out.println("<div class=\"row justify-content-end\">");
					       					
					       					out.println("<div class=\"col-5\" style=\"border-radius: 7px; background-color: #e7f1ff; margin-bottom:5px; padding-top: 8px; \"><h5>Moi</h5><p>"+c.getMessage()+"<span style=\"color: #0c8fee; float: right;\">"+c.getDate().format(formatter)+"</span></p></div>");
					       					
					       					out.println("</div>");
					       				} else {
					       					out.println("<div class=\"row justify-content-start\">");
					       					
					       					out.println("<div class=\"col-5\" style=\"border-radius: 7px; background-color: #edeff0; margin-bottom:5px; padding-top: 8px; \"><h5><a href=/profile/"+c.getSender().getId()+" style=\"outline: none; text-decoration: none; color: black;\">"+c.getSender().getLogin() 
					       					+ "</a></h5><p>"+c.getMessage()+"<span style=\"color: #87898a; float: right;\">"+c.getDate().format(formatter)+"</span></p></div>");
					       					
					       					out.println("</div>");
					       				}
					       				
					       					
					       				
					       			 } %>
					       		<% } %>
					       		</div>
					       		<!-- Liste des coms -->
					      </div>
					    </div>
					  </div>
					  
					  
					  <div class="accordion-item">
					    <h2 class="accordion-header" id="heading3">
					      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse3" aria-expanded="false" aria-controls="collapse3">
					        Photos
					      </button>
					    </h2>
					    <div id="collapse3" class="accordion-collapse collapse" aria-labelledby="heading3" data-bs-parent="#accordionExample">
					      <div class="accordion-body" style="position: relative;">
					       		<form class="form-inline" method="POST" action="/event/uploadImg/${ event.getId() }" enctype="multipart/form-data">
					       			<input type="file" class="form-control lg-2 mb-2 mr-sm-2" id="file" aria-describedby="emailHelp"  name="file" accept="image/png,image/jpg,image/jpeg,image/svg" required/>
					       			<input type="submit" class="btn btn-primary" value="Ajouter la photo"/>
					       		</form>
					       		
					       		
					       		<%//Recuperation de l'image du client, default_profile.png si aucune de dispo
									File eventDir = new File("src/main/webapp/eventImg/"+event.getId());	
									
									
									if(eventDir.listFiles() != null && eventDir.listFiles().length>0) {
										out.println("<br/>");
										
										String src="";
										String fileName="";
										for(int i =0; i < eventDir.listFiles().length ; i++) { 
											src="eventImg/"+event.getId()+"/"+eventDir.listFiles()[i].getName();
											fileName=eventDir.listFiles()[i].getName();
										%>
										
										<span class="artist-collection-photo">
											
											
												<img width="200" height="200" src="/<%=src %>" class="rounded" style="padding: 5px; background-color: #ced4da;">
												<a href="/event/deleteImg/${event.getId() }/<%=fileName%>"><img src="/img/remove.png"  width=20 style=" margin-bottom: 15%; transform: translateX(-100%)" title="Supprimer l'image"/></a>
										</span>
											
										
											<!-- <img src="/<%=src %>" class="rounded float-left" width=200 style="padding: 5px; background-color: #ced4da;"/> 
											<img src="/img/remove.png"  width=20 style=" margin-bottom: 15%; transform: translateX(-100%)"/> -->
											
										<% }
									
									} else {
										out.println("<br/>Aucune photo n'est présente pour cet événement ! Soyez le premier à ajouter une photo !");
									}%>
					       		
					      </div>
					    </div>
					  </div>
					  
				</div>
				
				<!-- Modal form ajout d'évènement -->
		<div class="modal fade" id="createTransactionModal" tabindex="-1" aria-labelledby="createTransactionModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="createTransactionModalLabel">Ajouter une transaction</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <form:form method="POST" action="/transaction/new/${ event.getId() }" modelAttribute="newTransaction">
		      <div class="modal-body">
		      
		 
		      		
		      		<form:input class="form-control" type="text" path="date" value="<%= LocalDateTime.now() %>" hidden="true" />
		      
					<div class="mb-3">
						<label class="form-label">Raison</label>
						<form:input class="form-control" type="text" path="reason"/>
					</div>
					
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Montant</label>
						<form:input class="form-control" type="number" path="amount" required="true"/>
					</div>
					
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Expéditeur</label>
						<form:select class="form-control" path="sender" required="true">
							<form:option value="${ user }" label="${ user.getLogin() }"/>
						</form:select>
					</div>
					
					<div class="mb-3">
						<p class="mb-0">Receveurs</p><hr>
						<% for (User participant : event.getParticipants()) { %>
							<form:checkbox path="receivers" value="<%= participant %>" /> <% out.print(participant.getLogin()); %> <br/>
						<% } %>
					</div>
					
			   </div>
			      <div class="modal-footer">
			        <button type="submit" class="btn btn-primary" data-bs-dismiss="modal">Ajouter</button>
			        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
			      </div>
		      </form:form>
		    </div>
		  </div>
		</div>

	</section>
	<script>
		function showNewTransactionModal() {
			$("#createTransactionModal").modal("show");
		}
	</script>
</body>
</html>
