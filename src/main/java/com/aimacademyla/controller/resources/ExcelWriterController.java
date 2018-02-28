package com.aimacademyla.controller.resources;

import com.aimacademyla.api.excel.impl.MemberInvoiceExcelWriter;
import com.aimacademyla.api.excel.impl.PeriodSummaryExcelWriter;
import com.aimacademyla.dao.flow.impl.AbstractDAOAccessFlowImpl;
import com.aimacademyla.dao.flow.impl.ChargeDAOAccessFlow;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.builder.dto.MemberChargesFinancesDTOBuilder;
import com.aimacademyla.model.builder.dto.OutstandingChargesPaymentDTOBuilder;
import com.aimacademyla.model.builder.dto.PeriodSummaryDTOBuilder;
import com.aimacademyla.model.dto.MemberChargesFinancesDTO;
import com.aimacademyla.model.dto.OutstandingChargesPaymentDTO;
import com.aimacademyla.model.dto.PeriodSummaryDTO;
import com.aimacademyla.model.temporal.CyclePeriod;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.type.LocalDateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/resources/excel/generate")
public class ExcelWriterController {

    private MemberService memberService;
    private CourseService courseService;
    private ChargeService chargeService;

    @Autowired
    public ExcelWriterController(MemberService memberService,
                                 CourseService courseService,
                                 ChargeService chargeService) {
        this.memberService = memberService;
        this.courseService = courseService;
        this.chargeService = chargeService;
    }

    @RequestMapping("/memberInvoice/{memberID}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Object generateMemberInvoice(@PathVariable("memberID") int memberID,
                                  @RequestParam(value="cycleStartDate") String cycleStartDateString,
                                  @RequestParam(value="cycleEndDate") String cycleEndDateString,
                                  HttpServletResponse httpServletResponse){
        Member member = memberService.get(memberID);
        LocalDate cycleStartDate = LocalDate.parse(cycleStartDateString);
        LocalDate cycleEndDate = LocalDate.parse(cycleEndDateString);
        CyclePeriod cyclePeriod = new CyclePeriod(cycleStartDate, cycleEndDate);

        List<Course> courseList = courseService.getList();
        List<Charge> chargeList = new ChargeDAOAccessFlow()
                                        .addQueryParameter(member)
                                        .addQueryParameter(cyclePeriod)
                                        .getList();

        chargeList = (List) chargeService.loadCollections(chargeList);

        PeriodSummaryDTO.MemberSummary memberSummary = new PeriodSummaryDTOBuilder.MemberSummaryBuilder()
                                                                .setMember(member)
                                                                .setCyclePeriod(cyclePeriod)
                                                                .setCourseList(courseList)
                                                                .setChargeList(chargeList)
                                                                .build();

        String fileName = generateInvoiceFileName(cyclePeriod, member);

        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try {
            generateMemberInvoice(memberSummary, httpServletResponse.getOutputStream());
        } catch (IOException | InvalidFormatException ioe) {
            System.out.println("ERROR: " + ioe);
        }
        return null;
    }

    @RequestMapping("/periodSummary")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Object generatePeriodSummary(
            @RequestParam(name="cycleStartDate") String cycleStartDateString,
            @RequestParam(name="cycleEndDate") String cycleEndDateString,
            HttpServletResponse httpServletResponse){

        LocalDate cycleStartDate = LocalDate.parse(cycleStartDateString);
        LocalDate cycleEndDate = LocalDate.parse(cycleEndDateString);

        CyclePeriod cyclePeriod = new CyclePeriod(cycleStartDate, cycleEndDate);


        PeriodSummaryDTO periodSummaryDTO = new PeriodSummaryDTOBuilder()
                                                .setCyclePeriod(cyclePeriod)
                                                .build();

        String fileName = generatePeriodSummaryFileName(cyclePeriod);

        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try {
            generatePeriodSummary(periodSummaryDTO, httpServletResponse.getOutputStream());
        } catch (IOException | InvalidFormatException ioe) {
            System.out.println("ERROR: " + ioe);
        }

        return null;
    }

