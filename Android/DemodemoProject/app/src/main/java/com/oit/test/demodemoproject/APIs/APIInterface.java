package com.oit.test.demodemoproject.APIs;

import com.oit.test.demodemoproject.Models.Student;
import com.oit.test.demodemoproject.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by anupamchugh on 09/01/17.
 */

public interface APIInterface {

    @POST("/addUser")
    Call<User> createUser(@Body User user);


    @GET("/users")
    Call<List<Student>> getMyJSON();


}
