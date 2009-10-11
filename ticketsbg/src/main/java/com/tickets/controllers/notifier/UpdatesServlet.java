package com.tickets.controllers.notifier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tickets.services.TicketService;

public class UpdatesServlet extends HttpServlet {

    private static final String FIRM_KEY_PARAM = "firmKey";
    private static final String FROM_STOP_PARAM = "fromStop";
    private static final String LAST_CHECK_PARAM = "lastCheck";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        request.setCharacterEncoding("utf-8");

        TicketService service = (TicketService) WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext()).getBean(
                        "ticketService");

        // No validation, because all attempts to open this servlet
        // should fail unless they are issued from the notifier client
        String firmKey = request.getParameter(FIRM_KEY_PARAM);
        String fromStop = request.getParameter(FROM_STOP_PARAM);
        long lastCheck = Long.parseLong(request.getParameter(LAST_CHECK_PARAM));

        long currentCheck = System.currentTimeMillis();
        int newTickets = service.getNewTicketsSinceLastChecks(firmKey, fromStop, lastCheck);

        response.getWriter().print(newTickets + ":" + currentCheck);
    }
}
