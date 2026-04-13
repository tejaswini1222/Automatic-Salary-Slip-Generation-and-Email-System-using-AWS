package com.project;


import java.io.InputStream;
import java.util.*;
import org.apache.poi.ss.usermodel.*;


public class ExcelReader {

	public static List<Map<String, String>> readExcel(InputStream input) {
        List<Map<String, String>> list = new ArrayList<>();

        try {
            Workbook wb = WorkbookFactory.create(input);
            Sheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
               
            if (row.getRowNum() == 0) continue;

            Map<String, String> data = new HashMap<>();
            data.put("name", getCellValue(row.getCell(0)));
            data.put("employeecode", getCellValue(row.getCell(1)));
            data.put("client", getCellValue(row.getCell(2)));
            data.put("dob", getCellValue(row.getCell(3)));
            data.put("department", getCellValue(row.getCell(4)));
            data.put("gmail", getCellValue(row.getCell(5)));
            data.put("totaldays", getCellValue(row.getCell(6)));
            data.put("paydays", getCellValue(row.getCell(7)));
            data.put("stipendrate", getCellValue(row.getCell(8)));
            data.put("total_stipend", getCellValue(row.getCell(9)));
            data.put("total_ot_amount_dec&_jan", getCellValue(row.getCell(10)));
            data.put("attendance_bonus", getCellValue(row.getCell(11)));
            data.put("gross_total_stipend", getCellValue(row.getCell(12)));
            data.put("deduction", getCellValue(row.getCell(13)));
            data.put("total_deduction", getCellValue(row.getCell(14)));
            data.put("final_stipend", getCellValue(row.getCell(15)));

            list.add(data);
        }
            wb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static String getCellValue(Cell cell) {

        if (cell == null) return "";

        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue();

            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
           
            case FORMULA:
                // 🔥 IMPORTANT: get evaluated value
                FormulaEvaluator evaluator = 
                    cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();

                CellValue value = evaluator.evaluate(cell);

                switch (value.getCellType()) {
                    case STRING:
                        return value.getStringValue();
                    case NUMERIC:
                        return String.valueOf(value.getNumberValue());
                    default:
                        return "";
                }

            default:
                return "";
        }
    }
}