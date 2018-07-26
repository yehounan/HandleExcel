import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * created by yezi on 2018/7/23
 */
public class ReadExcel {


    public List<Employee> readExcel(File file) {
        try {
            List<Employee> employees = new ArrayList();
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);
            for (int i = 1; i < sheet.getRows(); i++) {
                List innerList = new ArrayList();
                for (int j = 0; j < sheet.getColumns(); j++) {
                    String cellinfo = sheet.getCell(j, i).getContents();
                    innerList.add(cellinfo);
                }
                employees.add(converterEmployee(innerList));
                innerList.clear();
            }
            System.out.println("数据模板读取成功");
            return employees;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (BiffException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Employee converterEmployee(List l) {
        return new Employee(l.get(0).toString(), l.get(1).toString(), l.get(2).toString(), l.get(3).toString(), l.get(4).toString(),
                BigDecimal.valueOf(Double.valueOf(l.get(5).toString())), BigDecimal.valueOf(Double.valueOf(l.get(6).toString())), BigDecimal.valueOf(Double.valueOf(l.get(7).toString())), BigDecimal.valueOf(Double.valueOf(l.get(8).toString())), BigDecimal.valueOf(Double.valueOf(l.get(9).toString())),
                Float.valueOf(l.get(10).toString()),
                Double.valueOf(l.get(11).toString()), Double.valueOf(l.get(12).toString()),
                BigDecimal.valueOf(Double.valueOf(l.get(13).toString())),
                Double.valueOf(l.get(14).toString()), Double.valueOf(l.get(15).toString()), Double.valueOf(l.get(16).toString()), Double.valueOf(l.get(17).toString()), Double.valueOf(l.get(18).toString()), Double.valueOf(l.get(19).toString()), Double.valueOf(l.get(20).toString()), Double.valueOf(l.get(21).toString()), Double.valueOf(l.get(22).toString()), Double.valueOf(l.get(23).toString()), Double.valueOf(l.get(24).toString()), Double.valueOf(l.get(25).toString()), Double.valueOf(l.get(26).toString()),
                l.get(27).toString(),
                l.get(28).toString(), BigDecimal.valueOf(Double.valueOf(l.get(29).toString())), l.get(30).toString(), l.get(31).toString(), BigDecimal.valueOf(Double.valueOf(l.get(32).toString())), l.get(33).toString(),
                BigDecimal.valueOf(Double.valueOf(l.get(34).toString())), BigDecimal.valueOf(Double.valueOf(l.get(35).toString())),
                l.get(36).toString(),
                l.get(37).toString(),
                BigDecimal.valueOf(Double.valueOf(l.get(38).toString())),BigDecimal.valueOf(Double.valueOf(l.get(39).toString())),BigDecimal.valueOf(Double.valueOf(l.get(40).toString())),BigDecimal.valueOf(Double.valueOf(l.get(41).toString())),BigDecimal.valueOf(Double.valueOf(l.get(42).toString())));
    }
}
