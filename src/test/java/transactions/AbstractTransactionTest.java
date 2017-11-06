package transactions;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by davidkim on 6/28/17.
 */

@ContextConfiguration(
        /*
         * Unit tests only work right now if the locations for the resources are set in the following below formats for both the
         * context and the sql initialization scripts
         */
        locations = "file:src/test/java/resources/testContext.xml"
)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "file:src/test/java/resources/initializationScript.sql")
})
public abstract class AbstractTransactionTest {

}
