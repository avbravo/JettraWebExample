package com.jettra.example.services.library;

import com.jettra.example.entity.library.Publisher;
import com.jettra.example.model.library.PublisherModel;
import com.jettra.example.model.library.PublisherRecordModelConverter;
import com.jettra.example.restclient.library.PublisherRestClient;
import java.util.List;
import java.util.stream.Collectors;

public class PublisherService {
    
    private static final PublisherRecordModelConverter converter = new PublisherRecordModelConverter();

    public static List<PublisherModel> findAll() {
        List<Publisher> records = PublisherRestClient.findAll();
        if (records == null) return List.of();
        return records.stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }

    public static void save(PublisherModel model) {
        Publisher record = converter.toRecord(model);
        PublisherRestClient.save(record);
    }

    public static void delete(String id) {
        PublisherRestClient.delete(id);
    }
}
