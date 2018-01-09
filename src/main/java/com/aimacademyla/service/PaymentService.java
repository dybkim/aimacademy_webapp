package com.aimacademyla.service;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService extends GenericService<Payment, Integer>{
    void addPayment(Payment payment);
    void updatePayment(Payment payment);
    void removePayment(Payment payment);
}
