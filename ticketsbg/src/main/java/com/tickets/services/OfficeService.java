package com.tickets.services;

import java.util.List;

import com.tickets.model.Firm;
import com.tickets.model.Office;

public interface OfficeService extends Service<Office> {

    List<Office> getOffices(Firm firm);
}
