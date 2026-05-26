package com.jettra.example.restclient.library;

import com.jettra.example.entity.library.Author;
import com.jettra.example.restclient.library.interfaces.IAuthorRestClient;
import com.jettra.rest.client.RestClientBuilder;

import java.util.List;

public class AuthorRestClient {

 

    private static final IAuthorRestClient proxy = RestClientBuilder.create(IAuthorRestClient.class);

    public static List<Author> findAll() {
        return proxy.findAll();
    }

    public static void save(Author author) {
        proxy.save(author);
    }

    public static void delete(String id) {
        proxy.delete(id);
    }
}
