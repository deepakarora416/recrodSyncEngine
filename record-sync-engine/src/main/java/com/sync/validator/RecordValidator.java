package com.sync.validator;

import com.sync.domain.model.InternalRecord;
import com.sync.domain.model.ExternalRecord;

import java.util.List;

/**
 * Validates records before sync. Returns empty list if valid.
 */
public interface RecordValidator {

    List<String> validate(InternalRecord record);

    List<String> validate(ExternalRecord record);
}
