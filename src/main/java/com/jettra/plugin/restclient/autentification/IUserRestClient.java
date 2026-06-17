package com.jettra.plugin.restclient.autentification;

import com.jettra.plugin.entity.autentification.User;
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
