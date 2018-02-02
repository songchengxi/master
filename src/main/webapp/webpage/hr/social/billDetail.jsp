<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="billDetailList" checkbox="false" pagination="true" fitColumns="true" title="参保账单"
                    actionUrl="hrSocialController.do?billDetail&month=${month}" idField="id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" width="120"></t:dgCol>
            <t:dgCol title="姓名" field="userName"  width="150"></t:dgCol>
            <t:dgCol title="部门" field="departName" width="150"></t:dgCol>
            <t:dgCol title="入职日期" field="joinTime" formatter="yyyy-MM-dd" width="150"></t:dgCol>
            <t:dgCol title="在职状态" field="jobStatus" dictionary="jobStatus" width="120"></t:dgCol>
            <t:dgCol title="社保公司缴费数额" field="socialComVal" width="120"></t:dgCol>
            <t:dgCol title="社保个人缴纳数额" field="socialUserVal" width="120"></t:dgCol>
            <t:dgCol title="公积金公司缴纳数额" field="fundComVal" width="120"></t:dgCol>
            <t:dgCol title="公积金个人缴纳数额" field="fundUserVal" width="120"></t:dgCol>
            <t:dgToolBar title="导出" icon="icon-putout" funname="exportDetail"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript" src="plug-in/laydate/laydate.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $(".Wdate").removeAttr("onclick");

        //同时绑定多个
        lay('.Wdate').each(function(){
            laydate.render({
                elem: this
                ,trigger: 'click'
                ,calendar: true
            });
        });
    });

    //导出
    function exportDetail(){
        JeecgExcelExport("hrSocialController.do?exportDetail&month=${month}", "billDetailList");
    }
</script>