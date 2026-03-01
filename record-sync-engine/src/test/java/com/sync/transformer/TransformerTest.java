package com.sync.transformer;

import com.sync.domain.model.ExternalRecord;
import com.sync.domain.model.InternalRecord;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransformerTest {

    private SalesforceTransformer transformer;

    @Before
    public void setUp() {
        transformer = new SalesforceTransformer();
    }

    @Test
    public void testInternalToExternal() {
        InternalRecord internal = new InternalRecord("C-1001", "Contact");
        internal.setField("firstName", "Arjun");
        internal.setField("lastName", "Mehta");
        internal.setField("email", "arjun.mehta@techindia.com");
        internal.setField("phone", "9876543210");

        ExternalRecord external = transformer.toExternal(internal);

        assertEquals("salesforce", external.getProviderId());
        assertEquals("Contact", external.getEntityType());
        assertEquals("Arjun", external.getData("FirstName"));
        assertEquals("Mehta", external.getData("LastName"));
        assertEquals("arjun.mehta@techindia.com", external.getData("Email"));
        assertEquals("9876543210", external.getData("Phone"));
        assertEquals("C-1001", external.getInternalReferenceId());
    }

    @Test
    public void testExternalToInternal() {
        ExternalRecord external = new ExternalRecord("003R00000xyz", "salesforce", "Contact");
        external.setData("FirstName", "Priya");
        external.setData("LastName", "Sharma");
        external.setData("Email", "priya.sharma@infosys.com");

        InternalRecord internal = transformer.toInternal(external);

        assertEquals("Contact", internal.getRecordType());
        assertEquals("Priya", internal.getField("firstName"));
        assertEquals("Sharma", internal.getField("lastName"));
        assertEquals("priya.sharma@infosys.com", internal.getField("email"));
    }

    @Test
    public void testInternalReferenceIdPreserved() {
        ExternalRecord external = new ExternalRecord("003R00000abc", "salesforce", "Contact");
        external.setInternalReferenceId("C-2050");
        external.setData("FirstName", "Vikram");
        external.setData("LastName", "Patel");
        external.setData("Email", "vikram.patel@wipro.com");

        InternalRecord internal = transformer.toInternal(external);

        assertEquals("C-2050", internal.getId());
    }
}
