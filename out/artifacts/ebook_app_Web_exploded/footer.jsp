<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<footer>
    <div class="container">
        <div class="footer-content">
            <div class="footer-section about">
                <h3>About E-Book Store</h3>
                <p>E-Book Store is your one-stop destination for digital books. We offer a wide range of e-books across various genres.</p>
            </div>

            <div class="footer-section links">
                <h3>Quick Links</h3>
                <ul>
                    <li><a href="<%=request.getContextPath()%>/home">Home</a></li>
                    <li><a href="<%=request.getContextPath()%>/books">Books</a></li>
                    <li><a href="<%=request.getContextPath()%>/about">About Us</a></li>
                    <li><a href="<%=request.getContextPath()%>/contact">Contact</a></li>
                </ul>
            </div>

            <div class="footer-section contact">
                <h3>Contact Us</h3>
                <p><i class="fa fa-envelope"></i> info@ebookstore</p>
                <p><i class="fa fa-phone"></i> +1 123 456 7890</p>
            </div>
        </div>

        <div class="footer-bottom">
            <p>&copy; 2023 E-Book Store. All rights reserved.</p>
        </div>
    </div>
</footer>

<!-- JavaScript -->
<script src="<%=request.getContextPath()%>/js/script.js"></script>
<script src="<%=request.getContextPath()%>/js/header.js"></script>
