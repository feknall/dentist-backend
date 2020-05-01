package ir.beheshti.dandun;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class DandunApplicationTests {

    @Test
    void contextLoads() {
        Integer a = null;
        String key = a + "asd" + null;
        System.out.println(key);
    }

}
