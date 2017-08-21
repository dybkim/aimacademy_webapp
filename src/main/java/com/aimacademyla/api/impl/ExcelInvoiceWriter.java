package com.aimacademyla.api.impl;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.wrapper.MemberChargesFinancesWrapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelInvoiceWriter extends AbstractXlsView implements ResourceLoaderAware{

    private ResourceLoader resourceLoader;

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        MemberChargesFinancesWrapper memberChargesFinancesWrapper = (MemberChargesFinancesWrapper) map.get("memberChargesFinancesWrapper");
        List<Charge> chargeList = (ArrayList<Charge>) memberChargesFinancesWrapper.getChargeHashMap().values();
        HashMap<Integer, BigDecimal> hoursBilledHashMap = memberChargesFinancesWrapper.getHoursBilledHashMap();

        XSSFWorkbook invoiceTemplate = new XSSFWorkbook(resourceLoader.getResource("./spreadsheets/Invoice Template.xlsx").getURL().getPath());
        XSSFSheet invoiceSheet = invoiceTemplate.getSheetAt(0);

        Cell recipientCell = invoiceSheet.getRow(ExcelRowColumnWrapper.RECIPIENT_CELL.getRow()).getCell(ExcelRowColumnWrapper.RECIPIENT_CELL.getColumn());


        for(int count = 0; count < chargeList.size(); count++){
            Charge charge = chargeList.get(count);
            String chargeDescription = charge.getDescription();
            BigDecimal chargeAmount = charge.getChargeAmount();
            BigDecimal discountAmount = charge.getDiscountAmount();
            BigDecimal hoursBilled = hoursBilledHashMap.get(charge.getChargeID());
            BigDecimal balanceAmount = chargeAmount.subtract(discountAmount);

            Cell descriptionCell = invoiceSheet.getRow(ExcelRowColumnWrapper.DESCRIPTION_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.DESCRIPTION_CELL_START.getColumn());
            Cell quantityCell = invoiceSheet.getRow(ExcelRowColumnWrapper.QUANTITY_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.QUANTITY_CELL_START.getColumn());
            Cell priceCell = invoiceSheet.getRow(ExcelRowColumnWrapper.PRICE_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.PRICE_CELL_START.getColumn());
            Cell discountCell = invoiceSheet.getRow(ExcelRowColumnWrapper.DISCOUNT_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.DISCOUNT_CELL_START.getColumn());
            Cell totalCell = invoiceSheet.getRow(ExcelRowColumnWrapper.TOTAL_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.TOTAL_CELL_START.getColumn());
        }
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

        this.resourceLoader = resourceLoader;
    }

    private enum ExcelRowColumnWrapper{
        DESCRIPTION_CELL_START(20,2),
        QUANTITY_CELL_START(20,4),
        PRICE_CELL_START(20,5),
        DISCOUNT_CELL_START(20,6),
        TOTAL_CELL_START(20,7),
        RECIPIENT_CELL(10,2),
        INVOICE_NUMBER_CELL(5,7),
        DATE_CELL(6,7),
        STUDENT_ID_CELL(7,7);


        private int column;
        private int row;

        ExcelRowColumnWrapper(int row, int column){
            this.row = row;
            this.column = column;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }
    }
}
