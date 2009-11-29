package com.tickets.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tickets.model.Firm;
import com.tickets.model.Vehicle;

@Service("vehicleService")
public class VehicleServiceImpl extends BaseService<Vehicle> implements
        VehicleService {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger
            .getLogger(VehicleServiceImpl.class);

    @Override
    public List<Vehicle> getVehicles(Firm firm) {
        return getDao().findByNamedQuery("Vehicle.findByFirm",
                new String[] { "firm" }, new Object[] { firm });
    }

}