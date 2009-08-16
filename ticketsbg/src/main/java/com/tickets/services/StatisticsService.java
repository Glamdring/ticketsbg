package com.tickets.services;


public interface StatisticsService extends Service {

    int getCompaniesCount();

    int getDestinationsCount();

    void refreshStatistics();
}
