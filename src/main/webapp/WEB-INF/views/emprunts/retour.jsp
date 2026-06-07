<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Enregistrer retour</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container col-lg-6">
    <h1 class="mb-4">Enregistrer le retour</h1>
    <p><strong>Livre :</strong> ${emprunt.titreLivre}</p>
    <p><strong>Abonne :</strong> ${emprunt.nomAbonne}</p>
    <form method="post" action="${pageContext.request.contextPath}/emprunts">
        <input type="hidden" name="action" value="retour">
        <input type="hidden" name="id" value="${emprunt.id}">
        <div class="mb-3">
            <label class="form-label">Date de retour *</label>
            <input type="date" class="form-control" name="dateRetour" required>
        </div>
        <button type="submit" class="btn btn-success">Confirmer le retour</button>
        <a href="${pageContext.request.contextPath}/emprunts" class="btn btn-secondary">Annuler</a>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
