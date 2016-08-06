package com.dineoutmobile.dineout.models;


import com.google.android.gms.maps.model.LatLng;

public class AddressSchema {
    public Long addressId;
    public String name;
    public LatLng latLng = new LatLng( 42.6593506, 44.5551702 );
}
