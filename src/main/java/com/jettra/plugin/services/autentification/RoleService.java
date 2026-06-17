package com.jettra.plugin.services.autentification;

import com.jettra.plugin.entity.autentification.Role;
import com.jettra.plugin.model.autentification.RoleRecordModelConverter;
import com.jettra.plugin.restclient.autentification.RoleRestClient;
import com.jettra.plugin.restclient.autentification.IRoleRestClient;
import java.util.List;

public class RoleService {

    private static final RoleRecordModelConverter converter = new RoleRecordModelConverter();
    private static final IRoleRestClient client = new RoleRestClient();

    public static List<Role> findAll() {
        List<Role> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Role record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
