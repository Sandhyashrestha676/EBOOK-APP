<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Book" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Results - E-Book Store</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h1>Search Results</h1>

        <%
        String keyword = (String) request.getAttribute("keyword");
        if (keyword != null && !keyword.isEmpty()) {
        %>
        <p class="search-info">Showing results for: <strong><%= keyword %></strong></p>
        <% } %>

        <div class="book-grid">
            <%
            List<Book> books = (List<Book>) request.getAttribute("books");
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
            %>
            <div class="book-card">
                <div class="book-cover">
                    <img src="<%= book.getImageUrl() != null ? book.getImageUrl() : "images/default-book.jpg" %>" alt="<%= book.getTitle() %>">
                </div>
                <div class="book-info">
                    <h3><%= book.getTitle() %></h3>
                    <p class="author">by <%= book.getAuthor() %></p>
                    <p class="category"><%= book.getCategory() %></p>
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
            <div class="no-results">
                <p>No books found matching your search criteria.</p>
                <a href="<%=request.getContextPath()%>/books" class="btn btn-primary">Browse All Books</a>
            </div>
            <% } %>
        </div>
    </div>

    <jsp:include page="footer.jsp" />

    <script src="js/script.js"></script>
</body>
</html>
