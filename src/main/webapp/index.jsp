<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Book" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>E-Book Store - Home</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
    <div class="background-wrapper">
        <jsp:include page="header.jsp" />

        <div class="container">
        <div class="hero-section">
            <div class="hero-content">
                <div class="hero-text">
                    <h1>Discover Your Next Favorite Book</h1>
                    <p>Explore our vast collection of e-books across various genres</p>
                    <div class="hero-buttons">
                        <a href="<%=request.getContextPath()%>/books" class="btn btn-primary"><i class="fas fa-book"></i> Browse All Books</a>
                        <a href="<%=request.getContextPath()%>/books?category=Fiction" class="btn btn-secondary"><i class="fas fa-star"></i> Popular Titles</a>
                    </div>
                </div>
            </div>
        </div>

        <section class="featured-books">
            <h2>Featured Books</h2>
            <div class="book-grid">
                <%
                List<Book> featuredBooks = (List<Book>) request.getAttribute("featuredBooks");
                if (featuredBooks != null && !featuredBooks.isEmpty()) {
                    for (Book book : featuredBooks) {
                %>
                <div class="book-card">
                    <div class="book-cover">
                        <img src="<%= book.getImageUrl() != null ? request.getContextPath() + "/" + book.getImageUrl() : request.getContextPath() + "/images/default-book.jpg" %>" alt="<%= book.getTitle() %>">
                    </div>
                    <div class="book-info">
                        <h3><%= book.getTitle() %></h3>
                        <p class="author">by <%= book.getAuthor() %></p>
                        <p class="price">$<%= book.getPrice() %></p>
                        <div class="btn-container">
                            <a href="<%=request.getContextPath()%>/books/<%= book.getId() %>" class="btn btn-primary">View Details</a>
                            <% if (session.getAttribute("user") != null) { %>
                            <form action="<%=request.getContextPath()%>/cart" method="post">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="bookId" value="<%= book.getId() %>">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit" class="btn btn-success">Add to Cart</button>
                            </form>
                            <% } else { %>
                            <a href="<%=request.getContextPath()%>/login" class="btn btn-success">Add to Cart</a>
                            <% } %>
                        </div>
                    </div>
                </div>
                <%
                    }
                } else {
                %>
                <p>No featured books available at the moment.</p>
                <% } %>
            </div>
        </section>

        <section class="promo-section">
            <div class="promo-content">
                <div class="promo-text">
                    <h2>Join Our E-Book Community</h2>
                    <p>Get access to exclusive deals, new releases, and personalized recommendations.</p>
                    <div class="promo-form">
                        <input type="email" placeholder="Enter your email address">
                        <button class="btn btn-primary">Subscribe</button>
                    </div>
                </div>
            </div>
        </section>

        <section class="categories">
            <h2>Browse by Category</h2>
            <div class="category-grid">
                <a href="<%=request.getContextPath()%>/books?category=Fiction" class="category-card">
                    <i class="fas fa-book"></i>
                    <h3>Fiction</h3>
                    <p>Explore novels, short stories, and literary fiction</p>
                </a>
                <a href="<%=request.getContextPath()%>/books?category=Non-Fiction" class="category-card">
                    <i class="fas fa-landmark"></i>
                    <h3>Non-Fiction</h3>
                    <p>Discover biographies, history, and self-improvement</p>
                </a>
                <a href="<%=request.getContextPath()%>/books?category=Science" class="category-card">
                    <i class="fas fa-atom"></i>
                    <h3>Science</h3>
                    <p>Learn about physics, biology, and astronomy</p>
                </a>
                <a href="<%=request.getContextPath()%>/books?category=Technology" class="category-card">
                    <i class="fas fa-laptop-code"></i>
                    <h3>Technology</h3>
                    <p>Explore programming, AI, and digital innovation</p>
                </a>
            </div>
        </section>
    </div>
    </div>

    <jsp:include page="footer.jsp" />

    <script src="js/script.js"></script>
</body>
</html>
