package com.scx.hr.controller;

import com.scx.hr.entity.HRLeave;
import org.apache.commons.httpclient.util.DateUtil;
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

@Controller
@RequestMapping("/hrLeaveController")
public class HRLeaveController {

    private static final Logger log = LoggerFactory.getLogger(HRLeaveController.class);

    @Autowired
    private SystemService systemService;

    @RequestMapping(params = "list")
    public ModelAndView list() {
        return new ModelAndView("hr/leave/leaveList");
    }

    @RequestMapping(params = "datagrid")
    public void datagrid(HRLeave leave, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) throws ParseException {
        CriteriaQuery cq = new CriteriaQuery(HRLeave.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, leave);
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
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(HRLeave leave, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        leave = systemService.getEntity(HRLeave.class, leave.getId());
        message = "请假信息删除成功";
        try {
            systemService.delete(leave);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            log.info("[" + IpUtil.getIpAddr(request) + "][逻辑删除用户]" + message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "请假信息删除失败";
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
        message = "请假信息删除成功";
        try {
            for (String id : ids.split(",")) {
                HRLeave leave = systemService.getEntity(HRLeave.class, id);
                systemService.delete(leave);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
                log.info("[" + IpUtil.getIpAddr(request) + "][删除请假信息]" + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "请假信息删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 新增、编辑
     */
    @RequestMapping(params = "addOrUpdate")
    public ModelAndView addOrUpdate(HRLeave leave, HttpServletRequest request) {
        if (StringUtil.isNotEmpty(leave.getId())) {
            leave = systemService.getEntity(HRLeave.class, leave.getId());
            request.setAttribute("leave", leave);
        }
        return new ModelAndView("hr/leave/leave");
    }

    /**
     * 保存
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(HRLeave leave) {
        String message;
        AjaxJson j = new AjaxJson();
        if (StringUtil.isNotEmpty(leave.getId())) {
            systemService.saveOrUpdate(leave);
            message = "请假信息更新成功";
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            TSUser sessionUser = ResourceUtil.getSessionUser();
            leave.setCompanyId(sessionUser.getCompanyid());
            systemService.save(leave);
            message = "请假信息添加成功";
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        j.setMsg(message);
        return j;
    }
}