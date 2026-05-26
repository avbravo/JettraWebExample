package com.jettra.example.restclient.library;

import com.jettra.example.entity.library.Book;
import com.jettra.example.restclient.library.interfaces.IBookRestClient;
import com.jettra.rest.client.RestClientBuilder;

import java.util.List;

public class BookRestClient {

   

    private static final IBookRestClient proxy = RestClientBuilder.create(IBookRestClient.class);

    public static List<Book> findAll() {
        return proxy.findAll();
    }

    public static void save(Book book) {
        proxy.save(book);
    }

    public static void delete(String id) {
        proxy.delete(id);
    }
}
