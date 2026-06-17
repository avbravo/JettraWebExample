package com.jettra.plugin.restclient.autentification;

import com.jettra.plugin.entity.autentification.Department;
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
