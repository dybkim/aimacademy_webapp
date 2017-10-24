package controller;

import com.aimacademyla.controller.student.StudentListController;
import com.aimacademyla.model.Member;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.MemberCourseRegistrationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by davidkim on 2/14/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class StudentListControllerTest {

    @Mock
    private CourseService courseServiceMock;

    @Mock
    private MemberService memberServiceMock;

    @Mock
    private MemberCourseRegistrationService memberCourseRegistrationServiceMock;

    @InjectMocks
    private StudentListController studentListControllerMock;

    private MockMvc mockMvc;

    private Member member;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentListControllerMock).build();
        member = new Member();
        member.setMemberID(1);
    }

    @Test
    public void testEditStudent() throws Exception{
        mockMvc.perform(put("/admin/student/studentList/editStudent").param("memberID", Integer.toString(member.getMemberID())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/student/studentList"));
    }

    @Test
    public void testGetStudentInfo() throws Exception{

    }
}
