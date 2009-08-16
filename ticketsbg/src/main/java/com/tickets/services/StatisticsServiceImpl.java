package com.tickets.services;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.tickets.model.Firm;

/**
 * The only stateful service in the application.
 * State refreshed by a timer
 * @author Bozhidar Bozhanov
 */
@Service("statisticsService")
@Scope("singleton")
public class StatisticsServiceImpl extends BaseService implements StatisticsService {

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

}
