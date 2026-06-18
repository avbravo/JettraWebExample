package com.jettra.plugin.example.library.controller;

import com.jettra.plugin.example.library.entity.Book;
import com.jettra.plugin.example.library.repository.BookRepository;
import com.jettra.rest.annotations.*;
import com.jettra.rest.core.Response;

import java.util.List;

@Path("/library/books")
public class BookController {

    @GET
    @Produces("application/json")
    public List<Book> findAll() {
        return BookRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Book book) {
        BookRepository.save(book);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        BookRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
