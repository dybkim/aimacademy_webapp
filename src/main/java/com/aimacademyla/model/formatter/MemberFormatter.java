package com.aimacademyla.model.formatter;

import com.aimacademyla.model.Member;
import com.aimacademyla.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class MemberFormatter implements Formatter<Member> {

    private MemberService memberService;

    private static final Logger logger = LogManager.getLogger(MemberFormatter.class.getName());

    @Autowired
    public MemberFormatter(MemberService memberService){
        logger.debug("initialized memberFormatter");
        this.memberService = memberService;
    }

    @Override
    public Member parse(String memberID, Locale locale) throws ParseException {
        logger.debug("parsing memberID: " + memberID);
        return memberService.get(Integer.parseInt(memberID));
    }

    @Override
    public String print(Member member, Locale locale) {
        return member.toString();
    }
}
