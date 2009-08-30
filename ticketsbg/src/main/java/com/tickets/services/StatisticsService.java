package com.tickets.services;

import java.util.Date;
import java.util.List;

import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.stats.SoldTickets;

public interface StatisticsService extends Service {

    public static final int BY_RUN_TIME = 0;
    public static final int BY_PURCHASE_TIME = 1;

    int getCompaniesCount();

    int getDestinationsCount();

    void refreshStatistics();

    /**
     * Gets the sold tickets statistics for the given period
     *
     * @param route
     *     	the route for which the statistics is made if null is passed,
     *     	then all routes are assumed
     *
     * @param period
     *     	an integer constant from Calendar representing the period
     *
     * @param timeType
     * 		an integer constant identifying whether the run time, or
     * 		the purchase time should be taken into account
     *
     * @param firm
     * 		the firm of the currently logged user; null if super-admin
     *
     * @param fromDate
     * 		the starting date of the period for which the stats will
     * 		be calculated
     *
     * @param toDate
     * 		the last date of the period for which the stats will
     * 		be calculated
     *
     * @return list of SoldTickets to be used for statistics
     */
    List<SoldTickets> getSoldTickets(Route route,
            int period,
            int timeType,
            Firm firm, Date fromDate, Date toDate);
}
