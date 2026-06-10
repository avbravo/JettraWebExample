package com.jettra.example.services.library;

import com.jettra.example.entity.library.Author;
import com.jettra.example.model.library.AuthorModel;
import com.jettra.example.model.library.AuthorRecordModelConverter;
import com.jettra.example.restclient.library.AuthorRestClient;
import com.jettra.example.restclient.library.IAuthorRestClient;
import io.jettra.wui.components.Console;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorService {
    
    private static final AuthorRecordModelConverter converter = new AuthorRecordModelConverter();
    private static final IAuthorRestClient client = new AuthorRestClient();

    public static List<Author> findAll() {
        Console.addMessage("miConsola", "AuthorService.findAll()..", "info");
        System.out.println("...> paso f-1 AuthorService.findAll()");
        List<Author> records = client.findAll();
         System.out.println("...> paso f-2 ");
        if (records == null) return List.of();
        return records;
    }

    public static void save(Author record) {
        Console.addMessage("miConsola", "AuthorService.save()..", "info");
        System.out.println("...> paso 1 AuthorService.save()");
        System.out.println("...> paso 2");
        client.save(record);
        System.out.println("...> paso 3");
    }

    public static void delete(String id) {
        Console.addMessage("miConsola", "AuthorService.delete(" + id + ")..", "info");
        client.delete(id);
    }
}
