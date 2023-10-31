<%@ page isELIgnored = "false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="da2i.payetesdettes.entities.User, java.io.File"%>
    
<% User user = (User) session.getAttribute("user"); %>

    
	<%@ include file="/WEB-INF/jsp/header.jsp" %>

	<title>Profil</title>
	
	<%@ include file="/WEB-INF/jsp/navbar.jsp" %>




<body>
	<div class="modal fade" id="badPasswordModal" tabindex="-1" aria-labelledby="badPasswordModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="badPasswordModalLabel">Mot de passe non sécurisé !</h5>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      <div class="modal-body">
	        S'il vous plaît, veuillez utiliser un mot de passe sécurisé en respectant les préconisations.
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Fermer</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" id="samePasswordModal" tabindex="-1" aria-labelledby="samePasswordModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="samePasswordModalLabel">Les mots de passe ne correspondent pas !</h5>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      <div class="modal-body">
	        Les deux mots de passe saisis ne correspondent pas, merci de saisir à deux reprises le même mot de passe.
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Fermer</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	
	


	<div class="modal fade" id="changeProfilePicture" tabindex="-1" aria-labelledby="changeProfilePictureLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="changeProfilePictureLabel">Changer votre photo de profil</h5>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      
	      <!-- HEEEELP -->
	      <form:form method="POST" action="/user/uploadFile" modelAttribute="userUploadFile" enctype="multipart/form-data">
		      <div class="modal-body">
				
				<form:input class="form-control " type="text" id="id" name="id" value="${ user.getId() }" hidden="true" path="id"/>
				 <form:input type="file" class="form-control" id="upfile" aria-describedby="emailHelp"  name="upfile" path="file" accept="image/png,image/jpg,image/jpeg,image/svg"/>
	
		      </div>
		      <div class="modal-footer">
		      	
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
		        <input type="submit" class="btn btn-primary" value="Valider" />
		        
		      </div>
		   </form:form>
		   
		   
	    </div>
	  </div>
	</div>
	
	

	<section class="container">
	
			<%
	String error = request.getParameter("error");
	
	
	if(error!=null) { 
	
		if(error.equals("errorUpload")) {
	%>
		
		<div class="alert alert-danger" role="alert">
	  		Votre image n'a pas pu être importé. Vérifier que sa taille soit inférieure à 10 Mb.
		</div>
	<% } else if(error.equals("emailAlreadyUsed")) { %>
		
		<div class="alert alert-danger" role="alert">
	  		Cet email est déjà utilisé !
		</div>
		
	<% } else if(error.equals("passwordNotTheSame")) { %>
	
		<div class="alert alert-danger" role="alert">
	  		L'ancien mot de passe saisi et celui enregistré ne correspondent pas
		</div>
		
	<% } else if(error.equals("passwordNotSecure")) { %>
	
		<div class="alert alert-danger" role="alert">
	  		Votre nouveau mot de passe n'est pas assez sécurisé !
		</div>
		
	<% } else if(error.equals("deleteProfilePicture")) { %>
	
		<div class="alert alert-danger" role="alert">
	  		Erreur lors de la suppression de l'image de profil
		</div>
		
	<% }} %>

	
	
		<h2>Bonjour ${ user.getLogin() }</h2>
		
		<br />
		
		
		
		
		
		
		
				<%
					//Recuperation de l'image du client, default_profile.png si aucune de dispo
					File userDir = new File("src/main/webapp/profileImg/"+user.getId());	
				
					String profilImagePath = "img/default_profile.png";
					
					
				if(userDir.listFiles() != null && userDir.listFiles().length>0) {
					profilImagePath="profileImg/"+user.getId()+"/"+userDir.listFiles()[0].getName();
				} 
				
				%>
				
				
		<!--<form:form method="POST" path="/user/deleteProfilePicture/${ user.getId() }"  modelAttribute="userUploadFile">-->
				<!-- Image de profil -->
				<div>
				
					<img src="<%=profilImagePath%>" class="rounded float-left" alt="Photo de profil" width=200 style="padding: 5px; background-color: #ced4da;">
						
					
				</div>
				
				<br/>
				
				<div>
					<button type="button" class="btn btn-outline-primary btn-sm" onclick="changePhoto()">Mettre à jour ma photo</button>
					
					
					<a href="/user/deleteProfilePicture/${ user.getId() }"><input type="button" class="btn btn-outline-danger btn-sm" value="Supprimer ma photo" /></a>
					
				</div>
		<!--</form:form>-->
				
				<hr />
		<form:form method="POST" action="/user/edit" id="profileForm" modelAttribute="user">
		      
				<form:input class="form-control " type="text" path="id" value="${ user.getId() }" hidden="true" />
		      
				<!-- Form de changement du profil -->
				<div class="mb-3 ">
					<label for="exampleInputEmail1" class="form-label">Mon pseudo *</label>
					<form:input class="form-control " type="text" path="newLogin" value="${ user.getLogin() }" required="true"/>
				</div>
				
				<div class="mb-3">
					<label for="exampleInputEmail1" class="form-label">Mon mail *</label>
					<form:input class="form-control" type="mail" path="newMail" value="${ user.getEmail() }" required="true"/>
				</div>
				
				<div class="mb-3">
					<label for="exampleInputEmail1" class="form-label">Ancien mot de passe *</label>
					<form:input path="oldPassword" name="oldPassword" type="password" id="oldPassword" class="form-control" required="true" />
				</div>
				
				<div class="mb-3">
					<label for="exampleInputEmail1" class="form-label">Nouveau mot de passe</label>
					<form:input path="newPassword" name="newPassword" type="password" id="newPassword" class="form-control pr-password" />
				</div>
				
				<div class="mb-3">
					<label for="exampleInputEmail1" class="form-label">Nouveau mot de passe (confirmation)</label>
					<input name="passwordConfirm" type="password" id="passwordConfirm" class="form-control"/>
				</div>
			
	      
		        <button type="submit" class="btn btn-primary" data-bs-dismiss="modal" onclick="return formSubmit();">Enregistrer les modifications</button>
	      </form:form>

	</section>
	
	
	<script>
	
		function changePhoto() {
			$("#changeProfilePicture").modal("show")
		}
	
		function formSubmit() {
			if(document.getElementById("newPassword").value != document.getElementById("passwordConfirm").value) {
				$("#samePasswordModal").modal("show")
				return false;
			} else if(securePercentage!=100){
				$("#badPasswordModal").modal("show")
				return false;
			} else {
				return true;
			}
		}
	
		$(function(){
			$(".pr-password").passwordRequirements();
		});

		$(".pr-password").passwordRequirements({
		  numCharacters: 8,
		  useLowercase: true,
		  useUppercase: true,
		  useNumbers: true,
		  useSpecial: true
		});
		
		$('#newPassword').passtrength({
			  minChars: 8
		});
		

	</script>
</body>
</html>