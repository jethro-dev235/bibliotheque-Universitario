<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Penalites</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container">
    <jsp:include page="../includes/flash.jsp"/>
    <h1 class="mb-4">Penalites</h1>
    <c:if test="${not empty totauxAbonnes}">
        <div class="card shadow-sm mb-4">
            <div class="card-header">Total impaye par abonne</div>
            <ul class="list-group list-group-flush">
                <c:forEach var="t" items="${totauxAbonnes}">
                    <li class="list-group-item d-flex justify-content-between">
                        <span>${t.abonne.prenom} ${t.abonne.nom}</span>
                        <strong class="text-danger">${t.total} FCFA</strong>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    <table class="table table-hover">
        <thead class="table-light">
        <tr><th>Emprunt</th><th>Abonne</th><th>Montant</th><th>Date calcul</th><th>Statut</th><th>Action</th></tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${penalites}">
            <tr>
                <td>${p.titreLivre}</td>
                <td>${p.nomAbonne}</td>
                <td>${p.montant} FCFA</td>
                <td><fmt:formatDate value="${p.dateCalculFmt}" pattern="dd/MM/yyyy" type="date"/></td>
                <td>
                    <c:choose>
                        <c:when test="${p.statutPaiement == 'PAYE'}"><span class="badge bg-success">Paye</span></c:when>
                        <c:otherwise><span class="badge bg-danger">Impaye</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${p.statutPaiement != 'PAYE'}">
                        <form method="post" action="${pageContext.request.contextPath}/penalites" class="d-inline">
                            <input type="hidden" name="id" value="${p.id}">
                            <button type="submit" class="btn btn-sm btn-success">Marquer paye</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
