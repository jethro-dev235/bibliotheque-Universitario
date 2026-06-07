<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Abonne</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container col-lg-8">
    <h1 class="mb-4">${abonne.id > 0 ? 'Modifier abonne' : 'Nouvel abonne'}</h1>
    <c:if test="${not empty errors}">
        <div class="alert alert-danger"><ul class="mb-0"><c:forEach var="e" items="${errors}"><li>${e}</li></c:forEach></ul></div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/abonnes">
        <input type="hidden" name="action" value="${abonne.id > 0 ? 'update' : 'create'}">
        <c:if test="${abonne.id > 0}"><input type="hidden" name="id" value="${abonne.id}"></c:if>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label class="form-label">Nom *</label>
                <input type="text" class="form-control" name="nom" value="${abonne.nom}" required>
            </div>
            <div class="col-md-6 mb-3">
                <label class="form-label">Prenom *</label>
                <input type="text" class="form-control" name="prenom" value="${abonne.prenom}" required>
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label">Email *</label>
            <input type="email" class="form-control" name="email" value="${abonne.email}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Telephone</label>
            <input type="text" class="form-control" name="telephone" value="${abonne.telephone}">
        </div>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label class="form-label">Date inscription *</label>
                <input type="date" class="form-control" name="dateInscription" value="${abonne.dateInscription}" required>
            </div>
            <div class="col-md-6 mb-3">
                <label class="form-label">Statut</label>
                <select class="form-select" name="statut">
                    <option value="ACTIF" ${abonne.statut == 'ACTIF' ? 'selected' : ''}>Actif</option>
                    <option value="SUSPENDU" ${abonne.statut == 'SUSPENDU' ? 'selected' : ''}>Suspendu</option>
                </select>
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Enregistrer</button>
        <a href="${pageContext.request.contextPath}/abonnes" class="btn btn-secondary">Annuler</a>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
