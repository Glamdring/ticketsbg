package com.tickets.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

import com.tickets.model.Route;
import com.tickets.model.Run;
import com.tickets.model.Ticket;

public class SeatHandler {

    private Run run;
    private Route route;
    private Integer[] used;

    private List<Row> rows;
    private List<SelectItem> seatSelectItems;
    private List<Seat> selectedSeats;

    public SeatHandler(Route route) {
        this.route = route;
        refreshRows();
    }

    public SeatHandler(Run run) {
        this.route = run.getRoute();
        this.run = run;
        initVacantSeats();
        refreshRows();
    }


    public void refreshRows() {
        //TODO - implement differently - allow adding rows and selecting existing places

        int skippedSeats = 0;
        int rowCount = route.getSeats() / 4;
        if (route.getSeatSettings().getSecondDoorRow() > 0) {
            rowCount ++;
        }
        rows = new ArrayList<Row>(rowCount);

        for (int i = 0; i < rowCount; i ++) {
            Row row = new Row();
            boolean lastRow = i == route.getSeats() / 4 - 1;

            if (!lastRow || route.getSeatSettings().isLastRowHasFourSeats()) {
                if (route.getSeatSettings().isStartRight()) {
                    row.setFirst(new Seat(i * 4 + 4 - skippedSeats));
                    row.setSecond(new Seat(i * 4 + 3 - skippedSeats));
                    if (route.getSeatSettings().getSecondDoorRow() == i + 1) {
                        skippedSeats += 2;
                        row.setId(i + 1);
                        continue;
                    }
                    row.setThird(new Seat(i * 4 + 2 - skippedSeats));
                    row.setFourth(new Seat(i * 4 + 1 - skippedSeats));
                    row.setId(i + 1);
                } else {
                    row.setFirst(new Seat(i * 4 + 1 - skippedSeats));
                    row.setSecond(new Seat(i * 4 + 2 - skippedSeats));
                    if (route.getSeatSettings().getSecondDoorRow() == i + 1) {
                        skippedSeats += 2;
                        row.setId(i + 1);
                        continue;
                    }
                    row.setThird(new Seat(i * 4 + 3 - skippedSeats));
                    row.setFourth(new Seat(i * 4 + 4 - skippedSeats));
                    row.setId(i + 1);
                }
            } else {
                 if (route.getSeatSettings().isStartRight()) {
                     row.setFirst(new Seat(i * 4 + 5 - skippedSeats));
                     row.setSecond(new Seat(i * 4 + 4 - skippedSeats));
                     row.setMiddleSeat(new Seat(i * 4 + 3 - skippedSeats));
                     row.setThird(new Seat(i * 4 + 2 - skippedSeats));
                     row.setFourth(new Seat(i * 4 + 1 - skippedSeats));
                     row.setId(i + 1);
                 } else {
                     row.setFirst(new Seat(i * 4 + 1 - skippedSeats));
                     row.setSecond(new Seat(i * 4 + 2 - skippedSeats));
                     row.setMiddleSeat(new Seat(i * 4 + 3 - skippedSeats));
                     row.setThird(new Seat(i * 4 + 4 - skippedSeats));
                     row.setFourth(new Seat(i * 4 + 5 - skippedSeats));
                     row.setId(i + 1);
                 }
            }

            // Compute vacancies only when purchasing a ticket for a specific run
            if (run != null) {
                row.getFirst().setVacant(isVacant(row.getFirst().getNumber()));
                row.getSecond().setVacant(isVacant(row.getSecond().getNumber()));
                row.getThird().setVacant(isVacant(row.getThird().getNumber()));
                row.getFourth().setVacant(isVacant(row.getFourth().getNumber()));
                if (row.getMiddleSeat() != null) {
                    row.getMiddleSeat().setVacant(isVacant(row.getMiddleSeat().getNumber()));
                }
            }

            rows.add(row);
        }

        seatSelectItems = new ArrayList<SelectItem>(route.getSeats());
        for (Row row : rows) {
            if (route.getSeatSettings().isStartRight()) {
                SelectItem si = new SelectItem(row.getFourth(), "" + row.getFourth().getNumber());
                si.setDisabled(!row.getFourth().isVacant() || run == null);
                seatSelectItems.add(si);

                si = new SelectItem(row.getThird(), "" + row.getThird().getNumber());
                si.setDisabled(!row.getThird().isVacant() || run == null);
                seatSelectItems.add(si);

                if (row.getMiddleSeat() != null) {
                    si = new SelectItem(row.getMiddleSeat(), "" + row.getMiddleSeat().getNumber());
                    si.setDisabled(!row.getMiddleSeat().isVacant() || run == null);
                    seatSelectItems.add(si);
                }

                if (route.getSeatSettings().getSecondDoorRow() != row.getId()) {
                    si = new SelectItem(row.getSecond(), "" + row.getSecond().getNumber());
                    si.setDisabled(!row.getSecond().isVacant() || run == null);
                    seatSelectItems.add(si);

                    si = new SelectItem(row.getFirst(), "" + row.getFirst().getNumber());
                    si.setDisabled(!row.getFirst().isVacant() || run == null);
                    seatSelectItems.add(si);
                }
            } else {
                SelectItem si = new SelectItem(row.getFirst(), "" + row.getFirst().getNumber());
                si.setDisabled(!row.getFirst().isVacant() || run == null);
                seatSelectItems.add(si);

                si = new SelectItem(row.getSecond(), "" + row.getSecond().getNumber());
                si.setDisabled(!row.getSecond().isVacant() || run == null);
                seatSelectItems.add(si);

                if (row.getMiddleSeat() != null) {
                    si = new SelectItem(row.getMiddleSeat(), "" + row.getMiddleSeat().getNumber());
                    si.setDisabled(!row.getMiddleSeat().isVacant() || run == null);
                    seatSelectItems.add(si);
                }
                if (route.getSeatSettings().getSecondDoorRow() != row.getId()) {
                    si = new SelectItem(row.getThird(), "" + row.getThird().getNumber());
                    si.setDisabled(!row.getThird().isVacant() || run == null);
                    seatSelectItems.add(si);

                    si = new SelectItem(row.getFourth(), "" + row.getFourth().getNumber());
                    si.setDisabled(!row.getFourth().isVacant() || run == null);
                    seatSelectItems.add(si);
                }
            }
        }
    }

    private boolean isVacant(int number) {
        if (Arrays.binarySearch(used, number) < 0) {
            return true;
        }
        return false;
    }

    public void initVacantSeats() {
        List<Integer> u = new ArrayList<Integer>();

        for (Ticket ticket : run.getTickets()) {
            u.add(ticket.getSeat());
        }
        for (Ticket ticket : run.getReturnTickets()) {
            u.add(ticket.getReturnSeat());
        }

        used = u.toArray(new Integer[u.size()]);
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public List<SelectItem> getSeatSelectItems() {
        return seatSelectItems;
    }

    public void setSeatSelectItems(List<SelectItem> seatSelectItems) {
        this.seatSelectItems = seatSelectItems;
    }

    public List<Seat> getSelectedSeats() {
        return selectedSeats;
    }

    public void setSelectedSeats(List<Seat> selectedSeats) {
        this.selectedSeats = selectedSeats;
    }
}
