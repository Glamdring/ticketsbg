package com.tickets.controllers;

import javax.faces.model.ListDataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.model.News;
import com.tickets.services.NewsService;
import com.tickets.services.Service;

@Controller("newsController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.ADMINISTRATOR)
public class NewsController extends BaseCRUDController<News> {

    @Autowired
    private NewsService newsService;

    private News news;

    @Override
    protected News createEntity() {
        return new News();
    }

    @Override
    protected News getEntity() {
        return getNews();
    }

    @Override
    protected String getListScreenName() {
        return "newsList";
    }

    public ListDataModel getNewsListModel() {
        return getModel();
    }

    @Override
    protected ListDataModel getModel() {
        return new ListDataModel(newsService.list(News.class));
    }

    @Override
    protected Service getService() {
        return newsService;
    }

    @Override
    protected String getSingleScreenName() {
        return null;
    }

    @Override
    protected void refreshList() {
        // nothing, the list is refreshed on get
    }

    @Override
    protected void setEntity(News entity) {
        setNews(entity);
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

}
