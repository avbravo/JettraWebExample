package com.jettra.plugin.services.autentification;

import com.jettra.plugin.entity.autentification.Department;
import com.jettra.plugin.model.autentification.DepartmentRecordModelConverter;
import com.jettra.plugin.restclient.autentification.DepartmentRestClient;
import com.jettra.plugin.restclient.autentification.IDepartmentRestClient;
import java.util.List;

public class DepartmentService {

    private static final DepartmentRecordModelConverter converter = new DepartmentRecordModelConverter();
    private static final IDepartmentRestClient client = new DepartmentRestClient();

    public static List<Department> findAll() {
        List<Department> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Department record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
