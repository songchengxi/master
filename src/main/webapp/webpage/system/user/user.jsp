<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<t:base type="jquery,easyui,tools"></t:base>
    <script>
		function openDepartmentSelect() {
			$.dialog.setting.zIndex = getzIndex(); 
			var companyId = $("#companyid").val();
			$.dialog({content: 'url:companyController.do?companySelect&companyId='+companyId, zIndex: getzIndex(), title: '公司列表', lock: true, width: '400px', height: '350px', opacity: 0.4, button: [
			   {name: '<t:mutiLang langKey="common.confirm"/>', callback: callbackCompanySelect, focus: true},
			   {name: '<t:mutiLang langKey="common.cancel"/>', callback: function (){}}
		   ]}).zindex();
		}

        function callbackCompanySelect() {
            var iframe = this.iframe.contentWindow;
            var treeObj = iframe.$.fn.zTree.getZTreeObj("companySelect");
            var nodes = treeObj.getCheckedNodes(true);
            if (nodes.length > 0) {
                var id = '', name = '';
                for (i = 0; i < nodes.length; i++) {
                    var node = nodes[i];
                    id += node.id;
                    name += node.name;
                }
                $('#companyName').val(name);
                $('#companyName').blur();
                $('#companyid').val(id);
            }
        }

        function callbackClean() {
            $('#companyName').val('');
            $('#companyid').val('');
            $('#roleid').val('');
            $('#roleName').val('');
        }

        function openRoleSelect() {
            var companyId = $("#companyid").val();
            if ("" == companyId) {
                tip("请选择公司");
                return;
            }
            $.dialog.setting.zIndex = getzIndex();
            var roleids = $("#roleid").val();
            $.dialog({
                content: 'url:userController.do?roles&ids=' + roleids + '&companyId=' + companyId,
                zIndex: getzIndex(),
                title: '角色列表',
                lock: true,
                width: '400px',
                height: '350px',
                opacity: 0.4,
                button: [
                    {name: '<t:mutiLang langKey="common.confirm"/>', callback: callbackRoleSelect, focus: true},
                    {name: '<t:mutiLang langKey="common.cancel"/>', callback: function () {}}
                ]
            }).zindex();
        }

        function callbackRoleSelect() {
            var iframe = this.iframe.contentWindow;
            var roleName = iframe.getroleListSelections('roleName');
            if ($('#roleName').length >= 1) {
                $('#roleName').val(roleName);
                $('#roleName').blur();
            }
            if ($("input[name='roleName']").length >= 1) {
                $("input[name='roleName']").val(roleName);
                $("input[name='roleName']").blur();
            }
            var id = iframe.getroleListSelections('id');
            if (id !== undefined && id != "") {
                $('#roleid').val(id);
            }
        }

        function roleClean() {
            $('#roleid').val('');
            $('#roleName').val('');
        }

        $(function () {
            $("#companyName").prev().hide();
        });
    </script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="userController.do?saveUser">
	<input id="id" name="id" type="hidden" value="${user.id }">
	<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right" width="25%" nowrap>
                <label class="Validform_label">  <t:mutiLang langKey="common.username"/>:  </label>
            </td>
			<td class="value" width="85%">
                <c:if test="${user.id!=null }"> ${user.userName } </c:if>
                <c:if test="${user.id==null }">
                    <input id="userName" class="inputxt" name="userName" validType="t_s_base_user,userName,id" value="${user.userName }" datatype="s2-10" />
                    <span class="Validform_checktip"> <t:mutiLang langKey="username.rang2to10"/></span>
                </c:if>
            </td>
		</tr>
		<tr>
			<td align="right" width="10%" nowrap><label class="Validform_label"> <t:mutiLang langKey="common.real.name"/>: </label></td>
			<td class="value" width="10%">
                <input id="realName" class="inputxt" name="realName" value="${user.realName }" datatype="s2-10">
                <span class="Validform_checktip"><t:mutiLang langKey="fill.realname"/></span>
            </td>
		</tr>
		<c:if test="${user.id==null }">
			<tr>
				<td align="right"><label class="Validform_label"> <t:mutiLang langKey="common.password"/>: </label></td>
				<td class="value">
                    <input type="password" class="inputxt" value="" name="password" plugin="passwordStrength" datatype="*6-18" errormsg="" />
                    <span class="passwordStrength" style="display: none;">
                        <span><t:mutiLang langKey="common.weak"/></span>
                        <span><t:mutiLang langKey="common.middle"/></span>
                        <span class="last"><t:mutiLang langKey="common.strong"/></span>
                    </span>
                    <span class="Validform_checktip"> <t:mutiLang langKey="password.rang6to18"/></span>
                </td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> <t:mutiLang langKey="common.repeat.password"/>: </label></td>
				<td class="value">
                    <input id="repassword" class="inputxt" type="password" value="${user.password}" recheck="password" datatype="*6-18" errormsg="两次输入的密码不一致！">
                    <span class="Validform_checktip"><t:mutiLang langKey="common.repeat.password"/></span>
                </td>
			</tr>
		</c:if>
		<tr>
			<td align="right"><label class="Validform_label"> <t:mutiLang langKey="common.company"/>: </label></td>
			<td class="value">
				<c:if test="${user.id!=null}">${companyName}
                    <input type="hidden" id="companyid" name="companyid" value="${user.companyid}">
                </c:if>
				<c:if test="${user.id==null}">
					<input id="companyName" name="companyName" type="text" readonly="readonly" class="inputxt"
						   datatype="*" value="${companyName}">
					<input id="companyid" name="companyid" type="hidden" value="${user.companyid}">
					<a href="#" class="easyui-linkbutton" plain="true" icon="icon-search" id="departSearch"
					   onclick="openDepartmentSelect()">选择</a>
					<a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="departRedo"
					   onclick="callbackClean()">清空</a>
				</c:if>
			</td>
		</tr>
        <tr>
            <td align="right"><label class="Validform_label"> <t:mutiLang langKey="common.role"/>: </label></td>
            <td class="value" nowrap>
                <input id="roleid" name="roleid" type="hidden" value="${roleId}"/>
                <input id="roleName" name="roleName" class="inputxt" value="${roleName }" readonly="readonly" datatype="*" />
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-search" id="roleSearch"
                   onclick="openRoleSelect()">选择</a>
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="roleRedo"
                   onclick="roleClean()">清空</a>
                <span class="Validform_checktip"><t:mutiLang langKey="role.muti.select"/></span>
            </td>
		</tr>
		<tr>
			<td align="right" nowrap><label class="Validform_label">  <t:mutiLang langKey="common.phone"/>: </label></td>
			<td class="value">
                <input class="inputxt" name="mobilePhone" value="${user.mobilePhone}" datatype="m" errormsg="手机号码不正确" ignore="ignore">
                <span class="Validform_checktip"></span>
            </td>
		</tr>
		<tr>
			<td align="right"><label class="Validform_label"> <t:mutiLang langKey="common.tel"/>: </label></td>
			<td class="value">
                <input class="inputxt" name="officePhone" value="${user.officePhone}" datatype="n" errormsg="办公室电话不正确" ignore="ignore">
                <span class="Validform_checktip"></span>
            </td>
		</tr>
		<tr>
			<td align="right"><label class="Validform_label"> <t:mutiLang langKey="common.common.mail"/>: </label></td>
			<td class="value">
                <input class="inputxt" name="email" value="${user.email}" datatype="e" errormsg="邮箱格式不正确!" ignore="ignore">
                <span class="Validform_checktip"></span>
            </td>
		</tr>
        <tr>
            <td align="right"><label class="Validform_label"> <t:mutiLang langKey="common.common.dev"/>: </label></td>
            <td class="value">

                <t:dictSelect id="devFlag" field="devFlag" typeGroupCode="dev_flag" hasLabel="false" defaultVal="${user.devFlag}" type="radio" datatype="*"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
	</table>
</t:formvalid>
</body>