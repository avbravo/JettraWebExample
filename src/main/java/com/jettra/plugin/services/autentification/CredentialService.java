package com.jettra.plugin.services.autentification;

import com.jettra.plugin.entity.autentification.Credential;
import com.jettra.plugin.model.autentification.CredentialRecordModelConverter;
import com.jettra.plugin.restclient.autentification.CredentialRestClient;
import com.jettra.plugin.restclient.autentification.ICredentialRestClient;
import java.util.List;

public class CredentialService {

    private static final CredentialRecordModelConverter converter = new CredentialRecordModelConverter();
    private static final ICredentialRestClient client = new CredentialRestClient();

    public static List<Credential> findAll() {
        List<Credential> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Credential record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
