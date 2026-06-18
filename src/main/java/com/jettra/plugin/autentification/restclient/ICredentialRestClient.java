package com.jettra.plugin.autentification.restclient;

import com.jettra.plugin.autentification.entity.Credential;
import com.jettra.rest.annotations.*;
import com.jettra.rest.client.RestClient;
import java.util.List;

@RestClient(baseUri = "/api/autentification/credentials")
public interface ICredentialRestClient {

    @GET
    List<Credential> findAll();

    @POST
    void save(Credential model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
