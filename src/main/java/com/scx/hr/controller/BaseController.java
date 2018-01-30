package com.scx.hr.controller;

import com.scx.system.entity.Company;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 工作台
 */
@Controller
@RequestMapping("/baseController")
public class BaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private SystemService systemService;

    /**
     * 工作台
     */
    @RequestMapping(params = "workbench")
    public String workbench(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        TSUser sessionUser = ResourceUtil.getSessionUser();
        String companyid = sessionUser.getCompanyid();
        //总人数
        String userCountSql = "SELECT COUNT(id) FROM hr_user u WHERE u.company_id = ? AND u.delete_flag = ? ";
        Long userCount = systemService.getCountForJdbcParam(userCountSql, new Object[]{companyid, 0});
        modelMap.put("userCount", userCount);

        //男女比例
        String sexSql = "SELECT COUNT(id) COUNT FROM hr_user u WHERE u.company_id = ? AND u.delete_flag = ? AND u.sex = ? ";
        Long maleCount = systemService.getCountForJdbcParam(sexSql, new Object[]{companyid, 0, 0});
        Long femaleCount = systemService.getCountForJdbcParam(sexSql, new Object[]{companyid, 0, 1});
        modelMap.put("male", maleCount);//男性
        modelMap.put("female", femaleCount);//女性

        //最近10天转正
        String formalSql = "SELECT GROUP_CONCAT(NAME) NAME, COUNT(*) COUNT " +
                " FROM hr_user u " +
                " WHERE u.company_id = ? AND u.delete_flag = ? " +
                " AND DATEDIFF(CAST(CONCAT(YEAR(NOW()), DATE_FORMAT(formal_date, '-%m-%d')) AS DATE), CAST(DATE_FORMAT(NOW(), '%y-%m-%d') AS DATE)) BETWEEN 0 AND 10 " +
                " OR DATEDIFF(CAST(CONCAT(YEAR(NOW()) + 1, DATE_FORMAT(formal_date, '-%m-%d')) AS DATE), CAST(DATE_FORMAT(NOW(), '%y-%m-%d') AS DATE)) BETWEEN 0 AND 10 ";
        Map<String, Object> formal = systemService.findOneForJdbc(formalSql, companyid, 0);
        modelMap.put("formalName", formal.get("NAME"));
        modelMap.put("formalCount", formal.get("COUNT"));

        //最近10天合同到期
        String treatySql = "SELECT GROUP_CONCAT(NAME) AS NAME, COUNT(*) AS COUNT " +
                " FROM (SELECT u.name, MAX(t.treaty_end) AS maxdate " +
                "   FROM hr_user_treaty t LEFT JOIN hr_user u ON t.user_id = u.id " +
                "   WHERE t.company_id = ? AND t.delete_flag = ? GROUP BY t.user_id " +
                "   ) a " +
                "  WHERE DATEDIFF(CAST(CONCAT(YEAR(NOW()), DATE_FORMAT(maxdate, '-%m-%d')) AS DATE), CAST(DATE_FORMAT(NOW(), '%y-%m-%d') AS DATE)) BETWEEN 0 AND 10 " +
                "  OR DATEDIFF(CAST(CONCAT(YEAR(NOW()) + 1, DATE_FORMAT(maxdate, '-%m-%d')) AS DATE), CAST(DATE_FORMAT(NOW(), '%y-%m-%d') AS DATE)) BETWEEN 0 AND 10";
        Map<String, Object> treaty = systemService.findOneForJdbc(treatySql, companyid, 0);
        modelMap.put("treatyName", treaty.get("NAME"));
        modelMap.put("treatyCount", treaty.get("COUNT"));

        //最近7天生日
        String birthdaySql = "SELECT GROUP_CONCAT(NAME) NAME, COUNT(*) COUNT " +
                " FROM hr_user u " +
                " WHERE u.company_id = ? AND u.delete_flag = ? " +
                " AND DATEDIFF(CAST(CONCAT(YEAR(NOW()), DATE_FORMAT(birthday, '-%m-%d')) AS DATE), CAST(DATE_FORMAT(NOW(), '%y-%m-%d') AS DATE)) BETWEEN 0 AND 7 " +
                " OR DATEDIFF(CAST(CONCAT(YEAR(NOW()) + 1, DATE_FORMAT(birthday, '-%m-%d')) AS DATE), CAST(DATE_FORMAT(NOW(), '%y-%m-%d') AS DATE)) BETWEEN 0 AND 7";
        Map<String, Object> birthday = systemService.findOneForJdbc(birthdaySql, companyid, 0);
        modelMap.put("birthdayName", birthday.get("NAME"));
        modelMap.put("birthdayCount", birthday.get("COUNT"));

        //未参保人数
        String noSocialSql = "SELECT GROUP_CONCAT(u.name) AS NAME, COUNT(u.name) AS COUNT " +
                "FROM hr_user_social s LEFT JOIN hr_user u ON u.id = s.user_id " +
                "WHERE u.company_id = ? " +
                " AND u.delete_flag = ? " +
                " AND s.social_start IS NULL";
        Map<String, Object> noSocial = systemService.findOneForJdbc(noSocialSql, companyid, 0);
        modelMap.put("noSocialName", noSocial.get("NAME"));
        modelMap.put("noSocialCount", noSocial.get("COUNT"));

        //未签订合同人数
        String noTreatySql = "SELECT GROUP_CONCAT(u.name) AS NAME, COUNT(u.name) AS COUNT " +
                "FROM hr_user u LEFT JOIN (SELECT user_id " +
                " FROM hr_user_treaty " +
                " GROUP BY user_id " +
                " ) t ON u.id = t.user_id " +
                "WHERE u.company_id = ? " +
                " AND u.delete_flag = ? " +
                " AND t.user_id IS NULL";
        Map<String, Object> noTreaty = systemService.findOneForJdbc(noTreatySql, companyid, 0);
        modelMap.put("noTreatyName", noTreaty.get("NAME"));
        modelMap.put("noTreatyCount", noTreaty.get("COUNT"));

        //未设置薪酬

        //岗位人数

        //部门人数

        return "hr/workbench/workbench";
    }

    /**
     * 账号信息
     */
    @RequestMapping(params = "companyInfo")
    public String companyInfo(ModelMap modelMap) {
        TSUser user = ResourceUtil.getSessionUser();
        Company company = systemService.get(Company.class, user.getCompanyid());
        modelMap.put("company", company);
        return "hr/company/company";
    }
}