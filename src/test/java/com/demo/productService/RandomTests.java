package com.demo.productService;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class RandomTests {

    //Test case - method which gets executed automatically at
    // the time of building or deployment etc.

    //They don't accept any input and don't return anything.
    @Test
    public void sampleTest(){

        //Arrange(input params)
        int a = 10;
        int b = 7;

        //Act(Call the function that we want to test)
        int result = a + b;

        //Assert(Validate the actual output against to the expected one)
        assert result == 17;
        String str = null;

        assertEquals(17, result);
        assertNotEquals(30, result);
        assertNull(str);
        assertTimeout(Duration.ofMillis(12000),() -> Thread.sleep(1000));

    }
}

