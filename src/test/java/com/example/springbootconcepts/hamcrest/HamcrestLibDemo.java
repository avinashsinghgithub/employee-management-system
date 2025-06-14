package com.example.springbootconcepts.hamcrest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class HamcrestLibDemo {
     @Mock
     List<Integer> list;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
     void test(){
        List<Integer> arr = Arrays.asList(11,12,13,14);

        when(list.get(anyInt())).thenReturn(123);


        assertThat(arr, hasSize(4));
        assertEquals(list.get(1),123);
    }
}
