package com.jettra.plugin.controller.autentification;

import com.jettra.plugin.entity.autentification.Permission;
import com.jettra.plugin.repository.autentification.PermissionRepository;
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
