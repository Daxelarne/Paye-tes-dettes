<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="fr">
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
    <link rel="stylesheet" href="css/error.css">

    <div class="row justify-content-center">
        <div class="col-md-12 col-sm-12">
            <div class="card shadow-lg border-0 rounded-lg mt-5 mx-auto" style="width: 30rem;">
                <h3 class="card-header display-1 text-muted text-center">
                    Oops..
                </h3>

                <span class="card-subtitle mt-3 mb-2 text-muted text-center">
                    Une erreur est survenue.
                </span>

                <div class="card-body mx-auto">
                    <a type="button" href="/"
                       class="btn btn-sm btn-success text-white">Retour à l'accueil</a>
                </div>
            </div>
        </div>
    </div>
</html>