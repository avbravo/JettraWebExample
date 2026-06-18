package com.jettra.plugin.autentification.controller;

import com.jettra.plugin.autentification.entity.Role;
import com.jettra.plugin.autentification.repository.RoleRepository;
import com.jettra.rest.annotations.*;
import com.jettra.rest.core.Response;
import java.util.List;

@Path("/autentification/roles")
public class RoleController {

    @GET
    @Produces("application/json")
    public List<Role> findAll() {
        return RoleRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Role role) {
        RoleRepository.save(role);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        RoleRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
