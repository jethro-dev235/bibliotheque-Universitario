<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Abonnes</title>
    <jsp:include page="../includes/head.jsp"/>
</head>
<body>
<jsp:include page="../includes/navbar.jsp"/>
<div class="container">
    <jsp:include page="../includes/flash.jsp"/>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Abonnes</h1>
        <a href="${pageContext.request.contextPath}/abonnes?action=new" class="btn btn-primary">Ajouter</a>
    </div>
    <div class="table-responsive">
        <table class="table table-hover">
            <thead class="table-light">
            <tr><th>Nom</th><th>Email</th><th>Telephone</th><th>Inscription</th><th>Statut</th><th>Actions</th></tr>
            </thead>
            <tbody>
            <c:forEach var="a" items="${abonnes}">
                <tr>
                    <td>${a.prenom} ${a.nom}</td>
                    <td>${a.email}</td>
                    <td>${a.telephone}</td>
                    <td><fmt:formatDate value="${a.dateInscriptionFmt}" pattern="dd/MM/yyyy" type="date"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${a.statut == 'ACTIF'}"><span class="badge bg-success">Actif</span></c:when>
                            <c:otherwise><span class="badge bg-secondary">Suspendu</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-nowrap">
                        <a href="${pageContext.request.contextPath}/abonnes?action=historique&id=${a.id}" class="btn btn-sm btn-outline-info">Historique</a>
                        <a href="${pageContext.request.contextPath}/abonnes?action=edit&id=${a.id}" class="btn btn-sm btn-outline-primary">Modifier</a>
                        <c:choose>
                            <c:when test="${a.statut == 'ACTIF'}">
                                <a href="${pageContext.request.contextPath}/abonnes?action=suspendre&id=${a.id}" class="btn btn-sm btn-outline-warning">Suspendre</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/abonnes?action=reactiver&id=${a.id}" class="btn btn-sm btn-outline-success">Reactiver</a>
                            </c:otherwise>
                        </c:choose>
                        <a href="${pageContext.request.contextPath}/abonnes?action=delete&id=${a.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Supprimer ?')">Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
