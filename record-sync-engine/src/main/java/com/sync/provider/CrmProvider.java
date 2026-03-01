package com.sync.provider;

import com.sync.domain.model.ExternalRecord;

import java.util.Optional;

/**
 * Contract for external CRM systems.
 */
public interface CrmProvider {

    String getProviderId();

    ExternalRecord create(ExternalRecord record);

    Optional<ExternalRecord> read(String externalId);

    ExternalRecord update(ExternalRecord record);

    boolean delete(String externalId);
}
