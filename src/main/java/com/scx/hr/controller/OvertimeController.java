package com.scx.hr.controller;

import com.scx.hr.entity.HROvertime;
import org.jeecgframework.core.common.exception.BusinessException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * 加班信息
 */
@Controller
@RequestMapping("/hrOvertimeController")
public class OvertimeController {

    private static final Logger log = LoggerFactory.getLogger(OvertimeController.class);

    @Autowired
    private SystemService systemService;

    @RequestMapping(params = "list")
    public ModelAndView list() {
        return new ModelAndView("hr/overtime/overtimeList");
    }

    @RequestMapping(params = "listData")
    public void listData(HROvertime overtime, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) throws ParseException {
        CriteriaQuery cq = new CriteriaQuery(HROvertime.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, overtime);
        String startTimeBegin = request.getParameter("startTime_begin");
        String startTimeEnd = request.getParameter("startTime_end");
        String endTimeBegin = request.getParameter("endTime_begin");
        String endTimeEnd = request.getParameter("endTime_end");
        if (oConvertUtils.isNotEmpty(startTimeBegin)) {
            cq.ge("startTime", DateUtils.parseDate(startTimeBegin, "yyyy-MM-dd"));
        }
        if (oConvertUtils.isNotEmpty(startTimeEnd)) {
            cq.le("startTime", DateUtils.parseDate(startTimeEnd, "yyyy-MM-dd"));
        }
        if (oConvertUtils.isNotEmpty(endTimeBegin)) {
            cq.ge("endTime", DateUtils.parseDate(endTimeBegin, "yyyy-MM-dd"));
        }
        if (oConvertUtils.isNotEmpty(endTimeEnd)) {
            cq.le("endTime", DateUtils.parseDate(endTimeEnd, "yyyy-MM-dd"));
        }
        TSUser sessionUser = ResourceUtil.getSessionUser();
        cq.eq("companyId", sessionUser.getCompanyid());
        cq.eq("deleteFlag", Globals.Delete_Normal);
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(HROvertime overtime, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        overtime = systemService.getEntity(HROvertime.class, overtime.getId());
        message = "加班信息删除成功";
        try {
            overtime.setDeleteFlag(Globals.Delete_Forbidden);
            systemService.updateEntitie(overtime);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            log.info("[" + IpUtil.getIpAddr(request) + "][删除加班信息]" + message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "加班信息删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        message = "加班信息删除成功";
        try {
            for (String id : ids.split(",")) {
                HROvertime overtime = systemService.getEntity(HROvertime.class, id);
                overtime.setDeleteFlag(Globals.Delete_Forbidden);
                systemService.updateEntitie(overtime);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
                log.info("[" + IpUtil.getIpAddr(request) + "][删除加班信息]" + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "加班信息删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 新增、编辑
     */
    @RequestMapping(params = "addOrUpdate")
    public ModelAndView addOrUpdate(HROvertime overtime, HttpServletRequest request) {
        if (StringUtil.isNotEmpty(overtime.getId())) {
            overtime = systemService.getEntity(HROvertime.class, overtime.getId());
            request.setAttribute("overtime", overtime);
        }
        return new ModelAndView("hr/overtime/overtime");
    }

    /**
     * 保存
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(HROvertime overtime) {
        String message;
        AjaxJson j = new AjaxJson();
        if (StringUtil.isNotEmpty(overtime.getId())) {
            systemService.saveOrUpdate(overtime);
            message = "加班信息更新成功";
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            TSUser sessionUser = ResourceUtil.getSessionUser();
            overtime.setCompanyId(sessionUser.getCompanyid());
            overtime.setDeleteFlag(Globals.Delete_Normal);
            systemService.save(overtime);
            message = "加班信息添加成功";
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        j.setMsg(message);
        return j;
    }
}
