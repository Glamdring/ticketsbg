package com.tickets.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.ManagedSessionContext;

/**
 * To Support Long Conversations set isLongConversation to true
 */
public class SessionFilter implements Filter {

    private SessionFactory sessionFactory;

    private boolean isLongConversation = false;

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();

        Session hibernateSession = null;

        if (isLongConversation)
            hibernateSession = (Session) session.getAttribute(Constants.SESSION_KEY);

        if (hibernateSession == null){
            hibernateSession = sessionFactory.openSession();
        }
        if (isLongConversation)
            ManagedSessionContext.bind((org.hibernate.classic.Session) hibernateSession);

        chain.doFilter(request, response);

        if (isLongConversation)
            hibernateSession = ManagedSessionContext.unbind(sessionFactory);

        if (isLongConversation) {
            if (request.getParameter(Constants.END_LONG_CONVERSATION_FLAG) != null){
                hibernateSession.close();
            } else {
                session.setAttribute(Constants.SESSION_KEY, hibernateSession);
            }
        } else {
            hibernateSession.close();
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
            sessionFactory = (SessionFactory) Beans.get("sessionFactory");
    }

}