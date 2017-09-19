package com.aimacademyla.service;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService extends GenericService<Payment, Integer>{
    List<Payment> getPaymentsByMember(Member member);

    List<Payment> getPaymentsByMemberForCourse(Member member, Course course);

    List<Payment> getPaymentsForDate(LocalDate date);

    Payment getPaymentForMemberByDate(Member member, LocalDate date);

    void remove(List<Payment> paymentList);
}
