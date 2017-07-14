package controller;

import com.aimacademyla.controller.StudentListController;
import com.aimacademyla.model.Member;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.MemberCourseRegistrationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by davidkim on 2/14/17.
 */

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

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentListControllerMock).build();
    }

    @Test
    public void editStudentTest() throws Exception{
        Member member = new Member();
        member.setMemberID(1);

        mockMvc.perform(post("/admin/studentList/editStudent").param("memberID", Integer.toString(member.getMemberID())))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/admin/studentList"));

        verify(memberServiceMock, times(1));
    }
}
