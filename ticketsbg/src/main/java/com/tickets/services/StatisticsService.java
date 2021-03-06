package com.tickets.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.tickets.controllers.valueobjects.PurchaseMeansType;
import com.tickets.controllers.valueobjects.StatsDataType;
import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.Ticket;
import com.tickets.model.stats.StatsHolder;

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
     * @param dataType
     * 		whether sold tickets count, or summed prices should be seen
     *
     * @param selectedPurchaseMeansType
     * 		whether to display online tickets, cash-desk tickets, or both
     *
     * @return list of SoldTickets to be used for statistics
     */
    List<StatsHolder> getStatistics(Route route,
            int period,
            int timeType,
            Firm firm,
            Date fromDate,
            Date toDate,
            StatsDataType dataType,
            PurchaseMeansType selectedPurchaseMeansType);


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
     * @param dataType
     * 		whether sold tickets count, or summed prices should be seen
     *
     * @param selectedPurchaseMeansType
     * 		whether to display online tickets, cash-desk tickets, or both
     *
     * @return list of SoldTickets to be used for statistics
     */
    List<Ticket> getTickets(Route route,
            int period,
            int timeType,
            Firm firm,
            Date fromDate,
            Date toDate,
            StatsDataType dataType,
            PurchaseMeansType selectedPurchaseMeansType);

    /**
     * Calculates the total price of the tickets
     *
     * @param tickets
     * @return total price
     */
    BigDecimal getTotalPrice(List<Ticket> tickets);

    /**
     * Calculates the number of passengers
     * @param tickets
     * @return count
     */
    int getTotalPassengersCount(List<Ticket> tickets);
}
