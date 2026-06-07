<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm mb-4">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/dashboard">
            <i class="bi bi-book-half"></i> Biblio Univ
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard">Tableau de bord</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/livres">Livres</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/abonnes">Abonnes</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/emprunts">Emprunts</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/emprunts?action=retards">Retards</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/penalites">Penalites</a></li>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <span class="nav-link text-white-50">
                        <c:if test="${not empty sessionScope.utilisateur}">
                            ${sessionScope.utilisateur.prenom} ${sessionScope.utilisateur.nom}
                        </c:if>
                    </span>
                </li>
                <li class="nav-item">
                    <a class="nav-link btn btn-outline-light btn-sm ms-2" href="${pageContext.request.contextPath}/auth?action=logout">Deconnexion</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
