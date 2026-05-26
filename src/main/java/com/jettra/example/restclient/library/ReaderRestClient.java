package com.jettra.example.restclient.library;

import com.jettra.example.entity.library.Reader;
import com.jettra.example.restclient.library.interfaces.IReaderRestClient;
import com.jettra.rest.client.RestClientBuilder;

import java.util.List;

public class ReaderRestClient {

 

    private static final IReaderRestClient proxy = RestClientBuilder.create(IReaderRestClient.class);

    public static List<Reader> findAll() {
        return proxy.findAll();
    }

    public static void save(Reader reader) {
        proxy.save(reader);
    }

    public static void delete(String id) {
        proxy.delete(id);
    }
}
