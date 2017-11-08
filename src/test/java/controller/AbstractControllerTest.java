package controller;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
        /*
         * Unit service only work right now if the locations for the resources are set in the following below formats for both the
         * context and the sql initialization scripts
         */
        locations = "file:src/test/java/resources/testContext.xml"
)
public abstract class AbstractControllerTest {
}
