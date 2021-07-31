package xyz.easyboot.web.system.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class SysUserResourceTest {
    
    @Inject
    Logger logger;
    
    @Test
    void list() {
        given()
                .param("deptId", "1413756452069314560")
                .param("pageSize", "30")
                .when().get("/sys-user")
                .then()
                .statusCode(200)
                .body(anything())
                .log().body();
    }
}
