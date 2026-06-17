package com.jettra.plugin.restclient.autentification;

import com.jettra.plugin.entity.autentification.Permission;
import com.jettra.rest.annotations.*;
import com.jettra.rest.client.RestClient;
import java.util.List;

@RestClient(baseUri = "/api/autentification/permissions")
public interface IPermissionRestClient {

    @GET
    List<Permission> findAll();

    @POST
    void save(Permission model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
