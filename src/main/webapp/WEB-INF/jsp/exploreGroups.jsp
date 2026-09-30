<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="components/header.jsp" %>

<div class="container my-5">

    <div class="text-end mb-4">
	    <a href="${pageContext.request.contextPath}/groups/create" class="btn btn-primary">+ Create Group</a>
	</div>

    <h2 class="text-center mb-4 text-primary">Explore Groups</h2>
    <c:forEach items="${groups}" var="group">
        <div class="card mb-4 shadow-sm border border-primary-subtle">
            <div class="card-body">
                <h4 class="card-title fw-bold mb-2 text-primary">Group Name: <c:out value="${group.groupName}"/></h4>
                <p class="card-text mb-1"><strong>Description:</strong> <c:out value="${group.description}"/></p>
                <p class="card-text mb-1"><strong>Group Creator:</strong> ${group.owner.username}</p>
                <p class="card-text mb-3"><strong>Statistics:</strong></p>
                <span class="badge bg-secondary me-2">Members: ${group.memberCount}</span>
                <span class="badge bg-info text-dark">Posts: ${group.postCount}</span>
                <div class="mt-3">
                    <form method="post" action="${pageContext.request.contextPath}/groups/${group.groupId}/join">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					    <button type="submit" class="btn btn-outline-primary btn-sm">Join Group</button>
					</form>
                </div>
            </div>
        </div>
    </c:forEach>

</div>

<%@ include file="components/footer.jsp" %>
