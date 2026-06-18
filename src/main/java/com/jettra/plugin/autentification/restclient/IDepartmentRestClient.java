package com.jettra.plugin.autentification.restclient;

import com.jettra.plugin.autentification.entity.Department;
import com.jettra.rest.annotations.*;
import com.jettra.rest.client.RestClient;
import java.util.List;

@RestClient(baseUri = "/api/autentification/departments")
public interface IDepartmentRestClient {

    @GET
    List<Department> findAll();

    @POST
    void save(Department model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
