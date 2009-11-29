package com.tickets.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.tickets.utils.GeneralUtils;

@Entity
@Table(name="promotions")
public class Promotion extends DataObject implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int promotionId;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar start = GeneralUtils.createCalendar();

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar end = GeneralUtils.createCalendar();

    @Column
    private String shortText;

    @Column
    private String richText;

    @ManyToOne
    private Firm firm;

    @Column
    private boolean sendToEmail;

    @Column
    private boolean showInSite;

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getRichText() {
        return richText;
    }

    public void setRichText(String richText) {
        this.richText = richText;
    }

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public boolean isSendToEmail() {
        return sendToEmail;
    }

    public void setSendToEmail(boolean sendToEmail) {
        this.sendToEmail = sendToEmail;
    }

    public boolean isShowInSite() {
        return showInSite;
    }

    public void setShowInSite(boolean showInSite) {
        this.showInSite = showInSite;
    }
}
