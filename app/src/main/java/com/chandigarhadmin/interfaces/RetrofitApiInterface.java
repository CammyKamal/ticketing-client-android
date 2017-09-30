package com.chandigarhadmin.interfaces;

import com.chandigarhadmin.models.BranchesModel;
import com.chandigarhadmin.models.CreateTicketModel;
import com.chandigarhadmin.models.CreateTicketResponse;
import com.chandigarhadmin.models.CreateUserResponse;
import com.chandigarhadmin.models.GetTicketResponse;
import com.chandigarhadmin.models.LoginUserModel;
import com.chandigarhadmin.models.SingleTicketResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bimalchawla on 29/9/17.
 */

public interface RetrofitApiInterface {

    @POST("users")
    Call<CreateUserResponse> saveLoginDetail(@Body LoginUserModel body);

    @POST("tickets")
    Call<CreateTicketResponse> createTicket(@Body CreateTicketModel body);

    @GET("branches")
    Call<List<BranchesModel>> getBranches();

    @GET("users/{email}/")
    Call<CreateUserResponse> getUserByEmail(@Path("email") String email);

    @GET("tickets/{id}")
    Call<SingleTicketResponse> viewTicket(@Path("id") String id);

    @GET("tickets/search")
    Call<List<GetTicketResponse>> getAllTickets(@Query("reporter") String id);

}
