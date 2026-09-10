package com.aiproductivity.java_dev_tool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.sql.DataSource;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.datasource.url=jdbc:mysql://localhost:3306/javadevtool_db"
})
class JavaDevToolApplicationTests {

    @MockBean
    private DataSource dataSource;

    @Test
    void contextLoads() {
    }
}
