package filter;

import model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/user/*", "/admin/*", "/cart/*", "/checkout/*"})
public class AuthenticationFilter implements Filter {
    
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        
        if (!isLoggedIn) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        } else {
            // Check if admin is trying to access admin pages
            String requestURI = httpRequest.getRequestURI();
            if (requestURI.contains("/admin/")) {
                User user = (User) session.getAttribute("user");
                if (!user.isAdmin()) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/user/dashboard");
                    return;
                }
            }
            
            chain.doFilter(request, response);
        }
    }
    
    public void destroy() {
    }
}
