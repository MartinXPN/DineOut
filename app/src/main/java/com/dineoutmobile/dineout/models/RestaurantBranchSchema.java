package com.dineoutmobile.dineout.models;


import java.util.ArrayList;

public class RestaurantBranchSchema {

    ArrayList <?> backgroundURLs = new ArrayList<>();
    ArrayList <?> basicInfo = new ArrayList<>();
    ArrayList <?> services = new ArrayList<>();
    AddressSchema address = new AddressSchema();
    String phoneNumber;
    Boolean isStreetViewAvailable;
}
