package com.jettra.plugin.restclient.autentification;

import com.jettra.plugin.entity.autentification.Credential;
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
