package com.api.limiter.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Order(1)
public class ApiThrottlingFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(ApiThrottlingFilter.class);
    private static Map<String, AtomicInteger> throttleMap = new HashMap<>();
    private static long API_THROTTLE_LIMIT = 5;

    @Override
    public void doFilter(ServletRequest servletRequest,
        ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        log.info("API Throttling Filter");

        if (throttleMap.get(servletRequest.getRemoteAddr()) != null) {
            AtomicInteger requestCount = throttleMap.get(servletRequest.getRemoteAddr());
            int currentCount = requestCount.get();
            if(currentCount < API_THROTTLE_LIMIT){
                int curr = requestCount.getAndIncrement();
                log.info("Updated count {} ", curr);
                filterChain.doFilter(servletRequest, servletResponse);
            } else{
                log.error("Request Denied. API rate limit exceeded !");
            }
        } else {
            throttleMap.put(servletRequest.getRemoteAddr(), new AtomicInteger(1));
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
