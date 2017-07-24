package com.amitness.photon;

import com.amitness.photon.utils.BaudotCode;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class BaudotCodeUnitTest {

    @Test
    public void testDecode() throws Exception {
        BaudotCode bc = new BaudotCode();
        String expected = "A";
        String got = bc.decode("00011");
        assertEquals(expected, got);
    }
}
