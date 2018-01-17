package com.aimacademyla.model.builder.dto;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.dto.CourseRegistrationDTO;
import com.aimacademyla.model.dto.CourseRegistrationDTOListItem;

import java.util.ArrayList;
import java.util.List;

public class CourseRegistrationDTOBuilder extends GenericDTOBuilderImpl<CourseRegistrationDTO>{

    private Course course;
    private List<MemberCourseRegistration> memberCourseRegistrationList;
    private List<CourseRegistrationDTOListItem> courseRegistrationDTOListItemList;

    public CourseRegistrationDTOBuilder setCourse(Course course){
        this.course = course;
        courseRegistrationDTOListItemList = new ArrayList<>();
        return this;
    }

    @Override
    public CourseRegistrationDTO build() {
        CourseRegistrationDTO courseRegistrationDTO = new CourseRegistrationDTO();
        memberCourseRegistrationList = new ArrayList<>(course.getMemberCourseRegistrationSet());

        populateCourseRegistrationDTOListItemList();

        courseRegistrationDTO.setCourse(course);
        courseRegistrationDTO.setCourseRegistrationDTOListItems(courseRegistrationDTOListItemList);

        return courseRegistrationDTO;
    }

    private void populateCourseRegistrationDTOListItemList(){
        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList){
            if(!memberCourseRegistration.getIsEnrolled())
                continue;

            Member member = memberCourseRegistration.getMember();
            CourseRegistrationDTOListItem courseRegistrationDTOListItem = new CourseRegistrationDTOListItem();
            courseRegistrationDTOListItem.setIsDropped(false);
            courseRegistrationDTOListItem.setMember(member);
            courseRegistrationDTOListItemList.add(courseRegistrationDTOListItem);
        }
    }
}
