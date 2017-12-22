<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="orgUserList" checkbox="false" pagination="true" fitColumns="true" title="员工信息"
                    actionUrl="orgController.do?userList&departId=${departId}" idField="user.id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="user.id" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="姓名" field="user.name" width="120"></t:dgCol>
            <t:dgCol title="性别" field="user.sex" dictionary="sex" width="120"></t:dgCol>
            <t:dgCol title="联系电话" field="user.phone" width="120"></t:dgCol>
            <t:dgCol title="部门" field="user.userOrgList.tsDepart.departname" width="120"></t:dgCol>
            <t:dgCol title="职位" field="user.position" width="120"></t:dgCol>
            <t:dgCol title="职称" field="user.title" width="120"></t:dgCol>
            <t:dgCol title="入职日期" field="user.joinTime" formatter="yyyy-MM-dd" width="120"></t:dgCol>
            <t:dgCol title="在职状态" field="user.jobStatus" width="120"></t:dgCol>
            <t:dgCol title="合同期限" field="user.contractTerm" formatter="yyyy-MM-dd" width="120"></t:dgCol>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {

    });
</script>