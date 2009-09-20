package com.tickets.controllers.lifecycle;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

import com.tickets.constants.Constants;
import com.tickets.model.Firm;
import com.tickets.services.Service;
import com.tickets.utils.Beans;
import com.tickets.utils.GeneralUtils;

public class FirmIdentifier implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    @Override
    public void beforePhase(PhaseEvent evt) {
        HttpServletRequest request =
            (HttpServletRequest) evt.getFacesContext().getExternalContext().getRequest();

        String subdomain = GeneralUtils.getSubdomain(request);

        Firm currentFirm = (Firm) request.getSession().getAttribute(Constants.CURRENT_FIRM_KEY);
        if (currentFirm != null && (currentFirm.getSubdomain() == null || currentFirm.getSubdomain().equals(subdomain))) {
            request.getSession().removeAttribute(Constants.CURRENT_FIRM_KEY);
        }

        if (subdomain == null || subdomain.length() == 0) {
            return;
        }

        Service<Firm> service = (Service<Firm>) Beans.get("baseService");
        Firm firm = service.get(Firm.class, "subdomain", subdomain);

        if (firm != null) {
            request.getSession().setAttribute(Constants.CURRENT_FIRM_KEY, firm);
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
