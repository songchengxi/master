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
import org.jeecgframework.web.cgform.entity.config.CgFormHeadEntity;
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
            String parentId = request.getParameter("parentId");
            cq.eq("parent.id", parentId);
            TSUser sessionUser = ResourceUtil.getSessionUser();
            cq.eq("companyId", sessionUser.getCompanyid());
            //自定义追加查询条件
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        dataGrid.setFooter("companyProportion,userProportion,companyVal,userVal,name:合计");
        TagUtil.datagrid(response, dataGrid);
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
        TSUser user = ResourceUtil.getSessionUser();
        String sql = "SELECT * FROM ( " +
                " SELECT u.*,GROUP_CONCAT(d.departname) AS departName FROM ( " +
                "  SELECT u.id AS user_id,u.name,u.join_time,o.org_id FROM hr_user u LEFT JOIN hr_user_org o ON u.id=o.user_id WHERE u.COMPANY_ID=? AND u.delete_flag=0 " +
                " ) u LEFT JOIN t_s_depart d ON u.org_id = d.ID GROUP BY u.user_id " +
                ") u LEFT JOIN hr_user_social s ON u.user_id=s.user_id";

        ///master/hrSocialController.do?userSocialList=&field=id,userName,departName,joinTime,socialStart,socialBase,departName,departName,fundBase,fundComVal,fundUserVal,&page=1&rows=10&
        List<Map<String, Object>> resultList = systemService.findForJdbcParam(sql, dataGrid.getPage(), dataGrid.getRows(), user.getCompanyid());
        //将List转换成JSON存储
        List<Map<String, Object>> noticeList = new ArrayList<Map<String, Object>>();
        if (resultList != null && resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> obj = resultList.get(i);
                Map<String, Object> n = new HashMap<String, Object>();
                n.put("id", String.valueOf(obj.get("id")));
                n.put("isRead", String.valueOf(obj.get("is_read")));
                noticeList.add(n);
            }
        }
        dataGrid.setResults(noticeList);
        String getCountSql = "SELECT COUNT(*) FROM ( " +
                " SELECT u.*,GROUP_CONCAT(d.departname) AS departName FROM ( " +
                "  SELECT u.id,u.name,u.join_time,o.org_id FROM hr_user u LEFT JOIN hr_user_org o ON u.id=o.user_id WHERE u.COMPANY_ID = ? AND u.delete_flag = 0 " +
                " ) u LEFT JOIN t_s_depart d ON u.org_id = d.ID GROUP BY u.id " +
                ") u LEFT JOIN hr_user_social s ON u.id=s.ID";
        Long count = systemService.getCountForJdbcParam(getCountSql, new Object[]{user.getCompanyid()});
        dataGrid.setTotal(Integer.parseInt(String.valueOf(count)));
        dataGrid.setFooter("companyProportion,userProportion,companyVal,userVal,name:合计");
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 员工社保信息编辑
     */
    @RequestMapping(params = "goUserSocial")
    public ModelAndView goUserSocial(HttpServletRequest request, String id) {
        if (StringUtil.isNotEmpty(id)) {
            HRUserSocial userSocial = systemService.get(HRUserSocial.class, id);
            request.setAttribute("userSocial", userSocial);
        }
        return new ModelAndView("hr/social/userSocial");
    }

    /**
     * 保存员工社保信息
     */
    @RequestMapping(params = "saveUserSocial")
    @ResponseBody
    public AjaxJson saveUserSocial() {
        AjaxJson j = new AjaxJson();
        return j;
    }
}