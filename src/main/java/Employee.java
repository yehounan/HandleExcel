import java.math.BigDecimal;

/**
 * created by yezi on 2018/7/23
 */
public class Employee {

    private String empNo;//工号
    private String department;//部门
    private String position;//职位
    private String name;//姓名
    private String remarks;//备注
    private BigDecimal hireSalary = BigDecimal.ZERO;//聘用工资
    private BigDecimal baseSalary = BigDecimal.ZERO;//基本工资
    private BigDecimal positionSalary = BigDecimal.ZERO;//岗位工资
    private BigDecimal overtimeSalary = BigDecimal.ZERO;//加班工资
    private BigDecimal assessmentSalary = BigDecimal.ZERO;//考评工资
    private double leavedays = 0;//请假
    private double score = 0;//绩效评分
    private double rewardRatio = 0;//总裁奖系数
    private BigDecimal cqTax = BigDecimal.ZERO;//重庆个税
    private double attendanceDays = 0;//应出勤天数
    private double legalHolidays = 0;//法定假日
    private double neglectDays = 0;//旷工天数
    private double shijia = 0;
    private double bingjia = 0;
    private double tiaoxiu = 0;
    private double hunjia = 0;
    private double sangjia = 0;
    private double chanjia = 0;
    private double gongshangjia = 0;
    private double nianjia = 0;
    private double jiaban = 0;
    private double chuchai = 0;
    private String chuchaiRemarks;//出差备注
    private String deductingProject;//扣款项目
    private BigDecimal deductingSalary = BigDecimal.ZERO;//扣款金额
    private String deductingRemarks;//扣款备注
    private String subsidyProject;//补贴项目
    private BigDecimal subsidySalary = BigDecimal.ZERO;//补贴金额
    private String subsidyRemarks;//补贴备注
    private BigDecimal shebaodanweiheji = BigDecimal.ZERO;//社保单位合计
    private BigDecimal shebaogerenheji = BigDecimal.ZERO;//社保个人合计
    private String grantRemarks;//发放备注
    private String bankCardNum;//银行卡号
    private BigDecimal fullReward = BigDecimal.ZERO;//全勤奖
    private BigDecimal ageReward = BigDecimal.ZERO;//公司年龄津贴
    private BigDecimal phoneReward = BigDecimal.ZERO;//通讯补贴
    private BigDecimal gongjinjiGongsi = BigDecimal.ZERO;//公积金公司
    private BigDecimal gongjinjiGeren = BigDecimal.ZERO;//公积金个人


    public Employee() {
    }

    public Employee(String empNo, String department, String position, String name, String remarks, BigDecimal hireSalary, BigDecimal baseSalary, BigDecimal positionSalary, BigDecimal overtimeSalary, BigDecimal assessmentSalary, double leavedays, double score, double rewardRatio, BigDecimal cqTax, double attendanceDays, double legalHolidays, double neglectDays, double shijia, double bingjia, double tiaoxiu, double hunjia, double sangjia, double chanjia, double gongshangjia, double nianjia, double jiaban, double chuchai, String chuchaiRemarks, String deductingProject, BigDecimal deductingSalary, String deductingRemarks, String subsidyProject, BigDecimal subsidySalary, String subsidyRemarks, BigDecimal shebaodanweiheji, BigDecimal shebaogerenheji, String grantRemarks, String bankCardNum, BigDecimal fullReward, BigDecimal ageReward, BigDecimal phoneReward, BigDecimal gongjinjiGongsi, BigDecimal gongjinjiGeren) {
        this.empNo = empNo;
        this.department = department;
        this.position = position;
        this.name = name;
        this.remarks = remarks;
        this.hireSalary = hireSalary;
        this.baseSalary = baseSalary;
        this.positionSalary = positionSalary;
        this.overtimeSalary = overtimeSalary;
        this.assessmentSalary = assessmentSalary;
        this.leavedays = leavedays;
        this.score = score;
        this.rewardRatio = rewardRatio;
        this.cqTax = cqTax;
        this.attendanceDays = attendanceDays;
        this.legalHolidays = legalHolidays;
        this.neglectDays = neglectDays;
        this.shijia = shijia;
        this.bingjia = bingjia;
        this.tiaoxiu = tiaoxiu;
        this.hunjia = hunjia;
        this.sangjia = sangjia;
        this.chanjia = chanjia;
        this.gongshangjia = gongshangjia;
        this.nianjia = nianjia;
        this.jiaban = jiaban;
        this.chuchai = chuchai;
        this.chuchaiRemarks = chuchaiRemarks;
        this.deductingProject = deductingProject;
        this.deductingSalary = deductingSalary;
        this.deductingRemarks = deductingRemarks;
        this.subsidyProject = subsidyProject;
        this.subsidySalary = subsidySalary;
        this.subsidyRemarks = subsidyRemarks;
        this.shebaodanweiheji = shebaodanweiheji;
        this.shebaogerenheji = shebaogerenheji;
        this.grantRemarks = grantRemarks;
        this.bankCardNum = bankCardNum;
        this.fullReward = fullReward;
        this.ageReward = ageReward;
        this.phoneReward = phoneReward;
        this.gongjinjiGongsi = gongjinjiGongsi;
        this.gongjinjiGeren = gongjinjiGeren;
    }

