package transactions.tests.integration;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.initializer.impl.MonthlyFinancesSummaryDefaultValueInitializer;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.MonthlyFinancesSummaryService;
import com.aimacademyla.service.impl.ChargeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import transactions.AbstractTransactionTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChargeServiceTest extends AbstractTransactionTest {

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;

    private Member member;
    private Charge charge;
    private MonthlyFinancesSummary monthlyFinancesSummary;
    private LocalDate localDate;

    @Before
    public void setUp() {
        localDate = LocalDate.of(2000, 1, 1);

        member = new Member();
        member.setMemberID(1);

        charge = new Charge();
        charge.setChargeID(1);
        charge.setChargeAmount(BigDecimal.valueOf(100));
        charge.setMemberID(member.getMemberID());
        charge.setCycleStartDate(localDate);
    }

    @Test
    @Rollback
    public void testAddCharge() throws Exception {
        chargeService.add(charge);
        Charge retrievedCharge = chargeService.get(charge.getChargeID());

        assertEquals("Saved charge should persist within database", charge.getChargeID(), retrievedCharge.getChargeID());
    }
}
