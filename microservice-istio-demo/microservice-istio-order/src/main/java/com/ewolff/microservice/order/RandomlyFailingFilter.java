package com.ewolff.microservice.order;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RandomlyFailingFilter implements Filter {

	private final Logger log = LoggerFactory.getLogger(RandomlyFailingFilter.class);

	@Value("${failrandomly:false}")
	private boolean failRandomly = false;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if ((Math.random() <= 0.5D) || (!failRandomly)) {
			chain.doFilter(request, response);
		} else {
			log.trace("Made HTTP Request fail with 500");
			((HttpServletResponse) response).sendError(500);
		}
	}

	@Override
	public void destroy() {
	}

}
