package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/contact-us", "/contact"})
public class ContactUsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the contact-us.jsp page
        request.getRequestDispatcher("/contact-us.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        // Simple validation
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            subject == null || subject.trim().isEmpty() ||
            message == null || message.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All fields are required. Please fill out the form completely.");
            request.getRequestDispatcher("/contact-us.jsp").forward(request, response);
            return;
        }

        // Basic email validation
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            request.setAttribute("errorMessage", "Please enter a valid email address.");
            request.getRequestDispatcher("/contact-us.jsp").forward(request, response);
            return;
        }

        // In a real application, you would save this data to a database
        // or send an email to the support team
        // For now, we'll just show a success message

        System.out.println("Contact form submission received:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);

        // Set success message and forward back to contact page
        request.setAttribute("successMessage", "Thank you for your message! We'll get back to you soon.");
        request.getRequestDispatcher("/contact-us.jsp").forward(request, response);
    }
}
