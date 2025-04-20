<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Book" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Books - E-Book Store</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h1><%= request.getAttribute("selectedCategory") != null ? request.getAttribute("selectedCategory") + " Books" : "All Books" %></h1>

        <div class="filter-section">
            <form action="<%=request.getContextPath()%>/books" method="get" id="categoryForm">
                <div class="filter-group">
                    <label for="category">Category:</label>
                    <select name="category" id="category">
                        <option value="">All Categories</option>
                        <option value="Fiction" <%= (request.getAttribute("selectedCategory") != null && "Fiction".equals(request.getAttribute("selectedCategory"))) ? "selected" : "" %>>Fiction</option>
                        <option value="Non-Fiction" <%= (request.getAttribute("selectedCategory") != null && "Non-Fiction".equals(request.getAttribute("selectedCategory"))) ? "selected" : "" %>>Non-Fiction</option>
                        <option value="Science" <%= (request.getAttribute("selectedCategory") != null && "Science".equals(request.getAttribute("selectedCategory"))) ? "selected" : "" %>>Science</option>
                        <option value="Technology" <%= (request.getAttribute("selectedCategory") != null && "Technology".equals(request.getAttribute("selectedCategory"))) ? "selected" : "" %>>Technology</option>
                    </select>
                </div>
            </form>
        </div>

        <div class="book-grid">
            <%
            List<Book> books = (List<Book>) request.getAttribute("books");
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
            %>
            <div class="book-card">
                <div class="book-cover">
                    <img src="<%= book.getImageUrl() != null ? request.getContextPath() + "/" + book.getImageUrl() : request.getContextPath() + "/images/default-book.jpg" %>" alt="<%= book.getTitle() %>">
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
            <p>No books available.</p>
            <% } %>
        </div>
    </div>

    <jsp:include page="footer.jsp" />

    <script src="js/script.js"></script>
    <script>
        // Ensure the category dropdown works correctly
        document.getElementById('category').addEventListener('change', function() {
            document.getElementById('categoryForm').submit();
        });
    </script>
</body>
</html>
