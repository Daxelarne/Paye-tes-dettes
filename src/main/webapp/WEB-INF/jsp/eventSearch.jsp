<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored = "false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page import="da2i.payetesdettes.entities.User"%>
<%@ page import="da2i.payetesdettes.entities.Event, java.io.File"%>
<%@ page import="java.util.List,java.text.*"%>

<% User user = (User) session.getAttribute("user"); %>
<% Event newEvent = (Event) request.getAttribute("event"); %>
<% String keyword = (String) request.getAttribute("keyword"); %>
<% List<Event> resultsEvents = (List<Event>) request.getAttribute("resultsEvents"); %>
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>Résultat de votre recherche</title>

<%@ include file="/WEB-INF/jsp/navbar.jsp" %>

<body>
<hr/>
<div class="container">
  <div class="row justify-content-center">
    <% if (resultsEvents.size() == 0) { %>
    <h1 class="text-center mt-5">Aucun résultat pour votre recherche.</h1>
    <% } else { %>
    <h1 class="text-center">Résultats de votre recherche : </h1>
    <% for(Event event : resultsEvents) { %>
    <div class="col-sm-3 mx-2 mt-3">
      <div class="card w-100">
        <%
          String imgPath = "../../eventImg/"+event.getId()+"/"+event.getDefaultImg();
          if(event.getDefaultImg() != null) {

        %>

        <img class="card-img-top" src="<%= imgPath %>" alt="Event image">

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
    <% } %>


  </div>
</div>
</body>
<script>
  document.getElementById("search-bar").value = "<%= keyword %>"
</script>
</html>