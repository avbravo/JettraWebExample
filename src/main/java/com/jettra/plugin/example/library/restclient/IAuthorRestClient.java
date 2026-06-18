/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jettra.plugin.example.library.restclient;

import com.jettra.plugin.example.library.entity.Author;
import com.jettra.rest.annotations.DELETE;
import com.jettra.rest.annotations.GET;
import com.jettra.rest.annotations.POST;
import com.jettra.rest.annotations.Path;
import com.jettra.rest.annotations.PathParam;
import com.jettra.rest.annotations.HeaderParam;
import com.jettra.rest.client.RestClient;
import java.util.List;

/**
 *
 * @author avbravo
 */
@RestClient(baseUri = "/api/library/authors")
public interface IAuthorRestClient {

    @GET
    List<Author> findAll(@HeaderParam("Authorization") String token);

    @POST
    void save(@HeaderParam("Authorization") String token, Author model);

    @DELETE
    @Path("/{id}")
    void delete(@HeaderParam("Authorization") String token, @PathParam("id") String id);
}
