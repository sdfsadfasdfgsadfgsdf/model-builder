package de.microtema.model.builder.adapter.string;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mario on 25.03.2016.
 */
public class LinkRandomAdapterTest {

    LinkPropertyRandomAdapter sut = new LinkPropertyRandomAdapter();

    @Test
    public void randomValue() {

        String randomLink = sut.randomValue();

        assertNotNull(randomLink);
        assertTrue(randomLink.contains("://"));
        assertTrue(randomLink.contains("."));
    }

    @Test
    public void getPropertyName() {
        Assert.assertEquals("link", sut.getPropertyName());
    }
}
