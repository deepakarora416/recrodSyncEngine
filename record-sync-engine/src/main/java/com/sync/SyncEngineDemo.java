package com.sync;

import com.sync.domain.model.*;
import com.sync.engine.SyncEngine;
import com.sync.provider.SalesforceProvider;
import com.sync.transformer.SalesforceTransformer;
import com.sync.validator.ContactValidator;

/**
 * Demo: outbound sync, inbound sync, validation, and rate limiting.
 */
public class SyncEngineDemo {

    public static void main(String[] args) {
        System.out.println("=== Record Synchronization Engine Demo ===\n");

        SyncEngine engine = new SyncEngine(
            new SalesforceProvider(10),
            new SalesforceTransformer(),
            new ContactValidator()
        );

        demonstrateOutboundSync(engine);
        demonstrateInboundSync(engine);
        demonstrateValidation(engine);
        demonstrateRateLimiting(engine);

        System.out.println("=== Demo Complete ===");
    }

    private static void demonstrateOutboundSync(SyncEngine engine) {
        System.out.println("--- Outbound Sync (Internal -> Salesforce) ---");

        InternalRecord contact = new InternalRecord("C-7001", "Contact");
        contact.setField("firstName", "Deepak");
        contact.setField("lastName", "Arora");
        contact.setField("email", "deepak.arora@techcorp.in");
        contact.setField("phone", "9812345678");

        SyncRequest request = SyncRequest.builder()
            .providerId("salesforce")
            .operation(SyncOperation.CREATE)
            .direction(SyncDirection.OUTBOUND)
            .internalRecord(contact)
            .build();

        System.out.println("Sending: " + contact);
        SyncResult result = engine.sync(request);

        System.out.println("Result: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
        if (result.isSuccess()) {
            ExternalRecord ext = result.getResultingExternalRecord();
            System.out.println("Created in Salesforce: " + ext.getExternalId());
        }
        System.out.println();
    }

    private static void demonstrateInboundSync(SyncEngine engine) {
        System.out.println("--- Inbound Sync (Salesforce -> Internal) ---");

        ExternalRecord external = new ExternalRecord("003R00000ghi", "salesforce", "Contact");
        external.setData("FirstName", "Sneha");
        external.setData("LastName", "Reddy");
        external.setData("Email", "sneha.reddy@salesforce.com");

        SyncRequest request = SyncRequest.builder()
            .providerId("salesforce")
            .operation(SyncOperation.CREATE)
            .direction(SyncDirection.INBOUND)
            .externalRecord(external)
            .build();

        System.out.println("Receiving: " + external.getExternalId());
        SyncResult result = engine.sync(request);

        System.out.println("Result: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
        if (result.isSuccess()) {
            InternalRecord internal = result.getResultingInternalRecord();
            System.out.println("Created internally: " + internal.getId());
        }
        System.out.println();
    }

    private static void demonstrateValidation(SyncEngine engine) {
        System.out.println("--- Validation Demo ---");

        InternalRecord invalidContact = new InternalRecord("C-7002", "Contact");
        invalidContact.setField("firstName", "Ravi");

        SyncRequest request = SyncRequest.builder()
            .providerId("salesforce")
            .operation(SyncOperation.CREATE)
            .direction(SyncDirection.OUTBOUND)
            .internalRecord(invalidContact)
            .build();

        System.out.println("Sending invalid contact (missing lastName, email)...");
        SyncResult result = engine.sync(request);

        System.out.println("Result: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
        System.out.println("Message: " + result.getMessage());
        System.out.println();
    }

    private static void demonstrateRateLimiting(SyncEngine engine) {
        System.out.println("--- Rate Limiting Demo ---");
        System.out.println("Sending 15 rapid requests (limit is 10/sec)...");

        String[] firstNames = {"Arun", "Bhavna", "Chetan", "Divya", "Esha", 
                               "Farhan", "Gauri", "Harsh", "Isha", "Jai",
                               "Kavya", "Lalit", "Meera", "Nikhil", "Pooja"};

        int successCount = 0;
        int rateLimitedCount = 0;

        for (int i = 0; i < 15; i++) {
            InternalRecord contact = new InternalRecord("C-" + (8000 + i), "Contact");
            contact.setField("firstName", firstNames[i]);
            contact.setField("lastName", "TestUser");
            contact.setField("email", firstNames[i].toLowerCase() + "@test.in");

            SyncRequest request = SyncRequest.builder()
                .providerId("salesforce")
                .operation(SyncOperation.CREATE)
                .direction(SyncDirection.OUTBOUND)
                .internalRecord(contact)
                .build();

            SyncResult result = engine.sync(request);

            if (result.isSuccess()) {
                successCount++;
            } else if (result.getMessage().contains("Rate limit")) {
                rateLimitedCount++;
            }
        }

        System.out.println("Successful: " + successCount);
        System.out.println("Rate limited: " + rateLimitedCount);
        System.out.println();
    }
}
