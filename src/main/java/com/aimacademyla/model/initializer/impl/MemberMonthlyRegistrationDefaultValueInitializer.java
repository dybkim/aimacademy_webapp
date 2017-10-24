package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.initializer.GenericDefaultValueInitializer;
import com.aimacademyla.service.SeasonService;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class MemberMonthlyRegistrationDefaultValueInitializer extends GenericDefaultValueInitializerImpl<MemberMonthlyRegistration> implements GenericDefaultValueInitializer<MemberMonthlyRegistration>{

    private SeasonDAO seasonDAO;

    private int memberID;
    private LocalDate localDate;

    public MemberMonthlyRegistrationDefaultValueInitializer(DAOFactory daoFactory){
        super(daoFactory);
        seasonDAO = (SeasonDAO) getDAOFactory().getDAO(AIMEntityType.SEASON);
    }

    @Override
    public MemberMonthlyRegistration initialize() {
        MemberMonthlyRegistration memberMonthlyRegistration = new MemberMonthlyRegistration();
        LocalDate cycleStartDate = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1);
        Season season = seasonDAO.getSeason(cycleStartDate);

        if(season == null)
            season = seasonDAO.get(Season.NO_SEASON_FOUND);

        memberMonthlyRegistration.setCycleStartDate(cycleStartDate);
        memberMonthlyRegistration.setMemberID(memberID);
        memberMonthlyRegistration.setSeasonID(season.getSeasonID());

        return memberMonthlyRegistration;
    }

    public MemberMonthlyRegistrationDefaultValueInitializer setMemberID(int memberID) {
        this.memberID = memberID;
        return this;
    }

    public MemberMonthlyRegistrationDefaultValueInitializer setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }
}
