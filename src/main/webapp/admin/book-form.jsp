<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Book" %>
<%@ page import="java.io.File" %>
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
                boolean viewOnly = (request.getAttribute("viewOnly") != null && (Boolean)request.getAttribute("viewOnly"));

                if (viewOnly) {
                %>
                <h1>View Book</h1>
                <% } else if (book != null) { %>
                <h1>Edit Book</h1>
                <% } else { %>
                <h1>Add New Book</h1>
                <% } %>
            </div>

            <!-- Dashboard menu moved to header -->

            <div class="dashboard-content">
                <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
                <% } %>

                <div class="form-container">
                    <% if (viewOnly) { %>
                    <!-- View-only mode -->
                    <div class="view-book-details">
                        <ul class="book-details-list">
                            <li>
                                <div class="detail-label">Image:</div>
                                <div class="detail-value">
                                    <% if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) { %>
                                    <img src="<%= request.getContextPath() + "/" + book.getImageUrl() %>" alt="<%= book.getTitle() %>">
                                    <% } else { %>
                                    <img src="<%= request.getContextPath() %>/images/default-book.jpg" alt="<%= book.getTitle() %>">
                                    <% } %>
                                </div>
                            </li>
                            <li>
                                <div class="detail-label">Title:</div>
                                <div class="detail-value"><%= book.getTitle() %></div>
                            </li>
                            <li>
                                <div class="detail-label">Author:</div>
                                <div class="detail-value"><%= book.getAuthor() %></div>
                            </li>
                            <li>
                                <div class="detail-label">Category:</div>
                                <div class="detail-value"><%= book.getCategory() %></div>
                            </li>
                            <li>
                                <div class="detail-label">Genre:</div>
                                <div class="detail-value"><%= book.getGenre() %></div>
                            </li>
                            <li>
                                <div class="detail-label">Price:</div>
                                <div class="detail-value">$<%= book.getPrice() %></div>
                            </li>
                            <li>
                                <div class="detail-label">Stock:</div>
                                <div class="detail-value"><%= book.getStock() %> units</div>
                            </li>
                            <li>
                                <div class="detail-label">Description:</div>
                                <div class="detail-value"><%= book.getDescription() %></div>
                            </li>
                        </ul>

                        <div class="form-actions">
                            <a href="<%=request.getContextPath()%>/admin/books/edit/<%= book.getId() %>" class="btn btn-primary">Edit</a>
                            <a href="<%=request.getContextPath()%>/admin/books" class="btn btn-secondary">Back</a>
                        </div>
                    </div>
                    <% } else { %>
                    <!-- Edit/Add mode -->
                    <form action="<%=request.getContextPath()%>/admin/books" method="post" enctype="multipart/form-data" id="bookForm">
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
                            <label for="bookImage">Book Image</label>
                            <div class="custom-file-upload">
                                <input type="file" id="bookImage" name="bookImage" accept="image/*" onchange="previewImage(this);" class="file-input">
                                <label for="bookImage" class="file-label">Choose a file</label>
                                <span id="file-name">No file chosen</span>
                            </div>

                            <div id="image-preview-container" class="<%= (book != null && book.getImageUrl() != null && !book.getImageUrl().isEmpty()) ? "" : "hidden" %>">
                                <div class="current-image">
                                    <p id="preview-label"><%= (book != null && book.getImageUrl() != null && !book.getImageUrl().isEmpty()) ? "Current image:" : "Preview:" %></p>
                                    <%
                                    String imgPath = (book != null && book.getImageUrl() != null) ? book.getImageUrl() : "";
                                    String fullImagePath = imgPath.isEmpty() ? "" : (request.getContextPath() + "/" + imgPath);
                                    String altText = (book != null) ? book.getTitle() : "Preview";
                                    %>
                                    <img id="image-preview" src="<%= fullImagePath %>" alt="<%= altText %>" style="max-width: 150px; max-height: 200px;">
                                    <% if (book != null && book.getImageUrl() != null && !book.getImageUrl().isEmpty()) { %>
                                    <input type="hidden" name="currentImageUrl" value="<%= book.getImageUrl() %>">
                                    <% } %>
                                </div>
                            </div>
                            <p class="form-hint">Select an image file to upload. Supported formats: JPG, PNG, GIF.</p>
                        </div>

                        <script>
                        function previewImage(input) {
                            var fileNameSpan = document.getElementById('file-name');
                            var previewContainer = document.getElementById('image-preview-container');
                            var previewImage = document.getElementById('image-preview');
                            var previewLabel = document.getElementById('preview-label');

                            if (input.files && input.files[0]) {
                                var fileName = input.files[0].name;
                                fileNameSpan.textContent = fileName;
                                fileNameSpan.style.borderLeftColor = '#28a745'; // Green border for success

                                var reader = new FileReader();
                                reader.onload = function(e) {
                                    // Add a fade-in effect
                                    previewImage.style.opacity = '0';
                                    previewImage.src = e.target.result;
                                    previewLabel.textContent = 'Preview:';
                                    previewContainer.classList.remove('hidden');

                                    // Fade in the image
                                    setTimeout(function() {
                                        previewImage.style.transition = 'opacity 0.5s ease';
                                        previewImage.style.opacity = '1';
                                    }, 50);
                                };
                                reader.readAsDataURL(input.files[0]);
                            } else {
                                fileNameSpan.textContent = 'No file chosen';
                                fileNameSpan.style.borderLeftColor = '#007bff'; // Reset border color
                            }
                        }

                        // Add animation to the Choose file button when the page loads
                        document.addEventListener('DOMContentLoaded', function() {
                            var fileLabel = document.querySelector('.file-label');
                            fileLabel.style.transition = 'all 0.5s ease';
                            fileLabel.style.transform = 'translateY(0)';
                            fileLabel.style.opacity = '1';
                        });
                        </script>

                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary" id="saveButton">Save</button>
                            <a href="<%=request.getContextPath()%>/admin/books" class="btn btn-secondary">Cancel</a>
                        </div>

                        <script>
                        document.getElementById('bookForm').addEventListener('submit', function(e) {
                            console.log('Form submission detected');

                            // Check if a file is selected
                            var fileInput = document.getElementById('bookImage');
                            if (fileInput.files.length > 0) {
                                console.log('File selected: ' + fileInput.files[0].name);
                                console.log('File size: ' + fileInput.files[0].size + ' bytes');
                            } else {
                                console.log('No file selected');
                            }

                            // Log all form data
                            var formData = new FormData(this);
                            console.log('Form data:');
                            for (var pair of formData.entries()) {
                                console.log(pair[0] + ': ' + pair[1]);
                            }
                        });
                        </script>
                    </form>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../footer.jsp" />
</body>
</html>
