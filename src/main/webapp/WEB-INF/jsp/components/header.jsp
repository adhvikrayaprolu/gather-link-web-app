<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    com.gather_link.model.Users user = (com.gather_link.model.Users) session.getAttribute("loggedInUser");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>GatherLink</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/styles.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark custom-navbar">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">GL</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav" aria-controls="navbarNav"
                aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/explore-groups">Explore Groups</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/my-groups">My Groups</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users">Users</a></li>

                <c:choose>
                    <c:when test="${not empty sessionScope.loggedInUser}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <c:out value="${sessionScope.loggedInUser.username}"/>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">Edit Profile</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><form method="post" action="${pageContext.request.contextPath}/logout"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="dropdown-item" type="submit">Logout</button></form></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
