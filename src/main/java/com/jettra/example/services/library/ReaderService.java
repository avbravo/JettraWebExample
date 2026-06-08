package com.jettra.example.services.library;

import com.jettra.example.entity.library.Reader;
import com.jettra.example.model.library.ReaderModel;
import com.jettra.example.model.library.ReaderRecordModelConverter;
import com.jettra.example.restclient.library.ReaderRestClient;
import com.jettra.example.restclient.library.IReaderRestClient;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderService {
    
    private static final ReaderRecordModelConverter converter = new ReaderRecordModelConverter();
    private static final IReaderRestClient client = new ReaderRestClient();

    public static List<ReaderModel> findAll() {
        List<Reader> records = client.findAll();
        if (records == null) return List.of();
        return records.stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }

    public static void save(ReaderModel model) {
        Reader record = converter.toRecord(model);
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
