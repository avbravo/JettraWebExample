package com.jettra.plugin.services.autentification;

import com.jettra.plugin.entity.autentification.Permission;
import com.jettra.plugin.model.autentification.PermissionRecordModelConverter;
import com.jettra.plugin.restclient.autentification.PermissionRestClient;
import com.jettra.plugin.restclient.autentification.IPermissionRestClient;
import java.util.List;

public class PermissionService {

    private static final PermissionRecordModelConverter converter = new PermissionRecordModelConverter();
    private static final IPermissionRestClient client = new PermissionRestClient();

    public static List<Permission> findAll() {
        List<Permission> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Permission record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
