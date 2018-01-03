package com.scx.hr.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scx.hr.entity.HRPost;
import com.scx.hr.entity.HRUser;
import com.scx.hr.entity.HRUserOrg;
import com.scx.hr.entity.HRUserSocial;
import org.hibernate.criterion.Property;
import org.jeecgframework.core.util.*;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Validator;

/**
 * @version V1.0
 * @Description: 员工信息
 * @date 2017-10-28 18:36:49
 */
@Controller
@RequestMapping("/hrUserController")
public class HRUserController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(HRUserController.class);

    @Autowired
    private SystemService systemService;
    @Autowired
    private Validator validator;

    /**
     * 员工信息列表 页面跳转
     */
    @RequestMapping(params = "list")
    public ModelAndView list() {
        return new ModelAndView("hr/user/userList");
    }

    /**
     * easyui AJAX请求数据
     */
    @RequestMapping(params = "datagrid")
    public void datagrid(HRUser user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(HRUser.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);
        String orgIds = request.getParameter("userOrgList.tsDepart.departname");
        List<String> orgIdList = extractIdListByComma(orgIds);
        // 获取用户信息的部门
        if (!CollectionUtils.isEmpty(orgIdList)) {
            CriteriaQuery subCq = new CriteriaQuery(HRUserOrg.class);
            subCq.setProjection(Property.forName("user.id"));
            subCq.in("tsDepart.id", orgIdList.toArray());
            subCq.add();
            cq.add(Property.forName("id").in(subCq.getDetachedCriteria()));
        }
        TSUser sessionUser = ResourceUtil.getSessionUser();
        cq.eq("companyId", sessionUser.getCompanyid());
        cq.eq("deleteFlag", Globals.Delete_Normal);
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除员工信息
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(HRUser user, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        user = systemService.getEntity(HRUser.class, user.getId());
        message = "员工信息删除成功";
        try {
            //删除部门关系
            systemService.executeSql("delete from hr_user_org where user_id=?", user.getId());
            //删除社保信息
            systemService.executeSql("update hr_user_social set delete_flag = 1 where user_id=?", user.getId());
            //删除薪资信息
            systemService.executeSql("update hr_user_salary set delete_flag = 1 where user_id=?", user.getId());
            //删除合同信息
            systemService.executeSql("update hr_user_treaty set delete_flag = 1 where user_id=?", user.getId());
            //删除加班信息
            systemService.executeSql("update hr_overtime set delete_flag=1 where user_id=?", user.getId());
            //删除请假信息
            systemService.executeSql("update hr_leave set delete_flag=1 where user_id=?", user.getId());

            user.setDeleteFlag(Globals.Delete_Forbidden);
            systemService.updateEntitie(user);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            log.info("["+ IpUtil.getIpAddr(request)+"][逻辑删除用户]"+message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "员工信息删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 批量删除员工信息
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message;
        AjaxJson j = new AjaxJson();
        message = "员工信息删除成功";
        try {
            for (String id : ids.split(",")) {
                HRUser user = systemService.getEntity(HRUser.class, id);
                //删除部门关系
                systemService.executeSql("delete from hr_user_org where user_id=?", user.getId());
                //删除社保信息
                systemService.executeSql("update hr_user_social set delete_flag = 1 where user_id=?", user.getId());
                //删除薪资信息
                systemService.executeSql("update hr_user_salary set delete_flag = 1 where user_id=?", user.getId());
                //删除合同信息
                systemService.executeSql("update hr_user_treaty set delete_flag = 1 where user_id=?", user.getId());
                //删除加班信息
                systemService.executeSql("update hr_overtime set delete_flag = 1 where user_id=?", user.getId());
                //删除请假信息
                systemService.executeSql("update hr_leave set delete_flag=1 where user_id=?", user.getId());

                user.setDeleteFlag(Globals.Delete_Forbidden);
                systemService.updateEntitie(user);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
                log.info("["+ IpUtil.getIpAddr(request)+"][逻辑删除用户]"+message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "员工信息删除失败";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 员工信息新增、编辑页面跳转
     */
    @RequestMapping(params = "addOrUpdate")
    public ModelAndView addOrUpdate(HRUser user, HttpServletRequest req) {
        TSUser sessionUser = ResourceUtil.getSessionUser();
        req.setAttribute("companyId", sessionUser.getCompanyid());
        if (StringUtil.isNotEmpty(user.getId())) {
            user = systemService.getEntity(HRUser.class, user.getId());
            req.setAttribute("user", user);
            getDepartInfo(req, user);//部门名称、编码
        }
        return new ModelAndView("hr/user/user");
    }

    private void getDepartInfo(HttpServletRequest req, HRUser user) {
        List<HRUserOrg> departUsers = systemService.findByProperty(HRUserOrg.class, "user.id", user.getId());
        String departIds = "";
        String departName = "";
        if (departUsers.size() > 0) {
            for (HRUserOrg dUser : departUsers) {
                departIds += dUser.getTsDepart().getId() + ",";
                departName += dUser.getTsDepart().getDepartname() + ",";
            }
        }
        req.setAttribute("departIds", departIds);
        req.setAttribute("departName", departName);
    }

    /**
     * 添加、编辑员工信息
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(HRUser user, HttpServletRequest request) throws Exception {
        String message;
        String departIds = oConvertUtils.getString(request.getParameter("departIds"));//部门
        AjaxJson j = new AjaxJson();
        if (StringUtil.isNotEmpty(user.getId())) {
            //保存  员工--部门信息
            systemService.executeSql("delete from hr_user_org where user_id=?", user.getId());
            saveUserOrgList(departIds, user);

            systemService.saveOrUpdate(user);
            message = "员工信息更新成功";
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            TSUser sessionUser = ResourceUtil.getSessionUser();
            user.setDeleteFlag((short) 0);
            user.setCompanyId(sessionUser.getCompanyid());
            user.setJobStatus("3");//默认试用期员工
            Calendar c = Calendar.getInstance();
            c.setTime(user.getJoinTime());
            c.add(Calendar.MONTH, Integer.parseInt(user.getPeriod()));
            user.setFormalDate(c.getTime());//转正时间
            systemService.save(user);
            //保存员工--部门信息
            saveUserOrgList(departIds, user);

            //初始化员工社保信息
            HRUserSocial userSocial = new HRUserSocial();
            userSocial.setUserId(user.getId());
            systemService.save(userSocial);

            message = "员工信息添加成功";
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 保存 用户--组织机构 关系信息
     */
    private void saveUserOrgList(String departIds, HRUser user) {
        List<HRUserOrg> userOrgList = new ArrayList<HRUserOrg>();
        List<String> departIdList = extractIdListByComma(departIds);
        for (String departId : departIdList) {
            TSDepart depart = new TSDepart();
            depart.setId(departId);

            HRUserOrg userOrg = new HRUserOrg();
            userOrg.setUser(user);
            userOrg.setTsDepart(depart);

            userOrgList.add(userOrg);
        }
        if (!userOrgList.isEmpty()) {
            systemService.batchSave(userOrgList);
        }
    }

    /**
     * 请假、合同选择员工列表跳转页面
     */
    @RequestMapping(params = "goUserSelect")
    public ModelAndView goUserSelect(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("hr/user/userSelect");
        String id = oConvertUtils.getString(request.getParameter("id"));
        mv.addObject("id", id);
        return mv;
    }

    /**
     * 新增岗位
     */
    @RequestMapping(params = "addOrUpdatePost")
    public ModelAndView addOrUpdatePost(HttpServletRequest request) {
        return new ModelAndView("hr/user/post");
    }

    /**
     * 保存岗位
     */
    @RequestMapping(params = "savePost")
    @ResponseBody
    public AjaxJson savePost(HRPost post, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        TSUser sessionUser = ResourceUtil.getSessionUser();
        post.setCompanyId(sessionUser.getCompanyid());
        systemService.save(post);
        String message = "岗位信息添加成功";
        systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        j.setMsg(message);
        return j;
    }

    /**
     * 查询岗位信息
     */
    @RequestMapping(params = "getPostByCompanyId")
    @ResponseBody
    public Map<String, Object> getPostByCompanyId() {
        TSUser sessionUser = ResourceUtil.getSessionUser();
        List<HRPost> posts = systemService.findByProperty(HRPost.class, "companyId", sessionUser.getCompanyid());
        Map<String, Object> map = new HashMap<>();
        map.put("data", posts);
        return map;
    }

    /**
     * 转正页面
     */
    @RequestMapping(params = "goUpdateFormal")
    public ModelAndView goUpdateFormal(HttpServletRequest request, String id) {
        HRUser user = systemService.get(HRUser.class, id);
        request.setAttribute("user", user);
        return new ModelAndView("hr/user/formal");
    }

    /**
     * 转正确认
     */
    @RequestMapping(params = "updateFormal")
    @ResponseBody
    public AjaxJson updateFormal(HRUser user) {
        AjaxJson j = new AjaxJson();
        HRUser hrUser = systemService.get(HRUser.class, user.getId());
        hrUser.setJobStatus("6");
        hrUser.setFormalDate(user.getFormalDate());
        systemService.saveOrUpdate(hrUser);
        return j;
    }

    /**
     * 生日提醒
     */
    @RequestMapping(params = "goBirthday")
    public ModelAndView goBirthday() {
        return new ModelAndView("hr/care/birthday");
    }

    @RequestMapping(params = "birthday")
    public void birthday(HRUser user, DataGrid dataGrid, HttpServletRequest request,HttpServletResponse response) {
        CriteriaQuery cq = new CriteriaQuery(HRUser.class, dataGrid);
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);
        String orgIds = request.getParameter("userOrgList.tsDepart.departname");
        List<String> orgIdList = extractIdListByComma(orgIds);
        // 获取用户信息的部门
        if (!CollectionUtils.isEmpty(orgIdList)) {
            CriteriaQuery subCq = new CriteriaQuery(HRUserOrg.class);
            subCq.setProjection(Property.forName("user.id"));
            subCq.in("tsDepart.id", orgIdList.toArray());
            subCq.add();
            cq.add(Property.forName("id").in(subCq.getDetachedCriteria()));
        }
        TSUser sessionUser = ResourceUtil.getSessionUser();
        cq.eq("companyId", sessionUser.getCompanyid());
        cq.eq("deleteFlag", Globals.Delete_Normal);

        Map<String,Object> orderMap = new HashMap<String,Object>();
        orderMap.put("birthday",SortDirection.desc);
        cq.setOrder(orderMap);
        cq.add();
        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 导入功能跳转
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "userController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * 导出excel
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(HRUser user, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(HRUser.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user, request.getParameterMap());
        List<HRUser> users = this.systemService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "员工信息");
        modelMap.put(NormalExcelConstants.CLASS, HRUser.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("员工信息列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
                "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, users);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * 导出excel 使模板
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(HRUser user, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "员工信息");
        modelMap.put(NormalExcelConstants.CLASS, HRUser.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("员工信息列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(),
                "导出信息"));
        modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList());
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    @RequestMapping(params = "importExcel", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<HRUser> listHRUsers = ExcelImportUtil.importExcel(file.getInputStream(), HRUser.class, params);
                for (HRUser user : listHRUsers) {
                    systemService.save(user);
                }
                j.setMsg("文件导入成功！");
            } catch (Exception e) {
                j.setMsg("文件导入失败！");
                log.error(ExceptionUtil.getExceptionMessage(e));
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return j;
    }
}