<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<header>
    <div class="container">
        <div class="header-content">
            <div class="logo">
                <a href="<%=request.getContextPath()%>/home">
                    <h1>E-Book Store</h1>
                </a>
            </div>

            <%
            User user = (User) session.getAttribute("user");
            boolean isAdmin = (user != null && user.isAdmin());
            if (!isAdmin) {
            %>
            <div class="search-bar">
                <form action="<%=request.getContextPath()%>/search" method="get">
                    <input type="text" name="keyword" placeholder="Search by title, author, or category">
                    <button type="submit">Search</button>
                </form>
            </div>
            <% } else { %>
            <div style="flex: 1;"></div> <!-- Spacer to maintain layout -->
            <% } %>

            <nav class="main-nav">
                <ul>
                    <%
                    if (user != null) {
                        if (user.isAdmin()) {
                    %>
                    <li><a href="<%=request.getContextPath()%>/admin/dashboard" class="admin-link <%= request.getRequestURI().contains("/admin/dashboard") ? "active" : "" %>">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/books" class="admin-link <%= request.getRequestURI().contains("/admin/books") ? "active" : "" %>">Manage Books</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/users" class="admin-link <%= request.getRequestURI().contains("/admin/users") ? "active" : "" %>">Manage Users</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/orders" class="admin-link <%= request.getRequestURI().contains("/admin/orders") ? "active" : "" %>">Manage Orders</a></li>
                    <li><a href="<%=request.getContextPath()%>/logout" class="admin-link">Logout</a></li>
                    <% } else { %>
                    <li><a href="<%=request.getContextPath()%>/home">Home</a></li>
                    <li><a href="<%=request.getContextPath()%>/books">Books</a></li>
                    <li><a href="<%=request.getContextPath()%>/about">About</a></li>
                    <li><a href="<%=request.getContextPath()%>/contact-us">Contact Us</a></li>
                    <li><a href="<%=request.getContextPath()%>/user/dashboard">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/cart">Cart</a></li>
                    <li><a href="<%=request.getContextPath()%>/logout">Logout</a></li>
                    <% } %>
                    <% } else { %>
                    <li><a href="<%=request.getContextPath()%>/home">Home</a></li>
                    <li><a href="<%=request.getContextPath()%>/books">Books</a></li>
                    <li><a href="<%=request.getContextPath()%>/about">About</a></li>
                    <li><a href="<%=request.getContextPath()%>/contact-us">Contact Us</a></li>
                    <li><a href="<%=request.getContextPath()%>/login">Login</a></li>
                    <li><a href="<%=request.getContextPath()%>/register">Register</a></li>
                    <% } %>
                </ul>
            </nav>
        </div>
    </div>
</header>
