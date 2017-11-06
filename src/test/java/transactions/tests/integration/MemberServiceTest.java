package transactions.tests.integration;

import com.aimacademyla.model.Member;
import com.aimacademyla.service.MemberService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import transactions.AbstractTransactionTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by davidkim on 6/21/17.
 * NOTE: Since the unit test uses an h2
 */

@RunWith(SpringJUnit4ClassRunner.class)
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
        MockitoAnnotations.initMocks(this);
    }

    @Rollback
    @Test
    public void addMemberTest() throws Exception{
        memberService.add(member);
        Member memberRetrieved = memberService.get(member.getMemberID());

        assertEquals("Member1 should persist within the database", member.getMemberID(), memberRetrieved.getMemberID());
    }

    @Rollback
    @Test
    public void removeMemberTest() throws Exception{
        memberService.add(member);

        Member retrievedMember = memberService.get(member.getMemberID());

        assertEquals("Member1 should persist within the database", member.getMemberID(), retrievedMember.getMemberID());

        memberService.remove(member);

        member = memberService.get(member.getMemberID());
        assertNull("Member1 should no longer persist once removed", member);
    }


    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }
}
