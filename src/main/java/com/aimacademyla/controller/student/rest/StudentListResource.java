package com.aimacademyla.controller.student.rest;

import com.aimacademyla.model.Member;
import com.aimacademyla.service.MemberService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */

@Controller
@RequestMapping("/admin/studentList/rest")
public class StudentListResource {

    private MemberService memberService;

    private static final Logger logger = LogManager.getLogger(StudentListResource.class.getName());

    @Autowired
    public StudentListResource(MemberService memberService){
        this.memberService = memberService;
    }

    @RequestMapping("/getList")
    @ResponseBody
    public String getStudentList(){
        List<Member> memberList = memberService.getList();
        logger.debug("memberList size: " + memberList.size());
        String jsonOutput = new Gson().toJson(memberList);
        logger.debug("Constructed JSON:" + jsonOutput);
        return jsonOutput;
    }

    @RequestMapping("/getMembershipRates")
    @ResponseBody
    public HashMap<Integer, BigDecimal> getMembershipRates(){
        HashMap<Integer, BigDecimal> membershipRateHashMap = new HashMap<>();
        List<Member> memberList = memberService.getList();

        for(Member member : memberList){
            membershipRateHashMap.put(member.getMemberID(), member.getMembershipRate());
        }

        return membershipRateHashMap;
    }

}
