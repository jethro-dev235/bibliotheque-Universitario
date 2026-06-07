<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Historique emprunts</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container">
    <h1 class="mb-4">Historique - ${abonne.prenom} ${abonne.nom}</h1>
    <a href="${pageContext.request.contextPath}/abonnes" class="btn btn-secondary mb-3">Retour</a>
    <table class="table table-hover">
        <thead class="table-light">
        <tr><th>Livre</th><th>Emprunt</th><th>Retour prevu</th><th>Retour effectif</th><th>Statut</th></tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${historique}">
            <tr>
                <td>${e.titreLivre}</td>
                <td><fmt:formatDate value="${e.dateEmpruntFmt}" pattern="dd/MM/yyyy" type="date"/></td>
                <td><fmt:formatDate value="${e.dateRetourPrevueFmt}" pattern="dd/MM/yyyy" type="date"/></td>
                <td>
                    <c:if test="${not empty e.dateRetourEffective}">
                        <fmt:formatDate value="${e.dateRetourEffectiveFmt}" pattern="dd/MM/yyyy" type="date"/>
                    </c:if>
                    <c:if test="${empty e.dateRetourEffective}">—</c:if>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${e.statut == 'RETOURNE'}"><span class="badge bg-success">Retourne</span></c:when>
                        <c:when test="${e.statut == 'EN_RETARD'}"><span class="badge bg-danger">En retard</span></c:when>
                        <c:otherwise><span class="badge bg-warning text-dark">En cours</span></c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
