package com.dineoutmobile.dineout.util.models;


public class ReservedRestaurantInfo {

    private enum ReservationStatus {
        PENDING,
        RESERVED,
        VISITED,
        CANCELLED;
    }

    public ReservationStatus status = ReservationStatus.PENDING;
    public String deviceId = "device_id";
    public String personName = "Martin";
    public String personPhoneNumber = "099 029090";
    public int numberOfPeople = 2;
    public String date = "Jun 12";
    public String time = "19:30";
    public String restaurantName = "El Sky";
    public Address restaurantAddress = new Address();
}
