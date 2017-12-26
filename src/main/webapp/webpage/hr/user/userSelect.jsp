<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid pagination="true" name="userList" title="员工选择" actionUrl="hrUserController.do?datagrid"
                    idField="id" checkbox="true" singleSelect="true" showRefresh="false" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true"></t:dgCol>
            <t:dgCol title="姓名" field="name" width="50" query="true" ></t:dgCol>
            <t:dgCol title="部门" field="userOrgList.tsDepart.departname" width="50" query="true"></t:dgCol>
            <t:dgCol title="入职日期" field="joinTime" formatter="yyyy-MM-dd" width="50"></t:dgCol>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var id = "${id}";
        $("#userList").datagrid("selectRecord", id);

        $("#searchColums input[name='userOrgList.tsDepart.departname']").combotree({
            url : 'orgController.do?selOrgTree',
            width: 155,
            onSelect : function(node) {

            }
        });
    });
</script>