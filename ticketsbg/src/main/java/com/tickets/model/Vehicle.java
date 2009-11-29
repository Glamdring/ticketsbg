package com.tickets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "vehicles")
@NamedQueries({
    @NamedQuery(
            name = "Vehicle.findByFirm",
            query = "SELECT v FROM Vehicle v WHERE v.firm=:firm"
        )
})
public class Vehicle implements Serializable, Selectable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int vehicleId;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String email;

    @ManyToOne
    private Firm firm;

    @Column
    private int seats = 51;

    @Embedded
    private SeatSettings seatSettings = new SeatSettings();

    public Vehicle() {
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public SeatSettings getSeatSettings() {
        return seatSettings;
    }

    public void setSeatSettings(SeatSettings seatSettings) {
        this.seatSettings = seatSettings;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + vehicleId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vehicle other = (Vehicle) obj;
        if (vehicleId != other.vehicleId)
            return false;
        return true;
    }

    @Override
    public String getLabel() {
        return getName();
    }
}
