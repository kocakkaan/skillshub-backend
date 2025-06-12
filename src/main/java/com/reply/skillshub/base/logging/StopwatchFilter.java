package com.reply.skillshub.base.logging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class StopwatchFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(StopwatchFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        StopWatch stopWatch = new StopWatch(request.getRequestURI());
        stopWatch.start();

        try {
            filterChain.doFilter(request, response);
        } finally {
            stopWatch.stop();
            logger.info("Incoming API call to {} took {} ms", request.getRequestURI(), stopWatch.getTotalTimeMillis());
        }
    }
}
