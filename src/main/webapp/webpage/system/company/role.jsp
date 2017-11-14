<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<t:base type="jquery,easyui,tools"></t:base>
<script type="text/javascript">
	$(function() {
		$('#cc').combotree({
			url : 'companyController.do?selCompany',
            width: 155,
            onSelect : function(node) {

            }
        });

        $('#parentRole').attr("readonly", "readroly");
        if ($("#id").val()) {
            $('#cc').combotree('disable');
        }
        if ('${empty pid}' == 'false') { // 设置新增页面时的父级
            $('#parentRole').val('${pid}');
        }
	});
</script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="div" dialog="true" action="companyRoleController.do?save">
	<input id="id" name="id" type="hidden" value="${role.id }">
    <fieldset class="step">
        <div class="form">
            <%--todo--%>
            <label class="Validform_label"> 公司: </label>
            <input id="cc" name="companyId" value="${role.companyId}">
        </div>
        <div class="form">
            <label class="Validform_label"> 上级角色: </label>
            <input id="parentRole" name="parentRole.id" class="inputxt" value="${role.parentRole.id}">
        </div>
        <div class="form">
            <label class="Validform_label"> 角色名称: </label>
            <input name="roleName" class="inputxt" value="${role.roleName }" datatype="*">
        </div>
        <div class="form">
            <label class="Validform_label"> 角色编码: </label>
            <input name="roleCode" class="inputxt" value="${role.roleCode }">
        </div>
	</fieldset>
</t:formvalid>
</body>
</html>
