<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="components/header.jsp" %>

<div class="container my-5">

    <h2 class="text-center mb-4 text-primary">Groups I Own</h2>

    <c:choose>
        <c:when test="${not empty ownedGroups}">
            <c:forEach var="group" items="${ownedGroups}">
                <div class="card mb-4 shadow-sm border border-primary-subtle">
                    <div class="card-body">
                        <h4 class="card-title fw-bold mb-2 text-primary">Group Name: <c:out value="${group.groupName}"/></h4>
                        <p class="card-text mb-1"><strong>Description:</strong> <c:out value="${group.description}"/></p>
                        <p class="card-text mb-1"><strong>Statistics:</strong></p>
                        <span class="badge bg-secondary me-2">Members: ${group.memberCount}</span>
                        <span class="badge bg-info text-dark">Posts: ${group.postCount}</span>
                        <div class="mt-3">
						    <form method="get" action="${pageContext.request.contextPath}/posts/group/${group.groupId}" class="d-inline">
						        <button type="submit" class="btn btn-outline-primary btn-sm">View Posts</button>
						    </form>
						    <form method="get" action="${pageContext.request.contextPath}/groups/${group.groupId}/members" class="d-inline">
						        <button type="submit" class="btn btn-outline-primary btn-sm">View Members</button>
						    </form>
						</div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="text-center text-muted">You don't own any groups yet.</p>
        </c:otherwise>
    </c:choose>


    <h2 class="text-center mt-5 mb-4 text-primary">Groups I'm a Member Of</h2>

    <c:choose>
        <c:when test="${not empty memberGroups}">
            <c:forEach var="group" items="${memberGroups}">
                <div class="card mb-4 shadow-sm border border-success-subtle">
                    <div class="card-body">
                        <h4 class="card-title fw-bold mb-2 text-primary">Group Name: <c:out value="${group.groupName}"/></h4>
                        <p class="card-text mb-1"><strong>Description:</strong> <c:out value="${group.description}"/></p>
                        <p class="card-text mb-1"><strong>Owner:</strong> ${group.owner.username}</p>
                        <p class="card-text mb-1"><strong>Statistics:</strong></p>
                        <span class="badge bg-secondary me-2">Members: ${group.memberCount}</span>
                        <span class="badge bg-info text-dark">Posts: ${group.postCount}</span>
                        <div class="mt-3">
						    <form method="get" action="${pageContext.request.contextPath}/posts/group/${group.groupId}" class="d-inline">
						        <button type="submit" class="btn btn-outline-primary btn-sm">View Posts</button>
						    </form>
						    <form method="get" action="${pageContext.request.contextPath}/groups/${group.groupId}/members" class="d-inline">
						        <button type="submit" class="btn btn-outline-primary btn-sm">View Members</button>
						    </form>
						</div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="text-center text-muted">You're not a member of any groups yet.</p>
        </c:otherwise>
    </c:choose>

</div>

<%@ include file="components/footer.jsp" %>
