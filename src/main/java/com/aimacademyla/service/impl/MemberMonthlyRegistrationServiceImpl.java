package com.aimacademyla.service.impl;

import com.aimacademyla.dao.*;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.dao.flow.impl.ChargeDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.MemberMonthlyRegistrationDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.MonthlyFinancesSummaryDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.SeasonDAOAccessFlow;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.entity.ChargeBuilder;
import com.aimacademyla.model.builder.entity.ChargeLineBuilder;
import com.aimacademyla.model.builder.entity.MemberMonthlyRegistrationBuilder;
import com.aimacademyla.model.dto.MemberListDTO;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.enums.BillableUnitType;
import com.aimacademyla.model.initializer.impl.MemberMonthlyRegistrationDefaultValueInitializer;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davidkim on 6/19/17.
 */

@Service
public class MemberMonthlyRegistrationServiceImpl extends GenericServiceImpl<MemberMonthlyRegistration, Integer> implements MemberMonthlyRegistrationService {

    private MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO;
    private ChargeLineService chargeLineService;
    private ChargeService chargeService;
    private ChargeDAO chargeDAO;
    private CourseDAO courseDAO;
    private MemberDAO memberDAO;

    @Autowired
    public MemberMonthlyRegistrationServiceImpl(@Qualifier("memberMonthlyRegistrationDAO") GenericDAO<MemberMonthlyRegistration, Integer> genericDAO,
                                                ChargeLineService chargeLineService,
                                                ChargeService chargeService,
                                                ChargeDAO chargeDAO,
                                                CourseDAO courseDAO,
                                                MemberDAO memberDAO){
        super(genericDAO);
        this.chargeLineService = chargeLineService;
        this.chargeService = chargeService;
        this.memberMonthlyRegistrationDAO = (MemberMonthlyRegistrationDAO) genericDAO;
        this.chargeDAO = chargeDAO;
        this.courseDAO = courseDAO;
        this.memberDAO = memberDAO;
    }

    @Override
    public void updateMemberMonthlyRegistrationList(MemberListDTO memberListDTO){
        HashMap<Integer, Boolean> isActiveMemberHashMap = memberListDTO.getIsActiveMemberHashMap();
        LocalDate cycleStartDate = memberListDTO.getCycleStartDate();
        Course openStudyCourse = courseDAO.get(Course.OPEN_STUDY_ID);

        for(int memberID : isActiveMemberHashMap.keySet()){
            Member member = memberDAO.get(memberID);
            MemberMonthlyRegistration memberMonthlyRegistration = (MemberMonthlyRegistration) new MemberMonthlyRegistrationDAOAccessFlow(getDAOFactory())
                                                                                                .addQueryParameter(member)
                                                                                                .addQueryParameter(cycleStartDate)
                                                                                                .get();
            Boolean memberIsRegistered = isActiveMemberHashMap.get(memberID);

            if(memberIsRegistered == null)
                memberIsRegistered = false;

            Season season = (Season) new SeasonDAOAccessFlow(getDAOFactory())
                                         .addQueryParameter(cycleStartDate)
                                         .get();

            if(memberIsRegistered){
                if(memberMonthlyRegistration == null || memberMonthlyRegistration.getMemberMonthlyRegistrationID() == MemberMonthlyRegistration.INACTIVE){
                    memberMonthlyRegistration = new MemberMonthlyRegistrationBuilder()
                                                    .setMember(member)
                                                    .setCycleStartDate(cycleStartDate)
                                                    .setSeason(season)
                                                    .setMembershipCharge(member.getMembershipRate())
                                                    .build();

                    Charge charge = (Charge) new ChargeDAOAccessFlow(getDAOFactory())
                                                .addQueryParameter(member)
                                                .addQueryParameter(openStudyCourse)
                                                .addQueryParameter(cycleStartDate)
                                                .get();

                    ChargeLine chargeLine = new ChargeLineBuilder()
                                                .setBillableUnitsBilled(BigDecimal.ONE)
                                                .setChargeAmount(member.getMembershipRate())
                                                .setCharge(charge)
                                                .setDateCharged(cycleStartDate)
                                                .build();

                    chargeLineService.addChargeLine(chargeLine);
                    memberMonthlyRegistration.setCharge(charge);
                    memberMonthlyRegistrationDAO.update(memberMonthlyRegistration);
                }
            }

            else{
                if(memberMonthlyRegistration.getMemberMonthlyRegistrationID() != MemberMonthlyRegistration.INACTIVE){
                    Charge charge = (Charge) new ChargeDAOAccessFlow(getDAOFactory())
                            .addQueryParameter(member)
                            .addQueryParameter(openStudyCourse)
                            .addQueryParameter(cycleStartDate)
                            .get();

                    charge = chargeService.loadCollections(charge);
                    List<ChargeLine> chargeLineList = new ArrayList<>(charge.getChargeLineSet());

                    if(chargeLineList.isEmpty()){
                        chargeService.removeCharge(charge);
                        memberMonthlyRegistrationDAO.remove(memberMonthlyRegistration);
                        return;
                    }

                    ChargeLine chargeLine = new ArrayList<>(charge.getChargeLineSet()).get(0);
                    memberMonthlyRegistrationDAO.remove(memberMonthlyRegistration);
                    chargeLineService.removeChargeLine(chargeLine);
                }
            }
        }
    }

