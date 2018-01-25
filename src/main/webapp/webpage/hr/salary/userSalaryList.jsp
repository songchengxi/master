<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="userSalaryList" checkbox="false" pagination="true" fitColumns="true" title="薪酬信息"
                    actionUrl="salaryController.do?userSalaryData" idField="id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" width="150"></t:dgCol>
            <t:dgCol title="姓名" field="name" query="true" width="150"></t:dgCol>
            <t:dgCol title="部门" field="userOrgList.tsDepart.departname" query="true" width="150"></t:dgCol>
            <t:dgCol title="入职日期" field="joinTime" formatter="yyyy-MM-dd" width="150"></t:dgCol>
            <t:dgCol title="在职状态" field="jobStatus" dictionary="jobStatus" width="120"></t:dgCol>
            <t:dgCol title="固定工资" field="fixSalary" width="150"></t:dgCol>
            <t:dgCol title="奖励工资" field="rewardSalary" width="150"></t:dgCol>
            <t:dgToolBar title="编辑" icon="icon-edit" url="salaryController.do?goUpdateUserSalary" funname="update"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("#searchColums input[name='userOrgList.tsDepart.departname']").combotree({
            url: 'orgController.do?selOrgTree',
            width: 155,
            onSelect: function (node) {

            }
        });
    });
</script>