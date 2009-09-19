package com.tickets.services;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.tickets.controllers.valueobjects.PurchaseMeansType;
import com.tickets.controllers.valueobjects.StatsDataType;
import com.tickets.model.Firm;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Route;
import com.tickets.model.Ticket;
import com.tickets.model.stats.StatsHolder;
import com.tickets.utils.GeneralUtils;

/**
 * The only stateful service in the application.
 * State refreshed by a timer
 * @author Bozhidar Bozhanov
 */
@Service("statisticsService")
@Scope("singleton")
public class StatisticsServiceImpl
    extends BaseService
    implements StatisticsService {

    private int companiesCount;
    private int destinationsCount;

    @Override
    public int getCompaniesCount() {
        return companiesCount;
    }

    @Override
    public int getDestinationsCount() {
        return destinationsCount;
    }

    @SuppressWarnings("unchecked")
    @Override
    @PostConstruct
    public void refreshStatistics() {
        List destResult = getDao()
            .findByQuery("SELECT DISTINCT stop FROM Stop stop");
        destinationsCount = destResult.size();

        companiesCount = list(Firm.class).size();

    }

    @Override
    public List<StatsHolder> getStatistics(Route route, int period,
            int timeType, Firm firm, Date fromDate, Date toDate,
            StatsDataType dataType, PurchaseMeansType purchaseMeansType) {

        List<Ticket> preResult = getTickets(route, period, timeType, firm,
                fromDate, toDate, dataType, purchaseMeansType);

        List<StatsHolder> result = new LinkedList<StatsHolder>();

        for (Ticket ticket : preResult) {
            Calendar time = null;
            if (timeType == StatisticsService.BY_RUN_TIME) {
                time = ticket.getRun().getTime();
            } else if (timeType == StatisticsService.BY_PURCHASE_TIME){
                time = ticket.getCreationTime();
            }
            String periodStr = getPeriodString(time, period);
            StatsHolder sh = new StatsHolder(periodStr, BigDecimal.ZERO);
            int pos = Collections.binarySearch(result, sh);
            if (pos > -1) {
                sh = result.get(pos);
            }

            if (dataType == StatsDataType.TICKETS_COUNT) {
                sh.setValue(sh.getValue().add(BigDecimal.ONE));
            }
            if (dataType == StatsDataType.MONEY) {
                sh.setValue(sh.getValue().add(ticket.getPrice()));
            }
            result.add(sh);
        }
        //TODO order day of week

        return result;

    }


    @SuppressWarnings("unchecked")
    public List<Ticket> getTickets(Route route, int period,
            int timeType, Firm firm, Date fromDate, Date toDate,
            StatsDataType dataType, PurchaseMeansType purchaseMeansType) {

        String query = "SELECT t FROM Ticket t WHERE t.timeouted=false AND t.committed=true AND ";
        //String query = "SELECT t FROM Ticket t WHERE ";
        List<String> paramNames = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        if (route == null) {
            query += "t.run.route.firm=:firm ";
            paramNames.add("firm");
            values.add(firm);
        } else {
            query += "t.run.route=:route ";
            paramNames.add("route");
            values.add(route);
        }

        if (purchaseMeansType != PurchaseMeansType.BOTH) {
            //difference is only "=" and "!="
            if (purchaseMeansType == PurchaseMeansType.AT_CASH_DESK) {
                query += " AND paymentMethod=:paymentMethod";
            } else if (purchaseMeansType == PurchaseMeansType.ONLINE) {
                query += " AND paymentMethod!=:paymentMethod";

            }
            paramNames.add("paymentMethod");
            values.add(PaymentMethod.CASH_DESK);
        }

        List<Ticket> result = getDao().findByQuery(query,
                paramNames.toArray(new String[]{}),
                values.toArray());

        if (fromDate == null && toDate != null) {
            fromDate = new Date(0);
        }
        if (fromDate != null && toDate == null) {
            toDate = new Date();
        }

        if (fromDate != null && toDate != null) {
            for (Iterator<Ticket> it = result.iterator(); it.hasNext();) {
                Ticket t = it.next();

                // setting default value, just in case
                Date time = t.getRun().getTime().getTime();

                if (timeType == StatisticsService.BY_RUN_TIME) {
                    time = t.getRun().getTime().getTime();
                }
                if (timeType == StatisticsService.BY_PURCHASE_TIME) {
                    time = t.getCreationTime().getTime();
                }

                if (time.compareTo(fromDate) < 0
                    || time.compareTo(toDate) > 0) {
                    it.remove();
                }
            }
        }

        return result;
    }

    private String getPeriodString(Calendar time, int period) {
        String result = "";
        DateFormat df;
        if (period == Calendar.DAY_OF_MONTH) {
            df = new SimpleDateFormat("dd.MM", GeneralUtils.getLocale());
            result = df.format(time.getTime());
        }
        if (period == Calendar.MONTH) {
            df = new SimpleDateFormat("MM.yyyy", GeneralUtils.getLocale());
            result = df.format(time.getTime());
        }

        if (period == Calendar.DAY_OF_WEEK) {
            df = new SimpleDateFormat("EEEE", GeneralUtils.getLocale());
            result = df.format(time.getTime());
        }

        return result;

    }
}
