package com.sync.provider;

import com.sync.api.MockExternalApi;
import com.sync.domain.model.ExternalRecord;

import java.util.Optional;

/**
 * Salesforce CRM provider backed by mock API with rate limiting.
 */
public class SalesforceProvider implements CrmProvider {

    private final MockExternalApi api;

    public SalesforceProvider(int requestsPerSecond) {
        this.api = new MockExternalApi("salesforce", requestsPerSecond, 50);
    }

    @Override
    public String getProviderId() {
        return "salesforce";
    }

    @Override
    public ExternalRecord create(ExternalRecord record) {
        return api.create(record);
    }

    @Override
    public Optional<ExternalRecord> read(String externalId) {
        return api.read(externalId);
    }

    @Override
    public ExternalRecord update(ExternalRecord record) {
        return api.update(record);
    }

    @Override
    public boolean delete(String externalId) {
        return api.delete(externalId);
    }
}
