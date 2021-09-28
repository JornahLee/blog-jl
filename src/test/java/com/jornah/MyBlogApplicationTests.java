package com.jornah;

import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
@Disabled
public class MyBlogApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Test
    public void test(){
        Objects.equals(1,2);
    }

}
