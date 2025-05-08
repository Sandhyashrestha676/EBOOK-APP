<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container" style="background-color: #f5f5f5; min-height: 100vh; padding-top: 20px; padding-bottom: 20px;">
        <!-- Main Content -->
        <div class="main-content" style="padding: 20px;">
            <div class="contact-section">
                <h1>Contact Us</h1>
                <p class="contact-intro">Welcome to <strong>E-Book Store</strong>! Have questions or feedback? We'd love to hear from you! Feel free to reach out using any of the contact methods below or fill out our contact form.</p>

                <% if (request.getAttribute("successMessage") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMessage") %>
                </div>
                <% } %>

                <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
                <% } %>

                <!-- Two-column layout for contact info and form -->
                <div class="contact-layout">
                    <!-- Left column: Contact information -->
                    <div class="contact-info-full">
                        <div class="info-grid">
                            <div class="info-item">
                                <strong><i class="fas fa-map-marker-alt"></i> Address:</strong>
                                <p>Main Road, Near Central Bus Station<br>Itahari-5, Sunsari, Province 1, Nepal</p>
                            </div>
                            <div class="info-item">
                                <strong><i class="fas fa-phone"></i> Phone:</strong>
                                <p>+977 025-587654</p>
                            </div>
                            <div class="info-item">
                                <strong><i class="fas fa-envelope"></i> Email:</strong>
                                <p>support@ebookstore.np</p>
                            </div>
                            <div class="info-item">
                                <strong><i class="fas fa-clock"></i> Hours:</strong>
                                <p>Sunday - Friday: 10am - 7pm<br>Saturday: 11am - 5pm</p>
                            </div>
                        </div>

                        <div class="contact-map">
                            <h3>Find E-Book Store in Itahari</h3>
                            <p>Visit our flagship store in the heart of Itahari. We're conveniently located on Main Road with easy access to public transportation and ample parking for our customers.</p>
                            <div class="map-container">
                                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3572.0292358861096!2d87.26361937605848!3d26.45829997706841!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x39ef744e5cd4b8b7%3A0x5784d5d68b2782b8!2sMain%20Rd%2C%20Itahari%2056705!5e0!3m2!1sen!2snp!4v1718539200012!5m2!1sen!2snp" width="100%" height="450" style="border:0; border-radius: 8px;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
                                <div class="store-marker">
                                    <div class="marker-pin"></div>
                                    <div class="marker-label">E-Book Store</div>
                                </div>
                                <div class="location-highlight">
                                    <div class="highlight-text">Our Itahari Branch</div>
                                    <div class="highlight-arrow"></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Right column: Contact form -->
                    <div class="contact-form-container">
                        <h2><i class="fas fa-paper-plane"></i> Send Us a Message</h2>
                        <form action="<%=request.getContextPath()%>/contact-us" method="post" class="contact-form">
                            <div class="form-group">
                                <label for="name">Your Name</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-user"></i>
                                    <input type="text" id="name" name="name" placeholder="Enter your full name" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email">Email Address</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-envelope"></i>
                                    <input type="email" id="email" name="email" placeholder="Enter your email address" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="subject">Subject</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-tag"></i>
                                    <input type="text" id="subject" name="subject" placeholder="What is this regarding?" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="message">Your Message</label>
                                <textarea id="message" name="message" rows="6" placeholder="Type your message here..." required></textarea>
                            </div>

                            <div class="form-group">
                                <button type="submit" class="btn btn-primary"><i class="fas fa-paper-plane"></i> Send Message</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />

    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</body>
</html>
