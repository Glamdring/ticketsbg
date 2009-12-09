package com.tickets.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.util.DateUtil;

public class StaticResourceCacheFilter implements Filter {

    public static final String[] CACHEABLE_CONTENT_TYPES = new String[] {
            "text/css", "text/javascript", "image/png", "image/jpeg",
            "image/gif", "image/jpg" };

    static {
        Arrays.sort(CACHEABLE_CONTENT_TYPES);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        chain.doFilter(request, response);

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contentType = httpResponse.getContentType();
        if (contentType == null
                || Arrays.binarySearch(CACHEABLE_CONTENT_TYPES, contentType) > -1) {

            Calendar inTwoMonths = GeneralUtils.createCalendar();
            inTwoMonths.add(Calendar.MONTH, 2);

            System.out.println(((HttpServletRequest) request).getRequestURI() + " : " + DateUtil.formatDate(inTwoMonths.getTime()));
            httpResponse.setHeader("Expires", DateUtil.formatDate(inTwoMonths.getTime()));
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}
