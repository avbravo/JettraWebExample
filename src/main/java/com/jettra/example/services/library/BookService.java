package com.jettra.example.services.library;

import com.jettra.example.entity.library.Book;
import com.jettra.example.model.library.BookModel;
import com.jettra.example.model.library.BookRecordModelConverter;
import com.jettra.example.restclient.library.BookRestClient;
import com.jettra.example.restclient.library.IBookRestClient;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    
    private static final BookRecordModelConverter converter = new BookRecordModelConverter();
    private static final IBookRestClient client = new BookRestClient();

    public static List<Book> findAll() {
        List<Book> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Book record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
