package com.jettra.example.services.library;

import com.jettra.example.entity.library.Book;
import com.jettra.example.model.library.BookModel;
import com.jettra.example.model.library.BookRecordModelConverter;
import com.jettra.example.restclient.library.BookRestClient;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    
    private static final BookRecordModelConverter converter = new BookRecordModelConverter();

    public static List<BookModel> findAll() {
        List<Book> records = BookRestClient.findAll();
        if (records == null) return List.of();
        return records.stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }

    public static void save(BookModel model) {
        Book record = converter.toRecord(model);
        BookRestClient.save(record);
    }

    public static void delete(String id) {
        BookRestClient.delete(id);
    }
}
