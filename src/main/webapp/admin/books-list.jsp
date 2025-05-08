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

                <!-- Dashboard menu moved to header -->

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
                        <h2>Books (Page <%= request.getAttribute("currentPage") %> of <%= request.getAttribute("totalPages") %>, Total: <%= request.getAttribute("totalBooks") %>)</h2>
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
                                        <a href="<%=request.getContextPath()%>/admin/books/view/<%= book.getId() %>" class="btn btn-info btn-small"><i class="fas fa-eye"></i> View</a>
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

                        <!-- Pagination Controls -->
                        <%
                        Integer currentPage = (Integer) request.getAttribute("currentPage");
                        Integer totalPages = (Integer) request.getAttribute("totalPages");
                        String categoryParam = request.getAttribute("selectedCategory") != null ? "&category=" + request.getAttribute("selectedCategory") : "";

                        if (currentPage != null && totalPages != null && totalPages > 1) {
                        %>
                        <div style="width: 100%; display: flex; justify-content: center; margin-top: 20px;">
                            <div class="pagination">
                                <% if (currentPage > 1) { %>
                                <a href="<%=request.getContextPath()%>/admin/books?page=<%= currentPage - 1 %><%= categoryParam %>" class="pagination-link nav-btn" aria-label="Previous page"><i class="fas fa-chevron-left"></i></a>
                                <% } else { %>
                                <span class="pagination-link nav-btn disabled"><i class="fas fa-chevron-left"></i></span>
                                <% } %>

                                <%
                                // Simple pagination with limited numbers
                                int maxVisiblePages = 5; // Show at most 5 page numbers
                                int startPage = Math.max(1, currentPage - (maxVisiblePages / 2));
                                int endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

                                // Adjust start page if we're near the end
                                if (endPage - startPage < maxVisiblePages - 1) {
                                    startPage = Math.max(1, endPage - maxVisiblePages + 1);
                                }

                                // Display page numbers
                                for (int i = startPage; i <= endPage; i++) {
                                %>
                                <a href="<%=request.getContextPath()%>/admin/books?page=<%= i %><%= categoryParam %>"
                                   class="pagination-link <%= (i == currentPage) ? "active" : "" %>"><%= i %></a>
                                <% } %>

                                <% if (currentPage < totalPages) { %>
                                <a href="<%=request.getContextPath()%>/admin/books?page=<%= currentPage + 1 %><%= categoryParam %>" class="pagination-link nav-btn" aria-label="Next page"><i class="fas fa-chevron-right"></i></a>
                                <% } else { %>
                                <span class="pagination-link nav-btn disabled"><i class="fas fa-chevron-right"></i></span>
                                <% } %>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="../footer.jsp" />
    </div>

    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
