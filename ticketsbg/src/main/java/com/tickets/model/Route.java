package com.tickets.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.tickets.utils.GeneralUtils;

@Entity
@Table(name = "routes")
public class Route extends DataObject implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="firmId", referencedColumnName="firmId")
    private Firm firm;

    @Column
    private String name;

    @Column
    private int seats;

    @OneToMany(mappedBy="route")
    @OrderBy("minutes")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private List<RouteHour> routeHours = new ArrayList<RouteHour>();

    @OneToMany(mappedBy="route")
    @OrderBy("day")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private List<RouteDay> routeDays = new ArrayList<RouteDay>();

    @OneToMany(mappedBy="route")
    @OrderBy("idx")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private List<Stop> stops = new ArrayList<Stop>();


    @OneToMany(mappedBy="route")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.ALL})
    private List<Price> prices = new ArrayList<Price>();


    @OneToMany(mappedBy="route")
    @LazyCollection(LazyCollectionOption.TRUE)
    @Cascade({CascadeType.ALL})
    private List<Run> runs = new ArrayList<Run>();


    @OneToMany(mappedBy="route")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade({CascadeType.ALL})
    private List<Discount> discounts;

    @Column
    private int publishedRunsPeriod = 30;

    @Column
    private boolean singleRun;

    //Creating an empty calendar, so that the UI can interact
    //directly with the model
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar singleRunDateTime = GeneralUtils.createEmptyCalendar();

    @Column
    private boolean allowSeatChoice;

    @Column
    private boolean seasonal;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar seasonStart = GeneralUtils.createEmptyCalendar();

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar seasonEnd = GeneralUtils.createEmptyCalendar();

    @Embedded
    private SeatSettings seatSettings = new SeatSettings();

    public Route() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public int getSeats() {
        return this.seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RouteHour> getRouteHours() {
        return routeHours;
    }

    @Deprecated
    public void setRouteHours(List<RouteHour> routeHours) {
        this.routeHours = routeHours;
    }

    public List<RouteDay> getRouteDays() {
        return routeDays;
    }

    public void setRouteDays(List<RouteDay> routeDays) {
        this.routeDays = routeDays;
    }

    public void addRouteDay(RouteDay rd) {
        rd.setRoute(this);
        routeDays.add(rd);
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public List<Stop> getStops() {
        return stops;
    }

    @Deprecated
    public void setStops(List<Stop> stops) {
        //Not acting in case MyFaces calls it
        //if (stops instanceof PersistentBag)
            this.stops = stops;
    }

    public void addStop(Stop stop) {
        stop.setRoute(this);
        stops.add(stop);
    }

    public void addHour(RouteHour hour) {
        hour.setRoute(this);
        routeHours.add(hour);
    }

    public void addPrice(Price price) {
        price.setRoute(this);
        prices.add(price);
    }

    public int getPublishedRunsPeriod() {
        return publishedRunsPeriod;
    }

    public void setPublishedRunsPeriod(int publishedRunsPeriod) {
        this.publishedRunsPeriod = publishedRunsPeriod;
    }

    public List<Run> getRuns() {
        return runs;
    }

    @Deprecated
    public void setRuns(List<Run> runs) {
        this.runs = runs;
    }

    public void addRun(Run run) {
        run.setRoute(this);
        runs.add(run);
    }

    public boolean isSingleRun() {
        return singleRun;
    }

    public void setSingleRun(boolean singleRun) {
        this.singleRun = singleRun;
    }

    public boolean isAllowSeatChoice() {
        return allowSeatChoice;
    }

    public void setAllowSeatChoice(boolean allowSeatChoice) {
        this.allowSeatChoice = allowSeatChoice;
    }

    public Calendar getSingleRunDateTime() {
        return singleRunDateTime;
    }

    public void setSingleRunDateTime(Calendar singleRunDateTime) {
        this.singleRunDateTime = singleRunDateTime;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    @Deprecated
    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public void addDiscount(Discount discount) {
        discount.setRoute(this);
        discounts.add(discount);
    }

    public boolean isSeasonal() {
        return seasonal;
    }

    public void setSeasonal(boolean seasonal) {
        this.seasonal = seasonal;
    }

    public Calendar getSeasonStart() {
        return seasonStart;
    }

    public void setSeasonStart(Calendar seasonStart) {
        this.seasonStart = seasonStart;
    }

    public Calendar getSeasonEnd() {
        return seasonEnd;
    }

    public void setSeasonEnd(Calendar seasonEnd) {
        this.seasonEnd = seasonEnd;
    }

    public SeatSettings getSeatSettings() {
        return seatSettings;
    }

    public void setSeatSettings(SeatSettings seatSettings) {
        this.seatSettings = seatSettings;
    }
}