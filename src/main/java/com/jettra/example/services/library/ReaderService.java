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

    public static List<Reader> findAll() {
        List<Reader> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Reader record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
