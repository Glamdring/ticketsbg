package com.tickets.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Tickets generated by hbm2java
 */
@Entity
@Table(name = "tickets")
@NamedQueries({
    @NamedQuery(
        name = "Ticket.findByUser",
        query = "SELECT t FROM Ticket t WHERE t.user=:user"
    ),
    @NamedQuery(
        name = "Ticket.findUnconfirmed",
        query = "SELECT t FROM Ticket t WHERE t.committed=false"
    ),
    @NamedQuery(
        name = "Ticket.findByCodeAndEmail",
        query = "SELECT t FROM Ticket t WHERE t.ticketCode=:ticketCode AND " +
                "t.customerInformation.email=:email"),
    @NamedQuery(
        name = "Ticket.findSince",
        query = "SELECT t FROM Ticket t " +
                "WHERE t.run.route.firm.firmKey=:firmKey " +
                "AND t.startStop LIKE :fromStop " +
                "AND timestamp(t.creationTime) > :lastCheck " +
                "AND t.committed=true"
            )
})
public class Ticket implements Serializable, Comparable<Ticket> {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName="id")
    private User user;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name="customerId", referencedColumnName="id")
    private Customer customerInformation;

    @Column(length = 16)
    private String paymentCode;

    @Column(length = 30)
    private String ticketCode;

    @Column
    private String startStop;

    @Column
    private String endStop;

    @Column
    private boolean isTwoWay;

    @ManyToOne
    @JoinColumn(name="runId", referencedColumnName="runId")
    private Run run;

    @ManyToOne
    @JoinColumn(name="returnRunId", referencedColumnName="runId")
    private Run returnRun;

    @Column
    private boolean committed;

    @Column
    private boolean paymentInProcess;

    @Column
    private PaymentMethod paymentMethod;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creationTime;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar departureTime;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar returnDepartureTime;

    @Column
    private boolean timeouted;

    @Column
    private boolean altered;

    @Column
    private BigDecimal alterationPriceDifference = BigDecimal.ZERO;

    @Column
    private BigDecimal totalPrice;

    @Column
    private int passengersCount;

    @OneToMany(cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name="tickets_passengerDetails")
    private List<PassengerDetails> passengerDetails = new ArrayList<PassengerDetails>();

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentCode() {
        return this.paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getTicketCode() {
        return this.ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStartStop() {
        return startStop;
    }

    public void setStartStop(String startStop) {
        this.startStop = startStop;
    }

    public String getEndStop() {
        return endStop;
    }

    public void setEndStop(String endStop) {
        this.endStop = endStop;
    }

    public boolean isTwoWay() {
        return isTwoWay;
    }

    public void setTwoWay(boolean isTwoWay) {
        this.isTwoWay = isTwoWay;
    }

    public Customer getCustomerInformation() {
        return customerInformation;
    }

    public void setCustomerInformation(Customer customerInformation) {
        this.customerInformation = customerInformation;
    }

    public boolean isPaymentInProcess() {
        return paymentInProcess;
    }

    public void setPaymentInProcess(boolean paymentInProcess) {
        this.paymentInProcess = paymentInProcess;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }

    public Run getReturnRun() {
        return returnRun;
    }

    public void setReturnRun(Run returnRun) {
        this.returnRun = returnRun;
    }

    public boolean isTimeouted() {
        return timeouted;
    }

    public void setTimeouted(boolean timeouted) {
        this.timeouted = timeouted;
    }

    public boolean isAltered() {
        return altered;
    }

    public void setAltered(boolean altered) {
        this.altered = altered;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(int passengersCount) {
        this.passengersCount = passengersCount;
    }

    public List<PassengerDetails> getPassengerDetails() {
        return passengerDetails;
    }

    public void setPassengerDetails(List<PassengerDetails> passengerDetails) {
        this.passengerDetails = passengerDetails;
    }

    public BigDecimal getAlterationPriceDifference() {
        return alterationPriceDifference;
    }

    public void setAlterationPriceDifference(BigDecimal alterationPriceDifference) {
        this.alterationPriceDifference = alterationPriceDifference;
    }

    @Override
    public int compareTo(Ticket t) {
        if (this.getTicketCode() == null) {
            return -1;
        }

        return this.getTicketCode().compareTo(t.getTicketCode());
    }

    public void addPassengerDetails(PassengerDetails passengerDetails) {
        getPassengerDetails().add(passengerDetails);
    }

    public Calendar getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Calendar departureTime) {
        this.departureTime = departureTime;
    }

    public Calendar getReturnDepartureTime() {
        return returnDepartureTime;
    }

    public void setReturnDepartureTime(Calendar returnDepartureTime) {
        this.returnDepartureTime = returnDepartureTime;
    }
}
