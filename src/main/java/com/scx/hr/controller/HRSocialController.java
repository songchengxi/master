package com.scx.hr.controller;

import com.scx.hr.entity.HRSocialBillDetail;
import com.scx.hr.entity.HRSocialSecurity;
import com.scx.hr.entity.HRUserSocial;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * 社保设置--类型
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
     * 社保项目
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
        // TODO: 2018/1/30  更新员工信息


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
            // TODO: 2018/1/30  更新员工信息


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
                String sql = "SELECT a.base, SUM(a.company_val) comVal,SUM(a.user_val) userVal FROM hr_social_security a WHERE a.parent_id = ( " +
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

    /**
     * 社保月账单
     */
    @RequestMapping(params = "bill")
    public ModelAndView bill() {
        return new ModelAndView("hr/social/billList");
    }

    /**
     * 月账单数据
     */
    @RequestMapping(params = "billData")
    public void billData(HttpServletResponse response, DataGrid dataGrid) {
        TSUser user = ResourceUtil.getSessionUser();
        String sql = "SELECT COUNT(d.id) count, d.month month,SUM(d.social_com_val) social_com_val, " +
                "SUM(d.social_user_val) social_user_val,SUM(d.fund_com_val) fund_com_val, " +
                "SUM(d.fund_user_val) fund_user_val FROM hr_social_bill_detail d " +
                "WHERE d.company_id = ? GROUP BY d.month ORDER BY d.month DESC ";

        List<Map<String, Object>> resultList = systemService.findForJdbcParam(sql, dataGrid.getPage(), dataGrid.getRows(), user.getCompanyid());
        //将List转换成JSON存储
        List<Map<String, Object>> socialBill = new ArrayList<Map<String, Object>>();
        if (resultList != null && resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> obj = resultList.get(i);
                Map<String, Object> n = new HashMap<String, Object>();
                n.put("month", String.valueOf(obj.get("month")));
                n.put("count", String.valueOf(obj.get("count")));
                n.put("socialComVal", String.valueOf(obj.get("social_com_val")));
                n.put("socialUserVal", String.valueOf(obj.get("social_user_val")));
                n.put("fundComVal", String.valueOf(obj.get("fund_com_val")));
                n.put("fundUserVal", String.valueOf(obj.get("fund_user_val")));
                n.put("id", UUID.randomUUID().toString());
                socialBill.add(n);
            }
        }
        dataGrid.setResults(socialBill);
        String countSql = "SELECT COUNT(*) FROM (SELECT d.month FROM hr_social_bill_detail d WHERE d.company_id = ? GROUP BY d.month) s";
        Long count = systemService.getCountForJdbcParam(countSql, new Object[]{user.getCompanyid()});
        dataGrid.setTotal(Integer.parseInt(String.valueOf(count)));
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 生成账单页面
     */
    @RequestMapping(params = "goAddBill")
    public String goAddBill(ModelMap modelMap) {
        String max = DateUtils.date2Str(DateUtils.date_sdf);
        modelMap.put("max", max);
        return "hr/social/billAdd";
    }

    /**
     * 生成账单
     */
    @RequestMapping(params = "addBill")
    @ResponseBody
    public AjaxJson addBill(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String message;
        String month = request.getParameter("month");
        TSUser sessionUser = ResourceUtil.getSessionUser();
        List<Object> obj = systemService.findHql("from HRSocialBillDetail where companyId = ? and month = ?", sessionUser.getCompanyid(), month);
        if (!obj.isEmpty()) {
            message = "该月账单已生成";
            j.setMsg(message);
            return j;
        }
        //离职员工、已删除员工排除
        String sql = "SELECT s.user_id,s.social_com_val,s.social_user_val,s.fund_com_val,s.fund_user_val " +
                "FROM hr_user_social s,hr_user u " +
                "WHERE u.id = s.user_id AND u.job_status <> '9' AND u.delete_flag = 0 AND s.company_id = ? AND s.social_start <= ? ";
        List<Map<String, Object>> socialList = systemService.findForJdbc(sql, sessionUser.getCompanyid(), month);
        for (Map<String, Object> map : socialList) {
            HRSocialBillDetail detail = new HRSocialBillDetail();
            detail.setCompanyId(sessionUser.getCompanyid());
            detail.setUserId(String.valueOf(map.get("user_id")));
            detail.setMonth(month);
            detail.setSocialComVal(Double.parseDouble(String.valueOf(map.get("social_com_val"))));
            detail.setSocialUserVal(Double.parseDouble(String.valueOf(map.get("social_user_val"))));
            detail.setFundComVal(Double.parseDouble(String.valueOf(map.get("fund_com_val"))));
            detail.setFundUserVal(Double.parseDouble(String.valueOf(map.get("fund_user_val"))));
            systemService.save(detail);
        }
        message = "生成账单成功";
        systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        j.setMsg(message);
        return j;
    }

    /**
     * 账单重新生成
     */
    @RequestMapping(params = "rebuild")
    @ResponseBody
    public AjaxJson rebuild(HttpServletRequest request) {
        TSUser sessionUser = ResourceUtil.getSessionUser();

        AjaxJson j = new AjaxJson();
        String month = request.getParameter("month");
        //删除以前的账单
        systemService.executeSql("delete from hr_social_bill_detail where company_id = ? AND month = ?", sessionUser.getCompanyid(), month);

        //重新生成账单 离职员工、已删除员工排除
        String sql = "SELECT s.user_id,s.social_com_val,s.social_user_val,s.fund_com_val,s.fund_user_val " +
                "FROM hr_user_social s,hr_user u " +
                "WHERE u.id = s.user_id AND u.job_status <> '9' AND u.delete_flag = 0 AND s.company_id = ? AND s.social_start <= ? ";
        List<Map<String, Object>> socialList = systemService.findForJdbc(sql, sessionUser.getCompanyid(), month);
        for (Map<String, Object> map : socialList) {
            HRSocialBillDetail detail = new HRSocialBillDetail();
            detail.setCompanyId(sessionUser.getCompanyid());
            detail.setUserId(String.valueOf(map.get("user_id")));
            detail.setMonth(month);
            detail.setSocialComVal(Double.parseDouble(String.valueOf(map.get("social_com_val"))));
            detail.setSocialUserVal(Double.parseDouble(String.valueOf(map.get("social_user_val"))));
            detail.setFundComVal(Double.parseDouble(String.valueOf(map.get("fund_com_val"))));
            detail.setFundUserVal(Double.parseDouble(String.valueOf(map.get("fund_user_val"))));
            systemService.save(detail);
        }

        String msg = month + " 账单生成成功";
        systemService.addLog(msg, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        j.setMsg(msg);
        return j;
    }

    /**
     * 详细账单
     */
    @RequestMapping(params = "goBillDetail")
    public ModelAndView goBillDetail(HttpServletRequest request) {
        String month = request.getParameter("month");
        request.setAttribute("month", month);
        return new ModelAndView("hr/social/billDetail");
    }

    /**
     * 详细账单数据
     */
    @RequestMapping(params = "billDetail")
    public void billDetail(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        TSUser user = ResourceUtil.getSessionUser();
        String month = request.getParameter("month");
        String sql = "SELECT * FROM ( " +
                "SELECT u.*,GROUP_CONCAT(d.departname) AS departName FROM ( " +
                " SELECT u.id AS uid,u.name,u.join_time,u.job_status,o.org_id FROM hr_user u LEFT JOIN hr_user_org o ON u.id=o.user_id " +
                "  WHERE u.company_id = ? " +
                ") u LEFT JOIN t_s_depart d ON u.org_id = d.ID GROUP BY u.uid " +
                ") f RIGHT JOIN (SELECT * FROM hr_social_bill_detail d WHERE d.month = ? ) s ON s.user_id = f.uid ";
        List<Map<String, Object>> resultList = systemService.findForJdbcParam(sql, dataGrid.getPage(), dataGrid.getRows(), user.getCompanyid(), month);
        //将List转换成JSON存储
        List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
        if (resultList != null && resultList.size() > 0) {
            for (Map<String, Object> obj : resultList) {
                Map<String, Object> n = new HashMap<String, Object>();
                n.put("id", String.valueOf(obj.get("uid")));
                n.put("userName", String.valueOf(obj.get("name")));
                n.put("departName", String.valueOf(obj.get("departName")));
                n.put("joinTime", String.valueOf(obj.get("join_time")));
                n.put("jobStatus", String.valueOf(obj.get("job_status")));
                n.put("socialComVal", String.valueOf(obj.get("social_com_val")));
                n.put("socialUserVal", String.valueOf(obj.get("social_user_val")));
                n.put("fundComVal", String.valueOf(obj.get("fund_com_val")));
                n.put("fundUserVal", String.valueOf(obj.get("fund_user_val")));
                detailList.add(n);
            }
        }
        dataGrid.setResults(detailList);
        String countSql = "SELECT count(*) FROM ( " +
                "SELECT u.*,GROUP_CONCAT(d.departname) AS departName FROM ( " +
                " SELECT u.id AS uid,u.name,u.join_time,u.job_status,o.org_id FROM hr_user u LEFT JOIN hr_user_org o ON u.id=o.user_id " +
                "  WHERE u.company_id = ? " +
                ") u LEFT JOIN t_s_depart d ON u.org_id = d.ID GROUP BY u.uid " +
                ") f RIGHT JOIN (SELECT * FROM hr_social_bill_detail d WHERE d.month = ? ) s ON s.user_id = f.uid ";
        Long count = systemService.getCountForJdbcParam(countSql, new Object[]{user.getCompanyid(), month});
        dataGrid.setTotal(Integer.parseInt(String.valueOf(count)));
//        dataGrid.setFooter("socialComVal,socialUserVal,fundComVal,fundUserVal,userName:合计");
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 详细导出
     */
    @RequestMapping(params = "exportDetail")
    @ResponseBody
    public void exportParts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String month = request.getParameter("month");
        response.setContentType("application/vnd.ms-excel");

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        String sheetName = month + "社保详细信息";
        HSSFSheet sheet = workbook.createSheet(sheetName);
        //设置样式
        sheet.setColumnWidth(0, 20 * 250);
        sheet.setColumnWidth(1, 20 * 250);
        sheet.setDefaultColumnStyle(0, cellStyle);
        sheet.setDefaultColumnStyle(1, cellStyle);

        HSSFRow row1 = sheet.createRow(0);
        row1.setHeight((short) 500);
        HSSFCell cell = row1.createCell(0);
        cell.setCellValue(month + "社保详细信息");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));//合并单元格

        HSSFRow row2 = sheet.createRow(1);
        String[] title = {"姓名", "部门", "入职日期", "在职状态", "社保公司缴费数额", "社保个人缴纳数额", "公积金公司缴纳数额", "公积金个人缴纳数额"};
        for (int i = 0; i < title.length; i++) {
            row2.createCell(i).setCellValue(title[i]);
        }

        TSUser user = ResourceUtil.getSessionUser();
        String sql = "SELECT * FROM ( " +
                "SELECT u.*,GROUP_CONCAT(d.departname) AS departName FROM ( " +
                " SELECT u.id AS uid,u.name,u.join_time,u.job_status,o.org_id FROM hr_user u LEFT JOIN hr_user_org o ON u.id=o.user_id " +
                "  WHERE u.company_id = ? " +
                ") u LEFT JOIN t_s_depart d ON u.org_id = d.ID GROUP BY u.uid " +
                ") f RIGHT JOIN (SELECT * FROM hr_social_bill_detail d WHERE d.month = ? ) s ON s.user_id = f.uid ";
        List<Map<String, Object>> resultList = systemService.findForJdbc(sql, user.getCompanyid(), month);
        int i = 2;
        for (Map map : resultList) {
            HSSFRow row = sheet.createRow(i);
            row.createCell(0).setCellValue(String.valueOf(map.get("name")));
            row.createCell(1).setCellValue(String.valueOf(map.get("departName")));
            row.createCell(2).setCellValue(String.valueOf(map.get("join_time")));
            String jobStatus = String.valueOf(map.get("job_status"));
            if ("3".equals(jobStatus)) {
                row.createCell(3).setCellValue("试用期");
            } else if ("6".equals(jobStatus)) {
                row.createCell(3).setCellValue("正式");
            } else if ("9".equals(jobStatus)) {
                row.createCell(3).setCellValue("离职");
            }
            row.createCell(4).setCellValue(String.valueOf(map.get("social_com_val")));
            row.createCell(5).setCellValue(String.valueOf(map.get("social_user_val")));
            row.createCell(6).setCellValue(String.valueOf(map.get("fund_com_val")));
            row.createCell(7).setCellValue(String.valueOf(map.get("fund_user_val")));
            i++;
        }

        HSSFCellStyle timeStyle = workbook.createCellStyle();
        timeStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        timeStyle.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);

        HSSFRow row3 = sheet.createRow(i);
        sheet.addMergedRegion(new CellRangeAddress(i, i + 1, 0, 7));
        HSSFCell cell1 = row3.createCell(0);
        cell1.setCellValue("导出时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        cell1.setCellStyle(timeStyle);

        String fileName = java.net.URLEncoder.encode(month + "社保详细信息", "UTF-8");
        response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }
}