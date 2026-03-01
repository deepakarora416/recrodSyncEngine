package com.sync.transformer;

import com.sync.domain.model.ExternalRecord;
import com.sync.domain.model.InternalRecord;

/**
 * Converts records between internal and external formats.
 */
public interface RecordTransformer {

    ExternalRecord toExternal(InternalRecord internal);

    InternalRecord toInternal(ExternalRecord external);
}
