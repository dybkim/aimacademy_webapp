package com.aimacademyla.controller.resources;

import com.aimacademyla.api.excel.impl.ExcelInvoiceView;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.dto.MemberChargesFinancesDTOBuilder;
import com.aimacademyla.model.dto.MemberChargesFinancesDTO;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/resources/excel")
public class ExcelWriterController {

    private DAOFactory daoFactory;

    private MemberService memberService;
    private CourseService courseService;
    private ChargeService chargeService;
    private ChargeLineService chargeLineService;

    @Autowired
    public ExcelWriterController(DAOFactory daoFactory,
                                 MemberService memberService,
                                 CourseService courseService,
                                 ChargeService chargeService,
                                 ChargeLineService chargeLineService) {
        this.daoFactory = daoFactory;
        this.memberService = memberService;
        this.courseService = courseService;
        this.chargeService = chargeService;
        this.chargeLineService = chargeLineService;
    }


    @RequestMapping("/generateInvoice/student/{memberID}")
    public Object generateInvoice(@PathVariable("memberID") int memberID,
                                        @RequestParam(value="month", required = false) Integer month,
                                        @RequestParam(value="year", required = false) Integer year,
                                        HttpServletResponse httpServletResponse){
        Member member = memberService.get(memberID);

        if(month == null || year == null){
            month = LocalDate.now().getMonthValue();
            year = LocalDate.now().getYear();
        }

        LocalDate selectedDate = LocalDate.of(year, month, 1);
        MemberChargesFinancesDTO memberChargesFinancesDTO = new MemberChargesFinancesDTOBuilder(daoFactory).setMember(member).setSelectedDate(selectedDate).build();

        String selectedMonthString = selectedDate.getMonth().toString().substring(0,1) + selectedDate.getMonth().toString().substring(1).toLowerCase();
        String fileName = member.getMemberID() + "_" + selectedMonthString + selectedDate.getYear() + "_INVOICE.xlsx";

        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        return new ModelAndView(new ExcelInvoiceView(), "memberChargesFinancesWrapper", memberChargesFinancesDTO);
    }
}
