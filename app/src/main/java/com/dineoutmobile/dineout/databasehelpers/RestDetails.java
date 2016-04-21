package com.dineoutmobile.dineout.databasehelpers;

/**
 * Created by hhovik on 4/17/16.
 */
public class RestDetails {

String workTime = "17:00 - 24:00";
    private String priceRange = new String("Expensive");
    private String webSite;

    private String restName;
    private String phone1;
    private String phone2;
    private boolean iswifi = true;
    private boolean hasVIP = true;
    private boolean hasFourchet = true;
    private boolean hasShipping = true;
    private String creditCards = "";
    private String address;
    private boolean  hasSmoking = true;
    private boolean hasNonSmoking = true;
    private boolean wiFi;

    public String getRestName() {
        return restName;
    }

    public String getWork_time() {
        return workTime;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public boolean iswifi() {
        return iswifi;
    }

    public boolean isHasVIP() {
        return hasVIP;
    }

    public boolean isHasFourchet() {
        return hasFourchet;
    }

    public boolean isHasShipping() {
        return hasShipping;
    }

    public String getCreditCards() {
        return creditCards;
    }

    public String getAddress() {
        return address;
    }

    public boolean isHasSmoking() {
        return hasSmoking;
    }

    public boolean isHasNonSmoking() {
        return hasNonSmoking;
    }



    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public void setWorkTime(String work_time) {
        this.workTime = work_time;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public void setIswifi(boolean iswifi) {
        this.iswifi = iswifi;
    }

    public void setHasVIP(boolean hasVIP) {
        this.hasVIP = hasVIP;
    }

    public void setHasFourchet(boolean hasFourchet) {
        this.hasFourchet = hasFourchet;
    }

    public void setHasShipping(boolean hasShipping) {
        this.hasShipping = hasShipping;
    }

    public void setCreditCards(String creditCards) {
        this.creditCards = creditCards;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHasSmoking(boolean hasSmoking) {
        this.hasSmoking = hasSmoking;
    }

    public void setHasNonSmoking(boolean hasNonSmoking) {
        this.hasNonSmoking = hasNonSmoking;
    }



    public RestDetails(int position) {
        DatabaseHelper inst = DatabaseHelper.getInstance();
        inst.getRestaurantDetails(position, this);
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public boolean getWiFi() {
        return wiFi;
    }

    public void setWiFi(boolean wiFi) {
        this.wiFi = wiFi;
    }
}
