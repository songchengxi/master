package com.scx.hr.controller;

import com.scx.hr.entity.HRUserTreaty;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
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
 * 合同信息
 */
@Controller
@RequestMapping("/hrTreatyController")
public class TreatyController {

    private static final Logger log = LoggerFactory.getLogger(TreatyController.class);

    @Autowired
    private SystemService systemService;

    @RequestMapping(params = "list")
    public String list() {
        return "hr/treaty/treatyList";
    }

    @RequestMapping(params = "data")
    public void data(HRUserTreaty treaty, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) throws ParseException {
        CriteriaQuery cq = new CriteriaQuery(HRUserTreaty.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, treaty);
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
    public AjaxJson doDel(String id) {
        String message;
        AjaxJson j = new AjaxJson();
        HRUserTreaty entity = systemService.getEntity(HRUserTreaty.class, id);
        message = "合同信息 删除成功";
        systemService.delete(entity);
        systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        j.setMsg(message);
        return j;
    }

    /**
     * 添加、编辑页面
     */
    @RequestMapping(params = "addOrUpdate")
    public ModelAndView addOrUpdate(HttpServletRequest request, String id) {
        if (StringUtil.isNotEmpty(id)) {
            HRUserTreaty treaty = systemService.get(HRUserTreaty.class, id);
            request.setAttribute("treaty", treaty);
        }
        return new ModelAndView("hr/treaty/treaty");
    }

    /**
     * 保存
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(HRUserTreaty treaty) {
        String message;
        AjaxJson j = new AjaxJson();
        if (StringUtil.isNotEmpty(treaty.getId())) {
            message = "合同信息 更新成功";
            systemService.saveOrUpdate(treaty);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            message = "合同信息 添加成功";
            TSUser user = ResourceUtil.getSessionUser();
            treaty.setCompanyId(user.getCompanyid());
            systemService.save(treaty);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        j.setMsg(message);
        return j;
    }
}
