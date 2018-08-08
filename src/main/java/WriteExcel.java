import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * created by yezi on 2018/7/23
 */
public class WriteExcel {

    private List<Employee> data;
    private int month = Calendar.getInstance().get(Calendar.MONTH);
    private int year = Calendar.getInstance().get(Calendar.YEAR);


    public void writeExcel(List<Employee> data, String finalXlsxPath) {
        this.data = data;
        OutputStream out = null;
        try {
            HSSFWorkbook workBook = new HSSFWorkbook();
            this.createSummarySheet(workBook, workBook.createSheet("汇总表"));
            this.createSalaryDetailSheet(workBook, workBook.createSheet("工资明细"));
            this.creategrantDetailSheet(workBook, workBook.createSheet("发放明细"));
            this.createLeaveDetailSheet(workBook, workBook.createSheet("请假出差统计表"));
            this.createMealAllowanceSheet(workBook, workBook.createSheet("餐补"));
            this.createDeductSheet(workBook, workBook.createSheet("其它扣款"));
            this.createOtherAllowanceSheet(workBook, workBook.createSheet("其它补贴"));
            this.createPayRollSheet(workBook, workBook.createSheet("工资条"));

            workBook.write(new File(finalXlsxPath));

            workBook.close();//最后记得关闭工作
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }

    private HSSFCellStyle baseCellStyle(HSSFWorkbook workBook) {
        HSSFCellStyle cellStyle = workBook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private void createSummarySheet(HSSFWorkbook workBook, HSSFSheet sheet) {
        this.setSheetTitle(workBook, sheet, 10, year + "年" + month + "月份工资汇总表");

        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("部门");
        title.createCell(1).setCellValue("税前工资");
        title.createCell(2).setCellValue("社保（单位）");
        title.createCell(3).setCellValue("社保（个人）");
        title.createCell(4).setCellValue("公积金（单位）");
        title.createCell(5).setCellValue("公积金（个人）");
        title.createCell(6).setCellValue("计税工资");
        title.createCell(7).setCellValue("扣个税");
        title.createCell(8).setCellValue("其他补发");
        title.createCell(9).setCellValue("其他扣款");
        title.createCell(10).setCellValue("应发工资");

        List<SalarySummaryDto> dtos = this.getSalarySummaryDtos(data);
        for (int i = 0; i < dtos.size(); i++) {
            Row row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(dtos.get(i).getDepartment());
            row.createCell(1).setCellValue(dtos.get(i).getBeforeTaxSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(2).setCellValue(dtos.get(i).getCompanySocial().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(3).setCellValue(dtos.get(i).getPersonSocial().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(4).setCellValue(dtos.get(i).getCompanyGGJ().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(5).setCellValue(dtos.get(i).getPersonGGJ().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(6).setCellValue(dtos.get(i).getContainTaxSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(7).setCellValue(dtos.get(i).getTax().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(8).setCellValue(dtos.get(i).getOtherSubsidy().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(9).setCellValue(dtos.get(i).getOtherDeductMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(10).setCellValue(dtos.get(i).getShouldSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        }
        Row sumRow = sheet.createRow(dtos.size() + 3);
        sumRow.createCell(0).setCellValue("合计");
        sumRow.createCell(1).setCellValue(this.sumDecimalDto(x -> x.getBeforeTaxSalary(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(2).setCellValue(this.sumDecimalDto(x -> x.getCompanySocial(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(3).setCellValue(this.sumDecimalDto(x -> x.getPersonSocial(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(4).setCellValue(this.sumDecimalDto(x -> x.getCompanyGGJ(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(5).setCellValue(this.sumDecimalDto(x -> x.getPersonGGJ(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(6).setCellValue(this.sumDecimalDto(x -> x.getContainTaxSalary(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(7).setCellValue(this.sumDecimalDto(x -> x.getTax(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(8).setCellValue(this.sumDecimalDto(x -> x.getOtherSubsidy(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(9).setCellValue(this.sumDecimalDto(x -> x.getOtherDeductMoney(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(10).setCellValue(this.sumDecimalDto(x -> x.getShouldSalary(), dtos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        for (int i = 2; i < dtos.size() + 4; i++) {
            for (int j = 0; j < 11; j++) {
                sheet.getRow(i).getCell(j).setCellStyle(baseCellStyle(workBook));
            }
        }

        Row bottom = sheet.createRow(data.size() + 9);
        bottom.createCell(2).setCellValue("总经理：");
        bottom.createCell(5).setCellValue("财务经理：");
        bottom.createCell(8).setCellValue("制表人：");
    }

    private List<SalarySummaryDto> getSalarySummaryDtos(List<Employee> data) {
        List<SalarySummaryDto> dtos = new ArrayList<SalarySummaryDto>();
        Map<String, List<Employee>> map = new HashMap<String, List<Employee>>();
        for (Employee emp : data) {
            if (map.containsKey(emp.getDepartment())) {
                map.get(emp.getDepartment()).add(emp);
            } else {
                List<Employee> temp = new ArrayList<Employee>();
                temp.add(emp);
                map.put(emp.getDepartment(), temp);
            }
        }
        for (String key : map.keySet()) {
            SalarySummaryDto dto = new SalarySummaryDto();
            List<Employee> temp = map.get(key);
            dto.setDepartment(key);
            dto.setBeforeTaxSalary(this.sumDecimal(x -> x.getShuiQianSalary(), temp));
            dto.setCompanySocial(this.sumDecimal(x -> x.getShebaodanweiheji(), temp));
            dto.setPersonSocial(this.sumDecimal(x -> x.getShebaogerenheji(), temp));
            dto.setCompanyGGJ(this.sumDecimal(x -> x.getGongjinjiGongsi(), temp));
            dto.setPersonGGJ(this.sumDecimal(x -> x.getGongjinjiGeren(), temp));
            dto.setContainTaxSalary(this.sumDecimal(x -> x.getJiShuiSalary(), temp));
            dto.setTax(this.sumDecimal(x -> x.getCqTax().add(x.getChengduGeshui()), temp));
            dto.setOtherSubsidy(this.sumDecimal(x -> x.getSubsidySalary(), temp));
            dto.setOtherDeductMoney(this.sumDecimal(x -> x.getDeductingSalary(), temp));
            dto.setShouldSalary(this.sumDecimal(x -> x.getJiShuiSalary().subtract(x.getCqTax().add(x.getChengduGeshui())), temp));
            dtos.add(dto);
        }
        return dtos;
    }

    private BigDecimal sumDecimalDto(Function<SalarySummaryDto, BigDecimal> valueProperty, List<SalarySummaryDto> temp) {
        BigDecimal result = BigDecimal.ZERO;
        for (SalarySummaryDto item : temp) {
            result = result.add(valueProperty.apply(item));
        }
        return result;
    }

    private BigDecimal sumDecimal(Function<Employee, BigDecimal> valueProperty, List<Employee> temp) {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee item : temp) {
            result = result.add(valueProperty.apply(item));
        }
        return result;
    }


    private void createSalaryDetailSheet(HSSFWorkbook workBook, HSSFSheet sheet) {
        this.setSheetTitle(workBook, sheet, 29, month + "月份工资明细表");
        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("工号");
        title.createCell(1).setCellValue("部门");
        title.createCell(2).setCellValue("职位");
        title.createCell(3).setCellValue("姓名");
        title.createCell(4).setCellValue("聘用工资");
        title.createCell(5).setCellValue("备注");
        title.createCell(6).setCellValue("基本工资");
        title.createCell(7).setCellValue("岗位工资");
        title.createCell(8).setCellValue("周末加班工资");
        title.createCell(9).setCellValue("请假");
        title.createCell(10).setCellValue("绩效评分");
        title.createCell(11).setCellValue("考评后工资");
        title.createCell(12).setCellValue("请假扣款");
        title.createCell(13).setCellValue("全勤奖");
        title.createCell(14).setCellValue("司龄津贴");
        title.createCell(15).setCellValue("通讯补贴");
        title.createCell(16).setCellValue("工作餐补");
        title.createCell(17).setCellValue("总裁奖系数");
        title.createCell(18).setCellValue("总裁奖");
        title.createCell(19).setCellValue("其他扣款");
        title.createCell(20).setCellValue("税前工资");
        title.createCell(21).setCellValue("社保（公司）");
        title.createCell(22).setCellValue("社保（个人）");
        title.createCell(23).setCellValue("公积金（公司）");
        title.createCell(24).setCellValue("公积金（个人）");
        title.createCell(25).setCellValue("其他补发");
        title.createCell(26).setCellValue("计税工资");
        title.createCell(27).setCellValue("个税");
        title.createCell(28).setCellValue("应发工资");
        title.createCell(29).setCellValue("银行卡号");

        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(data.get(i).getEmpNo());
            row.createCell(1).setCellValue(data.get(i).getDepartment());
            row.createCell(2).setCellValue(data.get(i).getPosition());
            row.createCell(3).setCellValue(data.get(i).getName());
            row.createCell(4).setCellValue(data.get(i).getHireSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(5).setCellValue(data.get(i).getRemarks());
            row.createCell(6).setCellValue(data.get(i).getBaseSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(7).setCellValue(data.get(i).getPositionSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(8).setCellValue(data.get(i).getOvertimeSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(9).setCellValue(data.get(i).getLeavedays());
            row.createCell(10).setCellValue(data.get(i).getScore());
            row.createCell(11).setCellValue(data.get(i).getAssessmentSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(12).setCellValue(data.get(i).getQingjiaKouKuan().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(13).setCellValue(data.get(i).getFullReward().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(14).setCellValue(data.get(i).getAgeReward().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(15).setCellValue(data.get(i).getPhoneReward().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(16).setCellValue(data.get(i).getCanbu().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(17).setCellValue(data.get(i).getRewardRatio());
            row.createCell(18).setCellValue(data.get(i).getZongcaiReward().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(19).setCellValue(data.get(i).getDeductingSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(20).setCellValue(data.get(i).getShuiQianSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(21).setCellValue(data.get(i).getShebaodanweiheji().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(22).setCellValue(data.get(i).getShebaogerenheji().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(23).setCellValue(data.get(i).getGongjinjiGongsi().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(24).setCellValue(data.get(i).getGongjinjiGeren().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(25).setCellValue(data.get(i).getSubsidySalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(26).setCellValue(data.get(i).getJiShuiSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(27).setCellValue(data.get(i).getCqTax().add(data.get(i).getChengduGeshui()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(28).setCellValue(data.get(i).getJiShuiSalary().subtract(data.get(i).getCqTax().add(data.get(i).getChengduGeshui())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(29).setCellValue(data.get(i).getBankCardNum());
        }
        Row sumRow = sheet.createRow(data.size() + 3);
        sumRow.createCell(0).setCellValue("");
        sumRow.createCell(1).setCellValue("合计");
        sumRow.createCell(2).setCellValue("");
        sumRow.createCell(3).setCellValue("");
        sumRow.createCell(4).setCellValue("");
        sumRow.createCell(5).setCellValue("");
        sumRow.createCell(6).setCellValue("");
        sumRow.createCell(7).setCellValue("");
        sumRow.createCell(8).setCellValue("");
        sumRow.createCell(9).setCellValue("");
        sumRow.createCell(10).setCellValue("");
        sumRow.createCell(11).setCellValue(this.getAssessmentSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(12).setCellValue(this.getQingjiaKouKuanTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(13).setCellValue(this.getFullRewardTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(14).setCellValue(this.getAgeRewardTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(15).setCellValue(this.getPhoneRewardTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(16).setCellValue(this.getCanbuTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(17).setCellValue(this.getRewardRatioTotal());
        sumRow.createCell(18).setCellValue(this.getZongcaiRewardTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(19).setCellValue(this.getDeductingSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(20).setCellValue(this.getShuiqianSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(21).setCellValue(this.getShebaoDanweiTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(22).setCellValue(this.getShebaoGerenTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(23).setCellValue(this.getGjjGongsiTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(24).setCellValue(this.getGjjGerenTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(25).setCellValue(this.getSubsidySalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(26).setCellValue(this.getJiShuiSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(27).setCellValue(this.getGeShuiTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(28).setCellValue(this.getShouldSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(29).setCellValue("");

        Row sumInfo = sheet.createRow(data.size() + 6);
        sumInfo.createCell(1).setCellValue("总计");
        sumInfo.createCell(3).setCellValue("应发工资总额");
        sumInfo.createCell(5).setCellValue(this.getShouldSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        for (int i = 2; i < data.size() + 4; i++) {
            for (int j = 0; j < 30; j++) {
                sheet.getRow(i).getCell(j).setCellStyle(baseCellStyle(workBook));
            }
        }

        Row info = sheet.createRow(data.size() + 7);
        info.createCell(5).setCellValue("总计" + month + "月应支出工资费用=应发工资总额" + this.getShouldSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "元");
        CellStyle cellStyle2 = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFColor.RED.index);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        cellStyle2.setFont(font);
        sheet.getRow(data.size() + 7).getCell(5).setCellStyle(cellStyle2);

        Row bottom = sheet.createRow(data.size() + 9);
        bottom.createCell(1).setCellValue("制表人：");
        bottom.createCell(10).setCellValue("财务审核人：");
        bottom.createCell(21).setCellValue("总经理：");

    }

    private BigDecimal getShouldSalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getJiShuiSalary().subtract(emp.getCqTax().add(emp.getChengduGeshui())));
        }
        return result;
    }

    private BigDecimal getJiShuiSalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getJiShuiSalary());
        }
        return result;
    }

    private BigDecimal getSubsidySalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getSubsidySalary());
        }
        return result;
    }

    private BigDecimal getGjjGerenTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getGongjinjiGeren());
        }
        return result;
    }

    private BigDecimal getGjjGongsiTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getGongjinjiGongsi());
        }
        return result;
    }

    private BigDecimal getShebaoGerenTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getShebaogerenheji());
        }
        return result;
    }

    private BigDecimal getShebaoDanweiTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getShebaodanweiheji());
        }
        return result;
    }


    private BigDecimal getShuiqianSalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getShuiQianSalary());
        }
        return result;
    }

    private BigDecimal getDeductingSalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getDeductingSalary());
        }
        return result;
    }

    private BigDecimal getZongcaiRewardTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getZongcaiReward());
        }
        return result;
    }

    private double getRewardRatioTotal() {
        double result = 0.0;
        for (Employee emp : this.data) {
            result = result + emp.getRewardRatio();
        }
        return result;
    }

    private BigDecimal getCanbuTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getCanbu());
        }
        return result;
    }

    private BigDecimal getPhoneRewardTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getPhoneReward());
        }
        return result;
    }

    private BigDecimal getAgeRewardTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getAgeReward());
        }
        return result;
    }

    private BigDecimal getFullRewardTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getFullReward());
        }
        return result;
    }

    private BigDecimal getQingjiaKouKuanTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getQingjiaKouKuan());
        }
        return result;
    }

    private BigDecimal getAssessmentSalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getAssessmentSalary());
        }
        return result;
    }

    private void creategrantDetailSheet(HSSFWorkbook workBook, HSSFSheet sheet) {
        this.setSheetTitle(workBook, sheet, 11, month + "月份工资发放明细表");
        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("部门");
        title.createCell(1).setCellValue("职位");
        title.createCell(2).setCellValue("姓名");
        title.createCell(3).setCellValue("计税工资");
        title.createCell(4).setCellValue("成都发放工资");
        title.createCell(5).setCellValue("成都实发工资");
        title.createCell(6).setCellValue("重庆实发工资");
        title.createCell(7).setCellValue("成都个税");
        title.createCell(8).setCellValue("重庆个税");
        title.createCell(9).setCellValue("扣个税");
        title.createCell(10).setCellValue("实际发放总工资");
        title.createCell(11).setCellValue("发放备注");

        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(data.get(i).getDepartment());
            row.createCell(1).setCellValue(data.get(i).getPosition());
            row.createCell(2).setCellValue(data.get(i).getName());
            row.createCell(3).setCellValue(data.get(i).getJiShuiSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());//计税工资
            row.createCell(4).setCellValue(data.get(i).getChengduSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(5).setCellValue(data.get(i).getActualChengduSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(6).setCellValue(data.get(i).getActualChongQingSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(7).setCellValue(data.get(i).getChengduGeshui().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(8).setCellValue(data.get(i).getCqTax().setScale(2, BigDecimal.ROUND_HALF_UP).toString());//重庆个税
            row.createCell(9).setCellValue(data.get(i).getChengduGeshui().add(data.get(i).getCqTax()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(10).setCellValue(data.get(i).getActualChengduSalary().add(data.get(i).getActualChongQingSalary()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(11).setCellValue(data.get(i).getRemarks());
        }

        Row sumRow = sheet.createRow(data.size() + 3);
        sumRow.createCell(0).setCellValue("");
        sumRow.createCell(1).setCellValue("");
        sumRow.createCell(2).setCellValue("");
        sumRow.createCell(3).setCellValue(this.getJishuiTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(4).setCellValue(this.getChengduSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(5).setCellValue(this.getActualChengduSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(6).setCellValue(this.getActualChongQingSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(7).setCellValue(this.getChengduGeShuiTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(8).setCellValue(this.getChongqingGeShuiTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(9).setCellValue(this.getGeShuiTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(10).setCellValue(this.getActualSalaryTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        sumRow.createCell(11).setCellValue("");

        for (int i = 2; i < data.size() + 4; i++) {
            for (int j = 0; j < 12; j++) {
                sheet.getRow(i).getCell(j).setCellStyle(baseCellStyle(workBook));
            }
        }

        Row bottom = sheet.createRow(data.size() + 6);
        bottom.createCell(0).setCellValue("制表人：");
        bottom.createCell(4).setCellValue("财务审核人：");
        bottom.createCell(7).setCellValue("总经理：");

    }

    private BigDecimal getJishuiTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getJiShuiSalary());
        }
        return result;
    }

    private BigDecimal getActualChengduSalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getActualChengduSalary());
        }
        return result;
    }

    private BigDecimal getChengduSalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getChengduSalary());
        }
        return result;
    }

    private BigDecimal getActualChongQingSalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getActualChongQingSalary());
        }
        return result;
    }

    private BigDecimal getChengduGeShuiTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getChengduGeshui());
        }
        return result;
    }

    private BigDecimal getChongqingGeShuiTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getCqTax());
        }
        return result;
    }

    private BigDecimal getGeShuiTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getCqTax().add(emp.getChengduGeshui()));
        }
        return result;
    }

    private BigDecimal getActualSalaryTotal() {
        BigDecimal result = BigDecimal.ZERO;
        for (Employee emp : this.data) {
            result = result.add(emp.getActualChengduSalary().add(emp.getActualChongQingSalary()));
        }
        return result;
    }

    private void createLeaveDetailSheet(HSSFWorkbook workBook, HSSFSheet sheet) {
        this.setSheetTitle(workBook, sheet, 17, "员工" + month + "月请假出差统计表");
        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("部门");
        title.createCell(1).setCellValue("职位");
        title.createCell(2).setCellValue("姓名");
        title.createCell(3).setCellValue("本月应出勤天数");
        title.createCell(4).setCellValue("本月实际出勤天数");
        title.createCell(5).setCellValue("法定假日");
        title.createCell(6).setCellValue("旷工/天");
        title.createCell(7).setCellValue("事假/天");
        title.createCell(8).setCellValue("病假/天");
        title.createCell(9).setCellValue("调休/天");
        title.createCell(10).setCellValue("婚假/天");
        title.createCell(11).setCellValue("丧假/天");
        title.createCell(12).setCellValue("产（陪）假/天");
        title.createCell(13).setCellValue("工伤假/天");
        title.createCell(14).setCellValue("年休假/天");
        title.createCell(15).setCellValue("加班/天");
        title.createCell(16).setCellValue("出差");
        title.createCell(17).setCellValue("请假出差备注");

        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(data.get(i).getDepartment());
            row.createCell(1).setCellValue(data.get(i).getPosition());
            row.createCell(2).setCellValue(data.get(i).getName());
            row.createCell(3).setCellValue(data.get(i).getAttendanceDays());
            row.createCell(4).setCellValue(data.get(i).getActualChuqinDays());
            row.createCell(5).setCellValue(data.get(i).getLegalHolidays());
            row.createCell(6).setCellValue(data.get(i).getNeglectDays());
            row.createCell(7).setCellValue(data.get(i).getShijia());
            row.createCell(8).setCellValue(data.get(i).getBingjia());
            row.createCell(9).setCellValue(data.get(i).getTiaoxiu());
            row.createCell(10).setCellValue(data.get(i).getHunjia());
            row.createCell(11).setCellValue(data.get(i).getSangjia());
            row.createCell(12).setCellValue(data.get(i).getChanjia());
            row.createCell(13).setCellValue(data.get(i).getGongshangjia());
            row.createCell(14).setCellValue(data.get(i).getNianjia());
            row.createCell(15).setCellValue(data.get(i).getJiaban());
            row.createCell(16).setCellValue(data.get(i).getChuchai());
            row.createCell(17).setCellValue(data.get(i).getChuchaiRemarks());
        }

        for (int i = 2; i < data.size() + 3; i++) {
            for (int j = 0; j < 18; j++) {
                sheet.getRow(i).getCell(j).setCellStyle(baseCellStyle(workBook));
            }
        }

    }

    private void createMealAllowanceSheet(HSSFWorkbook workBook, HSSFSheet sheet) {
        this.setSheetTitle(workBook, sheet, 8, month + "月餐补明细表");

        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("序号");
        title.createCell(1).setCellValue("部门");
        title.createCell(2).setCellValue("职位");
        title.createCell(3).setCellValue("姓名");
        title.createCell(4).setCellValue("应出勤天数");
        title.createCell(5).setCellValue("实际出勤天数");
        title.createCell(6).setCellValue("餐补天数");
        title.createCell(7).setCellValue("补助金额");
        title.createCell(8).setCellValue("备注");

        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue(data.get(i).getDepartment());
            row.createCell(2).setCellValue(data.get(i).getPosition());
            row.createCell(3).setCellValue(data.get(i).getName());
            row.createCell(4).setCellValue(data.get(i).getAttendanceDays());
            row.createCell(5).setCellValue(data.get(i).getActualChuqinDays());//实际出勤天数
            row.createCell(6).setCellValue(Math.floor(data.get(i).getActualChuqinDays()));//实际出勤天数去掉小数点
            row.createCell(7).setCellValue(data.get(i).getCanbu().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(8).setCellValue("");//备注
        }

        for (int i = 2; i < data.size() + 3; i++) {
            for (int j = 0; j < 9; j++) {
                sheet.getRow(i).getCell(j).setCellStyle(baseCellStyle(workBook));
            }
        }
    }

    private void createDeductSheet(HSSFWorkbook workBook, HSSFSheet sheet) {
        this.setSheetTitle(workBook, sheet, 4, month + "月份工资其它扣款明细表");

        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("部门");
        title.createCell(1).setCellValue("姓名");
        title.createCell(2).setCellValue("扣款项目");
        title.createCell(3).setCellValue("金额（元）");
        title.createCell(4).setCellValue("备注");

        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(data.get(i).getDepartment());
            row.createCell(1).setCellValue(data.get(i).getName());
            row.createCell(2).setCellValue(data.get(i).getDeductingProject());
            row.createCell(3).setCellValue(data.get(i).getDeductingSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(4).setCellValue(data.get(i).getDeductingRemarks());
        }

        for (int i = 2; i < data.size() + 3; i++) {
            for (int j = 0; j < 5; j++) {
                sheet.getRow(i).getCell(j).setCellStyle(baseCellStyle(workBook));
            }
        }
    }

    private void createOtherAllowanceSheet(HSSFWorkbook workBook, HSSFSheet sheet) {
        sheet.setHorizontallyCenter(true);
        this.setSheetTitle(workBook, sheet, 4, month + "月份工资其它补助明细表");

        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("部门");
        title.createCell(1).setCellValue("姓名");
        title.createCell(2).setCellValue("补贴项目");
        title.createCell(3).setCellValue("金额（元）");
        title.createCell(4).setCellValue("备注");

        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(data.get(i).getDepartment());
            row.createCell(1).setCellValue(data.get(i).getName());
            row.createCell(2).setCellValue(data.get(i).getSubsidyProject());
            row.createCell(3).setCellValue(data.get(i).getSubsidySalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(4).setCellValue(data.get(i).getSubsidyRemarks());
        }

        for (int i = 2; i < data.size() + 3; i++) {
            for (int j = 0; j < 5; j++) {
                sheet.getRow(i).getCell(j).setCellStyle(baseCellStyle(workBook));
            }
        }

    }

    private void createPayRollSheet(HSSFWorkbook workBook, HSSFSheet sheet) {
        Row title = sheet.createRow(0);
        title.createCell(0).setCellValue("员工姓名");
        title.createCell(1).setCellValue("员工工号");
        title.createCell(2).setCellValue("年");
        title.createCell(3).setCellValue("月");
        title.createCell(4).setCellValue("应发工资");
        title.createCell(5).setCellValue("备注");
        title.createCell(6).setCellValue("基本工资");
        title.createCell(7).setCellValue("岗位工资");
        title.createCell(8).setCellValue("周末加班工资");
        title.createCell(9).setCellValue("请假");
        title.createCell(10).setCellValue("绩效评分");
        title.createCell(11).setCellValue("考评后工资");
        title.createCell(12).setCellValue("请假扣款");
        title.createCell(13).setCellValue("全勤奖");
        title.createCell(14).setCellValue("司龄津贴");
        title.createCell(15).setCellValue("通讯补贴");
        title.createCell(16).setCellValue("工作餐补");
        title.createCell(17).setCellValue("总裁奖系数");
        title.createCell(18).setCellValue("总裁奖");
        title.createCell(19).setCellValue("其他扣款");
        title.createCell(20).setCellValue("税前工资");
        title.createCell(21).setCellValue("社保（公司）");
        title.createCell(22).setCellValue("社保（个人）");
        title.createCell(23).setCellValue("公积金（公司）");
        title.createCell(24).setCellValue("公积金（个人）");
        title.createCell(25).setCellValue("其他补发");
        title.createCell(26).setCellValue("计税工资");
        title.createCell(27).setCellValue("个税");
        title.createCell(28).setCellValue("应发工资");

        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(data.get(i).getName());
            row.createCell(1).setCellValue(data.get(i).getEmpNo());
            row.createCell(2).setCellValue(year);
            row.createCell(3).setCellValue(month);
            row.createCell(4).setCellValue(data.get(i).getJiShuiSalary().subtract(data.get(i).getCqTax().add(data.get(i).getChengduGeshui())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(5).setCellValue(data.get(i).getRemarks());
            row.createCell(6).setCellValue(data.get(i).getBaseSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(7).setCellValue(data.get(i).getPositionSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(8).setCellValue(data.get(i).getOvertimeSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(9).setCellValue(data.get(i).getLeavedays());
            row.createCell(10).setCellValue(data.get(i).getScore());
            row.createCell(11).setCellValue(data.get(i).getAssessmentSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(12).setCellValue(data.get(i).getQingjiaKouKuan().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(13).setCellValue(data.get(i).getFullReward().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(14).setCellValue(data.get(i).getAgeReward().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(15).setCellValue(data.get(i).getPhoneReward().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(16).setCellValue(data.get(i).getCanbu().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(17).setCellValue(data.get(i).getRewardRatio());
            row.createCell(18).setCellValue(data.get(i).getZongcaiReward().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(19).setCellValue(data.get(i).getDeductingSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(20).setCellValue(data.get(i).getShuiQianSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(21).setCellValue(data.get(i).getShebaodanweiheji().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(22).setCellValue(data.get(i).getShebaogerenheji().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(23).setCellValue(data.get(i).getGongjinjiGongsi().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(24).setCellValue(data.get(i).getGongjinjiGeren().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(25).setCellValue(data.get(i).getSubsidySalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(26).setCellValue(data.get(i).getJiShuiSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(27).setCellValue(data.get(i).getCqTax().add(data.get(i).getChengduGeshui()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            row.createCell(28).setCellValue(data.get(i).getJiShuiSalary().subtract(data.get(i).getCqTax().add(data.get(i).getChengduGeshui())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }

        for (int i = 0; i < data.size() + 1; i++) {
            for (int j = 0; j < 29; j++) {
                sheet.getRow(i).getCell(j).setCellStyle(baseCellStyle(workBook));
            }
        }
    }

    private void setSheetTitle(HSSFWorkbook workBook, HSSFSheet sheet, int lastCol, String title) {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, lastCol));

        Row titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue(title);//month + "月餐补明细表"
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        CellStyle cellStyle1 = workBook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyle1.setFont(font);
        sheet.getRow(0).getCell(0).setCellStyle(cellStyle1);

        Row dateRow = sheet.createRow(1);
        dateRow.createCell(0).setCellValue("填报日期：" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
        CellStyle cellStyle2 = workBook.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.RIGHT);
        sheet.getRow(1).getCell(0).setCellStyle(cellStyle2);
    }
}
