package com.aimacademyla.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by davidkim on 5/22/17.
 */
@Entity
public class Season extends AIMEntity implements Serializable {

    private static final long serialVersionUID = 681613110921037803L;

    public static final int NO_SEASON_FOUND = 1;

    public enum SeasonDescription{
        FALL,
        WINTER,
        SPRING,
        SUMMER,
        NONE;

        SeasonDescription(){}

        public String toString(LocalDate date){
            switch(date.getMonthValue()){
                case 1: case 2:
                    return "Winter " + date.getYear();
                case 3: case 4: case 5:
                    return "Spring " + date.getYear();
                case 6: case 7: case 8:
                    return "Summer " + date.getYear();
                case 9: case 10: case 11:
                    return "Fall " + date.getYear();
                case 12:
                    return "Winter " + (date.getYear() + 1);
                default:
                    return "N/A";
            }
        }

        public static SeasonDescription getSeasonDescription(LocalDate date){
            switch(date.getMonthValue()){
                case 1: case 2: case 12:
                    return WINTER;
                case 3: case 4: case 5:
                    return SPRING;
                case 6: case 7: case 8:
                    return SUMMER;
                case 9: case 10: case 11:
                    return FALL;
                default:
                    return NONE;
            }
        }

        public static LocalDate getSeasonStartDate(LocalDate date){
            switch(date.getMonthValue()){
                case 1: case 2:
                    return LocalDate.of(date.getYear()-1, 12, 1);
                case 3: case 4: case 5:
                    return LocalDate.of(date.getYear(), 3, 1);
                case 6: case 7: case 8:
                    return LocalDate.of(date.getYear(), 6, 1);
                case 9: case 10: case 11:
                    return LocalDate.of(date.getYear(), 9, 1);
                case 12:
                    return LocalDate.of(date.getYear(), 12, 1);
                default:
                    return null;
            }
        }

        public static LocalDate getSeasonEndDate(LocalDate date){
            LocalDate localDate;
            switch(date.getMonthValue()){
                case 1: case 2:
                    localDate = LocalDate.of(date.getYear(), 2, 28);
                    if(localDate.isLeapYear())
                        localDate = LocalDate.of(localDate.getYear(), 2, 29);
                    return localDate;
                case 3: case 4: case 5:
                    return LocalDate.of(date.getYear(), 5, 31);
                case 6: case 7: case 8:
                    return LocalDate.of(date.getYear(), 8, 31);
                case 9: case 10: case 11:
                    return LocalDate.of(date.getYear(), 11, 30);
                case 12:
                    localDate = LocalDate.of(date.getYear() + 1, 2, 28);
                    if(localDate.isLeapYear())
                        localDate = LocalDate.of(localDate.getYear() + 1, 2, 29);
                    return localDate;
                default:
                    return null;
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SeasonID")
    private int seasonID;

    @Column(name="SeasonTitle")
    @Length(max=20)
    private String seasonTitle;

    @Column(name="StartDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate startDate;

    @Column(name="EndDate")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

    @Column(name="SeasonDescription")
    @Length(max=30)
    private String seasonDescription;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy="season")
    @MapKey(name="courseID")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<Integer, Course> courseMap;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "season")
    @MapKey(name="monthlyFinancesSummaryID")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<Integer, MonthlyFinancesSummary> monthlyFinancesSummaryMap;

    @Override
    public int getBusinessID() {
        return seasonID;
    }

    @Override
    public void setBusinessID(int seasonID){
        this.seasonID = seasonID;
    }

    public int getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public String getSeasonTitle() {
        return seasonTitle;
    }

    public void setSeasonTitle(String seasonTitle) {
        this.seasonTitle = seasonTitle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getSeasonDescription() {
        return seasonDescription;
    }

    public void setSeasonDescription(String seasonDescription) {
        this.seasonDescription = seasonDescription;
    }

    public Map<Integer, Course> getCourseMap() {
        return courseMap;
    }

    public void setCourseMap(Map<Integer, Course> courseMap) {
        this.courseMap = courseMap;
    }

    public Map<Integer, MonthlyFinancesSummary> getMonthlyFinancesSummaryMap() {
        return monthlyFinancesSummaryMap;
    }

    public void setMonthlyFinancesSummaryMap(Map<Integer, MonthlyFinancesSummary> monthlyFinancesSummaryMap) {
        this.monthlyFinancesSummaryMap = monthlyFinancesSummaryMap;
    }
}
