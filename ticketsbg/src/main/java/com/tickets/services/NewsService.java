package com.tickets.services;

import java.util.List;

import com.tickets.model.News;


public interface NewsService extends Service<News> {

    List<News> getFreshNews();

}
