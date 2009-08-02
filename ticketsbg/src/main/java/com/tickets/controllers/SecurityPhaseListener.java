package com.tickets.controllers;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SecurityPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent evt) {
        //TODO security check - both for logged user and for privilleges on @Actions
//        if (evt.getPhaseId().equals(PhaseId.RESTORE_VIEW)) {
//            User user = FacesUtils.getLoggedUser();
//            String viewId = evt.getFacesContext().getViewRoot().getViewId();
//            // TODO check if user is allowed to current service
//            if (user == null && viewId.indexOf(Constants.LOGIN_PAGE) == -1) {
//                FacesUtils.setRedirectViewId(viewId);
//                evt.getFacesContext().getApplication().getNavigationHandler()
//                        .handleNavigation(evt.getFacesContext(), null,
//                                Screen.LOGIN_SCREEN.getOutcome());
//
//                FacesContext.getCurrentInstance().renderResponse();
//            }
//        }
    }

    @Override
    public void beforePhase(PhaseEvent evt) {

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}

