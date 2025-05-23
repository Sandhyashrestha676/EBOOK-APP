<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Book" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Books - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <script>
    function deleteBook(bookId) {
        if (confirm('Are you sure you want to delete this book?')) {
            // Create a form dynamically
            var form = document.createElement('form');
            form.method = 'POST';
            form.action = '<%=request.getContextPath()%>/admin/books/delete/' + bookId;
            document.body.appendChild(form);
            form.submit();
        }
    }
    </script>
</head>
<body>
    <div class="background-wrapper">
        <jsp:include page="../header.jsp" />

        <div class="container">
            <div class="dashboard">
                <div class="dashboard-header">
                    <h1>Manage Books</h1>
                    <a href="<%=request.getContextPath()%>/admin/books/add" class="btn btn-primary"><i class="fas fa-plus"></i> Add New Book</a>
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
                    <% if (request.getParameter("added") != null) { %>
                    <div class="alert alert-success">
                        Book added successfully.
                    </div>
                    <% } %>

                    <% if (request.getParameter("updated") != null) { %>
                    <div class="alert alert-success">
                        Book updated successfully.
                    </div>
                    <% } %>

                    <% if (request.getParameter("deleted") != null) { %>
                    <div class="alert alert-success">
                        Book deleted successfully.
                    </div>
                    <% } %>

                    <div class="dashboard-section">
                        <div class="filter-section">
                            <form action="<%=request.getContextPath()%>/admin/books" method="get" id="categoryForm">
                                <div class="filter-group">
                                    <label for="category">Filter by Category:</label>
                                    <select name="category" id="category" onchange="document.getElementById('categoryForm').submit();">
                                        <option value="">All Categories</option>
                                        <option value="Fiction" <%= (request.getParameter("category") != null && "Fiction".equals(request.getParameter("category"))) ? "selected" : "" %>>Fiction</option>
                                        <option value="Non-Fiction" <%= (request.getParameter("category") != null && "Non-Fiction".equals(request.getParameter("category"))) ? "selected" : "" %>>Non-Fiction</option>
                                        <option value="Science" <%= (request.getParameter("category") != null && "Science".equals(request.getParameter("category"))) ? "selected" : "" %>>Science</option>
                                        <option value="Technology" <%= (request.getParameter("category") != null && "Technology".equals(request.getParameter("category"))) ? "selected" : "" %>>Technology</option>
                                    </select>
                                </div>
                            </form>
                        </div>

                        <table class="dashboard-table books-table">
                            <thead>
                                <tr>
                                    <th class="id-column">ID</th>
                                    <th class="image-column">Image</th>
                                    <th class="title-column">Title</th>
                                    <th class="author-column">Author</th>
                                    <th class="category-column">Category</th>
                                    <th class="price-column">Price</th>
                                    <th class="stock-column">Stock</th>
                                    <th class="actions-column">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                List<Book> books = (List<Book>) request.getAttribute("books");
                                if (books != null && !books.isEmpty()) {
                                    for (Book book : books) {
                                %>
                                <tr>
                                    <td class="id-column">#<%= book.getId() %></td>
                                    <td class="image-column">
                                        <img src="<%= book.getImageUrl() != null ? request.getContextPath() + "/" + book.getImageUrl() : request.getContextPath() + "/images/default-book.jpg" %>" alt="<%= book.getTitle() %>" width="40">
                                    </td>
                                    <td class="title-column"><%= book.getTitle() %></td>
                                    <td class="author-column"><%= book.getAuthor() %></td>
                                    <td class="category-column"><%= book.getCategory() %></td>
                                    <td class="price-column">$<%= book.getPrice() %></td>
                                    <td class="stock-column"><%= book.getStock() %></td>
                                    <td class="actions-column">
                                        <a href="<%=request.getContextPath()%>/admin/books/edit/<%= book.getId() %>" class="btn btn-small"><i class="fas fa-edit"></i> Edit</a>
                                        <a href="<%=request.getContextPath()%>/admin/books/delete/<%= book.getId() %>?t=<%= System.currentTimeMillis() %>" class="btn btn-danger btn-small">
                                            <i class="fas fa-trash"></i> Delete
                                        </a>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="8" class="text-center">No books available.</td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="../footer.jsp" />
    </div>

    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
