<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="components/header.jsp" %>

<div class="container mt-5">
    <h1 class="text-center mb-4 text-primary">All Users on GatherLink</h1>

    <div class="table-responsive">
        <table id="usersTable" class="table table-striped table-bordered shadow-sm custom-table">
            <thead>
                <tr>
                    <th>User ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Group Memberships</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.userId}</td>
                        <td><c:out value="${user.username}"/></td>
                        <td><c:out value="${user.email}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty userGroupsMap[user.userId]}">
                                    <c:forEach var="groupName" items="${userGroupsMap[user.userId]}" varStatus="loop">
                                        <c:out value="${groupName}"/><c:if test="${!loop.last}">, </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">No Groups</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="components/footer.jsp" %>
