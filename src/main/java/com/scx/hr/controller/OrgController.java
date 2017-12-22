package com.scx.hr.controller;

import com.scx.hr.entity.HRUser;
import com.scx.hr.entity.HRUserOrg;
import com.scx.system.entity.Company;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.dao.jdbc.JdbcDao;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.*;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 组织架构
 */
@Controller
@RequestMapping("orgController")
public class OrgController {

    private static final Logger log = LoggerFactory.getLogger(OrgController.class);

    @Autowired
    private SystemService systemService;

    @RequestMapping(params = "orgTree")
    public ModelAndView orgTree() {
        return new ModelAndView("hr/org/orgTree");
    }

    /**
     * ZTree展示组织架构
     */
    @RequestMapping(params = "getDepartTreeData", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson getDepartTreeData() {
        AjaxJson j = new AjaxJson();
        List<TSDepart> departList;
        TSUser user = ResourceUtil.getSessionUser();
        if ("admin".equals(user.getUserName())) {
            departList = systemService.findHql("from TSDepart");
        } else {
            departList = systemService.findHql("from TSDepart where companyid = ? ", user.getCompanyid());
        }
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (TSDepart tsdepart : departList) {
            map = new HashMap<String, Object>();
            map.put("chkDisabled", false);
            map.put("click", true);
            map.put("id", tsdepart.getId());
            map.put("name", tsdepart.getDepartname());
            map.put("nocheck", false);
            map.put("struct", "TREE");
            map.put("title", tsdepart.getDepartname());
            if (tsdepart.getTSPDepart() != null) {
                map.put("parentId", tsdepart.getTSPDepart().getId());
            } else {
                map.put("parentId", "0");
            }
            dataList.add(map);
        }
        j.setObj(dataList);
        return j;
    }

    /**
     * 部门用户信息
     */
    @RequestMapping(params = "goUserList")
    public ModelAndView goUserList(String id, HttpServletRequest request) {
        request.setAttribute("departId", id);
        return new ModelAndView("hr/org/orgUserList");
    }

    @RequestMapping(params = "userList")
    @ResponseBody
    public void userList(HRUserOrg userOrg, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(HRUserOrg.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, userOrg, request.getParameterMap());
        try {
            //自定义追加查询条件
            String departId = request.getParameter("departId");
            cq.eq("tsDepart.id", departId);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除部门：
     * <ul>
     * 组织机构下存在子机构时
     * <li>不允许删除 组织机构</li>
     * </ul>
     * <ul>
     * 组织机构下存在用户时
     * <li>不允许删除 组织机构</li>
     * </ul>
     * <ul>
     * 组织机构下 不存在子机构 且 不存在用户时
     * <li>删除 组织机构-角色 信息</li>
     * <li>删除 组织机构 信息</li>
     * </ul>
     *
     * @return 删除的结果信息
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(TSDepart depart, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        depart = systemService.getEntity(TSDepart.class, depart.getId());
        message = MutiLangUtil.paramDelSuccess("common.department");
        if (depart.getTSDeparts().size() == 0) {
            Long userCount = systemService.getCountForJdbc("select count(1) from hr_user_org where org_id='" + depart.getId() + "'");
            if (userCount == 0) { // 组织机构下没有用户时，该组织机构才允许删除。
                systemService.executeSql("delete from hr_user_org where org_id=?", depart.getId());
                systemService.delete(depart);

                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            } else {
                j.setSuccess(false);
                message = MutiLangUtil.getMutiLangInstance().getLang("common.department.hasuser");
            }
        } else {
            j.setSuccess(false);
            message = MutiLangUtil.paramDelFail("common.department");
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 添加部门
     */
    @RequestMapping(params = "add")
    public ModelAndView add(TSDepart depart, HttpServletRequest req) {
        req.setAttribute("pid", depart.getId());
        return new ModelAndView("hr/org/org");
    }

    /**
     * 编辑部门
     */
    @RequestMapping(params = "update")
    public ModelAndView update(TSDepart depart, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(depart.getId())) {
            depart = systemService.getEntity(TSDepart.class, depart.getId());
            req.setAttribute("depart", depart);
        }
        return new ModelAndView("hr/org/org");
    }

    /**
     * 添加、编辑部门
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(TSDepart depart, HttpServletRequest request) {
        String message;
        // 设置上级部门
        String pid = request.getParameter("TSPDepart.id");
        if (pid.equals("")) {
            depart.setTSPDepart(null);
        }
        AjaxJson j = new AjaxJson();
        //编辑
        if (StringUtil.isNotEmpty(depart.getId())) {
            systemService.saveOrUpdate(depart);
            if (!oConvertUtils.isNotEmpty(pid)) {
                Company company = systemService.get(Company.class, depart.getId());
                company.setName(depart.getDepartname());
                systemService.saveOrUpdate(company);
            }
            message = MutiLangUtil.paramUpdSuccess("common.department");
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {//添加
            //部门
            if (oConvertUtils.isNotEmpty(pid)) {
                TSDepart paretDept = systemService.findUniqueByProperty(TSDepart.class, "id", pid);
                String localMaxCode = getMaxLocalCode(paretDept.getOrgCode());
                depart.setOrgCode(YouBianCodeUtil.getSubYouBianCode(paretDept.getOrgCode(), localMaxCode));
                depart.setCompanyid(paretDept.getCompanyid());
                depart.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            }
            systemService.save(depart);
            message = MutiLangUtil.paramAddSuccess("common.department");
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 用户选择部门列表跳转页面
     */
    @RequestMapping(params = "departSelect")
    public String departSelect(HttpServletRequest req) {
        req.setAttribute("ids", req.getParameter("ids"));
        return "hr/user/departSelect";
    }

    @RequestMapping(params = "getDepartInfo")
    @ResponseBody
    public AjaxJson getDepartInfo(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();

        String departIds = request.getParameter("departIds");
        String[] ids = new String[]{};
        if (StringUtils.isNotBlank(departIds)) {
            departIds = departIds.substring(0, departIds.length() - 1);
            ids = departIds.split("\\,");
        }

        String parentid = request.getParameter("parentId");
        List<TSDepart> tSDeparts;
        TSUser user = ResourceUtil.getSessionUser();
        StringBuffer hql = new StringBuffer(" from TSDepart t where t.companyid = '" + user.getCompanyid() + "' ");
        if (StringUtils.isNotBlank(parentid)) {
            TSDepart dePart = this.systemService.getEntity(TSDepart.class, parentid);
            hql.append(" and TSPDepart = ?");
            tSDeparts = this.systemService.findHql(hql.toString(), dePart);
        } else {
            hql.append(" and t.orgType = ?");
            tSDeparts = this.systemService.findHql(hql.toString(), "1");
        }
        List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
        if (tSDeparts.size() > 0) {
            Map<String, Object> map;
            String sql;
            Object[] params;
            for (TSDepart depart : tSDeparts) {
                map = new HashMap<String, Object>();
                map.put("id", depart.getId());
                map.put("name", depart.getDepartname());
                if (ids.length > 0) {
                    for (String id : ids) {
                        if (id.equals(depart.getId())) {
                            map.put("checked", true);
                        }
                    }
                }

                if (StringUtils.isNotBlank(parentid)) {
                    map.put("pId", parentid);
                } else {
                    map.put("pId", "1");
                }
                //根据id判断是否有子节点
                sql = "select count(1) from t_s_depart t where t.parentdepartid = ?";
                params = new Object[]{depart.getId()};
                long count = this.systemService.getCountForJdbcParam(sql, params);
                if (count > 0) {
                    map.put("isParent", true);
                }
                dateList.add(map);
            }
        }
        JSONArray jsonArray = JSONArray.fromObject(dateList);
        j.setMsg(jsonArray.toString());
        return j;
    }

    /**
     * 组织机构树状下拉列表
     */
    @RequestMapping(params = "selOrgTree")
    @ResponseBody
    public List<ComboTree> selOrgTree(HttpServletRequest request, ComboTree comboTree) {
        CriteriaQuery cq = new CriteriaQuery(TSDepart.class);
        if(null != request.getParameter("selfId")){
            cq.notEq("id", request.getParameter("selfId"));
        }
        if (comboTree.getId() != null) {
            cq.eq("TSPDepart.id", comboTree.getId());
        }
        if (comboTree.getId() == null) {
            cq.isNull("TSPDepart");
            TSUser user = ResourceUtil.getSessionUser();
            if (!"admin".equals(user.getUserName())) {
                cq.eq("companyid", user.getCompanyid());
            }
        }

        cq.addOrder("orgCode", SortDirection.asc);

        cq.add();
        List<TSDepart> departsList = systemService.getListByCriteriaQuery(cq, false);
        List<ComboTree> comboTrees = new ArrayList<ComboTree>();
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "TSDeparts");

        TSDepart defaultDepart = new TSDepart();
        defaultDepart.setId("");
        defaultDepart.setDepartname("请选择组织机构");
        departsList.add(0, defaultDepart);

        comboTrees = systemService.ComboTree(departsList, comboTreeModel, null, true);
        return comboTrees;
    }

    private synchronized String getMaxLocalCode(String parentCode) {
        if (oConvertUtils.isEmpty(parentCode)) {
            parentCode = "";
        }
        int localCodeLength = parentCode.length() + YouBianCodeUtil.zhanweiLength;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT org_code FROM t_s_depart");

        if (ResourceUtil.getJdbcUrl().indexOf(JdbcDao.DATABSE_TYPE_SQLSERVER) != -1) {
            sb.append(" where LEN(org_code) = ").append(localCodeLength);
        } else {
            sb.append(" where LENGTH(org_code) = ").append(localCodeLength);
        }

        if (oConvertUtils.isNotEmpty(parentCode)) {
            sb.append(" and  org_code like '").append(parentCode).append("%'");
        }

        sb.append(" ORDER BY org_code DESC");
        List<Map<String, Object>> objMapList = systemService.findForJdbc(sb.toString(), 1, 1);
        String returnCode = null;
        if (objMapList != null && objMapList.size() > 0) {
            returnCode = (String) objMapList.get(0).get("org_code");
        }
        return returnCode;
    }
}
