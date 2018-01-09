package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.dto.CourseRegistrationDTO;
import com.aimacademyla.model.dto.CourseRegistrationDTOListItem;

import java.util.ArrayList;
import java.util.List;

public class CourseRegistrationDTOBuilder extends GenericDTOBuilderImpl<CourseRegistrationDTO>{

    private Course course;

    public CourseRegistrationDTOBuilder(DAOFactory daoFactory){
        super(daoFactory);
    }

    public CourseRegistrationDTOBuilder setCourse(Course course){
        this.course = course;
        return this;
    }

    @Override
    public CourseRegistrationDTO build() {
        CourseRegistrationDTO courseRegistrationDTO = new CourseRegistrationDTO();
        List<CourseRegistrationDTOListItem> courseRegistrationWrapperObjectList = new ArrayList<>();
        List<MemberCourseRegistration> memberCourseRegistrationList = new ArrayList<>(course.getMemberCourseRegistrationSet());

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList){
            if(!memberCourseRegistration.getIsEnrolled())
                continue;

            Member member = memberCourseRegistration.getMember();
            CourseRegistrationDTOListItem courseRegistrationWrapperObject = new CourseRegistrationDTOListItem();
            courseRegistrationWrapperObject.setIsDropped(false);
            courseRegistrationWrapperObject.setMember(member);
            courseRegistrationWrapperObjectList.add(courseRegistrationWrapperObject);
        }

        courseRegistrationDTO.setCourse(course);
        courseRegistrationDTO.setCourseRegistrationDTOListItems(courseRegistrationWrapperObjectList);

        return courseRegistrationDTO;
    }
}
