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
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.myfaces.orchestra.requestParameterProvider.RequestParameterServletFilter;

public class StaticResourceCacheFilter implements Filter {

    private String richfacesResourcePrefix;

    @Override
    public void init(FilterConfig cfg) throws ServletException {
        richfacesResourcePrefix = cfg.getServletContext().getInitParameter(
                "org.ajax4jsf.RESOURCE_URI_PREFIX");
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().indexOf(richfacesResourcePrefix) > -1) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        UrlMatcher matcher = new UrlMatcher();
        matcher.setIncludeContaining("/admin");
        if (!matcher.matches(httpRequest)) {
            // Stop MyFaces Orchestra from wrapping the response
            // in order to add the "?conversationContext" param
            httpRequest.setAttribute(
                    RequestParameterServletFilter.REQUEST_PARAM_FILTER_CALLED,
                    Boolean.TRUE);
        }

        chain.doFilter(httpRequest, new AddExpiresHeader(httpResponse));
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

class AddExpiresHeader extends HttpServletResponseWrapper {

    public static final String[] CACHEABLE_CONTENT_TYPES = new String[] {
        "text/css", "text/javascript", "image/png", "image/jpeg",
        "image/gif", "image/jpg" };

    static {
        Arrays.sort(CACHEABLE_CONTENT_TYPES);
    }

    public AddExpiresHeader(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void setContentType(String contentType) {
        if (contentType != null && Arrays.binarySearch(CACHEABLE_CONTENT_TYPES, contentType) > -1) {
            Calendar inTwoMonths = GeneralUtils.createCalendar();
            inTwoMonths.add(Calendar.MONTH, 2);

            super.setDateHeader("Expires", inTwoMonths.getTimeInMillis());
        } else {
            super.setHeader("Expires", "-1");
            super.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        }
        super.setContentType(contentType);
    }
}