    /**
     * 1、请假小于2天，（基本工资/21.75）*请假天数
     * 2、请假天数大于2天，（基本工资/21.75）*2 + （考评后工资/21.75）*（请假天数-2）
     *
     * @return 计算请假扣款
     */
    public BigDecimal getQingjiaKouKuan() {
        if (this.leavedays <= 2) {
            return this.baseSalary.divide(BigDecimal.valueOf(21.75), 2).multiply(BigDecimal.valueOf(this.leavedays));
        } else {
            return this.baseSalary.divide(BigDecimal.valueOf(21.75), 2).multiply(BigDecimal.valueOf(2))
                    .add(this.assessmentSalary.divide(BigDecimal.valueOf(21.75), 2).multiply(BigDecimal.valueOf(this.leavedays - 2)));
        }
    }

    /**
     * 税前工资 = 考评后工资 - 请假扣款 + 全勤奖 + 司龄津贴 + 通讯补贴 + 工作餐补 + 总裁奖 - 其他扣款
     *
     * @return 计算税前工资
     */
    public BigDecimal getShuiQianSalary() {
        return this.assessmentSalary.subtract(this.getQingjiaKouKuan()).add(this.fullReward).add(this.ageReward).add(this.phoneReward).add(this.getCanbu())
                .add(this.getZongcaiReward()).subtract(this.deductingSalary);
    }

    /**
     * 计税工资 = 税前工资 - 社保(个人) - 公积金(个人) + 其他补发
     *
     * @return 计算计税工资
     */
    public BigDecimal getJiShuiSalary() {
        return this.getShuiQianSalary().subtract(this.shebaogerenheji).subtract(this.gongjinjiGeren).add(this.subsidySalary);
    }


    /**
     * 大于等于7000元 成都发放工资为7000，小于7000元按计税工资计算
     *
     * @return 成都发放工资
     */
    public BigDecimal getChengduSalary() {
        if (this.getJiShuiSalary().compareTo(BigDecimal.valueOf(7000)) < 0) {
            return this.getJiShuiSalary();
        } else {
            return BigDecimal.valueOf(7000);
        }
    }

    /**
     * @return 成都实发工资 = 成都发放工资 - 成都个税
     */
    public BigDecimal getActualChengduSalary() {
        return this.getChengduSalary().subtract(this.getChengduGeshui());
    }

    /**
     * @return 重庆实发工资 = 计税工资 - 成都发放工资 - 重庆个税
     */
    public BigDecimal getActualChongQingSalary() {
        return this.getJiShuiSalary().subtract(this.getChengduSalary()).subtract(this.getCqTax());
    }

    /**
     * @return 成都个税
     */
    public BigDecimal getChengduGeshui() {

        return BigDecimal.valueOf(SalaryTaxHelper.salaryTax(this.getChengduSalary().doubleValue()));
    }

    /**
     * < .6  向下取整，否则向上取整
     *
     * @return 计算当月餐补
     */
    public BigDecimal getCanbu() {
        Double days = Math.floor((this.getActualChuqinDays() + 0.4));
        return BigDecimal.valueOf(15).multiply(BigDecimal.valueOf(days));
    }

