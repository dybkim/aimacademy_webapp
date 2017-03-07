package controller;

import com.aimacademyla.controller.CourseController;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;
import com.aimacademyla.service.AttendanceService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.StudentRegistrationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by davidkim on 2/14/17.
 */

public class CourseControllerTest {

    @Mock
    private CourseService courseServiceMock;

    @Mock
    private MemberService memberServiceMock;

    @Mock
    private StudentRegistrationService studentRegistrationServiceMock;

    @Mock
    private AttendanceService attendanceServiceMock;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    public void testViewEnrollment() throws Exception{
        List<Member> memberList = new ArrayList<>();
        int courseID = 1;
        Course course = new Course();
        course.setCourseID(courseID);
        memberList.add(new Member());
        memberList.add(new Member());

        when(courseServiceMock.getCourseByID(courseID)).thenReturn(course);
        when(memberServiceMock.getMembersByCourse(course)).thenReturn(memberList);

        mockMvc.perform(get("/admin/courseList/viewEnrollment/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/course/viewEnrollment"))
                .andExpect(forwardedUrl(("/course/viewEnrollment")))
                .andExpect(model().attribute("studentList", hasSize(2)));

        verify(courseServiceMock, times(1));
    }

    @Test
    public void testEditCourse() throws Exception{
        int courseID = 1;
        Course course = new Course();
        course.setCourseID(courseID);

        mockMvc.perform(post("/admin/courseList/editCourse/1"))
                .andExpect(redirectedUrl("/course/courseList"));

        verify(courseServiceMock, times(1));
    }

    @Test
    public void testAddStudentToCourse() throws Exception{

    }

    @Test
    public void testFetchAttendanceForCourseSession() throws Exception{
        int courseSessionID = 1;
        int courseID = 2;

        CourseSession courseSession = new CourseSession();

        courseSession.setCourseID(courseID);
        courseSession.setCourseSessionID(1);

        //TODO: Add mockMvc.perform();
    }

}
