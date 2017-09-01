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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations="classpath:resources/testContext.xml"
)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:initializationScript.sql")
})
@Transactional
public abstract class AbstractTransactionTest {

}
