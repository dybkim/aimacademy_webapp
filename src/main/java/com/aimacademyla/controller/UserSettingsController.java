package com.aimacademyla.controller;

import com.aimacademyla.model.User;
import com.aimacademyla.model.dto.NewPasswordFormDTO;
import com.aimacademyla.service.AuthorityService;
import com.aimacademyla.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin/settings")
public class UserSettingsController {

    private UserService userService;

    @Autowired
    public UserSettingsController(AuthorityService authorityService){
        this.userService = userService;
    }

    @RequestMapping()
    public String getSettings(Model model){
        model.addAttribute("newPasswordFormWrapper", new NewPasswordFormDTO());
        return "settings";
    }

    @RequestMapping(method= RequestMethod.POST)
    public String setSettings(@ModelAttribute("newPasswordFormWrapper") NewPasswordFormDTO newPasswordFormDTO, final RedirectAttributes redirectAttributes){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.get(userDetails.getUsername());
        String currentPassword = user.getPassword();

        if(!newPasswordFormDTO.getCurrentPassword().equals(currentPassword)){
            redirectAttributes.addFlashAttribute("currentPasswordErrorMessage", "Current Password is incorrect");

            if(!newPasswordFormDTO.getNewPassword().equals(newPasswordFormDTO.getConfirmPassword()))
                redirectAttributes.addFlashAttribute("confirmPasswordErrorMessage", "New Passwords did not match!");

            return "redirect:/admin/settings";
        }

        if(!newPasswordFormDTO.getNewPassword().equals(newPasswordFormDTO.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("confirmPasswordErrorMessage", "New Passwords did not match!");
            return "redirect:/admin/settings";
        }

        user.setPassword(newPasswordFormDTO.getNewPassword());
        user.setIsEnabled(true);
        userService.update(user);
        return "redirect:/admin/home";
    }
}
