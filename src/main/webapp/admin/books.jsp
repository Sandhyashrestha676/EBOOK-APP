<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Book" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Form - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container">
        <div class="dashboard">
            <div class="dashboard-header">
                <% 
                Book book = (Book) request.getAttribute("book");
                if (book != null) {
                %>
                <h1>Edit Book</h1>
                <% } else { %>
                <h1>Add New Book</h1>
                <% } %>
            </div>
            
            <div class="dashboard-menu">
                <ul>
                    <li><a href="<%=request.getContextPath()%>/admin/dashboard">Dashboard</a></li>
                    <li class="active"><a href="<%=request.getContextPath()%>/admin/books">Manage Books</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/users">Manage Users</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/orders">Manage Orders</a></li>
                </ul>
            </div>
            
            <div class="dashboard-content">
                <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
                <% } %>
                
                <div class="form-container">
                    <form action="<%=request.getContextPath()%>/admin/books" method="post">
                        <% if (book != null) { %>
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="<%= book.getId() %>">
                        <% } else { %>
                        <input type="hidden" name="action" value="add">
                        <% } %>
                        
                        <div class="form-group">
                            <label for="title">Title</label>
                            <input type="text" id="title" name="title" value="<%= book != null ? book.getTitle() : "" %>" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="author">Author</label>
                            <input type="text" id="author" name="author" value="<%= book != null ? book.getAuthor() : "" %>" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="description">Description</label>
                            <textarea id="description" name="description" rows="5" required><%= book != null ? book.getDescription() : "" %></textarea>
                        </div>
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="category">Category</label>
                                <select id="category" name="category" required>
                                    <option value="">Select Category</option>
                                    <option value="Fiction" <%= book != null && "Fiction".equals(book.getCategory()) ? "selected" : "" %>>Fiction</option>
                                    <option value="Non-Fiction" <%= book != null && "Non-Fiction".equals(book.getCategory()) ? "selected" : "" %>>Non-Fiction</option>
                                    <option value="Science" <%= book != null && "Science".equals(book.getCategory()) ? "selected" : "" %>>Science</option>
                                    <option value="Technology" <%= book != null && "Technology".equals(book.getCategory()) ? "selected" : "" %>>Technology</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="genre">Genre</label>
                                <input type="text" id="genre" name="genre" value="<%= book != null ? book.getGenre() : "" %>" required>
                            </div>
                        </div>
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="price">Price</label>
                                <input type="number" id="price" name="price" step="0.01" min="0" value="<%= book != null ? book.getPrice() : "" %>" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="stock">Stock</label>
                                <input type="number" id="stock" name="stock" min="0" value="<%= book != null ? book.getStock() : "" %>" required>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="imageUrl">Image URL</label>
                            <input type="text" id="imageUrl" name="imageUrl" value="<%= book != null ? book.getImageUrl() : "" %>">
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Save</button>
                            <a href="<%=request.getContextPath()%>/admin/books" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="../footer.jsp" />
    
    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
