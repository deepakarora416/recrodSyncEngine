package com.sync.engine;

import com.sync.domain.exception.RateLimitException;
import com.sync.domain.exception.SyncException;
import com.sync.domain.exception.ValidationException;
import com.sync.domain.model.*;
import com.sync.provider.CrmProvider;
import com.sync.transformer.RecordTransformer;
import com.sync.validator.RecordValidator;

import java.util.List;

/**
 * Orchestrates sync: validate -> transform -> call provider.
 */
public class SyncEngine {

    private final CrmProvider provider;
    private final RecordTransformer transformer;
    private final RecordValidator validator;

    public SyncEngine(CrmProvider provider, RecordTransformer transformer, RecordValidator validator) {
        this.provider = provider;
        this.transformer = transformer;
        this.validator = validator;
    }

    public SyncResult sync(SyncRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            if (request.getDirection() == SyncDirection.OUTBOUND) {
                return processOutbound(request, startTime);
            } else {
                return processInbound(request, startTime);
            }
        } catch (SyncException e) {
            return SyncResult.failure(request.getRequestId(), e.getMessage())
                .processingTimeMs(System.currentTimeMillis() - startTime)
                .build();
        }
    }

    private SyncResult processOutbound(SyncRequest request, long startTime) {
        InternalRecord internal = request.getInternalRecord();

        List<String> errors = validator.validate(internal);
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed: " + String.join(", ", errors));
        }

        ExternalRecord external = transformer.toExternal(internal);
        ExternalRecord result = executeOperation(request.getOperation(), external);

        return SyncResult.success(request.getRequestId())
            .message("Outbound sync completed")
            .resultingInternalRecord(internal)
            .resultingExternalRecord(result)
            .processingTimeMs(System.currentTimeMillis() - startTime)
            .build();
    }

    private SyncResult processInbound(SyncRequest request, long startTime) {
        ExternalRecord external = request.getExternalRecord();

        List<String> errors = validator.validate(external);
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed: " + String.join(", ", errors));
        }

        InternalRecord internal = transformer.toInternal(external);

        return SyncResult.success(request.getRequestId())
            .message("Inbound sync completed")
            .resultingInternalRecord(internal)
            .resultingExternalRecord(external)
            .processingTimeMs(System.currentTimeMillis() - startTime)
            .build();
    }

    private ExternalRecord executeOperation(SyncOperation operation, ExternalRecord record) {
        switch (operation) {
            case CREATE:
                return provider.create(record);
            case UPDATE:
                return provider.update(record);
            case DELETE:
                provider.delete(record.getExternalId());
                return record;
            case READ:
                return provider.read(record.getExternalId())
                    .orElseThrow(() -> new SyncException("Record not found: " + record.getExternalId()));
            default:
                throw new SyncException("Unsupported operation: " + operation);
        }
    }
}
