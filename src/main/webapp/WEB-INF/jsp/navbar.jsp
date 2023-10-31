<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg bg-body-tertiary"
	data-bs-theme="dark">
	<div class="container-fluid">
		<a class="navbar-brand" style="color: #198754" href="/">Paye Tes Dettes</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
				<li class="nav-item"><a class="nav-link active" style="color: #198754" aria-current="page" href="/event">Evènements</a></li>
			</ul>
			<form method="POST" action="/event/search" class="d-flex me-auto" role="search">
				<input class="form-control me-2" size="25" id="search-bar" name="keyword" required="true" type="search" placeholder="Rechercher un évènement..." aria-label="Champ de recherche d'un évènement"/>
				<button class="btn btn-outline-success" type="submit">Rechercher</button>
			</form>
			<ul class="nav justify-content-end">
				<li class="nav-item"><a class="btn btn-success mx-1"
					href="/profile"><i class="fa-solid fa-user"></i>   Mon profil</a></li>
				<li class="nav-item"><a class="btn btn-danger" href="/logout"><i class="fa-solid fa-right-from-bracket"></i>   Se déconnecter</a></li>
			</ul>
		</div>
	</div>
</nav>