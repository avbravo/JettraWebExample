package com.jettra.plugin.autentification.controller;

import com.jettra.plugin.autentification.entity.User;
import com.jettra.plugin.autentification.repository.UserRepository;
import com.jettra.rest.annotations.*;
import com.jettra.rest.core.Response;
import java.util.List;

@Path("/autentification/users")
public class UserController {

    @GET
    @Produces("application/json")
    public List<User> findAll() {
        return UserRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(User user) {
        UserRepository.save(user);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        UserRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
