package com.aimacademyla.controller.resources;

import com.aimacademyla.api.excel.impl.MemberInvoiceExcelWriter;
import com.aimacademyla.api.excel.impl.PeriodSummaryExcelWriter;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.dto.MemberChargesFinancesDTOBuilder;
import com.aimacademyla.model.dto.MemberChargesFinancesDTO;
import com.aimacademyla.service.MemberService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

@Controller
@RequestMapping("/admin/resources/excel/generate")
public class ExcelWriterController {

    private MemberService memberService;

    @Autowired
    public ExcelWriterController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping("/memberInvoice/{memberID}")
    @ResponseBody
    public Object generateMemberInvoice(@PathVariable("memberID") int memberID,
                                  @RequestParam(value="cycleStartDate") String cycleStartDateString,
                                  @RequestParam(value="cycleEndDate") String cycleEndDateString,
                                  HttpServletResponse httpServletResponse){
        Member member = memberService.get(memberID);
        LocalDate cycleStartDate = LocalDate.parse(cycleStartDateString);
        LocalDate cycleEndDate = LocalDate.parse(cycleEndDateString);

        MemberChargesFinancesDTO memberChargesFinancesDTO = new MemberChargesFinancesDTOBuilder().setMember(member)
                                                                .setCycleStartDate(cycleStartDate)
                                                                .setCycleEndDate(cycleEndDate)
                                                                .build();
        String cyclePeriodString;

        if(cycleStartDate.getMonthValue() == cycleEndDate.getMonthValue() && cycleStartDate.getYear() == cycleEndDate.getYear())
            cyclePeriodString = cycleStartDate.getMonth().toString().substring(0,1) + " " + cycleStartDate.getMonth().toString().substring(1).toLowerCase()
                                + ", " + cycleStartDate.getYear();

        else
            cyclePeriodString = cycleStartDate.getMonth().toString().substring(0,1) + " " + cycleStartDate.getMonth().toString().substring(1).toLowerCase()
                                    + ", " + cycleStartDate.getYear() + " - " + cycleEndDate.getMonth().toString().substring(0,1)
                                    + " " + cycleEndDate.getMonth().toString().substring(1).toLowerCase() + ", " + cycleEndDate.getYear();

        String fileName = member.getMemberID() + "_" + cyclePeriodString + "_INVOICE.xlsx";

        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try {
            generateMemberInvoice(httpServletResponse.getOutputStream());
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
        return null;
    }

    @RequestMapping("/periodSummary")
    @ResponseBody
    public Object generatePeriodSummary(
            @RequestParam(name="cycleStartDate") String cycleStartDateString,
            @RequestParam(name="cycleEndDate") String cycleEndDateString,
            HttpServletResponse httpServletResponse){

        String fileName = "Summary.xlsx";

        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        return null;
    }

    private void generateMemberInvoice(ServletOutputStream servletOutputStream) throws IOException, InvalidFormatException{
        Workbook workbook = getWorkbook("Invoice_Template.xlsx");

        MemberInvoiceExcelWriter memberInvoiceExcelWriter = new MemberInvoiceExcelWriter(workbook);
//        memberInvoiceExcelWriter.writeToWorkbook();

        workbook.write(servletOutputStream);
        servletOutputStream.close();
    }

    private void generatePeriodSummry(ServletOutputStream servletOutputStream) throws IOException, InvalidFormatException{
        Workbook workbook = getWorkbook("PeriodSummary_Template.xlsx");

        PeriodSummaryExcelWriter periodSummaryExcelWriter = new PeriodSummaryExcelWriter(workbook);
//        periodSummaryExcelWriter.writeToWorkbook();

        workbook.write(servletOutputStream);
        servletOutputStream.close();
    }

    private Workbook getWorkbook(String fileName) throws IOException, InvalidFormatException{
        String invoiceTemplatePath = getClass().getClassLoader().getResource(fileName).getFile();
        File invoiceTemplateFile = new File(invoiceTemplatePath);
        FileInputStream inputStream = new FileInputStream(invoiceTemplateFile);
        OPCPackage opcPackage = OPCPackage.open(inputStream);

        Workbook workbook = new XSSFWorkbook(opcPackage);
        inputStream.close();
        opcPackage.close();

        return workbook;
    }
}
