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

            <div class="search-bar">
                <form action="<%=request.getContextPath()%>/search" method="get">
                    <input type="text" name="keyword" placeholder="Search by title, author, or category">
                    <button type="submit">Search</button>
                </form>
            </div>

            <nav class="main-nav">
                <ul>
                    <li><a href="<%=request.getContextPath()%>/home">Home</a></li>

                    <%
                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                        if (user.isAdmin()) {
                    %>
                    <li><a href="<%=request.getContextPath()%>/admin/dashboard">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/about">About</a></li>
                    <li><a href="<%=request.getContextPath()%>/logout">Logout</a></li>
                    <% } else { %>
                    <li><a href="<%=request.getContextPath()%>/books">Books</a></li>
                    <li><a href="<%=request.getContextPath()%>/about">About</a></li>
                    <li><a href="<%=request.getContextPath()%>/contact-us">Contact Us</a></li>
                    <li><a href="<%=request.getContextPath()%>/user/dashboard">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/cart">Cart</a></li>
                    <li><a href="<%=request.getContextPath()%>/logout">Logout</a></li>
                    <% } %>
                    <% } else { %>
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
