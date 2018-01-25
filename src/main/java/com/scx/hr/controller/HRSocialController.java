package com.scx.hr.controller;

import com.scx.hr.entity.HRSocialSecurity;
import com.scx.hr.entity.HRUserSocial;
import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社保
 */
@Controller
@RequestMapping("/hrSocialController")
public class HRSocialController {

    private static final Logger log = LoggerFactory.getLogger(HRSocialController.class);

    @Autowired
    private SystemService systemService;

    /**
     * 上下特殊布局
     */
    @RequestMapping(params = "setting")
    public ModelAndView setting(HttpServletRequest request) {
        return new ModelAndView("hr/social/setting");
    }

    @RequestMapping(params = "data")
    public void data(HRSocialSecurity social, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(HRSocialSecurity.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, social);
        TSUser sessionUser = ResourceUtil.getSessionUser();
        cq.isNull("parent");
        cq.eq("companyId", sessionUser.getCompanyid());
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 合计
     */
    @RequestMapping(params = "datagrid")
    public void datagrid(HRSocialSecurity social, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(HRSocialSecurity.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, social, request.getParameterMap());
        try {
            //自定义追加查询条件
            String parentId = request.getParameter("parentId");
            cq.eq("parent.id", parentId);
            TSUser sessionUser = ResourceUtil.getSessionUser();
            cq.eq("companyId", sessionUser.getCompanyid());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        dataGrid.setFooter("companyProportion,userProportion,companyVal,userVal,name:合计");
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除社保
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(HRSocialSecurity social, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        social = systemService.getEntity(HRSocialSecurity.class, social.getId());
        systemService.delete(social);
        message = social.getName() + " 删除成功";
        systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        j.setMsg(message);
        return j;
    }

    /**
     * 启用、停用
     */
    @RequestMapping(params = "openAndClose")
    @ResponseBody
    public AjaxJson openAndClose(HRSocialSecurity social) {
        String message = "";
        AjaxJson j = new AjaxJson();
        social = systemService.get(HRSocialSecurity.class, social.getId());
        if ("Y".equals(social.getStatus())) {
            social.setStatus("N");
            message = social.getName() + "停用成功";
        } else if ("N".equals(social.getStatus())) {
            social.setStatus("Y");
            message = social.getName() + "启用成功";
        }
        systemService.updateEntitie(social);
        systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        j.setMsg(message);
        return j;
    }

    /**
     * 计算器
     */
    @RequestMapping(params = "openCalculator")
    public ModelAndView openCalculator() {
        return new ModelAndView("hr/calculator/calculator");
    }

    /**
     * 保存新增&更新的行数据
     */
    @RequestMapping(params = "saveRows")
    @ResponseBody
    public AjaxJson saveRows(HRSocialSecurity page, String pId) {
        TSUser sessionUser = ResourceUtil.getSessionUser();
        HRSocialSecurity parent = new HRSocialSecurity();
        parent.setId(pId);

        String message = "";
        List<HRSocialSecurity> childs = page.getChilds();
        AjaxJson j = new AjaxJson();
        if (CollectionUtils.isNotEmpty(childs)) {
            for (HRSocialSecurity social : childs) {
                if (StringUtil.isNotEmpty(social.getId())) {
                    HRSocialSecurity t = systemService.get(HRSocialSecurity.class, social.getId());
                    try {
                        message = social.getName() + " 更新成功";
                        MyBeanUtils.copyBeanNotNull2Bean(social, t);
                        systemService.saveOrUpdate(t);
                        systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        social.setCompanyId(sessionUser.getCompanyid());
                        social.setParent(parent);
                        social.setStatus("N");
                        systemService.save(social);
                        message = social.getName() + " 添加成功";
                        systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 员工社保信息
     */
    @RequestMapping(params = "goUserSocialList")
    public ModelAndView goUserSocialList() {
        return new ModelAndView("hr/social/userSocialList");
    }

    /**
     * 员工社保信息
     */
    @RequestMapping(params = "userSocialList")
    public void userSocialList(HRSocialSecurity social, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        String userName = request.getParameter("userName");
        if (null == userName) {
            userName = "";
        }
        TSUser user = ResourceUtil.getSessionUser();
        String sql = "SELECT * FROM ( " +
                " SELECT u.*,GROUP_CONCAT(d.departname) AS departName FROM ( " +
                "  SELECT u.id AS uid,u.name,u.join_time,u.job_status,o.org_id FROM hr_user u LEFT JOIN hr_user_org o ON u.id=o.user_id " +
                "   WHERE u.company_id=? AND u.delete_flag=? AND u.name LIKE ? " +
                " ) u LEFT JOIN t_s_depart d ON u.org_id = d.ID GROUP BY u.uid " +
                ") u LEFT JOIN hr_user_social s ON u.uid=s.user_id";

        List<Map<String, Object>> resultList = systemService.findForJdbcParam(sql, dataGrid.getPage(), dataGrid.getRows(), user.getCompanyid(), 0, "%" + userName + "%");
        //将List转换成JSON存储
        List<Map<String, Object>> socialList = new ArrayList<Map<String, Object>>();
        if (resultList != null && resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> obj = resultList.get(i);
                Map<String, Object> n = new HashMap<String, Object>();
                n.put("id", String.valueOf(obj.get("user_id")));
                n.put("userName", String.valueOf(obj.get("name")));
                n.put("departName", String.valueOf(obj.get("departName")));
                n.put("joinTime", String.valueOf(obj.get("join_time")));
                n.put("jobStatus", String.valueOf(obj.get("job_status")));
                if (!"null".equals(String.valueOf(obj.get("social_start")))) {
                    n.put("insure", "Y");//是否参保
                    n.put("socialStart", String.valueOf(obj.get("social_start")));
                    n.put("socialBase", String.valueOf(obj.get("social_base")));
                    n.put("socialComVal", String.valueOf(obj.get("social_com_val")));
                    n.put("socialUserVal", String.valueOf(obj.get("social_user_val")));
                    n.put("fundBase", String.valueOf(obj.get("fund_base")));
                    n.put("fundComVal", String.valueOf(obj.get("fund_com_val")));
                    n.put("fundUserVal", String.valueOf(obj.get("fund_user_val")));
                } else {
                    n.put("insure", "N");
                }
                socialList.add(n);
            }
        }
        dataGrid.setResults(socialList);
        String countSql = "SELECT COUNT(id) FROM hr_user u " +
                " WHERE u.company_id = ? AND u.delete_flag = ? AND u.name LIKE ? ";
        Long count = systemService.getCountForJdbcParam(countSql, new Object[]{user.getCompanyid(), 0, "%" + userName + "%"});
        dataGrid.setTotal(Integer.parseInt(String.valueOf(count)));
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 员工社保信息编辑界面跳转
     */
    @RequestMapping(params = "goUserSocial")
    public ModelAndView goUserSocial(HttpServletRequest request) {
        TSUser sessionUser = ResourceUtil.getSessionUser();
        String id = request.getParameter("id");
        if (StringUtil.isNotEmpty(id)) {
            HRUserSocial userSocial = systemService.get(HRUserSocial.class, id);
            request.setAttribute("userSocial", userSocial);

            String insure = request.getParameter("insure");
            if ("N".equals(insure)) {//参保
                Map<String, Object> map = new HashMap<String, Object>();
                String sql = "SELECT a.base, SUM(a.company_val) comVal,SUM(a.user_val) userVal FROM hr_social_security a WHERE a.parent_id = (\n" +
                        " SELECT s.id FROM hr_social_security s WHERE s.company_id=? AND s.code=?)";
                List<Map<String, Object>> social = systemService.findForJdbc(sql, sessionUser.getCompanyid(), "SB");//社保
                List<Map<String, Object>> fund = systemService.findForJdbc(sql, sessionUser.getCompanyid(), "GJJ");//公积金
                if (!social.isEmpty()) {
                    for (int i = 0; i < social.size(); i++) {
                        Map<String, Object> m = social.get(i);
                        map.put("socialBase", String.valueOf(m.get("base")));
                        map.put("socialCom", String.valueOf(m.get("comVal")));
                        map.put("socialUser", String.valueOf(m.get("userVal")));
                    }
                }
                if (!fund.isEmpty()) {
                    for (int i = 0; i < fund.size(); i++) {
                        Map<String, Object> m = fund.get(i);
                        map.put("fundBase", String.valueOf(m.get("base")));
                        map.put("fundCom", String.valueOf(m.get("comVal")));
                        map.put("fundUser", String.valueOf(m.get("userVal")));
                    }
                }
                request.setAttribute("val", map);
            }
            request.setAttribute("insure", insure);
        }
        return new ModelAndView("hr/social/userSocial");
    }

    /**
     * 保存员工社保信息
     */
    @RequestMapping(params = "saveUserSocial")
    @ResponseBody
    public AjaxJson saveUserSocial(HRUserSocial social) {
        String message;
        AjaxJson j = new AjaxJson();
        if (StringUtil.isNotEmpty(social.getUserId())) {
            message = "社保信息 更新成功";
            systemService.saveOrUpdate(social);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            message = "社保信息 添加成功";
            social.setDeleteFlag(Globals.Delete_Normal);
            systemService.save(social);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        j.setMsg(message);
        return j;
    }
}