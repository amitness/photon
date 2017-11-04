package com.amitness.photon;

import com.amitness.photon.utils.Code;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CodeUnitTest {

    @Test
    public void testDecode() throws Exception {
        Code code = new Code();
        String expected = "A";
        String got = code.decode("001");
        assertEquals(expected, got);
    }
}
