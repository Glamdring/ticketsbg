package com.tickets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.model.News;
import com.tickets.services.NewsService;

@Controller("publicNewsController")
@Scope("request")
public class PublicNewsController extends BaseController {

    @Autowired
    private NewsService newsService;

    private List<News> freshNewsList;

    @Action(accessLevel = AccessLevel.PUBLIC)
    public List<News> getFreshNews() {
        if (freshNewsList == null) {
            freshNewsList = newsService.getFreshNews();
        }
        return freshNewsList;
    }
}
