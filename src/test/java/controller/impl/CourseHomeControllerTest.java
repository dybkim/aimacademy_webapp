package controller.impl;

import com.aimacademyla.controller.course.CourseHomeController;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.enums.BillableUnitType;
import com.aimacademyla.model.dto.CourseRegistrationDTO;
import com.aimacademyla.model.dto.CourseRegistrationDTOListItem;
import com.aimacademyla.service.AttendanceService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberCourseRegistrationService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.factory.ServiceFactory;
import controller.AbstractControllerTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by davidkim on 2/14/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class CourseHomeControllerTest extends AbstractControllerTest{

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
    private Course activeCourse;
    private Course inactiveCourse;
    private Course otherCourse;
    private Course inactiveOtherCourse;
    private Course openStudyCourse;
    private Member member;
    private int courseID;
    private CourseRegistrationDTO courseRegistrationDTO;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseHomeController).build();
        courseID = 10;
        activeCourse = new Course();
        activeCourse.setCourseID(courseID);
        activeCourse.setBillableUnitType(BillableUnitType.PER_SESSION.toString());
        activeCourse.setIsActive(true);

        openStudyCourse = new Course();
        openStudyCourse.setCourseID(Course.OPEN_STUDY_ID);
        openStudyCourse.setBillableUnitType(BillableUnitType.PER_SESSION.toString());
        openStudyCourse.setIsActive(true);

        inactiveCourse = new Course();
        inactiveCourse.setCourseID(11);
        inactiveCourse.setBillableUnitType(BillableUnitType.PER_SESSION.toString());
        inactiveCourse.setIsActive(false);

        otherCourse = new Course();
        otherCourse.setCourseID(Course.OTHER_ID);
        otherCourse.setBillableUnitType(BillableUnitType.PER_SESSION.toString());
        otherCourse.setIsActive(true);

        inactiveOtherCourse = new Course();
        inactiveOtherCourse.setCourseID(Course.OTHER_ID);
        inactiveOtherCourse.setBillableUnitType(BillableUnitType.PER_SESSION.toString());
        inactiveOtherCourse.setIsActive(false);

        member = new Member();
        member.setMemberID(1);

        List<CourseRegistrationDTOListItem> courseRegistrationDTOListItems = new ArrayList<>();
        CourseRegistrationDTOListItem courseRegistrationDTOListItem = new CourseRegistrationDTOListItem();
        courseRegistrationDTOListItem.setMember(member);
        courseRegistrationDTOListItems.add(courseRegistrationDTOListItem);

        courseRegistrationDTO = new CourseRegistrationDTO();
        courseRegistrationDTO.setCourse(activeCourse);
        courseRegistrationDTO.setCourseRegistrationDTOListItems(courseRegistrationDTOListItems);
    }

    @Test
    public void testGetCourseList() throws Exception{
        List<Course> courseList = new ArrayList<>();
        courseList.add(activeCourse);
        courseList.add(openStudyCourse);
        courseList.add(otherCourse);
        courseList.add(inactiveCourse);
        courseList.add(inactiveOtherCourse);

        when(courseServiceMock.getList()).thenReturn(courseList);

        RequestBuilder requestBuilder = get("/admin/courseList");

        mockMvc.perform(requestBuilder)
                .andExpect(model().attribute("inactiveCourseList", hasItem(inactiveCourse)))
                .andExpect(model().attribute("courseList", hasItem(activeCourse)))
                .andExpect(view().name("/course/courseList"));

        verify(courseServiceMock, times(1)).getList();
    }

    @Test
    public void testGetEmptyCourseList() throws Exception{
        List<Course> courseList = new ArrayList<>();

        when(courseServiceMock.getList()).thenReturn(courseList);

        RequestBuilder requestBuilder = get("/admin/courseList");

        mockMvc.perform(requestBuilder)
                .andExpect(model().attribute("inactiveCourseList", hasSize(0)))
                .andExpect(model().attribute("courseList", hasSize(0)))
                .andExpect(view().name("/course/courseList"));

        verify(courseServiceMock, times(1)).getList();
    }

    @Test
    public void testEditCourse() throws Exception{
        RequestBuilder requestBuilder = post("/admin/courseList/editCourse/1").flashAttr("courseRegistrationDTO", courseRegistrationDTO);

        mockMvc.perform(requestBuilder)
                .andExpect(redirectedUrl("/admin/courseList/courseInfo/1"))
                .andExpect(model().attribute("courseRegistrationDTO", hasProperty("course", hasProperty("courseID", Matchers.equalTo(activeCourse.getCourseID())))))
                .andExpect(view().name("redirect:/admin/courseList/courseInfo/1"));

        verify(courseServiceMock, times(1)).update(courseRegistrationDTO.getCourse());
    }

    @Test
    public void testAddCourse() throws Exception{
        RequestBuilder requestBuilder = post("/admin/courseList/addCourse").flashAttr("courseRegistrationDTO", courseRegistrationDTO);

        mockMvc.perform(requestBuilder)
                .andExpect(model().attribute("courseRegistrationDTO", hasProperty("course", hasProperty("courseID", Matchers.equalTo(activeCourse.getCourseID())))))
                .andExpect(redirectedUrl("/admin/courseList"))
                .andExpect(view().name("redirect:/admin/courseList"));

        verify(courseServiceMock, times(1)).add(courseRegistrationDTO.getCourse());
    }

}