    /**
     * 总裁奖 = （考评后工资 - 请假扣款 + 全勤奖 + 司龄津贴 + 通讯补贴 + 工作餐补）*总裁奖系数
     *
     * @return 计算当月总裁奖
     */
    public BigDecimal getZongcaiReward() {
        return this.assessmentSalary.subtract(this.getQingjiaKouKuan()).add(this.fullReward).add(this.ageReward).add(this.phoneReward).add(this.getCanbu()).multiply(BigDecimal.valueOf(this.rewardRatio));
    }

    /**
     * @return 实际出勤天数
     */
    public double getActualChuqinDays() {
        return this.attendanceDays - this.legalHolidays - this.shijia - this.bingjia - this.tiaoxiu
                - this.hunjia - this.sangjia - this.chanjia - this.gongshangjia - this.nianjia + this.jiaban;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getHireSalary() {
        return hireSalary;
    }

    public void setHireSalary(BigDecimal hireSalary) {
        this.hireSalary = hireSalary;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getPositionSalary() {
        return positionSalary;
    }

    public void setPositionSalary(BigDecimal positionSalary) {
        this.positionSalary = positionSalary;
    }

    public BigDecimal getOvertimeSalary() {
        return overtimeSalary;
    }

    public void setOvertimeSalary(BigDecimal overtimeSalary) {
        this.overtimeSalary = overtimeSalary;
    }

    public BigDecimal getAssessmentSalary() {
        return assessmentSalary;
    }

    public void setAssessmentSalary(BigDecimal assessmentSalary) {
        this.assessmentSalary = assessmentSalary;
    }

    public double getLeavedays() {
        return leavedays;
    }

    public void setLeavedays(double leavedays) {
        this.leavedays = leavedays;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getRewardRatio() {
        return rewardRatio;
    }

    public void setRewardRatio(double rewardRatio) {
        this.rewardRatio = rewardRatio;
    }

    public BigDecimal getCqTax() {
        return cqTax;
    }

    public void setCqTax(BigDecimal cqTax) {
        this.cqTax = cqTax;
    }

    public double getAttendanceDays() {
        return attendanceDays;
    }

    public void setAttendanceDays(double attendanceDays) {
        this.attendanceDays = attendanceDays;
    }

    public double getLegalHolidays() {
        return legalHolidays;
    }

    public void setLegalHolidays(double legalHolidays) {
        this.legalHolidays = legalHolidays;
    }

    public double getNeglectDays() {
        return neglectDays;
    }

    public void setNeglectDays(double neglectDays) {
        this.neglectDays = neglectDays;
    }

    public double getShijia() {
        return shijia;
    }

    public void setShijia(double shijia) {
        this.shijia = shijia;
    }

    public double getBingjia() {
        return bingjia;
    }

    public void setBingjia(double bingjia) {
        this.bingjia = bingjia;
    }

    public double getTiaoxiu() {
        return tiaoxiu;
    }

    public void setTiaoxiu(double tiaoxiu) {
        this.tiaoxiu = tiaoxiu;
    }

    public double getHunjia() {
        return hunjia;
    }

    public void setHunjia(double hunjia) {
        this.hunjia = hunjia;
    }

    public double getSangjia() {
        return sangjia;
    }

    public void setSangjia(double sangjia) {
        this.sangjia = sangjia;
    }

    public double getChanjia() {
        return chanjia;
    }

    public void setChanjia(double chanjia) {
        this.chanjia = chanjia;
    }

    public double getGongshangjia() {
        return gongshangjia;
    }

    public void setGongshangjia(double gongshangjia) {
        this.gongshangjia = gongshangjia;
    }

    public double getNianjia() {
        return nianjia;
    }

    public void setNianjia(double nianjia) {
        this.nianjia = nianjia;
    }

    public double getJiaban() {
        return jiaban;
    }

    public void setJiaban(double jiaban) {
        this.jiaban = jiaban;
    }

    public double getChuchai() {
        return chuchai;
    }

    public void setChuchai(double chuchai) {
        this.chuchai = chuchai;
    }

    public String getChuchaiRemarks() {
        return chuchaiRemarks;
    }

    public void setChuchaiRemarks(String chuchaiRemarks) {
        this.chuchaiRemarks = chuchaiRemarks;
    }

    public String getDeductingProject() {
        return deductingProject;
    }

    public void setDeductingProject(String deductingProject) {
        this.deductingProject = deductingProject;
    }

    public BigDecimal getDeductingSalary() {
        return deductingSalary;
    }

    public void setDeductingSalary(BigDecimal deductingSalary) {
        this.deductingSalary = deductingSalary;
    }

    public String getDeductingRemarks() {
        return deductingRemarks;
    }

    public void setDeductingRemarks(String deductingRemarks) {
        this.deductingRemarks = deductingRemarks;
    }

    public BigDecimal getSubsidySalary() {
        return subsidySalary;
    }

    public void setSubsidySalary(BigDecimal subsidySalary) {
        this.subsidySalary = subsidySalary;
    }

    public String getSubsidyProject() {
        return subsidyProject;
    }

    public void setSubsidyProject(String subsidyProject) {
        this.subsidyProject = subsidyProject;
    }

    public String getSubsidyRemarks() {
        return subsidyRemarks;
    }

    public void setSubsidyRemarks(String subsidyRemarks) {
        this.subsidyRemarks = subsidyRemarks;
    }

    public BigDecimal getShebaodanweiheji() {
        return shebaodanweiheji;
    }

    public void setShebaodanweiheji(BigDecimal shebaodanweiheji) {
        this.shebaodanweiheji = shebaodanweiheji;
    }

    public BigDecimal getShebaogerenheji() {
        return shebaogerenheji;
    }

    public void setShebaogerenheji(BigDecimal shebaogerenheji) {
        this.shebaogerenheji = shebaogerenheji;
    }

    public String getGrantRemarks() {
        return grantRemarks;
    }

    public void setGrantRemarks(String grantRemarks) {
        this.grantRemarks = grantRemarks;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public BigDecimal getFullReward() {
        return fullReward;
    }

    public void setFullReward(BigDecimal fullReward) {
        this.fullReward = fullReward;
    }

    public BigDecimal getAgeReward() {
        return ageReward;
    }

    public void setAgeReward(BigDecimal ageReward) {
        this.ageReward = ageReward;
    }

    public BigDecimal getPhoneReward() {
        return phoneReward;
    }

    public void setPhoneReward(BigDecimal phoneReward) {
        this.phoneReward = phoneReward;
    }

    public BigDecimal getGongjinjiGongsi() {
        return gongjinjiGongsi;
    }

    public void setGongjinjiGongsi(BigDecimal gongjinjiGongsi) {
        this.gongjinjiGongsi = gongjinjiGongsi;
    }

    public BigDecimal getGongjinjiGeren() {
        return gongjinjiGeren;
    }

    public void setGongjinjiGeren(BigDecimal gongjinjiGeren) {
        this.gongjinjiGeren = gongjinjiGeren;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empNo='" + empNo + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", name='" + name + '\'' +
                ", remarks='" + remarks + '\'' +
                ", hireSalary=" + hireSalary +
                ", baseSalary=" + baseSalary +
                ", positionSalary=" + positionSalary +
                ", overtimeSalary=" + overtimeSalary +
                ", assessmentSalary=" + assessmentSalary +
                ", leavedays=" + leavedays +
                ", score=" + score +
                ", rewardRatio=" + rewardRatio +
                ", cqTax=" + cqTax +
                ", attendanceDays=" + attendanceDays +
                ", legalHolidays=" + legalHolidays +
                ", neglectDays=" + neglectDays +
                ", shijia=" + shijia +
                ", bingjia=" + bingjia +
                ", tiaoxiu=" + tiaoxiu +
                ", hunjia=" + hunjia +
                ", sangjia=" + sangjia +
                ", chanjia=" + chanjia +
                ", gongshangjia=" + gongshangjia +
                ", nianjia=" + nianjia +
                ", jiaban=" + jiaban +
                ", chuchai=" + chuchai +
                ", chuchaiRemarks='" + chuchaiRemarks + '\'' +
                ", deductingProject='" + deductingProject + '\'' +
                ", deductingSalary=" + deductingSalary +
                ", deductingRemarks='" + deductingRemarks + '\'' +
                ", subsidyProject='" + subsidyProject + '\'' +
                ", subsidySalary=" + subsidySalary +
                ", subsidyRemarks='" + subsidyRemarks + '\'' +
                ", shebaodanweiheji=" + shebaodanweiheji +
                ", shebaogerenheji=" + shebaogerenheji +
                ", grantRemarks='" + grantRemarks + '\'' +
                ", bankCardNum='" + bankCardNum + '\'' +
                '}';
    }
}
