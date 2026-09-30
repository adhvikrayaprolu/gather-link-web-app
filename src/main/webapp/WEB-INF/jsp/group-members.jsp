<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="components/header.jsp" %>

<div class="container mt-5">
    <h2 class="text-center mb-4 text-primary">Members of "<c:out value="${group.groupName}"/>"</h2>

    <div class="table-responsive">
        <table class="table table-striped table-bordered shadow-sm custom-table">
            <thead>
                <tr>
                    <th>User ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                </tr>
            </thead>
            <tbody>
                <!-- Group owner as first row -->
                <tr>
                    <td>${group.owner.userId}</td>
                    <td>${group.owner.username}</td>
                    <td>${group.owner.email}</td>
                    <td><span class="badge bg-primary">OWNER</span></td>
                </tr>

                <!-- Other members -->
                <c:forEach var="membership" items="${memberships}">
                    <c:if test="${membership.user.userId != group.owner.userId}">
                        <tr>
                            <td>${membership.user.userId}</td>
                            <td><c:out value="${membership.user.username}"/></td>
                            <td><c:out value="${membership.user.email}"/></td>
                            <td>
							    <c:choose>
							        <c:when test="${sessionScope.loggedInUser.userId == group.owner.userId}">
							            <form action="${pageContext.request.contextPath}/groups/${group.groupId}/members/${membership.user.userId}/role" method="post" class="d-flex">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							                <select name="role" class="form-select form-select-sm me-2">
							                    <option value="MEMBER" <c:if test="${membership.role == 'MEMBER'}">selected</c:if>>MEMBER</option>
							                    <option value="MODERATOR" <c:if test="${membership.role == 'MODERATOR'}">selected</c:if>>MODERATOR</option>
							                    <option value="ADMIN" <c:if test="${membership.role == 'ADMIN'}">selected</c:if>>ADMIN</option>
							                </select>
							                <button type="submit" class="btn btn-sm btn-outline-success">Update</button>
							            </form>
							        </c:when>
							
							        <c:otherwise>
							            <c:choose>
							                <c:when test="${membership.role == 'MODERATOR'}">
							                    <span class="badge bg-warning text-dark">MODERATOR</span>
							                </c:when>
							                <c:when test="${membership.role == 'ADMIN'}">
							                    <span class="badge bg-danger">ADMIN</span>
							                </c:when>
							                <c:otherwise>
							                    <span class="badge bg-secondary">MEMBER</span>
							                </c:otherwise>
							            </c:choose>
							        </c:otherwise>
							    </c:choose>
							</td>
                        </tr>
                    </c:if>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="components/footer.jsp" %>
