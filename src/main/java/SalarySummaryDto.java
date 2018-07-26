import java.math.BigDecimal;

/**
 * created by yezi on 2018/7/25
 */
public class SalarySummaryDto {

    private String department;
    private BigDecimal beforeTaxSalary;//税前工资
    private BigDecimal personSocial;//社保（单位）
    private BigDecimal companySocial;//社保（个人）
    private BigDecimal companyGGJ;//公积金（单位）
    private BigDecimal personGGJ;//公积金（个人）
    private BigDecimal containTaxSalary;//计税工资
    private BigDecimal tax;//扣个税
    private BigDecimal otherSubsidy;//其他补发
    private BigDecimal otherDeductMoney;//其他扣款
    private BigDecimal shouldSalary;//应发工资

    public SalarySummaryDto() {
    }

    public SalarySummaryDto(String department, BigDecimal beforeTaxSalary, BigDecimal personSocial, BigDecimal companySocial, BigDecimal companyGGJ, BigDecimal personGGJ, BigDecimal containTaxSalary, BigDecimal tax, BigDecimal otherSubsidy, BigDecimal otherDeductMoney, BigDecimal shouldSalary) {
        this.department = department;
        this.beforeTaxSalary = beforeTaxSalary;
        this.personSocial = personSocial;
        this.companySocial = companySocial;
        this.companyGGJ = companyGGJ;
        this.personGGJ = personGGJ;
        this.containTaxSalary = containTaxSalary;
        this.tax = tax;
        this.otherSubsidy = otherSubsidy;
        this.otherDeductMoney = otherDeductMoney;
        this.shouldSalary = shouldSalary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getBeforeTaxSalary() {
        return beforeTaxSalary;
    }

    public void setBeforeTaxSalary(BigDecimal beforeTaxSalary) {
        this.beforeTaxSalary = beforeTaxSalary;
    }

    public BigDecimal getPersonSocial() {
        return personSocial;
    }

    public void setPersonSocial(BigDecimal personSocial) {
        this.personSocial = personSocial;
    }

    public BigDecimal getCompanySocial() {
        return companySocial;
    }

    public void setCompanySocial(BigDecimal companySocial) {
        this.companySocial = companySocial;
    }

    public BigDecimal getCompanyGGJ() {
        return companyGGJ;
    }

    public void setCompanyGGJ(BigDecimal companyGGJ) {
        this.companyGGJ = companyGGJ;
    }

    public BigDecimal getPersonGGJ() {
        return personGGJ;
    }

    public void setPersonGGJ(BigDecimal personGGJ) {
        this.personGGJ = personGGJ;
    }

    public BigDecimal getContainTaxSalary() {
        return containTaxSalary;
    }

    public void setContainTaxSalary(BigDecimal containTaxSalary) {
        this.containTaxSalary = containTaxSalary;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getOtherSubsidy() {
        return otherSubsidy;
    }

    public void setOtherSubsidy(BigDecimal otherSubsidy) {
        this.otherSubsidy = otherSubsidy;
    }

    public BigDecimal getOtherDeductMoney() {
        return otherDeductMoney;
    }

    public void setOtherDeductMoney(BigDecimal otherDeductMoney) {
        this.otherDeductMoney = otherDeductMoney;
    }

    public BigDecimal getShouldSalary() {
        return shouldSalary;
    }

    public void setShouldSalary(BigDecimal shouldSalary) {
        this.shouldSalary = shouldSalary;
    }
}
