<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Tableau de bord</title>
    <jsp:include page="includes/head.jsp"/>
</head>
<body>
<jsp:include page="includes/navbar.jsp"/>
<div class="container">
    <jsp:include page="includes/flash.jsp"/>
    <h1 class="mb-4">Tableau de bord</h1>
    <div class="row g-4">
        <div class="col-md-6 col-lg-3">
            <div class="card stat-card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="bi bi-book fs-1 text-primary"></i>
                    <h3 class="mt-2">${countLivres}</h3>
                    <p class="text-muted mb-0">Livres</p>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-3">
            <div class="card stat-card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="bi bi-people fs-1 text-success"></i>
                    <h3 class="mt-2">${countAbonnes}</h3>
                    <p class="text-muted mb-0">Abonnes actifs</p>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-3">
            <div class="card stat-card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="bi bi-arrow-left-right fs-1 text-warning"></i>
                    <h3 class="mt-2">${countEmprunts}</h3>
                    <p class="text-muted mb-0">Emprunts en cours</p>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-3">
            <div class="card stat-card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="bi bi-exclamation-triangle fs-1 text-danger"></i>
                    <h3 class="mt-2">${countRetards}</h3>
                    <p class="text-muted mb-0">Retards</p>
                </div>
            </div>
        </div>
    </div>
    <div class="row mt-5">
        <div class="col-12">
            <div class="card shadow-sm">
                <div class="card-header bg-white"><h5 class="mb-0">Actions rapides</h5></div>
                <div class="card-body d-flex flex-wrap gap-2">
                    <a href="${pageContext.request.contextPath}/livres?action=new" class="btn btn-outline-primary">Nouveau livre</a>
                    <a href="${pageContext.request.contextPath}/abonnes?action=new" class="btn btn-outline-success">Nouvel abonne</a>
                    <a href="${pageContext.request.contextPath}/emprunts?action=new" class="btn btn-outline-warning">Nouvel emprunt</a>
                    <a href="${pageContext.request.contextPath}/penalites" class="btn btn-outline-danger">Voir penalites</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
