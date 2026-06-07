<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Emprunts en retard</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container">
    <jsp:include page="../includes/flash.jsp"/>
    <h1 class="mb-4">Emprunts en retard</h1>
    <p class="text-muted">Penalite : 50 FCFA par jour de retard</p>
    <table class="table table-hover">
        <thead class="table-light">
        <tr><th>Livre</th><th>Abonne</th><th>Retour prevu</th><th>Jours retard</th><th>Penalite (FCFA)</th><th>Statut</th></tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${emprunts}">
            <tr>
                <td>${e.titreLivre}</td>
                <td>${e.nomAbonne}</td>
                <td><fmt:formatDate value="${e.dateRetourPrevueFmt}" pattern="dd/MM/yyyy" type="date"/></td>
                <td><span class="badge bg-danger">${e.joursRetard}</span></td>
                <td><strong>${e.montantPenalite}</strong></td>
                <td><span class="badge bg-danger">En retard</span></td>
            </tr>
        </c:forEach>
        <c:if test="${empty emprunts}">
            <tr><td colspan="6" class="text-center text-muted">Aucun retard.</td></tr>
        </c:if>
        </tbody>
    </table>
    <a href="${pageContext.request.contextPath}/penalites" class="btn btn-outline-danger">Voir penalites</a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
