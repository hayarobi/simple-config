package com.github.hayarobi.simple_config.load;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Date;

public class DateUtilsTest extends TestCase {

    @Test
    public void testTestFormat() {
        Date now = new Date();
        System.out.println("Formatted Date: " + DateUtils.format(now));
    }
}