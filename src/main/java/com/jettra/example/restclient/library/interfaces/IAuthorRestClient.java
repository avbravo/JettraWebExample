/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jettra.example.restclient.library.interfaces;

import com.jettra.example.entity.library.Author;
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
@RestClient(baseUri = "/api/library/authors")
public interface IAuthorRestClient {

    @GET
    List<Author> findAll();

    @POST
    void save(Author model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
