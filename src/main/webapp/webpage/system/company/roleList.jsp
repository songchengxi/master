<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker,ztree"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="companyRoleList" title="公司角色" fitColumns="true" actionUrl="companyRoleController.do?roleTree" treegrid="true" idField="id" pagination="false">
            <t:dgCol title="ID" field="id" treefield="id" hidden="true"></t:dgCol>
            <t:dgCol title="公司名称" field="companyId" dictionary="sys_company,id,name" treefield="text" width="120"></t:dgCol>
            <t:dgCol title="角色名称" field="roleName" treefield="fieldMap.roleName" width="100"></t:dgCol>
            <t:dgCol title="角色编码" field="roleCode" treefield="fieldMap.roleCode" width="100"></t:dgCol>
            <t:dgCol title="common.operation" field="opt" width="150"></t:dgCol>
            <t:dgFunOpt funname="delRole(id)" title="common.delete" urlclass="ace_button"  urlStyle="background-color:#ec4758;" urlfont="fa-trash-o"></t:dgFunOpt>
            <t:dgFunOpt funname="userListbyrole(id,src)" title="common.user" urlclass="ace_button"  urlfont="fa-user"></t:dgFunOpt>
            <t:dgFunOpt funname="setfunbyrole(id,src)" title="permission.set" urlclass="ace_button" urlStyle="background-color:#18a689;"  urlfont="fa-cog"></t:dgFunOpt>
        </t:datagrid>
        <div id="departListtb" style="padding: 3px; height: 25px">
            <div style="float: left;">
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-add" onclick="addRole()"><t:mutiLang langKey="common.add.param" langArg="角色"/></a>
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit" onclick="update('<t:mutiLang langKey="common.edit.param" langArg="common.department"/>','companyRoleController.do?goUpdate','companyRoleList','680px','450px')"><t:mutiLang langKey="common.edit.param" langArg="角色"/></a>
            </div>
        </div>
    </div>
</div>
<div region="east" style="width: 600px;" split="true">
    <div tools="#tt" class="easyui-panel" title='<t:mutiLang langKey="permission.set"/>' style="padding: 10px;" fit="true" border="false" id="function-panel"></div>
</div>
<div id="tt"></div>
</div>
<script type="text/javascript">
    function addRole() {
        var id = "";
        var rowsData = $('#companyRoleList').datagrid('getSelections');
        if (rowsData.length == 1) {
            id = rowsData[0].id;
        }
        var url = "companyRoleController.do?goAdd&id=" + id;
        add('<t:mutiLang langKey="common.add.param" langArg="角色"/>', url, "companyRoleList","660px","480px");
    }

    //设置权限
    function setfunbyrole(id,roleName) {
        $("#function-panel").panel(
                {
                    title :roleName+ ':' + '<t:mutiLang langKey="current.permission"/>',
                    href:"companyRoleController.do?fun&roleId=" + id
                }
        );
        //$('#function-panel').panel("refresh" );
    }

    //用户
    function userListbyrole(id,roleName) {
        $("#function-panel").panel(
                {
                    title :roleName+ ':' + '<t:mutiLang langKey="common.user"/>',
                    href:"companyRoleController.do?userList&roleId=" + id
                }
        );
        //$('#function-panel').panel("refresh" );
    }

    //删除角色
    function delRole(id){
        var tabName= 'companyRoleList';
        var url= 'companyRoleController.do?delRole&id='+id;
        $.dialog.confirm('<t:mutiLang langKey="confirm.delete.this.record"/>', function(){
            doSubmit(url,tabName);
            rowid = '';
            $("#function-panel").html("");//删除角色后，清空对应的权限
        }, function(){
        });
    }
</script>
