package com.amitness.photon;

import com.amitness.photon.utils.Conversion;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ConversionUnitTest {

    @Test
    public void testAlphabetToAscii() throws Exception {
        byte[] expected = {65, 90, 97, 122};
        byte[] got = Conversion.toAscii("AZaz");
        assertArrayEquals(expected, got);
    }

    @Test
    public void testNumbersToAscii() throws Exception {
        byte[] expected = {48, 57};
        byte[] got = Conversion.toAscii("09");
        assertArrayEquals(expected, got);
    }

    @Test
    public void testSpaceToAscii() throws Exception {
        byte[] expected = {32};
        byte[] got = Conversion.toAscii(" ");
        assertArrayEquals(expected, got);
    }
}