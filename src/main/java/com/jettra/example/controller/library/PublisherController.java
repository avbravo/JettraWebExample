package com.jettra.example.controller.library;

import com.jettra.example.entity.library.Publisher;
import com.jettra.example.model.library.PublisherModel;
import com.jettra.example.repository.library.PublisherRepository;
import com.jettra.rest.annotations.*;
import com.jettra.rest.core.Response;

import java.util.List;

@Path("/library/publishers")
public class PublisherController {

    @GET
    @Produces("application/json")
    public List<Publisher> findAll() {
        return PublisherRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Publisher publisher) {
        PublisherRepository.save(publisher);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        PublisherRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
