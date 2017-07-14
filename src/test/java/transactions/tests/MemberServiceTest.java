package transactions.tests;

import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.service.MemberService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import transactions.AbstractTransactionTest;

import static org.junit.Assert.*;

/**
 * Created by davidkim on 6/21/17.
 */

public class MemberServiceTest extends AbstractTransactionTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private MemberCourseRegistrationDAO memberCourseRegistrationDAO;

    private static Member member1;

    private static MemberCourseRegistrationPK memberCourseRegistrationPK1;

    @BeforeClass
    public static void setUp(){
        member1 = new Member();
        member1.setMemberID(1);
        member1.setMemberFirstName("Foo");
        member1.setMemberLastName("Young");
        memberCourseRegistrationPK1 = new MemberCourseRegistrationPK(member1.getMemberID(), Course.OPEN_STUDY_ID);
    }


    @Test
    public void addMemberTest() throws Exception{
        memberService.add(member1);

        MemberCourseRegistration memberCourseRegistration1 = memberCourseRegistrationDAO.get(memberCourseRegistrationPK1);

        Member memberRetrieved1 = memberService.get(member1.getMemberID());

        assertEquals("Member1 should persist within the database", memberRetrieved1.getMemberID(), member1.getMemberID());

        assertNotNull("Adding a member1 should also add an open study course registration", memberCourseRegistration1);
    }

    @Test
    public void deactivateMemberTest() throws Exception{
        memberService.add(member1);
        Member member = memberService.get(member1.getMemberID());
        MemberCourseRegistrationPK memberCourseRegistrationPK = new MemberCourseRegistrationPK(member1.getMemberID(), Course.OPEN_STUDY_ID);
        MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistrationPK);

        assertEquals("Member1 should persist within the database", member.getMemberID(), member1.getMemberID());
        assertNotNull("Member1 should also have a course registration for open study", memberCourseRegistration);

        member1.setMemberIsActive(false);
        memberService.update(member1);

        memberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistrationPK);
        member = memberService.get(member1.getMemberID());

        assertEquals("Member1 should still persist within the database", member.getMemberID(), member1.getMemberID());
        assertNull("memberCourseRegistration should no longer persist as member1 is no longer an active member", memberCourseRegistration);
    }

    @Test
    public void reactivateMemberTest() throws Exception{
        memberService.add(member1);

        Member member = memberService.get(member1.getMemberID());
        MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistrationPK1);

        assertEquals("Member1 should persist within the database", member.getMemberID(), member1.getMemberID());
        assertNotNull("Member1 should also have a course registration for open study", memberCourseRegistration);

        member1.setMemberIsActive(true);
        memberService.update(member1);

        member = memberService.get(member1.getMemberID());
        memberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistrationPK1);

        assertEquals("Member1 should still persist within the database,", member.getMemberID(), member1.getMemberID());
        assertNotNull("memberCourseRegistration for open study for member1 should persist", memberCourseRegistration);
    }

    @Test
    public void removeMemberTest() throws Exception{
        memberService.add(member1);

        Member member = memberService.get(member1.getMemberID());
        MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistrationPK1);

        assertEquals("Member1 should persist within the database", member.getMemberID(), member1.getMemberID());
        assertNotNull("Member1 should also have a course registration for open study", memberCourseRegistration);

        memberService.remove(member1);

        member = memberService.get(member1.getMemberID());
        memberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistrationPK1);

        assertNull("Member1 should no longer persist once removed", member);
        assertNull("Member1's course registration for open study should no longer persist as member2 has been removed", memberCourseRegistration);
    }

}
