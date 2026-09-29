<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="components/header.jsp" %>

<div class="container my-5">
    <div class="card shadow-sm border-0">
        <div class="card-body">
            <h3 class="card-title text-primary mb-4">Create a New Group</h3>
            <form method="post" action="${pageContext.request.contextPath}/groups/add">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="mb-3">
                    <label for="groupName" class="form-label"><b>Group Name</b></label>
                    <input type="text" id="groupName" name="groupName" maxlength="100" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label for="description" class="form-label"><b>Description</b></label>
                    <textarea id="description" name="description" maxlength="2000" class="form-control" rows="3" required></textarea>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-primary px-4">Create Group</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="components/footer.jsp" %>
