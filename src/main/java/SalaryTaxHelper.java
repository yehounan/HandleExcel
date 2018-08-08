import java.text.DecimalFormat;

public class SalaryTaxHelper {

    /**
     * 返回个税
     *
     * @param salaryBeforeTax 计税工资
     * @return
     */
    public static double salaryTax(double salaryBeforeTax) {
        double taxbase = salaryBeforeTax - 3500;
        double Taxrate = 0;//这里税率没有除以百分比；
        double Quickdeduction = 0;
        if (taxbase <= 0)//低于个税起征点
        {
            return 0;
        } else if (taxbase <= 1500) {
            Taxrate = 3;
            Quickdeduction = 0;
        } else if (taxbase <= 4500) {
            Taxrate = 10;
            Quickdeduction = 105;
        } else if (taxbase <= 9000) {
            Taxrate = 20;
            Quickdeduction = 555;
        } else if (taxbase <= 35000) {
            Taxrate = 25;
            Quickdeduction = 1005;
        } else if (taxbase <= 55000) {
            Taxrate = 30;
            Quickdeduction = 2755;
        } else if (taxbase <= 80000) {
            Taxrate = 35;
            Quickdeduction = 5505;
        } else {
            Taxrate = 45;
            Quickdeduction = 13505;
        }
        double tax = (salaryBeforeTax - 3500) * Taxrate / 100 - Quickdeduction;
        return getTwoDecimal(tax);
    }

    public static double getTwoDecimal(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String yearString = dFormat.format(num);
        Double temp = Double.valueOf(yearString);
        return temp;
    }


}