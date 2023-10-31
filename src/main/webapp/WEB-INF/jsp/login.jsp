<%@ page isELIgnored = "false" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html lang="fr">


<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>Connexion</title>

<body style="background-color: #eee;">
	<section>
		<div class="container py-5 h-1000">
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
									
									<% String error = request.getParameter("error");
									   String success = request.getParameter("success");
									if ("loginFailed".equals(error)) { %>
										<div class="alert alert-danger" role="alert">
									  		Adresse e-mail ou mot de passe incorrect.
										</div>
									<% } else if ("emailNotConfirmed".equals(error)) { %>
										<div class="alert alert-danger" role="alert">
									  		Vous devez confirmer votre adresse e-mail pour vous connecter.
										</div>
									<% } else if ("confirmEmailInvalidSecurityKey".equals(error)) { %>
										<div class="alert alert-danger" role="alert">
									  		Votre clé de sécurité est invalide, confirmation de votre e-mail impossible.
	  										Veuillez contacter un administrateur.
										</div>
									<% } else if ("logout".equals(success)) { %>
										<div class="alert alert-success" role="alert">
									  		Vous avez été déconnecté avec succès.
										</div>
									<% } else if ("welcomeEmailSent".equals(success)) { %>
										<div class="alert alert-success" role="alert">
									  		Consultez votre boîte mail! Un e-mail vient de vous être envoyé pour activer votre compte.
										</div>
									<% } else if ("emailConfirmed".equals(success)) { %>
										<div class="alert alert-success" role="alert">
									  		Votre adresse e-mail a été confirmée avec succès. Vous pouvez désormais vous connectez.<br/>
									  		Merci!
										</div>
									<% }  else if ("resetEmailSent".equals(success)) {%>
										<div class="alert alert-success" role="alert">
									  		Consultez votre boîte mail! Un e-mail vient de vous être envoyé avec les instructions afin de réinitialiser votre mot de passe.
										</div>
									<% }  else if ("changePassword".equals(success)) {%>
										<div class="alert alert-success" role="alert">
									  		Votre mot de passe a bien été modifié! Vous pouvez désormais vous connecter à l'aide de votre nouveau mot de passe.
										</div>
									<% } %>
									<form method="POST" action="/login">
										<p>Connexion</p>

										<div class="form-outline mb-4">
											<input name="email" type="text" id="email"
												class="form-control" placeholder="Adresse e-mail" required />
										</div>

										<div class="form-outline mb-4">
											<input name="password" type="password" id="password"
												class="form-control" placeholder="Mot de passe" required />
										</div>

										<div class="text-center pt-1 mb-2 pb-1">
											<input
												class="btn btn-success btn-block fa-lg mb-2"
												type="submit" value="Se connecter"> <br/>
										</div>
									</form>
									
											
										<div class="d-flex align-items-center justify-content-center pb-2">
											<a class="mb-0 me-2" href="/forgotPassword">
												Mot de passe oublié ?
											</a>
										</div>

										<div class="d-flex align-items-center justify-content-center pb-4">
											<p class="mb-0 me-2">Pas encore de compte ?</p>
											<a href="/register">
												<button type="button" class="btn btn-outline-success">S'inscrire</button>
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