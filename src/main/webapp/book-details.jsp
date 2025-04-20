<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Book" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Details - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <%
        Book book = (Book) request.getAttribute("book");
        if (book != null) {
        %>
        <div class="book-details">
            <div class="book-image">
                <img src="<%= book.getImageUrl() != null ? request.getContextPath() + "/" + book.getImageUrl() : request.getContextPath() + "/images/default-book.jpg" %>" alt="<%= book.getTitle() %>">
            </div>

            <div class="book-info">
                <h1><%= book.getTitle() %></h1>
                <p class="author">by <%= book.getAuthor() %></p>
                <p class="category"><strong>Category:</strong> <%= book.getCategory() %></p>
                <p class="genre"><strong>Genre:</strong> <%= book.getGenre() %></p>
                <p class="price"><strong>Price:</strong> $<%= book.getPrice() %></p>
                <p class="stock"><strong>Availability:</strong> <%= book.getStock() > 0 ? "In Stock" : "Out of Stock" %></p>

                <div class="description">
                    <h3>Description</h3>
                    <p><%= book.getDescription() %></p>
                </div>

                <% if (book.getStock() > 0) { %>
                <form action="<%=request.getContextPath()%>/cart" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="bookId" value="<%= book.getId() %>">

                    <div class="form-group">
                        <label for="quantity">Quantity:</label>
                        <input type="number" id="quantity" name="quantity" value="1" min="1" max="<%= book.getStock() %>">
                    </div>

                    <button type="submit" class="btn btn-primary">Add to Cart</button>
                </form>
                <% } else { %>
                <p class="out-of-stock">This book is currently out of stock.</p>
                <% } %>
            </div>
        </div>
        <% } else { %>
        <div class="error-message">
            <p>Book not found.</p>
            <a href="<%=request.getContextPath()%>/books" class="btn btn-primary">Back to Books</a>
        </div>
        <% } %>
    </div>

    <jsp:include page="footer.jsp" />

    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
