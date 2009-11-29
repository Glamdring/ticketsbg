package com.tickets.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.tickets.utils.GeneralUtils;

@Entity
@Table(name = "news")
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int newsId;

    @Column
    private String headline;

    @Column(length=1000)
    private String content;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar date = GeneralUtils.createCalendar();

    @Column
    private int customDaysFresh;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getCustomDaysFresh() {
        return customDaysFresh;
    }

    public void setCustomDaysFresh(int customDaysFresh) {
        this.customDaysFresh = customDaysFresh;
    }
}
