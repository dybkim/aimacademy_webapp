package transactions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import transactions.tests.integration.ChargeLineServiceTest;
import transactions.tests.integration.ChargeServiceTest;
import transactions.tests.integration.MemberServiceTest;

/**
 * Created by davidkim on 6/28/17.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({MemberServiceTest.class, ChargeServiceTest.class, ChargeLineServiceTest.class})
public class TransactionSuite {

}
