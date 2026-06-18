package com.jettra.plugin.autentification.restclient;

import com.jettra.plugin.autentification.entity.User;
import com.jettra.rest.annotations.*;
import com.jettra.rest.client.RestClient;
import java.util.List;

@RestClient(baseUri = "/api/autentification/users")
public interface IUserRestClient {

    @GET
    List<User> findAll();

    @POST
    void save(User model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
