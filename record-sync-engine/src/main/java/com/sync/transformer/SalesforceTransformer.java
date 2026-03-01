package com.sync.transformer;

import com.sync.domain.model.ExternalRecord;
import com.sync.domain.model.InternalRecord;

/**
 * Maps fields between internal camelCase and Salesforce PascalCase.
 */
public class SalesforceTransformer implements RecordTransformer {

    private static final String PROVIDER_ID = "salesforce";

    @Override
    public ExternalRecord toExternal(InternalRecord internal) {
        ExternalRecord external = new ExternalRecord(
            PROVIDER_ID + "_" + internal.getId(),
            PROVIDER_ID,
            internal.getRecordType()
        );
        external.setInternalReferenceId(internal.getId());

        copyIfPresent(internal, external, "firstName", "FirstName");
        copyIfPresent(internal, external, "lastName", "LastName");
        copyIfPresent(internal, external, "email", "Email");
        copyIfPresent(internal, external, "phone", "Phone");
        copyIfPresent(internal, external, "company", "Company");

        return external;
    }

    @Override
    public InternalRecord toInternal(ExternalRecord external) {
        String internalId = external.getInternalReferenceId() != null
            ? external.getInternalReferenceId()
            : "int_" + external.getExternalId();

        InternalRecord internal = new InternalRecord(internalId, external.getEntityType());

        copyIfPresent(external, internal, "FirstName", "firstName");
        copyIfPresent(external, internal, "LastName", "lastName");
        copyIfPresent(external, internal, "Email", "email");
        copyIfPresent(external, internal, "Phone", "phone");
        copyIfPresent(external, internal, "Company", "company");

        return internal;
    }

    private void copyIfPresent(InternalRecord from, ExternalRecord to, String fromKey, String toKey) {
        if (from.hasField(fromKey)) {
            to.setData(toKey, from.getField(fromKey));
        }
    }

    private void copyIfPresent(ExternalRecord from, InternalRecord to, String fromKey, String toKey) {
        if (from.getData(fromKey) != null) {
            to.setField(toKey, from.getData(fromKey));
        }
    }
}
