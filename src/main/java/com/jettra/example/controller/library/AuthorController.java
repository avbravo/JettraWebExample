package com.jettra.example.controller.library;

import com.jettra.example.entity.library.Author;
import com.jettra.example.model.library.AuthorModel;
import com.jettra.example.repository.library.AuthorRepository;
import com.jettra.rest.annotations.*;
import com.jettra.rest.core.Response;

import java.util.List;

@Path("/api/library/authors")
public class AuthorController {

    @GET
    @Produces("application/json")
    public List<Author> findAll() {
        return AuthorRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Author author) {
        AuthorRepository.save(author);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        AuthorRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
