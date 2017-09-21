package com.oit.test.demodemoproject.Models;

import com.google.gson.annotations.SerializedName;


public class User {


    @SerializedName("firstName")
    public String firstName;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("gender")
    public String gender;
    @SerializedName("dob")
    public String dob;
    @SerializedName("dept")
    public String dept;
    @SerializedName("photo")
    public String photo;

    public User(String firstName, String lastName, String gender, String dob, String dept, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender=gender;
        this.dob=dob;
        this.dept=dept;
        this.photo=photo;
    }


}
