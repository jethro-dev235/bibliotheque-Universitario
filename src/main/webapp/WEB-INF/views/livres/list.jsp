<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Livres</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container">
    <jsp:include page="../includes/flash.jsp"/>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Livres</h1>
        <a href="${pageContext.request.contextPath}/livres?action=new" class="btn btn-primary"><i class="bi bi-plus-lg"></i> Ajouter</a>
    </div>
    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/livres" class="row g-3">
                <div class="col-md-3">
                    <input type="text" class="form-control" name="titre" placeholder="Titre" value="${titre}">
                </div>
                <div class="col-md-3">
                    <input type="text" class="form-control" name="auteur" placeholder="Auteur" value="${auteur}">
                </div>
                <div class="col-md-3">
                    <input type="text" class="form-control" name="isbn" placeholder="ISBN" value="${isbn}">
                </div>
                <div class="col-md-2">
                    <input type="text" class="form-control" name="categorie" placeholder="Categorie" value="${categorie}">
                </div>
                <div class="col-md-1">
                    <button type="submit" class="btn btn-secondary w-100">Chercher</button>
                </div>
            </form>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light">
            <tr>
                <th>Titre</th><th>Auteur</th><th>ISBN</th><th>Categorie</th><th>Annee</th><th>Exemplaires</th><th>Disponibles</th><th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="l" items="${livres}">
                <tr>
                    <td>${l.titre}</td>
                    <td>${l.auteur}</td>
                    <td>${l.isbn}</td>
                    <td>${l.categorie}</td>
                    <td>${l.anneePublication}</td>
                    <td>${l.nbExemplaires}</td>
                    <td>
                        <c:choose>
                            <c:when test="${l.nbDisponibles > 0}">
                                <span class="badge bg-success">${l.nbDisponibles}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-danger">0</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/livres?action=edit&id=${l.id}" class="btn btn-sm btn-outline-primary">Modifier</a>
                        <a href="${pageContext.request.contextPath}/livres?action=delete&id=${l.id}"
                           class="btn btn-sm btn-outline-danger"
                           onclick="return confirm('Supprimer ce livre ?')">Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty livres}">
                <tr><td colspan="8" class="text-center text-muted">Aucun livre trouve.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
