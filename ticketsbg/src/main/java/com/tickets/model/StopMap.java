package com.tickets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="stopMaps")
@NamedQueries({
    @NamedQuery(
        name = "StopMap.findByStopName",
        query = "SELECT sm FROM StopMap sm WHERE " +
                "sm.stopName=:stopName AND sm.firm=:firm"
    )
})
public class StopMap implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int stopMapId;

    @Column
    private String stopName;

    @Lob
    private String mapUrl;

    @ManyToOne
    private Firm firm;

    public int getStopMapId() {
        return stopMapId;
    }

    public void setStopMapId(int stopMapId) {
        this.stopMapId = stopMapId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }
}
