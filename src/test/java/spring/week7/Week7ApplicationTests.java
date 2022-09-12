package spring.week7;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class Week7ApplicationTests {
        static {
            System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
        }

    @Test
    void contextLoads() {
    }

}
