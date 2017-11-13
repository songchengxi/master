package com.scx.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.scx.system.entity.CompanyRole;
import com.scx.system.entity.CompanyRoleFunction;
import com.scx.system.entity.CompanyRoleUser;
import com.scx.system.service.CompanyRoleService;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Array;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.*;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.pojo.base.*;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 公司角色
 */
@Controller
@RequestMapping("/companyRoleController")
public class CompanyRoleController {

    private static final Logger log = LoggerFactory.getLogger(CompanyRoleController.class);

    @Autowired
    private CompanyRoleService companyRoleService;

    @Autowired
    private SystemService systemService;

    @RequestMapping(params = "list")
    public ModelAndView list() {
        return new ModelAndView("system/company/roleList");
    }

    /**
     * 公司角色，树形展示
     */
    @RequestMapping(params = "roleTree")
    @ResponseBody
    public Object roleTree(CompanyRole t, HttpServletRequest request, HttpServletResponse response, TreeGrid treegrid) {
        CriteriaQuery cq = new CriteriaQuery(CompanyRole.class);
        if ("yes".equals(request.getParameter("isSearch"))) {
            treegrid.setId(null);
            t.setId(null);
        }
        if (treegrid.getId() == null) {
            cq.isNull("parentRole");
        } else {
            cq.eq("parentRole.id", treegrid.getId());
        }
        cq.add();
        List<TreeGrid> roleList = systemService.getListByCriteriaQuery(cq, false);
        List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
        TreeGridModel treeGridModel = new TreeGridModel();
        treeGridModel.setTextField("companyId");
        treeGridModel.setParentText("parentRole_companyId");
        treeGridModel.setParentId("parentRole_id");
        treeGridModel.setSrc("roleName");
        treeGridModel.setIdField("id");
        treeGridModel.setChildList("childRoles");
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("roleName", "roleName");
        fieldMap.put("roleCode", "roleCode");
        treeGridModel.setFieldMap(fieldMap);
        treeGrids = systemService.treegrid(roleList, treeGridModel);
        JSONArray jsonArray = new JSONArray();
        for (TreeGrid treeGrid : treeGrids) {
            jsonArray.add(JSON.parse(treeGrid.toJson()));
        }
        return jsonArray;
    }

    /**
     * 添加页面
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(CompanyRole role, HttpServletRequest request) {
        request.setAttribute("pid", role.getId());
        return new ModelAndView("system/company/role");
    }

    /**
     * 更新页面
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(CompanyRole role, HttpServletRequest request) {
        role = systemService.getEntity(CompanyRole.class, role.getId());
        request.setAttribute("role", role);
        return new ModelAndView("system/company/role");
    }

    /**
     * 添加、更新
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(CompanyRole companyRole, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        // 设置上级部门
        String pid = request.getParameter("parentRole.id");
        if ("".equals(pid)) {
            companyRole.setParentRole(null);
        }
        if (StringUtil.isNotEmpty(companyRole.getId())) {
            systemService.saveOrUpdate(companyRole);
            message = "公司角色更新成功";
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            systemService.save(companyRole);
            message = "公司角色添加成功";
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 删除角色
     */
    @RequestMapping(params = "delRole")
    @ResponseBody
    public AjaxJson delRole(CompanyRole role, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        int childCount = companyRoleService.getChildRole(role.getId());
        if (childCount == 0) {
            int count = companyRoleService.getUsersOfThisRole(role.getId());
            if (count == 0) {
                //删除角色之前先删除角色权限关系
                delRoleFunction(role);

                role = systemService.getEntity(CompanyRole.class, role.getId());
                systemService.delete(role);
                message = "角色: " + role.getRoleName() + "被删除成功";
                systemService.addLog(message, Globals.Log_Type_DEL,
                        Globals.Log_Leavel_INFO);
            } else {
                message = "角色: 仍被用户使用，请先解除关联关系";
            }
        } else {
            message = "该角色还有下级角色，请先删除下级角色";
        }
        j.setMsg(message);
        log.info(message);
        return j;
    }

    /**
     * 删除角色权限
     */
    protected void delRoleFunction(CompanyRole role) {
        List<CompanyRoleFunction> roleFunctions = systemService.findByProperty(
                CompanyRoleFunction.class, "TSRole.id", role.getId());
        if (roleFunctions.size() > 0) {
            for (CompanyRoleFunction roleFunction : roleFunctions) {
                systemService.delete(roleFunction);
            }
        }
    }

