<%@ page isELIgnored = "false" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html lang="fr">


<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>Inscription</title>


<body style="background-color: #eee;" >

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

	<%String error = request.getParameter("error");
	
	
	if(error!=null && error.equals("passwordNotSecure")) { %>
		
		<div class="alert alert-danger" role="alert">
	  		Votre mot de passe n'est pas assez sécurisé !
		</div>
	<% } %>

	<section>
		<div class="container py-5 h-100">
			<div class="row d-flex justify-content-center align-items-center h-100">
				<div class="col-xl-10">
					<div class="card rounded-3 text-black">
						<div class="row g-0">
							<div class="col-lg-6">
								<div class="card-body p-md-5 mx-md-4">

									<div class="text-center">
										<img src="img/logo.png" style="width: 150px; height: 145px" alt=" " id="pageLogo"/>
										<h4 class="mt-1 mb-5 pb-1">Paye tes dettes</h4>
									</div>

									<form:form method="POST" action="/user/register" id="registerForm" modelAttribute="user">
										<p>Inscription</p>
										
										<div class="form-outline mb-4">
											<form:input path="email" name="login" type="text" id="login"
												class="form-control" placeholder="Adresse e-mail" required="true" />
										</div>

										<div class="form-outline mb-4">
											<form:input path="password" name="password" type="password" id="password"
												class="form-control pr-password" placeholder="Mot de passe" required="true" />
										</div>
										
										<div class="form-outline mb-4">
											<input name="passwordConfirm" type="password" id="passwordConfirm"
												class="form-control" placeholder="Mot de passe (confirmation)" required />
										</div>
										

										<div class="form-outline mb-4">
											<form:input path="login" name="login" type="text" id="login"
												class="form-control" placeholder="Pseudo" required="true" />
										</div>

										<div class="text-center pt-1 mb-2 pb-1">
											<input
												class="btn btn-success btn-block fa-lg mb-2"
												type="submit" onclick="return formSubmit();" value="S'inscrire" />
										</div>
									</form:form>

										<div class="d-flex align-items-center justify-content-center pb-4">
											<p class="mb-0 me-2">Déjà inscrit ?</p>
											<a href="login">
												<button type="button" class="btn btn-outline-success">Se connecter</button>
											</a>
										</div>



								</div>
							</div>
							<div class="col-lg-6 d-flex align-items-center" style="background-color: #0a9e36">
								<div class="text-white px-3 py-4 p-md-5 mx-md-4">
									<h4 class="mb-4">Paye tes dettes :</h4>
									<p class="small mb-0">Paye tes dettes règle les soucis de dettes entre amis.<br>
										Il vous permets d'organiser toutes vos dépenses de groupes.
										C'est complètement gratuit et sans téléchargement!
									</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	
	<!-- Script de sécurisation des mots de passes et de la confirmation -->
	<script>
	
		function formSubmit() {
			if(document.getElementById("password").value != document.getElementById("passwordConfirm").value) {
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
		
		$('#password').passtrength({
			  minChars: 8
		});
		

	</script>
	
	
	<script>
		interval=10
		var inter = setInterval(everySecond, interval);
		
		rotate=2;
		function everySecond(){
			clearInterval(inter);
			document.getElementById("pageLogo").style.transform = 'rotate('+rotate+'deg)';
			
			rotate+=2
			interval=10
			
			if(rotate==180) {
				rotate=2
				interval=10000
			}
			inter = setInterval(everySecond, interval);
		}
	
	
	</script>
</body>
</html>