<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="components/header.jsp" %>

<div class="container my-5">
    <h2 class="text-center mb-4 text-primary">
        Create a New Post for <span class="fw-bold"><c:out value="${group.groupName}"/></span>
    </h2>

    <form method="post" action="${pageContext.request.contextPath}/posts/create">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="groupId" value="${group.groupId}" />

        <div class="mb-3">
            <label for="content" class="form-label"><b>Post Content</b></label>
            <textarea name="content" id="content" class="form-control" maxlength="5000" rows="5" required></textarea>
        </div>

        <div class="text-center">
            <button type="submit" class="btn btn-success">Submit Post</button>
        </div>
    </form>
</div>

<%@ include file="components/footer.jsp" %>
