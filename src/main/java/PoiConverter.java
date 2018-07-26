import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

/**
 * created by yezi on 2018/7/23
 */
public class PoiConverter {

    private static final String importPath = "D://yls/";
    private static final String fileName = "易联社工资基本信息模板.xls";
    private static final String outPath = "D://yls/" + Calendar.getInstance().get(Calendar.MONTH) + "月份易联社工资.xls";
    private static List<Employee> employees;

    public static void main(String[] args) {
        File file1 = new File(importPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File file = new File(importPath + fileName);
        if (!file.exists()) {
            System.out.println("位置:【D://yls/】缺少信息模板文件【易联社工资基本信息模板.xls】");
            exit();
            return;
        }
        ReadExcel readExcel = new ReadExcel();
        employees = readExcel.readExcel(file);

        System.out.println("==================================================================");

        WriteExcel writeExcel = new WriteExcel();
        writeExcel.writeExcel(employees, outPath);

        System.out.println("==================================================================");
        exit();

    }

    private static void exit() {
        Scanner inupt = new Scanner(System.in);
        System.out.println("输入【t】按回车结束程序。。。");
        String feedback = inupt.next();
        while (!"t".equals(feedback)) {
            System.out.println("输入【t】按回车结束程序。。。");
            feedback = inupt.next();
        }
    }


}
