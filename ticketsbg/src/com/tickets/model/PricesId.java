package com.tickets.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PricesId generated by hbm2java
 */
@Embeddable
public class PricesId implements java.io.Serializable {

	private int routeId;

	private int start;

	private int end;

	public PricesId() {
	}

	public PricesId(int routeId, int start, int end) {
		this.routeId = routeId;
		this.start = start;
		this.end = end;
	}

	@Column(name = "route_id", nullable = false)
	public int getRouteId() {
		return this.routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	@Column(name = "start", nullable = false)
	public int getStart() {
		return this.start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	@Column(name = "end", nullable = false)
	public int getEnd() {
		return this.end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PricesId))
			return false;
		PricesId castOther = (PricesId) other;

		return (this.getRouteId() == castOther.getRouteId())
				&& (this.getStart() == castOther.getStart())
				&& (this.getEnd() == castOther.getEnd());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRouteId();
		result = 37 * result + this.getStart();
		result = 37 * result + this.getEnd();
		return result;
	}

}
