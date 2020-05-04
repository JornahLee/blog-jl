package com.wip;

import com.wip.utils.Commons;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBlogApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Test
    public void test(){
        Objects.equals(1,2);
    }

}
