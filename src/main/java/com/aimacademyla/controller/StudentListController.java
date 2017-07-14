package com.aimacademyla.controller;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.wrapper.MemberListWrapper;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by davidkim on 1/24/17.
 */

@Controller
@RequestMapping("/admin/studentList")
public class StudentListController {

    private MemberService memberService;

    @Autowired
    public StudentListController(MemberService memberService){
        this.memberService = memberService;
    }

    @RequestMapping
    public String getStudentList(Model model){
        List<Member> memberList = memberService.getMemberList();
        MemberListWrapper memberListWrapper = new MemberListWrapper(memberList);
        model.addAttribute("memberListWrapper", memberListWrapper);
        return "/student/studentList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String getStudentList(@ModelAttribute("memberListWrapper") MemberListWrapper memberListWrapper, Model model){
        memberService.updateMemberList(memberListWrapper.getMemberList());
        model.addAttribute(memberListWrapper);
        return "/student/studentList";
    }

    @RequestMapping("/addStudent")
    public String addStudent(Model model){
        Member member = new Member();
        model.addAttribute(member);
        return "/student/addStudent";
    }

    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public String addStudent(@Valid @ModelAttribute("member") Member member, BindingResult result){
        if(result.hasErrors())
            return "/student/addStudent";

        memberService.add(member);
        return "redirect:/admin/studentList";
    }

    @RequestMapping(value="/editStudent/{id}", method=RequestMethod.GET)
    public String editStudent(@PathVariable("id") int id, Model model){
        Member member = memberService.get(id);
        model.addAttribute(member);
        return "/student/editStudent";
    }

    @RequestMapping(value = "/editStudent", method = RequestMethod.POST)
    public String editStudent(@Valid @ModelAttribute("member") Member member, BindingResult result){
        if(result.hasErrors())
            return "/student/editStudent";

        memberService.update(member);
        return "redirect:/admin/studentList";
    }

    @RequestMapping("/{id}")
    public String getStudentHistory(@PathVariable("id") int id, Model model){
        Member member = memberService.get(id);
        model.addAttribute(member);
        return "/student/studentHistory";
    }
}
