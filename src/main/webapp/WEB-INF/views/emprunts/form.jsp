<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Nouvel emprunt</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container col-lg-8">
    <jsp:include page="../includes/flash.jsp"/>
    <h1 class="mb-4">Nouvel emprunt</h1>
    <c:if test="${not empty errors}">
        <div class="alert alert-danger"><ul class="mb-0"><c:forEach var="e" items="${errors}"><li>${e}</li></c:forEach></ul></div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/emprunts">
        <input type="hidden" name="action" value="create">
        <div class="mb-3">
            <label class="form-label">Livre *</label>
            <select class="form-select" name="idLivre" required>
                <option value="">-- Choisir --</option>
                <c:forEach var="l" items="${livres}">
                    <option value="${l.id}">${l.titre} (${l.nbDisponibles} dispo.)</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Abonne *</label>
            <select class="form-select" name="idAbonne" required>
                <option value="">-- Choisir --</option>
                <c:forEach var="a" items="${abonnes}">
                    <option value="${a.id}">${a.prenom} ${a.nom}</option>
                </c:forEach>
            </select>
        </div>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label class="form-label">Date emprunt *</label>
                <input type="date" class="form-control" name="dateEmprunt" value="${dateEmprunt}" required>
            </div>
            <div class="col-md-6 mb-3">
                <label class="form-label">Date retour prevue *</label>
                <input type="date" class="form-control" name="dateRetourPrevue" value="${dateRetourPrevue}" required>
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Enregistrer</button>
        <a href="${pageContext.request.contextPath}/emprunts" class="btn btn-secondary">Annuler</a>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
