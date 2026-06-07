<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title><c:choose><c:when test="${livre.id > 0}">Modifier</c:when><c:otherwise>Nouveau</c:otherwise></c:choose> livre</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container col-lg-8">
    <jsp:include page="../includes/flash.jsp"/>
    <h1 class="mb-4"><c:choose><c:when test="${livre.id > 0}">Modifier le livre</c:when><c:otherwise>Nouveau livre</c:otherwise></c:choose></h1>
    <c:if test="${not empty errors}">
        <div class="alert alert-danger">
            <ul class="mb-0">
                <c:forEach var="err" items="${errors}"><li>${err}</li></c:forEach>
            </ul>
        </div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/livres">
        <input type="hidden" name="action" value="${livre.id > 0 ? 'update' : 'create'}">
        <c:if test="${livre.id > 0}"><input type="hidden" name="id" value="${livre.id}"></c:if>
        <div class="mb-3">
            <label class="form-label">Titre *</label>
            <input type="text" class="form-control" name="titre" value="${livre.titre}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Auteur *</label>
            <input type="text" class="form-control" name="auteur" value="${livre.auteur}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">ISBN *</label>
            <input type="text" class="form-control" name="isbn" value="${livre.isbn}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Categorie *</label>
            <input type="text" class="form-control" name="categorie" value="${livre.categorie}" required>
        </div>
        <div class="row">
            <div class="col-md-4 mb-3">
                <label class="form-label">Annee *</label>
                <input type="number" class="form-control" name="anneePublication" value="${livre.anneePublication}" required>
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label">Exemplaires *</label>
                <input type="number" class="form-control" name="nbExemplaires" value="${livre.nbExemplaires > 0 ? livre.nbExemplaires : 1}" required>
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label">Disponibles</label>
                <input type="number" class="form-control" name="nbDisponibles" value="${livre.nbDisponibles}">
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Enregistrer</button>
        <a href="${pageContext.request.contextPath}/livres" class="btn btn-secondary">Annuler</a>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
