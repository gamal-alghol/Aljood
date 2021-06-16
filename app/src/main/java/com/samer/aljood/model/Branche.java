package com.samer.aljood.model;

import androidx.annotation.Keep;

@Keep
public class Branche {
    String name , location ,phone;

    public Branche(String name, String location, String phone) {
        this.name = name;
        this.location = location;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
