<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Emprunts en cours</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container">
    <jsp:include page="../includes/flash.jsp"/>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Emprunts en cours</h1>
        <a href="${pageContext.request.contextPath}/emprunts?action=new" class="btn btn-primary">Nouvel emprunt</a>
    </div>
    <table class="table table-hover">
        <thead class="table-light">
        <tr><th>Livre</th><th>Abonne</th><th>Emprunt</th><th>Retour prevu</th><th>Statut</th><th>Actions</th></tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${emprunts}">
            <tr>
                <td>${e.titreLivre}</td>
                <td>${e.nomAbonne}</td>
                <td><fmt:formatDate value="${e.dateEmpruntFmt}" pattern="dd/MM/yyyy" type="date"/></td>
                <td><fmt:formatDate value="${e.dateRetourPrevueFmt}" pattern="dd/MM/yyyy" type="date"/></td>
                <td>
                    <c:choose>
                        <c:when test="${e.statut == 'EN_RETARD'}"><span class="badge bg-danger">En retard</span></c:when>
                        <c:otherwise><span class="badge bg-warning text-dark">En cours</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/emprunts?action=retour&id=${e.id}" class="btn btn-sm btn-success">Enregistrer retour</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty emprunts}">
            <tr><td colspan="6" class="text-center text-muted">Aucun emprunt en cours.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
