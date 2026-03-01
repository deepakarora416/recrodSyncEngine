package com.sync.engine;

import com.sync.domain.model.*;
import com.sync.provider.SalesforceProvider;
import com.sync.transformer.SalesforceTransformer;
import com.sync.validator.ContactValidator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyncEngineTest {

    private SyncEngine engine;

    @Before
    public void setUp() {
        engine = new SyncEngine(
            new SalesforceProvider(100),
            new SalesforceTransformer(),
            new ContactValidator()
        );
    }

    @Test
    public void testOutboundSync() {
        InternalRecord contact = new InternalRecord("C-3001", "Contact");
        contact.setField("firstName", "Rahul");
        contact.setField("lastName", "Verma");
        contact.setField("email", "rahul.verma@tcs.com");

        SyncRequest request = SyncRequest.builder()
            .providerId("salesforce")
            .operation(SyncOperation.CREATE)
            .direction(SyncDirection.OUTBOUND)
            .internalRecord(contact)
            .build();

        SyncResult result = engine.sync(request);

        assertTrue(result.isSuccess());
        assertNotNull(result.getResultingExternalRecord());
        assertEquals("Rahul", result.getResultingExternalRecord().getData("FirstName"));
    }

    @Test
    public void testInboundSync() {
        ExternalRecord external = new ExternalRecord("003R00000def", "salesforce", "Contact");
        external.setData("FirstName", "Ananya");
        external.setData("LastName", "Iyer");
        external.setData("Email", "ananya.iyer@zoho.com");

        SyncRequest request = SyncRequest.builder()
            .providerId("salesforce")
            .operation(SyncOperation.CREATE)
            .direction(SyncDirection.INBOUND)
            .externalRecord(external)
            .build();

        SyncResult result = engine.sync(request);

        assertTrue(result.isSuccess());
        assertEquals("Ananya", result.getResultingInternalRecord().getField("firstName"));
        assertEquals("Iyer", result.getResultingInternalRecord().getField("lastName"));
    }

    @Test
    public void testValidationFailure() {
        InternalRecord contact = new InternalRecord("C-3002", "Contact");
        contact.setField("firstName", "Kiran");

        SyncRequest request = SyncRequest.builder()
            .providerId("salesforce")
            .operation(SyncOperation.CREATE)
            .direction(SyncDirection.OUTBOUND)
            .internalRecord(contact)
            .build();

        SyncResult result = engine.sync(request);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Validation failed"));
    }
}
