package com.jettra.plugin.example.library.controller;

import com.jettra.plugin.example.library.entity.Reader;
import com.jettra.plugin.example.library.repository.ReaderRepository;
import com.jettra.rest.annotations.*;
import com.jettra.rest.core.Response;

import java.util.List;

@Path("/library/readers")
public class ReaderController {

    @GET
    @Produces("application/json")
    public List<Reader> findAll() {
        return ReaderRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Reader reader) {
        ReaderRepository.save(reader);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        ReaderRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
