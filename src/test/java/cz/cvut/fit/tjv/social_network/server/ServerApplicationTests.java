package cz.cvut.fit.tjv.social_network.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerApplicationTests {
    static {
        System.setProperty("SPRING_DATASOURCE_URL", "jdbc:postgresql://localhost:5432/database");
        System.setProperty("SPRING_DATASOURCE_USERNAME", "user");
        System.setProperty("SPRING_DATASOURCE_PASSWORD", "pass");
    }

    @Test
    void contextLoads() {
    }
}
