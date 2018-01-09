package transactions.service.integration;

import com.aimacademyla.model.*;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MonthlyFinancesSummaryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import transactions.AbstractTransactionTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChargeLineServiceTest extends AbstractTransactionTest{

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;

    @Autowired
    private ChargeLineService chargeLineService;

    @Autowired
    private CourseService courseService;

    private Member member;
    private Charge charge;
    private ChargeLine chargeLine;
    private Attendance attendance;
    private Course course;
    private MonthlyFinancesSummary monthlyFinancesSummary;
    private LocalDate cycleStartDate;

    @Before
    public void setUp(){
        cycleStartDate = LocalDate.of(2000,1,1);

        member = new Member();
        member.setMemberID(1);

        attendance = new Attendance();
        attendance.setWasPresent(true);
        attendance.setAttendanceID(1);

        course = new Course();
        course.setCourseID(1);
        course.setBillableUnitDuration(BigDecimal.ZERO);
        courseService.add(course);

        monthlyFinancesSummary = new MonthlyFinancesSummary();
        monthlyFinancesSummary.setMonthlyFinancesSummaryID(1);
        monthlyFinancesSummary.setTotalChargeAmount(BigDecimal.ZERO);
        monthlyFinancesSummary.setCycleStartDate(cycleStartDate);
        monthlyFinancesSummary.setNumTotalCharges(0);
        monthlyFinancesSummary.setNumCourses(0);

        charge = new Charge();
        charge.setChargeID(1);
        charge.setMember(member);
        charge.setChargeAmount(BigDecimal.ZERO);
        charge.setBillableUnitsBilled(BigDecimal.ZERO);
        charge.setNumChargeLines(0);
        charge.setCourse(course);

        chargeLine = new ChargeLine();
        chargeLine.setChargeLineID(1);
        chargeLine.setChargeAmount(BigDecimal.valueOf(100));
        chargeLine.setCharge(charge);
        chargeLine.setAttendance(attendance);
    }

    @Test
    @Rollback
    public void testAddChargeLine() throws Exception{
        monthlyFinancesSummaryService.add(monthlyFinancesSummary);
        chargeLineService.addChargeLine(chargeLine);

        Charge retrievedCharge = chargeService.get(charge.getChargeID());
        assertNotNull(retrievedCharge);
    }
}
