package com.tickets.services;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.tickets.model.News;
import com.tickets.utils.GeneralUtils;

/**
 * The only stateful service in the application. State refreshed by a timer
 *
 * @author Bozhidar Bozhanov
 */
@Service("newsService")
@Scope("singleton")
public class NewsServiceImpl extends BaseService<News> implements NewsService {

    private static final int DAYS_FRESH_DEFAULT = 7;

    @Override
    public List<News> getFreshNews() {
        List<News> newsList = list(News.class);
        for (Iterator<News> it = newsList.iterator(); it.hasNext();) {
            News news = it.next();
            int daysFresh = news.getCustomDaysFresh() > 0 ? news
                    .getCustomDaysFresh() : DAYS_FRESH_DEFAULT;

            Calendar freshnessTreshold = GeneralUtils.createCalendar();
            freshnessTreshold.add(Calendar.DAY_OF_YEAR, -daysFresh);
            if (news.getDate().compareTo(freshnessTreshold) < 0) {
                it.remove();
            }
        }

        return newsList;
    }

}