    /*
     * Charge is generated if none exists
     * chargeAmount for OpenStudy is set as a constant value since it is not calculated per hour
     *
     * IMPORTANT: AN OPEN STUDY COURSE ENTITY MUST EXIST ALREADY IN THE DATABASE, ELSE NO ENTITY WILL BE RETURNED
     * TODO: MUST IMPLEMENT A CHECK TO SEE IF OPEN STUDY COURSE ENTITY EXISTS
     */
    @Override
    public void addMemberMonthlyRegistration(MemberMonthlyRegistration memberMonthlyRegistration) {
        Member member = memberMonthlyRegistration.getMember();
        Course course = courseDAO.get(Course.OPEN_STUDY_ID);
        Charge charge = (Charge) new ChargeDAOAccessFlow(getDAOFactory())
                                        .addQueryParameter(member)
                                        .addQueryParameter(course)
                                        .addQueryParameter(memberMonthlyRegistration.getCycleStartDate())
                                        .get();

        ChargeLine chargeLine = new ChargeLineBuilder()
                                        .setCharge(charge)
                                        .setChargeAmount(member.getMembershipRate())
                                        .setBillableUnitsBilled(BigDecimal.ONE)
                                        .setDateCharged(memberMonthlyRegistration.getCycleStartDate())
                                        .build();

        charge.setChargeAmount(memberMonthlyRegistration.getMembershipCharge());
        charge.setBillableUnitsBilled(BigDecimal.ONE);
        chargeLineService.addChargeLine(chargeLine);

        add(memberMonthlyRegistration);
    }

    @Override
    public void updateMemberMonthlyRegistration(MemberMonthlyRegistration memberMonthlyRegistration) {
        Member member = memberMonthlyRegistration.getMember();
        Course course = courseDAO.get(Course.OPEN_STUDY_ID);
        Charge charge = (Charge) new ChargeDAOAccessFlow(getDAOFactory())
                .addQueryParameter(member)
                .addQueryParameter(course)
                .addQueryParameter(memberMonthlyRegistration.getCycleStartDate())
                .get();

        charge = chargeService.loadCollections(charge);

        ChargeLine chargeLine;

        if(!charge.getChargeLineSet().isEmpty())
            chargeLine = new ArrayList<>(charge.getChargeLineSet()).get(0);

        else
            chargeLine = new ChargeLineBuilder().setCharge(charge)
                                                .setChargeAmount(member.getMembershipRate())
                                                .setBillableUnitsBilled(BigDecimal.ONE)
                                                .setDateCharged(memberMonthlyRegistration.getCycleStartDate())
                                                .build();

        chargeLine.setChargeAmount(member.getMembershipRate());
        charge.updateChargeLine(chargeLine);

        chargeLineService.updateChargeLine(chargeLine);

        update(memberMonthlyRegistration);
    }

    @Override
    public void removeMemberMonthlyRegistration(MemberMonthlyRegistration memberMonthlyRegistration) {
        Member member = memberMonthlyRegistration.getMember();
        Course course = courseDAO.get(Course.OPEN_STUDY_ID);

        Charge charge = (Charge) new ChargeDAOAccessFlow(getDAOFactory())
                .addQueryParameter(member)
                .addQueryParameter(course)
                .addQueryParameter(memberMonthlyRegistration.getCycleStartDate())
                .get();

        charge = chargeService.loadCollections(charge);

        if(charge.getChargeLineSet().isEmpty()) {
            chargeService.removeCharge(charge);
            remove(memberMonthlyRegistration);
            return;
        }

        ChargeLine chargeLine = new ArrayList<>(charge.getChargeLineSet()).get(0);

        if(chargeDAO.get(charge.getChargeID()) != null)
            chargeLineService.removeChargeLine(chargeLine);

        remove(memberMonthlyRegistration);
    }

}
