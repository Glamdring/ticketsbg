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
import org.apache.myfaces.orchestra.requestParameterProvider.RequestParameterServletFilter;

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

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        UrlMatcher matcher = new UrlMatcher();
        matcher.setIncludeContaining("/admin");
        if (!matcher.matches(httpRequest)) {
            // Stop MyFaces Orchestra from wrapping the response
            // in order to add the "?conversationContext" param
            httpRequest.setAttribute(
                    RequestParameterServletFilter.REQUEST_PARAM_FILTER_CALLED,
                    Boolean.TRUE);
        }

        chain.doFilter(httpRequest, httpResponse);


        String contentType = httpResponse.getContentType();
        if (contentType == null
                || Arrays.binarySearch(CACHEABLE_CONTENT_TYPES, contentType) > -1) {

            Calendar inTwoMonths = GeneralUtils.createCalendar();
            inTwoMonths.add(Calendar.MONTH, 2);

            httpResponse.setHeader("Expires", DateUtil.formatDate(inTwoMonths.getTime()));
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}

class UrlMatcher {
    private String includeContaining;

    public boolean matches(HttpServletRequest req) {

        if (includeContaining != null) {
            return (req.getRequestURI().indexOf(includeContaining) > -1);
        }

        return true;
    }

    public String getIncludeContaining() {
        return includeContaining;
    }

    public void setIncludeContaining(String includeContaining) {
        this.includeContaining = includeContaining;
    }
}