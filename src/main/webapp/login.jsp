<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - E-Book Store</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <div class="auth-container">
            <div class="auth-image">
                <img src="<%=request.getContextPath()%>/images/login-image.jpg" alt="Login">
                <div class="auth-image-content">
                    <h3>Welcome Back!</h3>
                    <p>Access your account to explore our vast collection of e-books, track your orders, and discover new titles.</p>
                </div>
            </div>
            <div class="auth-form">
                <h2>Login</h2>

                <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
                <% } %>

                <% if (request.getAttribute("successMessage") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMessage") %>
                </div>
                <% } %>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <div class="input-with-icon">
                            <i class="fas fa-user"></i>
                            <input type="text" id="username" name="username" placeholder="Enter your username" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <div class="input-with-icon">
                            <i class="fas fa-lock"></i>
                            <input type="password" id="password" name="password" placeholder="Enter your password" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Login <i class="fas fa-sign-in-alt"></i></button>
                    </div>
                </form>

                <div class="auth-links">
                    <p>Don't have an account? <a href="<%=request.getContextPath()%>/register">Register</a></p>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />

    <script src="js/script.js"></script>
</body>
</html>
