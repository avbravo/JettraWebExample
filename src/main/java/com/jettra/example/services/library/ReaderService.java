package com.jettra.example.services.library;

import com.jettra.example.entity.library.Reader;
import com.jettra.example.model.library.ReaderModel;
import com.jettra.example.model.library.ReaderRecordModelConverter;
import com.jettra.example.restclient.library.ReaderRestClient;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderService {
    
    private static final ReaderRecordModelConverter converter = new ReaderRecordModelConverter();

    public static List<ReaderModel> findAll() {
        List<Reader> records = ReaderRestClient.findAll();
        if (records == null) return List.of();
        return records.stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }

    public static void save(ReaderModel model) {
        Reader record = converter.toRecord(model);
        ReaderRestClient.save(record);
    }

    public static void delete(String id) {
        ReaderRestClient.delete(id);
    }
}
