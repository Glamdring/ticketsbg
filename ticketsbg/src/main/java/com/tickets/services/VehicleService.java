package com.tickets.services;

import java.util.List;

import com.tickets.model.Firm;
import com.tickets.model.Vehicle;

public interface VehicleService extends Service<Vehicle> {

    List<Vehicle> getVehicles(Firm firm);
}
