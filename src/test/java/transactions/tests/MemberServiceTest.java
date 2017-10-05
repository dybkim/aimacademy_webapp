package transactions.tests;

import com.aimacademyla.dao.*;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.service.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import transactions.AbstractTransactionTest;

import static org.junit.Assert.*;

/**
 * Created by davidkim on 6/21/17.
 * NOTE: Since the unit test uses an h2
 */

@RunWith(SpringJUnit4ClassRunner.class)
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
@Transactional
public class MemberServiceTest extends AbstractTransactionTest {

    @Autowired
    private MemberService memberService;

    private static Member member;

    @BeforeClass
    public static void setUpBeforeClass(){
        member = new Member();
        member.setMemberID(1);
        member.setMemberFirstName("Foo");
        member.setMemberLastName("Young");
    }

    @Before
    public void setUp(){
        if(memberService.get(member.getMemberID()) != null)
            memberService.remove(member);
    }

    @Test
    public void addMemberTest() throws Exception{
        memberService.add(member);
        Member memberRetrieved = memberService.get(member.getMemberID());

        assertEquals("Member1 should persist within the database", memberRetrieved.getMemberID(), member.getMemberID());
    }

    @Test
    public void removeMemberTest() throws Exception{
        memberService.add(member);

        Member retrievedMember = memberService.get(member.getMemberID());

        assertEquals("Member1 should persist within the database", retrievedMember.getMemberID(), member.getMemberID());

        memberService.remove(member);

        member = memberService.get(member.getMemberID());
        assertNull("Member1 should no longer persist once removed", member);
    }


    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }
}
