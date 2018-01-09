package controller.impl;

import com.aimacademyla.controller.UserSettingsController;
import com.aimacademyla.model.User;
import com.aimacademyla.model.dto.NewPasswordFormDTO;
import com.aimacademyla.service.UserService;
import controller.AbstractControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserSettingsControllerTest extends AbstractControllerTest{

    @Mock
    private UserService userService;

    @InjectMocks
    private UserSettingsController userSettingsController;

    private MockMvc mockMvc;

    private User user;

    private NewPasswordFormDTO newPasswordFormDTOCorrect;
    private NewPasswordFormDTO newPasswordFormDTOIncorrect;
    private String currentPassword = "currentPassword";
    private String currentPasswordIncorrect = "incorrectCurrentPassword";
    private String newPassword = "newPassword";
    private String newPasswordIncorrect = "incorrectNewPassword";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userSettingsController).build();

        user = new User();
        user.setUsername("user");
        user.setPassword(currentPassword);
        user.setIsEnabled(true);

        GrantedAuthority grantedAuthority = () -> "read";
        ArrayList<GrantedAuthority> grantedAuthorityArrayList = new ArrayList<>();
        grantedAuthorityArrayList.add(grantedAuthority);

        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorityArrayList);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        newPasswordFormDTOCorrect = new NewPasswordFormDTO();
        newPasswordFormDTOCorrect.setNewPassword(newPassword);
        newPasswordFormDTOCorrect.setConfirmPassword(newPassword);
        newPasswordFormDTOCorrect.setCurrentPassword(currentPassword);

        newPasswordFormDTOIncorrect = new NewPasswordFormDTO();
    }

    @Test
    public void testSubmitCorrectPasswordChange() throws Exception{
        when(userService.get(user.getUsername())).thenReturn(user);

        RequestBuilder requestBuilder = post("/admin/settings/").flashAttr("newPasswordFormWrapper", newPasswordFormDTOCorrect);
        mockMvc.perform(requestBuilder)
                .andExpect(redirectedUrl("/admin/home"))
                .andExpect(view().name("redirect:/admin/home"));

        verify(userService, times(1)).get(user.getUsername());
        verify(userService, times(1)).update(user);

        assertTrue(user.getPassword().equals(newPassword));
    }

    @Test
    public void testSubmitIncorrectConfirmationPasswordChange() throws Exception{
        newPasswordFormDTOIncorrect.setNewPassword(newPassword);
        newPasswordFormDTOIncorrect.setConfirmPassword(newPasswordIncorrect);
        newPasswordFormDTOIncorrect.setCurrentPassword(currentPassword);

        when(userService.get(user.getUsername())).thenReturn(user);

        RequestBuilder requestBuilder = post("/admin/settings/").flashAttr("newPasswordFormWrapper", newPasswordFormDTOIncorrect);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.flash().attributeExists("confirmPasswordErrorMessage"))
                .andExpect(redirectedUrl("/admin/settings"))
                .andExpect(view().name("redirect:/admin/settings"));

        verify(userService, times(1)).get(user.getUsername());
    }

    @Test
    public void testSubmitIncorrectCurrentPasswordChange() throws Exception{
        newPasswordFormDTOIncorrect.setNewPassword(newPassword);
        newPasswordFormDTOIncorrect.setConfirmPassword(newPassword);
        newPasswordFormDTOIncorrect.setCurrentPassword(currentPasswordIncorrect);

        when(userService.get(user.getUsername())).thenReturn(user);

        RequestBuilder requestBuilder = post("/admin/settings/").flashAttr("newPasswordFormWrapper", newPasswordFormDTOIncorrect);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.flash().attributeExists("currentPasswordErrorMessage"))
                .andExpect(redirectedUrl("/admin/settings"))
                .andExpect(view().name("redirect:/admin/settings"));

        verify(userService, times(1)).get(user.getUsername());
    }
}
