package com.jettra.plugin.autentification.controller;

import com.jettra.plugin.autentification.entity.Permission;
import com.jettra.plugin.autentification.repository.PermissionRepository;
import com.jettra.rest.annotations.*;
import com.jettra.rest.core.Response;
import java.util.List;

@Path("/autentification/permissions")
public class PermissionController {

    @GET
    @Produces("application/json")
    public List<Permission> findAll() {
        return PermissionRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Permission permission) {
        PermissionRepository.save(permission);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        PermissionRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
