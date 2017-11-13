package com.scx.system.controller;

import com.scx.system.entity.Company;
import net.sf.json.JSONArray;
import org.jeecgframework.core.common.dao.jdbc.JdbcDao;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.YouBianCodeUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
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
 * 公司管理
 */
@Controller
@RequestMapping("/companyController")
public class CompanyController {

    private final static Logger log = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private SystemService systemService;

    @RequestMapping(params = "list")
    public ModelAndView list() {
        return new ModelAndView("system/company/companyList");
    }

    /**
     * 数据请求
     */
    @RequestMapping(params = "companyGrid")
    public void companyGrid(Company t, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(Company.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, t, request.getParameterMap());
        try {
            //自定义追加查询条件
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除公司信息
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(Company t) {
        String message;
        AjaxJson j = new AjaxJson();
        t = systemService.getEntity(Company.class, t.getId());
        message = "公司信息删除成功";
        try {
            systemService.delete(t);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "公司信息删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除公司信息
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids) {
        String message;
        AjaxJson j = new AjaxJson();
        message = "公司信息删除成功";
        try {
            for (String id : ids.split(",")) {
                Company t = systemService.getEntity(Company.class, id);
                systemService.delete(t);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "公司信息删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 公司信息新增页面跳转
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd() {
        return new ModelAndView("system/company/company");
    }

    /**
     * 公司信息编辑页面跳转
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(Company t, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(t.getId())) {
            t = systemService.getEntity(Company.class, t.getId());
            req.setAttribute("page", t);
        }
        return new ModelAndView("system/company/company");
    }

    /**
     * 公司录入
     */
    @RequestMapping(params = "saveCompany")
    @ResponseBody
    public AjaxJson saveCompany(Company t, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        if (StringUtil.isNotEmpty(t.getId())) {
            systemService.saveOrUpdate(t);
            message = "公司: " + t.getName() + "被更新成功";
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            systemService.save(t);
            //部门表增加一级分类公司信息
            TSDepart depart = new TSDepart();
            String localMaxCode  = getMaxLocalCode();
            depart.setOrgCode(YouBianCodeUtil.getNextYouBianCode(localMaxCode));
            depart.setOrgType("1");
            depart.setDepartname(t.getName());
            depart.setId(t.getId());
            systemService.save(depart);
            message = "公司: " + t.getName() + "被添加成功";
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        j.setMsg(message);
        log.info(message);
        return j;
    }

    /**
     * 用户选择公司列表跳转页面
     */
    @RequestMapping(params = "companySelect")
    public String companySelect(HttpServletRequest req) {
        req.setAttribute("companyId", req.getParameter("companyId"));
        return "system/company/companySelect";
    }

    @RequestMapping(params = "getCompanyInfo")
    @ResponseBody
    public AjaxJson getCompanyInfo(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String companyId = request.getParameter("companyId");
        List<Company> companys = systemService.loadAll(Company.class);
        List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
        if (companys.size() > 0) {
            Map<String, Object> map;
            for (Company company : companys) {
                map = new HashMap<String, Object>();
                map.put("id", company.getId());
                map.put("name", company.getName());
                if (company.getId().equals(companyId)) {
                    map.put("checked", true);
                }
                companyList.add(map);
            }
        }
        JSONArray jsonArray = JSONArray.fromObject(companyList);
        j.setMsg(jsonArray.toString());
        return j;
    }

    private synchronized String getMaxLocalCode(){
        int localCodeLength = YouBianCodeUtil.zhanweiLength;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT org_code FROM t_s_depart");

        if(ResourceUtil.getJdbcUrl().indexOf(JdbcDao.DATABSE_TYPE_SQLSERVER)!=-1){
            sb.append(" where LEN(org_code) = ").append(localCodeLength);
        }else{
            sb.append(" where LENGTH(org_code) = ").append(localCodeLength);
        }
        sb.append(" ORDER BY org_code DESC");
        List<Map<String, Object>> objMapList = systemService.findForJdbc(sb.toString(), 1, 1);
        String returnCode = null;
        if(objMapList!=null && objMapList.size()>0){
            returnCode = (String)objMapList.get(0).get("org_code");
        }
        return returnCode;
    }
}