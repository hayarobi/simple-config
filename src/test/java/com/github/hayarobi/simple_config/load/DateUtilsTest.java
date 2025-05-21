package com.github.hayarobi.simple_config.load;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class DateUtilsTest {

    @Test
    public void testTestFormat() {
        Date now = new Date();
        System.out.println("Formatted Date: " + DateUtils.format(now));
    }
}