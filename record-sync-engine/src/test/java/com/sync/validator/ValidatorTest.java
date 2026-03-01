package com.sync.validator;

import com.sync.domain.model.InternalRecord;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ValidatorTest {

    private ContactValidator validator;

    @Before
    public void setUp() {
        validator = new ContactValidator();
    }

    @Test
    public void testValidContact() {
        InternalRecord contact = new InternalRecord("C-4001", "Contact");
        contact.setField("firstName", "Neha");
        contact.setField("lastName", "Gupta");
        contact.setField("email", "neha.gupta@hcl.com");

        List<String> errors = validator.validate(contact);

        assertTrue(errors.isEmpty());
    }

    @Test
    public void testMissingRequiredFields() {
        InternalRecord contact = new InternalRecord("C-4002", "Contact");
        contact.setField("firstName", "Amit");

        List<String> errors = validator.validate(contact);

        assertEquals(2, errors.size());
        assertTrue(errors.stream().anyMatch(e -> e.contains("lastName")));
        assertTrue(errors.stream().anyMatch(e -> e.contains("email")));
    }

    @Test
    public void testNameTooLong() {
        InternalRecord contact = new InternalRecord("C-4003", "Contact");
        contact.setField("firstName", "A".repeat(100));
        contact.setField("lastName", "Kumar");
        contact.setField("email", "test@example.com");

        List<String> errors = validator.validate(contact);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("firstName"));
        assertTrue(errors.get(0).contains("50"));
    }

    @Test
    public void testNonContactTypeSkipsValidation() {
        InternalRecord account = new InternalRecord("A-5001", "Account");

        List<String> errors = validator.validate(account);

        assertTrue(errors.isEmpty());
    }
}
