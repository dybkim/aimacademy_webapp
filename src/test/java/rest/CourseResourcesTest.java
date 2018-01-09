package rest;

import com.aimacademyla.controller.course.rest.CourseResources;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;
import com.aimacademyla.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class CourseResourcesTest {

    @Mock
    private MemberService memberServiceMock;

    @Mock
    private CourseService courseService;

    @Mock
    private MemberCourseRegistrationService memberCourseRegistrationService;

    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private CourseResources courseResources;

    private MockMvc mockMvc;
    private Course courseWithMember;
    private Course courseWithoutMember;
    private Member member;
    private List<Member> emptyMemberList;
    private List<Member> memberList;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseResources).build();

        courseWithMember = new Course();
        courseWithMember.setCourseID(1);

        courseWithoutMember = new Course();
        courseWithoutMember.setCourseID(2);

        member = new Member();
        member.setMemberID(1);

        memberList = new ArrayList<>();
        memberList.add(member);
        emptyMemberList = new ArrayList<>();
    }

    @Test
    public void testValidateAddCourseSessionWithMembers() throws  Exception{
//        when(courseService.get(courseWithMember.getCourseID())).thenReturn(courseWithMember);
//        when(memberServiceMock.getActiveMembersByCourse(courseWithMember)).thenReturn(memberList);

        RequestBuilder requestBuilderWithMember = get("/admin/courseList/rest/" + courseWithMember.getCourseID() + "/validateAddCourseSession");

        mockMvc.perform(requestBuilderWithMember)
                .andExpect(content().json("{'errorCode':200}"));

        verify(courseService, times(1)).get(courseWithMember.getCourseID());
//        verify(memberServiceMock, times(1)).getActiveMembersByCourse(courseWithMember);
    }

    @Test
    public void testValidateAddCourseSessionWithoutMembers() throws  Exception{
        when(courseService.get(courseWithoutMember.getCourseID())).thenReturn(courseWithoutMember);
//        when(memberServiceMock.getActiveMembersByCourse(courseWithoutMember)).thenReturn(emptyMemberList);

        RequestBuilder requestBuilderWithoutMember = get("/admin/courseList/rest/" + courseWithoutMember.getCourseID() + "/validateAddCourseSession");

        mockMvc.perform(requestBuilderWithoutMember)
                .andExpect(content().json("{'errorCode':500}"));

        verify(courseService, times(1)).get(courseWithoutMember.getCourseID());
//        verify(memberServiceMock, times(1)).getActiveMembersByCourse(courseWithoutMember);
    }

    @Test
    public void testDeleteCourse() throws Exception{
        when(courseService.get(courseWithMember.getCourseID())).thenReturn(courseWithMember);
        RequestBuilder requestBuilder = delete("/admin/courseList/rest/deleteCourse/" + courseWithMember.getCourseID());

        mockMvc.perform(requestBuilder)
                .andExpect(content().json("{'errorCode':200}"));

        verify(courseService, times(1)).get(courseWithMember.getCourseID());
        verify(courseService, times(1)).remove(courseWithMember);
    }

    @Test
    public void testGetCourseSession() throws Exception{
        CourseSession courseSession = new CourseSession();
        courseSession.setCourse(courseWithMember);
        courseSession.setCourseSessionID(1);

        Attendance attendance = new Attendance();
        attendance.setAttendanceID(1);
        attendance.setMember(member);
        attendance.setCourseSession(courseSession);
        attendance.setWasPresent(true);

        List<Attendance> attendanceList = new ArrayList<>();
        attendanceList.add(attendance);

        when(memberServiceMock.get(member.getMemberID())).thenReturn(member);

        RequestBuilder requestBuilder = get("/admin/courseList/rest/getCourseSession/" + courseSession.getCourseSessionID());

        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$[0].memberID", is(1)));
//
//        verify(attendanceService, times(1)).getAttendanceForCourseSession(courseSession.getCourseSessionID());
//        verify(memberServiceMock, times(1)).get(attendance.getMemberID());
    }

}
