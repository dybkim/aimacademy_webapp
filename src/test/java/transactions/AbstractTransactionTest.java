package transactions;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

/**
 * Created by davidkim on 6/28/17.
 */

@ContextConfiguration(
        /*
         * Unit service only work right now if the locations for the resources are set in the following below formats for both the
         * context and the sql initialization scripts
         */
        locations = "file:**/resources/testContext.xml"
)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "file:src/test/java/resources/testInitializationScript.sql")
})
public abstract class AbstractTransactionTest {

}
