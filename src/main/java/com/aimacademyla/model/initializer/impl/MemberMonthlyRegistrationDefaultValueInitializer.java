package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.dao.flow.impl.SeasonDAOAccessFlow;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.entity.MemberMonthlyRegistrationBuilder;

import java.time.LocalDate;

public class MemberMonthlyRegistrationDefaultValueInitializer extends GenericDefaultValueInitializerImpl<MemberMonthlyRegistration>{

    private SeasonDAO seasonDAO;

    private Member member;
    private LocalDate localDate;

    public MemberMonthlyRegistrationDefaultValueInitializer(){
        seasonDAO = (SeasonDAO) getDAOFactory().getDAO(Season.class);
    }

    @Override
    public MemberMonthlyRegistration initialize() {
        LocalDate cycleStartDate = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1);
        Season season = (Season) new SeasonDAOAccessFlow()
                                     .addQueryParameter(cycleStartDate)
                                     .get();

        if(season == null)
            season = seasonDAO.get(Season.NO_SEASON_FOUND);

        return new MemberMonthlyRegistrationBuilder()
                    .setCycleStartDate(cycleStartDate)
                    .setMember(member)
                    .setSeason(season)
                    .build();
    }

    public MemberMonthlyRegistrationDefaultValueInitializer setMember(Member member){
        this.member = member;
        return this;
    }

    public MemberMonthlyRegistrationDefaultValueInitializer setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }
}
