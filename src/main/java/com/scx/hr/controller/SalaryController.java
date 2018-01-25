package com.scx.hr.controller;

import com.scx.hr.entity.HRSalary;
import com.scx.hr.entity.HRUser;
import com.scx.hr.entity.HRUserOrg;
import com.scx.hr.entity.HRUserSalary;
import com.scx.system.entity.Company;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.*;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/salaryController")
public class SalaryController {

    private static final Logger log = LoggerFactory.getLogger(SalaryController.class);

    @Autowired
    private SystemService systemService;

    @RequestMapping(params = "typeList")
    public String typeList() {
        return "hr/salary/salaryTypeList";
    }

    /**
     * easyuiAJAX请求数据
     */
    @RequestMapping(params = "salaryTypeGrid")
    public void salaryTypeGrid(HRSalary salary, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(HRSalary.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, salary);
        TSUser sessionUser = ResourceUtil.getSessionUser();
        cq.isNull("parent");
        cq.eq("companyId", sessionUser.getCompanyid());
//        cq.or(Restrictions.eq("companyId", sessionUser.getCompanyid()), Restrictions.eq("isSys", "Y"));
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除类型
     */
    @RequestMapping(params = "delType")
    @ResponseBody
    public AjaxJson delType(HRSalary salary, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        salary = systemService.getEntity(HRSalary.class, salary.getId());
        message = "薪酬类型: " + salary.getName() + " 被删除成功";
        if (ListUtils.isNullOrEmpty(salary.getChilds())) {
            systemService.delete(salary);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } else {
            message = "薪酬类型: " + salary.getName() + " 下有薪酬信息，不能删除！";
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 薪酬类型新增、编辑
     */
    @RequestMapping(params = "addOrUpdateType")
    public String addOrUpdateType(ModelMap map, HRSalary salary) {
        if (StringUtil.isNotEmpty(salary.getId())) {
            salary = systemService.get(HRSalary.class, salary.getId());
            map.put("salary", salary);
        }
        return "hr/salary/salaryType";
    }

    /**
     * 跳转到薪酬页面
     */
    @RequestMapping(params = "goSalary")
    public ModelAndView goSalary(HttpServletRequest request) {
        String parentId = request.getParameter("parentId");
        request.setAttribute("parentId", parentId);
        return new ModelAndView("hr/salary/salaryList");
    }

    @RequestMapping(params = "salaryGrid")
    public void salaryGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        String parentId = request.getParameter("parentId");
        CriteriaQuery cq = new CriteriaQuery(HRSalary.class, dataGrid);
        cq.eq("parent.id", parentId);
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 薪酬新增、编辑
     */
    @RequestMapping(params = "addOrUpdate")
    public ModelAndView addOrUpdate(HRSalary salary, HttpServletRequest req) {
        String parentId = req.getParameter("parentId");
        HRSalary parentSalary = systemService.get(HRSalary.class, parentId);
        req.setAttribute("parentId", parentId);
        req.setAttribute("parentName", parentSalary.getName());
        req.setAttribute("code", parentSalary.getCode());
        if (StringUtil.isNotEmpty(salary.getId())) {
            salary = systemService.getEntity(HRSalary.class, salary.getId());
            req.setAttribute("salary", salary);
        }
        return new ModelAndView("hr/salary/salary");
    }

    /**
     * 删除薪酬
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(HRSalary salary, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();

        //删除员工的该工资信息
        systemService.executeSql("delete from hr_user_salary where salary_id = ? ", salary.getId());

        //更新员工薪资信息
        TSUser sessionUser = ResourceUtil.getSessionUser();
        String sql = "SELECT s.user_id, s.salary_code, SUM(s.value) AS value " +
                "FROM hr_user_salary s,hr_user u " +
                "WHERE u.company_id = ? AND u.id = s.user_id " +
                "AND s.salary_code IN ('01', '02') " +
                "GROUP BY s.user_id, s.salary_code";
        List<Map<String, Object>> salaryList = systemService.findForJdbc(sql, sessionUser.getCompanyid());
        for (Map<String, Object> map : salaryList) {
            if ("01".equals(map.get("salary_code"))) {
                systemService.executeSql("update hr_user set fix_salary = ? where id = ? ", map.get("value"), map.get("user_id"));
            } else if ("02".equals(map.get("salary_code"))) {
                systemService.executeSql("update hr_user set reward_salary = ? where id = ? ", map.get("value"), map.get("user_id"));
            }
        }

        salary = systemService.getEntity(HRSalary.class, salary.getId());
        systemService.delete(salary);
        message = "薪酬: " + salary.getName() + " 删除成功";
        systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        j.setMsg(message);
        return j;
    }

    /**
     * 保存薪酬
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(HRSalary salary) {
        String message;
        AjaxJson j = new AjaxJson();
        if (StringUtil.isNotEmpty(salary.getId())) {
            message = "薪酬: " + salary.getName() + " 更新成功";
            systemService.saveOrUpdate(salary);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            message = "薪酬: " + salary.getName() + " 添加成功";
            salary.setStatus("N");
            TSUser user = ResourceUtil.getSessionUser();
            salary.setCompanyId(user.getCompanyid());
            systemService.save(salary);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 启用、停用
     */
    @RequestMapping(params = "openAndClose")
    @ResponseBody
    public AjaxJson openAndClose(HRSalary salary) {
        String message = "";
        AjaxJson j = new AjaxJson();
        salary = systemService.get(HRSalary.class, salary.getId());
        if ("Y".equals(salary.getStatus())) {
            //删除员工的该工资信息
            systemService.executeSql("delete from hr_user_salary where salary_id = ? ", salary.getId());

            //更新员工薪资信息
            TSUser sessionUser = ResourceUtil.getSessionUser();
            String sql = "SELECT s.user_id, s.salary_code, SUM(s.value) AS value " +
                    "FROM hr_user_salary s,hr_user u " +
                    "WHERE u.company_id = ? AND u.id = s.user_id " +
                    "AND s.salary_code IN ('01', '02') " +
                    "GROUP BY s.user_id, s.salary_code";
            List<Map<String, Object>> salaryList = systemService.findForJdbc(sql, sessionUser.getCompanyid());
            for (Map<String, Object> map : salaryList) {
                if ("01".equals(map.get("salary_code"))) {
                    systemService.executeSql("update hr_user set fix_salary = ? where id = ? ", map.get("value"), map.get("user_id"));
                } else if ("02".equals(map.get("salary_code"))) {
                    systemService.executeSql("update hr_user set reward_salary = ? where id = ? ", map.get("value"), map.get("user_id"));
                }
            }

            salary.setStatus("N");
            message = salary.getName() + "停用成功";
        } else if ("N".equals(salary.getStatus())) {
            salary.setStatus("Y");
            message = salary.getName() + "启用成功";
        }
        systemService.updateEntitie(salary);
        systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        j.setMsg(message);
        return j;
    }

    /**
     * 薪酬信息
     */
    @RequestMapping(params = "userSalary")
    public ModelAndView userSalary(){
        return new ModelAndView("hr/salary/userSalaryList");
    }

    /**
     * 薪酬信息数据
     */
    @RequestMapping(params = "userSalaryData")
    public void userSalaryData(HRUser hrUser, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(HRUser.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, hrUser);
        TSUser sessionUser = ResourceUtil.getSessionUser();
        cq.eq("companyId", sessionUser.getCompanyid());
        cq.eq("deleteFlag", Globals.Delete_Normal);
        cq.add();
        systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 编辑薪酬信息
     */
    @RequestMapping(params = "goUpdateUserSalary")
    public ModelAndView goUpdateUserSalary(HttpServletRequest request, String id) {
        if (StringUtil.isNotEmpty(id)) {
            HRUser user = systemService.get(HRUser.class, id);
            request.setAttribute("user", user);

            String orgName = "";
            List<HRUserOrg> userOrgList = user.getUserOrgList();
            for (HRUserOrg hrUserOrg : userOrgList) {
                orgName += hrUserOrg.getTsDepart().getDepartname() + "，";
            }
            request.setAttribute("orgName", orgName);

            StringBuilder hql = new StringBuilder();
            hql.append(" from HRSalary t");
            hql.append(" where t.parent is null");
            hql.append(" and t.code <> '03'");//代扣代缴排除
            hql.append(" and t.companyId =?");

            List<HRUserSalary> userSalaries = systemService.findByProperty(HRUserSalary.class, "userId", id);

            List<HRSalary> parentList = systemService.findHql(hql.toString(), user.getCompanyId());
            for (int i = 0; i < parentList.size(); i++) {
                List<HRSalary> childs = parentList.get(i).getChilds();
                Iterator<HRSalary> iterator = childs.iterator();
                while (iterator.hasNext()) {
                    HRSalary salary = iterator.next();
                    if ("N".equals(salary.getStatus())) {
                        iterator.remove();
                        continue;
                    }
                    for (HRUserSalary userSalary : userSalaries) {
                        if (userSalary.getSalaryId().equals(salary.getId())) {
                            salary.setValue(userSalary.getValue());
                            salary.setProbation(userSalary.getProbation());
                        }
                    }
                }
            }
            request.setAttribute("parentList", parentList);
        }
        return new ModelAndView("hr/salary/userSalary");
    }

    /**
     * 保存薪酬信息
     */
    @RequestMapping(params = "saveUserSalary")
    @ResponseBody
    public AjaxJson saveUserSalary(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String id = request.getParameter("id");
        systemService.executeSql("delete from hr_user_salary where user_id=?", id);

        HRUser hrUser = systemService.get(HRUser.class, id);
        double fix = 0;//固定工资
        double reward = 0;//奖励工资
        String hql = " from HRSalary t " +
                " where t.parent IS NOT NULL " +
                " and t.status = 'Y' " +
                " and t.code IN('01','02') " +
                " and t.companyId = ? ";
        List<HRSalary> childs = systemService.findHql(hql, hrUser.getCompanyId());

        List<HRUserSalary> salaryList = new ArrayList<HRUserSalary>();
        Map<String, String[]> salaryMap = request.getParameterMap();
        for (Map.Entry<String, String[]> next : salaryMap.entrySet()) {
            if ("id".equals(next.getKey()) || "probationSalary".equals(next.getKey()) ||
                    "saveUserSalary".equals(next.getKey())) {
                continue;
            }
            HRUserSalary userSalary = new HRUserSalary();
            userSalary.setUserId(id);
            userSalary.setSalaryId(next.getKey());
            userSalary.setValue(oConvertUtils.getDouble(next.getValue()[0], 0));
            userSalary.setProbation(oConvertUtils.getDouble(next.getValue()[1], 0));
            userSalary.setDeleteFlag(Globals.Delete_Normal);

            for (HRSalary child : childs) {
                //固定工资
                if ("01".equals(child.getCode()) && userSalary.getSalaryId().equals(child.getId())) {
                    userSalary.setSalaryCode("01");
                    fix += userSalary.getValue();
                    break;
                }
                //奖励工资
                if ("02".equals(child.getCode()) && userSalary.getSalaryId().equals(child.getId())) {
                    userSalary.setSalaryCode("02");
                    reward += userSalary.getValue();
                    break;
                }
            }
            salaryList.add(userSalary);
        }
        if (!salaryList.isEmpty()) {
            systemService.batchSave(salaryList);
            systemService.addLog("编辑薪酬信息", Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }

        //员工表保存工资信息
        hrUser.setProbationSalary(request.getParameter("probationSalary"));
        hrUser.setFixSalary(fix);
        hrUser.setRewardSalary(reward);
        systemService.updateEntitie(hrUser);
        return j;
    }

    /**
     * 计薪规则
     */
    @RequestMapping(params = "setting")
    public ModelAndView setting(HttpServletRequest request) {
        TSUser user = ResourceUtil.getSessionUser();
        Company company = systemService.get(Company.class, user.getCompanyid());
        request.setAttribute("company", company);
        return new ModelAndView("hr/company/salary");
    }

    /**
     * 保存月计薪天数，日计薪小时数
     * 保存请假扣款比例
     * 保存加班设定
     */
    @RequestMapping(params = "saveComSalary", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson saveComSalary(Company company, HttpServletRequest request) {
        String message = "保存成功";
        AjaxJson j = new AjaxJson();
        Company t = systemService.get(Company.class, company.getId());
        try {
            MyBeanUtils.copyBeanNotNull2Bean(company, t);
            systemService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            message = "保存失败";
            e.printStackTrace();
        }
        j.setMsg(message);
        return j;
    }
}