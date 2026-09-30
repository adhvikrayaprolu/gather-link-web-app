<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="components/header.jsp" %>

<div class="container mt-5">
    <c:if test="${param.error != null}"><p role="alert">Invalid username or password</p></c:if><c:if test="${param.registered != null}"><p role="status">Account created. Sign in below.</p></c:if><h2 class="text-center mb-4 text-primary">Login</h2>
    <form action="${pageContext.request.contextPath}/login" method="post" class="mx-auto" style="max-width: 400px;">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input id="username" type="text" class="form-control" name="username" required />
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input id="password" type="password" class="form-control" name="password" required />
        </div>
        <button type="submit" class="btn btn-primary w-100">Login</button>
    </form><p class="text-center"><a href="${pageContext.request.contextPath}/register">Create an account</a></p>
</div>

<%@ include file="components/footer.jsp" %>
