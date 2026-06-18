/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jettra.plugin.example.library.restclient;

import com.jettra.plugin.example.library.entity.Book;
import com.jettra.rest.annotations.DELETE;
import com.jettra.rest.annotations.GET;
import com.jettra.rest.annotations.POST;
import com.jettra.rest.annotations.Path;
import com.jettra.rest.annotations.PathParam;
import com.jettra.rest.client.RestClient;
import java.util.List;

/**
 *
 * @author avbravo
 */
@RestClient(baseUri = "/api/library/books")
public interface IBookRestClient {

    @GET
    List<Book> findAll();

    @POST
    void save(Book model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
