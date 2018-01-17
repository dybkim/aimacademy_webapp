package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.model.Member;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * Created by davidkim on 1/26/17.
 */

@Service
public class MemberServiceImpl extends GenericServiceImpl<Member, Integer> implements MemberService{

    private MemberDAO memberDAO;

    @Autowired
    public MemberServiceImpl(@Qualifier("memberDAO") GenericDAO<Member, Integer> genericDAO){
        super(genericDAO);
        this.memberDAO = (MemberDAO) genericDAO;
    }

    @Override
    public List<Member> getList(){
        List<Member> memberList = memberDAO.getList();
        Iterator it = memberList.iterator();
        while(it.hasNext()){
            Member member = (Member)it.next();
            if(member.getMemberID() <= 1000)
                it.remove();
        }
        return memberList;
    }
}
