<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register - E-Book Store</title>
        <link rel="stylesheet" href="css/style.css">
    </head>

    <body>
        <jsp:include page="header.jsp" />

        <div class="container">
            <div class="auth-container">
                <div class="auth-image">
                    <img src="<%=request.getContextPath()%>/images/register-image.jpg" alt="Register">
                    <div class="auth-image-content">
                        <h3>Join Our Community</h3>
                        <p>Create an account to access exclusive deals, save your favorite books, and enjoy a personalized reading experience.</p>
                    </div>
                </div>
                <div class="auth-form">
                    <h2>Register</h2>

                    <% if (request.getAttribute("errorMessage") !=null) { %>
                        <div class="alert alert-error">
                            <%= request.getAttribute("errorMessage") %>
                        </div>
                    <% } %>

                    <form action="<%=request.getContextPath()%>/register" method="post">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <div class="input-with-icon">
                                <i class="fas fa-user"></i>
                                <input type="text" id="username" name="username" placeholder="Choose a username" required>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="password">Password</label>
                            <div class="input-with-icon">
                                <i class="fas fa-lock"></i>
                                <input type="password" id="password" name="password" placeholder="Create a password" required>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="confirmPassword">Confirm Password</label>
                            <div class="input-with-icon">
                                <i class="fas fa-check-circle"></i>
                                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm your password" required>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="email">Email (Gmail only)</label>
                            <div class="input-with-icon">
                                <i class="fas fa-envelope"></i>
                                <input type="email" id="email" name="email" placeholder="Enter your Gmail address" pattern="[a-zA-Z0-9._%+-]+@gmail\.com$" title="Please enter a valid Gmail address" required>
                            </div>
                            <small class="form-text">Must be a valid Gmail address (e.g., example@gmail.com)</small>
                        </div>

                        <div class="form-group">
                            <label for="fullName">Full Name</label>
                            <div class="input-with-icon">
                                <i class="fas fa-id-card"></i>
                                <input type="text" id="fullName" name="fullName" placeholder="Enter your full name" required>
                            </div>
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">Register <i class="fas fa-user-plus"></i></button>
                        </div>
                    </form>

                    <div class="auth-links">
                        <p>Already have an account? <a href="<%=request.getContextPath()%>/login">Login</a></p>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp" />

        <script src="js/script.js"></script>
    </body>

    </html>