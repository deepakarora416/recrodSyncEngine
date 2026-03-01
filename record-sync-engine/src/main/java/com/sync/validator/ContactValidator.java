package com.sync.validator;

import com.sync.domain.model.ExternalRecord;
import com.sync.domain.model.InternalRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates Contact records: firstName, lastName, email required.
 */
public class ContactValidator implements RecordValidator {

    private static final int MAX_NAME_LENGTH = 50;

    @Override
    public List<String> validate(InternalRecord record) {
        List<String> errors = new ArrayList<>();

        if (!"Contact".equals(record.getRecordType())) {
            return errors;
        }

        String firstName = (String) record.getField("firstName");
        String lastName = (String) record.getField("lastName");
        String email = (String) record.getField("email");

        if (isEmpty(firstName)) {
            errors.add("firstName is required");
        } else if (firstName.length() > MAX_NAME_LENGTH) {
            errors.add("firstName exceeds " + MAX_NAME_LENGTH + " characters");
        }

        if (isEmpty(lastName)) {
            errors.add("lastName is required");
        } else if (lastName.length() > MAX_NAME_LENGTH) {
            errors.add("lastName exceeds " + MAX_NAME_LENGTH + " characters");
        }

        if (isEmpty(email)) {
            errors.add("email is required");
        }

        return errors;
    }

    @Override
    public List<String> validate(ExternalRecord record) {
        List<String> errors = new ArrayList<>();

        if (!"Contact".equals(record.getEntityType())) {
            return errors;
        }

        if (isEmpty((String) record.getData("FirstName"))) {
            errors.add("FirstName is required");
        }
        if (isEmpty((String) record.getData("LastName"))) {
            errors.add("LastName is required");
        }
        if (isEmpty((String) record.getData("Email"))) {
            errors.add("Email is required");
        }

        return errors;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