    private String generateInvoiceFileName(CyclePeriod cyclePeriod, Member member){
        LocalDate cycleStartDate = cyclePeriod.getCycleStartDate();
        LocalDate cycleEndDate = cyclePeriod.getCycleEndDate();

        String cyclePeriodString;

        if(cycleStartDate.getMonthValue() == cycleEndDate.getMonthValue() && cycleStartDate.getYear() == cycleEndDate.getYear())
            cyclePeriodString = cycleStartDate.getMonth().toString().substring(0,1) + cycleStartDate.getMonth().toString().substring(1).toLowerCase()
                    + ", " + cycleStartDate.getYear();

        else
            cyclePeriodString = cycleStartDate.getMonth().toString().substring(0,1) + cycleStartDate.getMonth().toString().substring(1).toLowerCase()
                    + ", " + cycleStartDate.getYear() + " - " + cycleEndDate.getMonth().toString().substring(0,1)
                    + cycleEndDate.getMonth().toString().substring(1).toLowerCase() + ", " + cycleEndDate.getYear();

        return member.getMemberFirstName() + "_" + member.getMemberLastName() + "_" + cyclePeriodString + "_INVOICE.xlsx";
    }

    private String generatePeriodSummaryFileName(CyclePeriod cyclePeriod){
        LocalDate cycleStartDate = cyclePeriod.getCycleStartDate();
        LocalDate cycleEndDate = cyclePeriod.getCycleEndDate();

        return cycleStartDate.getMonth().toString().substring(0,1) + cycleStartDate.getMonth().toString().substring(1,3).toLowerCase()
                + cycleStartDate.getDayOfMonth() + " " + cycleStartDate.getYear() + "_" +
                cycleEndDate.getMonth().toString().substring(0,1) + cycleEndDate.getMonth().toString().substring(1,3).toLowerCase()
                + cycleEndDate.getDayOfMonth() + " " + cycleEndDate.getYear() + " Period Summary.xlsx";
    }

    /*
     * IMPORTANT: For the following two methods (generateMemberInvoice and generatePeriodSummary), must make write
     * changes to workbook while various streams are open, else the resulting file will come out corrupted.
     */
    private void generateMemberInvoice(PeriodSummaryDTO.MemberSummary memberSummary, ServletOutputStream servletOutputStream) throws IOException, InvalidFormatException{
        String invoiceTemplatePath = getClass().getClassLoader().getResource(MemberInvoiceExcelWriter.TEMPLATE_FILE_NAME).getFile();
        File invoiceTemplateFile = new File(invoiceTemplatePath);
        FileInputStream inputStream = new FileInputStream(invoiceTemplateFile);
        OPCPackage opcPackage = OPCPackage.open(inputStream);
        Workbook workbook = new XSSFWorkbook(opcPackage);

        workbook = new MemberInvoiceExcelWriter(workbook)
                        .setMemberSummary(memberSummary)
                        .writeToWorkbook();
        workbook.write(servletOutputStream);

        inputStream.close();
        opcPackage.close();

        servletOutputStream.close();
    }

    private void generatePeriodSummary(PeriodSummaryDTO periodSummaryDTO, ServletOutputStream servletOutputStream) throws IOException, InvalidFormatException{
        String invoiceTemplatePath = getClass().getClassLoader().getResource(PeriodSummaryExcelWriter.TEMPLATE_FILE_NAME).getFile();
        File invoiceTemplateFile = new File(invoiceTemplatePath);
        FileInputStream inputStream = new FileInputStream(invoiceTemplateFile);
        OPCPackage opcPackage = OPCPackage.open(inputStream);
        Workbook workbook = new XSSFWorkbook(opcPackage);

        workbook = new PeriodSummaryExcelWriter(workbook)
                        .setPeriodSummaryDTO(periodSummaryDTO)
                        .writeToWorkbook();

        workbook.write(servletOutputStream);

        inputStream.close();
        opcPackage.close();
        servletOutputStream.close();
    }
}
