<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="components/header.jsp" %>
<main class="container my-5"><h1>Create account</h1><c:if test="${not empty error}"><p role="alert"><c:out value="${error}"/></p></c:if>
<form method="post" action="${pageContext.request.contextPath}/register" class="mx-auto" style="max-width:500px">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
<label for="username">Username</label><input class="form-control mb-3" id="username" name="username" required minlength="3" maxlength="50" pattern="[A-Za-z0-9_-]+" autocomplete="username"/>
<label for="email">Email</label><input class="form-control mb-3" id="email" name="email" type="email" required maxlength="100" autocomplete="email"/>
<label for="password">Password (12–72 characters)</label><input class="form-control mb-3" id="password" name="password" type="password" required minlength="12" maxlength="72" autocomplete="new-password"/>
<button class="btn btn-primary" type="submit">Create account</button><a class="btn btn-link" href="${pageContext.request.contextPath}/login">Sign in</a></form></main>
<%@ include file="components/footer.jsp" %>
