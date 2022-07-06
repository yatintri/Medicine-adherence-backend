package com.example.user_service.security;

import com.example.user_service.config.filter.UserDetailService;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthenticationHandler implements HandlerInterceptor {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserDetailService userDetailService;
    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String authorizationHeader = request.getHeader("Authorization");

        String jwt = null;
        String username = null;
        final String id = request.getParameter("userId");
        logger.info(id);
        logger.info(authorizationHeader);
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(jwt);
                logger.info(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (username != null) {
            try {

                UserDetails userDetails = userDetailService.loadUserByUsername(id);
                if (Boolean.FALSE.equals(jwtUtil.validateToken(jwt.trim(), userDetails,request))) {
                    if (request.getAttribute("expired").equals("true")) {
                        logger.info("expired");
                        response.setStatus(401);
                        return false;
                    }
                    response.setStatus(403);
                    return false;
                } else {
                    logger.info(userDetails.getUsername());
                    response.setHeader("jwt",jwt);
                    return true;

                }
            } catch (Exception usernameNotFoundException) {

                response.setStatus(404);
                String content = "{\n" +
                        "    \"status\": \"failed\",\n" +
                        "    \"message\": \"User invalid\",\n" +
                        "}";
                response.setContentType("application/json");
                response.getWriter().write(content);
                return false;
            }

        }
        response.setStatus(401);
        return false;
    }
}
