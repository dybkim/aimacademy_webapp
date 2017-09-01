package com.aimacademyla.controller.student.rest;

import com.aimacademyla.model.Member;
import com.aimacademyla.service.MemberService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */

@Controller
@RequestMapping("/admin/studentList/rest/")
public class StudentListResource {

    private MemberService memberService;

    @Autowired
    public StudentListResource(MemberService memberService){
        this.memberService = memberService;
    }

    @RequestMapping("/getList")
    @ResponseBody
    public String getStudentList(){
        return new Gson().toJson(memberService.getMemberList());
    }

}
