<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <div class="container">
        <div class="error-container">
            <h1>Oops! Something went wrong</h1>
            <p>We're sorry, but an error occurred while processing your request.</p>
            <p>Error code: <%= response.getStatus() %></p>
            <a href="<%=request.getContextPath()%>/home" class="btn btn-primary">Go to Home Page</a>
        </div>
    </div>
    
    <jsp:include page="footer.jsp" />
    
    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
