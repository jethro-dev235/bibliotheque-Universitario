<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Connexion - Bibliotheque Universitaire</title>
    <jsp:include page="includes/head.jsp"/>
</head>
<body class="login-body d-flex align-items-center">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow-lg border-0">
                <div class="card-body p-5">
                    <h2 class="text-center mb-4 text-primary"><i class="bi bi-book-half"></i> Biblio Univ</h2>
                    <p class="text-muted text-center">Gestion de bibliotheque universitaire</p>
                    <c:if test="${not empty errorLogin}">
                        <div class="alert alert-danger">${errorLogin}</div>
                    </c:if>
                    <jsp:include page="includes/flash.jsp"/>
                    <form method="post" action="${pageContext.request.contextPath}/auth">
                        <div class="mb-3">
                            <label class="form-label" for="email">Email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="${email}" required placeholder="admin@biblio.com">
                        </div>
                        <div class="mb-4">
                            <label class="form-label" for="password">Mot de passe</label>
                            <input type="password" class="form-control" id="password" name="password"
                                   required placeholder="admin123">
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Se connecter</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
