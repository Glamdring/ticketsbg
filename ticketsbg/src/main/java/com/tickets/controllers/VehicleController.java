package com.tickets.controllers;

import java.util.Arrays;
import java.util.List;

import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.handlers.SeatHandler;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.Vehicle;
import com.tickets.services.Service;
import com.tickets.services.VehicleService;
import com.tickets.utils.SelectItemUtils;


@Controller("vehicleController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
public class VehicleController extends BaseCRUDController<Vehicle> {

    @Autowired
    private VehicleService service;

    @Autowired
    private SeatController seatController;

    private Vehicle vehicle = new Vehicle();

    private ListDataModel vehiclesModel;

    private List<SelectItem> vehicleSelectItems;

    @Override
    public void save() {
        vehicle.setFirm(LoggedUserHolder.getUser().getFirm());
        super.save();
    }


    public void showEdit() {
        seatController.setSeatHandler(new SeatHandler(getEntity()));
    }

    @Override
    protected void refreshList() {
        List<Vehicle> vehicles = service.getVehicles(LoggedUserHolder.getUser().getFirm());
        vehiclesModel = new ListDataModel(vehicles);

        // End the current conversation in case the list of roles
        // is refreshed, but only if the bean has not just been constructed
        if (vehicle != null)
            endConversation();
    }

    public List<SelectItem> getVehicleSelectItems() {
        if (vehicleSelectItems == null) {
            vehicleSelectItems = SelectItemUtils.formSelectItems(service.getVehicles(LoggedUserHolder.getUser().getFirm()));
        }

        return vehicleSelectItems;
    }

    @Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
    public Vehicle getVehicle() {
        return vehicle;
    }


    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
    public ListDataModel getVehiclesModel() {
        return vehiclesModel;
    }

    public void setVehiclesModel(ListDataModel vehiclesModel) {
        this.vehiclesModel = vehiclesModel;
    }

    @Override
    protected Vehicle createEntity() {
        Vehicle vehicle = new Vehicle();
        vehicle.getSeatSettings().getMissingSeats().addAll(Arrays.asList(RouteController.DEFAULT_SECOND_DOOR_SEATS));
        return vehicle;
    }

    @Override
    protected Vehicle getEntity() {
        return vehicle;
    }

    @Override
    protected void setEntity(Vehicle entity) {
        setVehicle(entity);
    }

    @Override
    protected String getListScreenName() {
        return "vehiclesList";
    }

    @Override
    protected String getSingleScreenName() {
        return null;
    }

    @Override
    protected ListDataModel getModel() {
        return vehiclesModel;
    }

    @Override
    protected Service getService() {
        return service;
    }
}
