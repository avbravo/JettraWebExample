package com.jettra.example.services.library;

import com.jettra.example.entity.library.Publisher;
import com.jettra.example.model.library.PublisherModel;
import com.jettra.example.model.library.PublisherRecordModelConverter;
import com.jettra.example.restclient.library.PublisherRestClient;
import com.jettra.example.restclient.library.IPublisherRestClient;
import java.util.List;
import java.util.stream.Collectors;

public class PublisherService {
    
    private static final PublisherRecordModelConverter converter = new PublisherRecordModelConverter();
    private static final IPublisherRestClient client = new PublisherRestClient();

    public static List<Publisher> findAll() {
        List<Publisher> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Publisher record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
