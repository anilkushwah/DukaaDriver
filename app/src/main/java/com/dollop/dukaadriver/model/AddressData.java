package com.dollop.dukaadriver.model;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.libraries.places.api.model.Place;

public class AddressData implements Parcelable {

    private String address;
    private String locality;
    private String city;
    private String state;
    private String postalCode;
    private String latitude;
    private String longitude;
    private String country;
    private Place place;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    private String area;
    private Address addressObject;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    private String street;

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    private String office;
    private String block;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Address getAddressObject() {
        return addressObject;
    }

    public void setAddressObject(Address addressObject) {
        this.addressObject = addressObject;
    }

    public Double getLat() {
        if (latitude == null)
            return Double.valueOf("0");
        else if (latitude.isEmpty())
            return Double.valueOf("0");
        else
            return Double.valueOf(latitude);
    }

    public Double getLng() {
        if (longitude == null)
            return Double.valueOf("0");
        else if (longitude.isEmpty())
            return Double.valueOf("0");
        else
            return Double.valueOf(longitude);
    }


    public AddressData(Parcel in) {
        address = in.readString();
        locality = in.readString();
        city = in.readString();
        state = in.readString();
        postalCode = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        country = in.readString();
        addressObject = in.readParcelable(Address.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(locality);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(postalCode);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(country);
        dest.writeParcelable(addressObject, flags);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressData> CREATOR = new Creator<AddressData>() {
        @Override
        public AddressData createFromParcel(Parcel in) {
            return new AddressData(in);
        }

        @Override
        public AddressData[] newArray(int size) {
            return new AddressData[size];
        }
    };
}
