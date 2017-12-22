<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="hrLeaveList" checkbox="false" pagination="true" fitColumns="true" title="请假信息"
                    actionUrl="hrLeaveController.do?datagrid" idField="id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="姓名" field="userName" query="true" queryMode="single" width="120"></t:dgCol>
            <%--<t:dgCol title="部门" field="departName" query="true" queryMode="single" width="150"></t:dgCol>--%>
            <t:dgCol title="请假类型" field="leaveType" query="true" queryMode="single" dictionary="leaveType" width="150"></t:dgCol>
            <t:dgCol title="开始时间" field="startTime" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group" width="120"></t:dgCol>
            <t:dgCol title="结束时间" field="endTime" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group" width="120"></t:dgCol>
            <t:dgCol title="时长" field="duration" width="120"></t:dgCol>
            <t:dgCol title="天数" field="dayNum" width="120"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <t:dgDelOpt title="删除" url="hrLeaveController.do?doDel&id={id}" urlStyle="background-color:#ec4758;" urlclass="ace_button" urlfont="fa-trash-o"/>
            <t:dgToolBar title="录入" icon="icon-add" url="hrLeaveController.do?addOrUpdate" funname="add"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="icon-edit" url="hrLeaveController.do?addOrUpdate" funname="update"></t:dgToolBar>
            <%--<t:dgToolBar title="批量删除" icon="icon-remove" url="hrLeaveController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>--%>
            <%--<t:dgToolBar title="查看" icon="icon-search" url="hrLeaveController.do?goUpdate" funname="detail"></t:dgToolBar>--%>
            <%--<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
            <%--<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>--%>
            <%--<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
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

    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'hrLeaveController.do?upload', "hrLeaveList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("hrLeaveController.do?exportXls", "hrLeaveList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("hrLeaveController.do?exportXlsByT", "hrLeaveList");
    }
</script>