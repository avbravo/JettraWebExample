package com.jettra.example.controller.library;

import com.jettra.example.entity.library.Book;
import com.jettra.example.repository.library.BookRepository;
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
