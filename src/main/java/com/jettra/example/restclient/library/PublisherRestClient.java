package com.jettra.example.restclient.library;

import com.jettra.example.entity.library.Publisher;
import com.jettra.example.model.library.PublisherModel;
import com.jettra.example.restclient.library.interfaces.IPublisherRestClient;
import com.jettra.rest.client.RestClientBuilder;

import java.util.List;

public class PublisherRestClient {

   

    private static final IPublisherRestClient proxy = RestClientBuilder.create(IPublisherRestClient.class);

    public static List<Publisher> findAll() {
        return proxy.findAll();
    }

    public static void save(Publisher publisher) {
        proxy.save(publisher);
    }

    public static void delete(String id) {
        proxy.delete(id);
    }
}
