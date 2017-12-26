package com.scx.hr.controller;

import com.scx.hr.entity.HRSalary;
import com.scx.hr.entity.HRUser;
import com.scx.hr.entity.HRUserOrg;
import com.scx.hr.entity.HRUserSalary;
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

    @RequestMapping(params = "setting")
    public ModelAndView setting() {
        return new ModelAndView("hr/salary/setting");
    }

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
     * 薪酬信息展示列
     */
    @RequestMapping(params = "getCompanySalary")
    @ResponseBody
    public AjaxJson getCompanySalary() {
        AjaxJson j = new AjaxJson();
        TSUser user = ResourceUtil.getSessionUser();
        StringBuilder hql = new StringBuilder();
        hql.append(" select t.id,t.parent.name,t.name from HRSalary t");
        hql.append(" where t.parent is not null");
        hql.append(" and t.status =?");
        hql.append(" and t.companyId =?");
        List<Object[]> salaryList = systemService.findHql(hql.toString(), "Y", user.getCompanyid());

        StringBuilder columns = new StringBuilder("[");
        columns.append("{field: 'id', title: '主键', hidden: true},");
        columns.append("{field: 'name', title: '姓名', width: 150},");
        for (int i = 0; i < salaryList.size(); i++) {
            Object[] salary = salaryList.get(i);
            columns.append("{field: '").append(salary[0]).append("', title: '").append(salary[2]).append("', width: 150},");
        }
        columns.append("{field: 'opt', title: '操作', width: 100, formatter: function (value, rec, index) {if (!rec.id) {return '';} var href = '';return href;}}]");
        j.setObj(columns);
        return j;
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
        List<HRUser> results = dataGrid.getResults();
        for (int i = 0; i < results.size(); i++) {
            HRUser user = results.get(i);
            List<HRUserSalary> salaryList = systemService.findByProperty(HRUserSalary.class, "userId", user.getId());
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < salaryList.size(); j++) {
                sb.append("{\"salaryId\":\"" + salaryList.get(j).getSalaryId() + "\",\"value\":\"" + salaryList.get(j).getValue() + "\"},");
            }
            if (sb.length() != 0) {
                sb = sb.deleteCharAt(sb.length() - 1);
            }
            user.setSalaryStr(sb.toString());
        }
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

        List<HRUserSalary> salaryList = new ArrayList<HRUserSalary>();
        Map<String, String[]> salaryMap = request.getParameterMap();
        for (Map.Entry<String, String[]> next : salaryMap.entrySet()) {
            if ("id".equals(next.getKey()) || "saveUserSalary".equals(next.getKey()) || "".equals(next.getValue()[0])) {
                continue;
            }
            HRUserSalary userSalary = new HRUserSalary();
            userSalary.setUserId(id);
            userSalary.setSalaryId(next.getKey());
            userSalary.setValue(Double.valueOf(next.getValue()[0]));
            userSalary.setDeleteFlag(Globals.Delete_Normal);
            salaryList.add(userSalary);
        }
        if (!salaryList.isEmpty()) {
            systemService.batchSave(salaryList);
            systemService.addLog("编辑薪酬信息", Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        return j;
    }
}