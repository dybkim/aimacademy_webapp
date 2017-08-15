package com.aimacademyla.model;

import com.aimacademyla.model.reference.TemporalReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by davidkim on 5/22/17.
 */
@Entity
public class Season implements Serializable {

    private static final long serialVersionUID = 681613110921037803L;

    public static final int NO_SEASON_FOUND = 1;

    public enum SeasonDescription{
        FALL,
        WINTER,
        SPRING,
        SUMMER,
        NONE;

        SeasonDescription(){}

        public static String toString(LocalDate date){
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
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SeasonID")
    private int seasonID;

    @Column(name="SeasonTitle")
    private String seasonTitle;

    @Column(name="StartDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate startDate;

    @Column(name="EndDate")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

    @Column(name="SeasonDescription")
    private String seasonDescription;

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
}
