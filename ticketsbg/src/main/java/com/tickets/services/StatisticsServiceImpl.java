package com.tickets.services;

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

import com.tickets.model.Firm;
import com.tickets.model.Route;
import com.tickets.model.Ticket;
import com.tickets.model.stats.SoldTickets;
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

    @SuppressWarnings({ "unchecked", "null" })
    @Override
    public List<SoldTickets> getSoldTickets(Route route, int period,
            int timeType, Firm firm, Date fromDate, Date toDate) {

        String query = "SELECT t FROM Ticket t WHERE ";
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

        List<Ticket> preResult = getDao().findByQuery(query,
                paramNames.toArray(new String[]{}),
                values.toArray());

        if (fromDate == null && toDate != null) {
            fromDate = new Date(0);
        }
        if (fromDate != null && toDate == null) {
            toDate = new Date();
        }
        if (fromDate != null && toDate != null) {
            for (Iterator<Ticket> it = preResult.iterator(); it.hasNext();) {
                Ticket t = it.next();
                Date time = null;
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

        List<SoldTickets> result = new LinkedList<SoldTickets>();

        for (Ticket ticket : preResult) {
            Calendar time = null;
            if (timeType == StatisticsService.BY_RUN_TIME) {
                time = ticket.getRun().getTime();
            } else if (timeType == StatisticsService.BY_PURCHASE_TIME){
                time = ticket.getCreationTime();
            }
            String periodStr = getPeriodString(time, period);
            SoldTickets st = new SoldTickets(periodStr, 0);
            int pos = Collections.binarySearch(result, st);
            if (pos > -1) {
                st = result.get(pos);
            }

            st.setTickets(st.getTickets() + 1);
            result.add(st);
        }
        //TODO order day of week

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
