package com.jettra.plugin.services.autentification;

import com.jettra.plugin.entity.autentification.User;
import com.jettra.plugin.model.autentification.UserRecordModelConverter;
import com.jettra.plugin.restclient.autentification.UserRestClient;
import com.jettra.plugin.restclient.autentification.IUserRestClient;
import java.util.List;

public class UserService {

    private static final UserRecordModelConverter converter = new UserRecordModelConverter();
    private static final IUserRestClient client = new UserRestClient();

    public static List<User> findAll() {
        List<User> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(User record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
