package com.jettra.example.services.library;

import com.jettra.example.entity.library.Author;
import com.jettra.example.model.library.AuthorModel;
import com.jettra.example.model.library.AuthorRecordModelConverter;
import com.jettra.example.restclient.library.AuthorRestClient;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorService {
    
    private static final AuthorRecordModelConverter converter = new AuthorRecordModelConverter();

    public static List<AuthorModel> findAll() {
        List<Author> records = AuthorRestClient.findAll();
        if (records == null) return List.of();
        return records.stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }

    public static void save(AuthorModel model) {
        Author record = converter.toRecord(model);
        AuthorRestClient.save(record);
    }

    public static void delete(String id) {
        AuthorRestClient.delete(id);
    }
}
