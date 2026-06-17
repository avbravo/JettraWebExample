package com.jettra.plugin.restclient.autentification;

import com.jettra.plugin.entity.autentification.Role;
import com.jettra.rest.annotations.*;
import com.jettra.rest.client.RestClient;
import java.util.List;

@RestClient(baseUri = "/api/autentification/roles")
public interface IRoleRestClient {

    @GET
    List<Role> findAll();

    @POST
    void save(Role model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
