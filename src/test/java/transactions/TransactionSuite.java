package transactions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ContextConfiguration;
import transactions.tests.MemberServiceTest;

/**
 * Created by davidkim on 6/28/17.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({MemberServiceTest.class, })
public class TransactionSuite {

}
