package controller.impl;

import com.aimacademyla.controller.course.coursesession.CourseSessionController;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.factory.ServiceFactory;
import controller.AbstractControllerTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
public class CourseSessionControllerTest extends AbstractControllerTest{

    @Mock
    private CourseService courseService;

    @Mock
    private MemberService memberService;

    @Mock
    private ServiceFactory serviceFactory;

    @InjectMocks
    private CourseSessionController courseSessionController;

    private MockMvc mockMvc;

    private CourseSession courseSession;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

    }

}