    /**
     * 设置角色菜单权限页面跳转
     */
    @RequestMapping(params = "fun")
    public ModelAndView fun(HttpServletRequest request) {
        String roleId = request.getParameter("roleId");
        request.setAttribute("roleId", roleId);
        return new ModelAndView("system/company/roleSet");
    }

    /**
     * 设置权限
     */
    @RequestMapping(params = "setAuthority")
    @ResponseBody
    public List<ComboTree> setAuthority(CompanyRole role, HttpServletRequest request, ComboTree comboTree) {
        List<ComboTree> comboTrees = new ArrayList<ComboTree>();
        String roleId = request.getParameter("roleId");
        List<TSFunction> loginActionlist = new ArrayList<TSFunction>();// 已有权限菜单
        role = this.systemService.get(CompanyRole.class, roleId);
        if (role != null) {
            List<CompanyRoleFunction> roleFunctionList = systemService.findByProperty(CompanyRoleFunction.class, "TSRole.id", role.getId());
            if (roleFunctionList.size() > 0) {
                for (CompanyRoleFunction roleFunction : roleFunctionList) {
                    TSFunction function = roleFunction.getTSFunction();
                    loginActionlist.add(function);
                }
            }
            roleFunctionList.clear();
        }

        CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
        if (comboTree.getId() != null) {
            cq.eq("TSFunction.id", comboTree.getId());
        }
        if (comboTree.getId() == null) {
            cq.isNull("TSFunction");
        }
        if (role.getParentRole() != null) {
            List<Object> parentFuns = systemService.findHql("select rf.TSFunction.id from CompanyRoleFunction rf where rf.TSRole.id = ? ", role.getParentRole().getId());
            Object[] objects = parentFuns.toArray();
            cq.in("id", objects);
        }
        cq.notEq("functionLevel", Short.parseShort("-1"));
        cq.add();
        List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq, false);
        Collections.sort(functionList, new NumberComparator());

        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "functionName", "TSFunctions");
        comboTrees = comboTree(functionList, comboTreeModel, loginActionlist, true);
        MutiLangUtil.setMutiComboTree(comboTrees);

        functionList.clear();
        functionList = null;
        loginActionlist.clear();
        loginActionlist = null;
        return comboTrees;
    }

    private List<ComboTree> comboTree(List<TSFunction> all, ComboTreeModel comboTreeModel, List<TSFunction> in, boolean recursive) {
        List<ComboTree> trees = new ArrayList<ComboTree>();
        for (TSFunction obj : all) {
            trees.add(comboTree(obj, comboTreeModel, in, recursive));
        }
        all.clear();
        return trees;
    }

    /**
     * 构建ComboTree
     *
     * @param obj
     * @param comboTreeModel ComboTreeModel comboTreeModel = new ComboTreeModel("id","functionName", "TSFunctions");
     * @param in
     * @param recursive      是否递归子节点
     */
    private ComboTree comboTree(TSFunction obj, ComboTreeModel comboTreeModel, List<TSFunction> in, boolean recursive) {
        ComboTree tree = new ComboTree();
        String id = oConvertUtils.getString(obj.getId());
        tree.setId(id);
        tree.setText(oConvertUtils.getString(obj.getFunctionName()));

        if (in == null) {
        } else {
            if (in.size() > 0) {
                for (TSFunction inobj : in) {
                    String inId = oConvertUtils.getString(inobj.getId());
                    if (inId.equals(id)) {
                        tree.setChecked(true);
                    }
                }
            }
        }

        List<TSFunction> curChildList = obj.getTSFunctions();

        Collections.sort(curChildList, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                TSFunction tsFunction1 = (TSFunction) o1;
                TSFunction tsFunction2 = (TSFunction) o2;
                int flag = tsFunction1.getFunctionOrder().compareTo(tsFunction2.getFunctionOrder());
                if (flag == 0) {
                    return tsFunction1.getFunctionName().compareTo(tsFunction2.getFunctionName());
                } else {
                    return flag;
                }
            }
        });

        if (curChildList != null && curChildList.size() > 0) {
            tree.setState("closed");

            if (recursive) { // 递归查询子节点
                List<ComboTree> children = new ArrayList<ComboTree>();
                for (TSFunction childObj : curChildList) {
                    ComboTree t = comboTree(childObj, comboTreeModel, in, recursive);
                    children.add(t);
                }
                tree.setChildren(children);
            }
        }

        if (obj.getFunctionType() == 1) {
            if (curChildList != null && curChildList.size() > 0) {
                tree.setIconCls("icon-user-set-o");
            } else {
                tree.setIconCls("icon-user-set");
            }
        }

        if (curChildList != null) {
            curChildList.clear();
        }

        return tree;
    }

    /**
     * 更新权限
     */
    @RequestMapping(params = "updateAuthority")
    @ResponseBody
    public AjaxJson updateAuthority(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            String roleId = request.getParameter("roleId");
            String rolefunction = request.getParameter("rolefunctions");
            CompanyRole role = this.systemService.get(CompanyRole.class, roleId);
            List<CompanyRoleFunction> roleFunctionList = systemService.findByProperty(
                    CompanyRoleFunction.class, "TSRole.id", role.getId());
            Map<String, CompanyRoleFunction> map = new HashMap<String, CompanyRoleFunction>();
            for (CompanyRoleFunction functionOfRole : roleFunctionList) {
                map.put(functionOfRole.getTSFunction().getId(), functionOfRole);
            }
            String[] roleFunctions = rolefunction.split(",");
            Set<String> set = new HashSet<String>();
            for (String s : roleFunctions) {
                set.add(s);
            }
            updateCompare(set, role, map);
            j.setMsg("权限更新成功");
        } catch (Exception e) {
            log.error(ExceptionUtil.getExceptionMessage(e));
            j.setMsg("权限更新失败");
        }
        return j;
    }

    /**
     * 权限比较
     *
     * @param set  最新的权限列表
     * @param role 当前角色
     * @param map  旧的权限列表
     */
    private void updateCompare(Set<String> set, CompanyRole role,
                               Map<String, CompanyRoleFunction> map) {
        List<CompanyRoleFunction> entitys = new ArrayList<CompanyRoleFunction>();
        List<CompanyRoleFunction> deleteEntitys = new ArrayList<CompanyRoleFunction>();
        for (String s : set) {
            if (map.containsKey(s)) {
                map.remove(s);
            } else {
                CompanyRoleFunction rf = new CompanyRoleFunction();
                TSFunction f = this.systemService.get(TSFunction.class, s);
                rf.setTSFunction(f);
                rf.setTSRole(role);
                entitys.add(rf);
            }
        }
        Collection<CompanyRoleFunction> collection = map.values();
        Iterator<CompanyRoleFunction> it = collection.iterator();
        for (; it.hasNext(); ) {
            deleteEntitys.add(it.next());
        }
        systemService.batchSave(entitys);
        systemService.deleteAllEntitie(deleteEntitys);
    }

    /**
     * 页面控件权限
     */
    @RequestMapping(params = "operationListForFunction")
    public ModelAndView operationListForFunction(HttpServletRequest request, String functionId, String roleId) {
        CriteriaQuery cq = new CriteriaQuery(TSOperation.class);
        cq.eq("TSFunction.id", functionId);
        cq.eq("status", Short.valueOf("0"));
        cq.add();
        List<TSOperation> operationList = this.systemService.getListByCriteriaQuery(cq, false);
        Set<String> operationCodes = companyRoleService.getOperationCodesByRoleIdAndFunctionId(roleId, functionId);
        request.setAttribute("operationList", operationList);
        request.setAttribute("operationcodes", operationCodes);
        request.setAttribute("functionId", functionId);
        return new ModelAndView("system/company/operationListForFunction");
    }

    /**
     * 更新页面控件权限
     */
    @RequestMapping(params = "updateOperation")
    @ResponseBody
    public AjaxJson updateOperation(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String roleId = request.getParameter("roleId");
        String functionId = request.getParameter("functionId");
        String operationcodes = null;

        try {
            operationcodes = URLDecoder.decode(request.getParameter("operationcodes"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        CriteriaQuery cq1 = new CriteriaQuery(CompanyRoleFunction.class);
        cq1.eq("CompanyRole.id", roleId);
        cq1.eq("TSFunction.id", functionId);
        cq1.add();
        List<CompanyRoleFunction> rFunctions = systemService.getListByCriteriaQuery(cq1, false);
        if (null != rFunctions && rFunctions.size() > 0) {
            CompanyRoleFunction tsRoleFunction = rFunctions.get(0);
            tsRoleFunction.setOperation(operationcodes);
            systemService.saveOrUpdate(tsRoleFunction);
        }
        j.setMsg("按钮权限更新成功");
        return j;
    }

    /**
     * 数据权限配置
     */
    @RequestMapping(params = "dataRuleListForFunction")
    public ModelAndView dataRuleListForFunction(HttpServletRequest request, String functionId, String roleId) {
        CriteriaQuery cq = new CriteriaQuery(TSDataRule.class);
        cq.eq("TSFunction.id", functionId);
        cq.add();
        List<TSDataRule> dataRuleList = this.systemService.getListByCriteriaQuery(cq, false);
        Set<String> dataRulecodes = companyRoleService.getOperationCodesByRoleIdAndruleDataId(roleId, functionId);
        request.setAttribute("dataRuleList", dataRuleList);
        request.setAttribute("dataRulecodes", dataRulecodes);
        request.setAttribute("functionId", functionId);
        return new ModelAndView("system/company/dataRuleListForFunction");
    }

    /**
     * 更新数据权限
     */
    @RequestMapping(params = "updateDataRule")
    @ResponseBody
    public AjaxJson updateDataRule(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String roleId = request.getParameter("roleId");
        String functionId = request.getParameter("functionId");
        String dataRulecodes = null;

        try {
            dataRulecodes = URLDecoder.decode(request.getParameter("dataRulecodes"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        CriteriaQuery cq1 = new CriteriaQuery(CompanyRoleFunction.class);
        cq1.eq("TSRole.id", roleId);
        cq1.eq("TSFunction.id", functionId);
        cq1.add();
        List<CompanyRoleFunction> rFunctions = systemService.getListByCriteriaQuery(cq1, false);
        if (null != rFunctions && rFunctions.size() > 0) {
            CompanyRoleFunction tsRoleFunction = rFunctions.get(0);
            tsRoleFunction.setDataRule(dataRulecodes);
            systemService.saveOrUpdate(tsRoleFunction);
        }
        j.setMsg("数据权限更新成功");
        return j;
    }

    /**
     * 角色所有用户信息列表页面跳转
     */
    @RequestMapping(params = "userList")
    public ModelAndView userList(HttpServletRequest request) {
        request.setAttribute("roleId", request.getParameter("roleId"));
        return new ModelAndView("system/company/roleUserList");
    }

    /**
     * 用户列表查询
     */
    @RequestMapping(params = "roleUserDatagrid")
    public void roleUserDatagrid(TSUser user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);

        //查询条件组装器
        String roleId = request.getParameter("roleId");
        List<CompanyRoleUser> roleUser = systemService.findByProperty(CompanyRoleUser.class, "TSRole.id", roleId);

        Criterion cc = null;
        if (roleUser.size() > 0) {
            for (int i = 0; i < roleUser.size(); i++) {
                if (i == 0) {
                    cc = Restrictions.eq("id", roleUser.get(i).getTSUser().getId());
                } else {
                    cc = cq.getor(cc, Restrictions.eq("id", roleUser.get(i).getTSUser().getId()));
                }
            }
        } else {
            cc = Restrictions.eq("id", "-1");
        }
        cq.add(cc);
        cq.eq("deleteFlag", Globals.Delete_Normal);
        cq.add();
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除用户对应角色关系
     */
    @RequestMapping(params = "delUserRole")
    @ResponseBody
    public AjaxJson delUserRole(@RequestParam(required = true) String userid, @RequestParam(required = true) String roleid) {
        AjaxJson ajaxJson = new AjaxJson();
        try {
            List<CompanyRoleUser> roleUserList = this.systemService.findByProperty(CompanyRoleUser.class, "TSUser.id", userid);
            if (roleUserList.size() == 1) {
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("不可删除用户的角色关系，请使用修订用户角色关系");
            } else {
                String sql = "delete from sys_company_role_user where userid = ? and roleid = ?";
                this.systemService.executeSql(sql, userid, roleid);
                ajaxJson.setMsg("成功删除用户对应的角色关系");
            }
        } catch (Exception e) {
            LogUtil.log("删除用户对应的角色关系失败", e.getMessage());
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg(e.getMessage());
        }
        return ajaxJson;
    }
}