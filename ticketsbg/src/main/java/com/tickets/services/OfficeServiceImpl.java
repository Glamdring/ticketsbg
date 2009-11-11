package com.tickets.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tickets.model.Firm;
import com.tickets.model.Office;

@Service("officeService")
public class OfficeServiceImpl extends BaseService<Office> implements OfficeService {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(OfficeServiceImpl.class);

    @Override
    public List<Office> getOffices(Firm firm) {
        return getDao().findByNamedQuery("Office.findByFirm",
                new String[] { "firm" }, new Object[] { firm });
    }

}