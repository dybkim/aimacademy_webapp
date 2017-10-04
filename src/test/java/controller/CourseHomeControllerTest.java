package controller;

import com.aimacademyla.controller.course.CourseHomeController;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.impl.CourseRegistrationWrapperBuilder;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapperObject;
import com.aimacademyla.service.AttendanceService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberCourseRegistrationService;
import com.aimacademyla.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by davidkim on 2/14/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class CourseHomeControllerTest {

    @Mock
    private CourseService courseServiceMock;

    @Mock
    private MemberService memberServiceMock;

    @Mock
    private MemberCourseRegistrationService memberCourseRegistrationServiceMock;

    @Mock
    private AttendanceService attendanceServiceMock;

    @InjectMocks
    private CourseHomeController courseHomeController;

    private MockMvc mockMvc;
    private Course course;
    private Member member;
    private int courseID;
    private CourseRegistrationWrapper courseRegistrationWrapper;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseHomeController).build();
        courseID = 1;
        course = new Course();
        course.setCourseID(courseID);

        member = new Member();
        member.setMemberID(1);

        List<CourseRegistrationWrapperObject> courseRegistrationWrapperObjectList = new ArrayList<>();
        CourseRegistrationWrapperObject courseRegistrationWrapperObject = new CourseRegistrationWrapperObject();
        courseRegistrationWrapperObject.setMember(member);
        courseRegistrationWrapperObjectList.add(courseRegistrationWrapperObject);

        courseRegistrationWrapper = new CourseRegistrationWrapper();
        courseRegistrationWrapper.setCourse(course);
        courseRegistrationWrapper.setCourseRegistrationWrapperObjectList(courseRegistrationWrapperObjectList);
    }

    @Test
    public void testDeleteCourse() throws Exception{
        when(courseServiceMock.get(courseID)).thenReturn(course);
        mockMvc.perform(delete("/admin/courseList/deleteCourse/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/courseList"))
                    .andExpect(view().name("redirect:/admin/courseList"));
    }

    @Test
    public void testEditCourse() throws Exception{
        RequestBuilder requestBuilder = post("/admin/courseList/editCourse/1").flashAttr("courseRegistrationWrapper", courseRegistrationWrapper);

        mockMvc.perform(requestBuilder)
                .andExpect(redirectedUrl("/admin/courseList/courseInfo/1"))
                .andExpect(model().attribute("courseRegistrationWrapper", hasProperty("course", hasProperty("courseID", Matchers.equalTo(1)))))
                .andExpect(view().name("redirect:/admin/courseList/courseInfo/1"));
    }

    @Test
    public void testAddCourse() throws Exception{
        RequestBuilder requestBuilder = post("/admin/courseList/addCourse").flashAttr("courseRegistrationWrapper", courseRegistrationWrapper);

        mockMvc.perform(requestBuilder)
                .andExpect(model().attribute("courseRegistrationWrapper", hasProperty("course", hasProperty("courseID", Matchers.equalTo(1)))))
                .andExpect(redirectedUrl("/admin/courseList"))
                .andExpect(view().name("redirect:/admin/courseList"));
    }

}
