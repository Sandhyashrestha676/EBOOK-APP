<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - E-Book Store</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <div class="container">
        <div class="about-section">
            <h1>About E-Book Store</h1>
            
            <div class="about-content">
                <div class="about-image">
                    <img src="images/about-image.jpg" alt="E-Book Store">
                </div>
                
                <div class="about-text">
                    <h2>Our Story</h2>
                    <p>E-Book Store was founded in 2023 with a simple mission: to make digital books accessible to everyone. We believe that knowledge should be available to all, and our platform is designed to make finding and purchasing e-books as easy as possible.</p>
                    
                    <h2>Our Vision</h2>
                    <p>We aim to be the leading e-book marketplace, offering a wide range of titles across various genres. Our focus is on providing a seamless user experience, competitive pricing, and excellent customer service.</p>
                    
                    <h2>Why Choose Us?</h2>
                    <ul>
                        <li><strong>Wide Selection:</strong> Thousands of e-books across multiple categories</li>
                        <li><strong>Competitive Pricing:</strong> Best prices for digital content</li>
                        <li><strong>User-Friendly Platform:</strong> Easy to browse, search, and purchase</li>
                        <li><strong>Secure Transactions:</strong> Your payment information is always protected</li>
                        <li><strong>Customer Support:</strong> Our team is always ready to assist you</li>
                    </ul>
                </div>
            </div>
            
            <div class="team-section">
                <h2>Our Team</h2>
                <div class="team-members">
                    <div class="team-member">
                        <img src="images/team-member1.jpg" alt="Team Member">
                        <h3>John Doe</h3>
                        <p>Founder & CEO</p>
                    </div>
                    
                    <div class="team-member">
                        <img src="images/team-member2.jpg" alt="Team Member">
                        <h3>Jane Smith</h3>
                        <p>Chief Technology Officer</p>
                    </div>
                    
                    <div class="team-member">
                        <img src="images/team-member3.jpg" alt="Team Member">
                        <h3>Michael Johnson</h3>
                        <p>Head of Content</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="footer.jsp" />
    
    <script src="js/script.js"></script>
</body>
</html>
