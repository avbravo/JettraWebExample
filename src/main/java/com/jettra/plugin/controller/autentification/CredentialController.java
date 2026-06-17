package com.jettra.plugin.controller.autentification;

import com.jettra.plugin.entity.autentification.Credential;
import com.jettra.plugin.repository.autentification.CredentialRepository;
import com.jettra.rest.annotations.*;
import com.jettra.rest.core.Response;
import java.util.List;

@Path("/autentification/credentials")
public class CredentialController {

    @GET
    @Produces("application/json")
    public List<Credential> findAll() {
        return CredentialRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Credential credential) {
        CredentialRepository.save(credential);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        CredentialRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
