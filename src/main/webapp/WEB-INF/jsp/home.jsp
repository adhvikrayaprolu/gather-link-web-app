<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ include file="components/header.jsp" %>

<main>
    <h1 class="mb-4">Welcome to GatherLink!</h1>
    <p>Explore, connect, and collaborate with like-minded people on this Interest Group Platform!</p>
    
    <br>
    <br>
    
    <p><b>Welcome, <c:out value="${key.username}"/>!</b></p>
</main>

<%@ include file="components/footer.jsp" %>
