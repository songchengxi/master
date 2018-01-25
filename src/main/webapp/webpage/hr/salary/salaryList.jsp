<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="salaryList" title="薪酬列表" queryMode="group"
                    actionUrl="salaryController.do?salaryGrid&parentId=${parentId}" idField="id">
            <t:dgCol title="common.code" field="id" hidden="true"></t:dgCol>
            <t:dgCol title="薪酬名称" field="name" width="80"></t:dgCol>
            <t:dgCol title="是否启用" field="status" replace="已启用_Y,已停用_N" style="background:red;color:#FFFFFF;_N" width="60"></t:dgCol>
            <t:dgCol title="是否系统" field="isSys" hidden="true" width="60"></t:dgCol>
            <t:dgCol title="common.operation" field="opt"></t:dgCol>
            <t:dgFunOpt funname="openAndCloseSalary(id,status)" title="启用" exp="status#eq#N" urlclass="ace_button" urlStyle="background-color:#18a689;" urlfont="fa-cog"></t:dgFunOpt>
            <t:dgFunOpt funname="openAndCloseSalary(id,status)" title="停用" exp="status#eq#Y" urlclass="ace_button" urlStyle="background-color:#18a689;" urlfont="fa-cog"></t:dgFunOpt>
            <t:dgFunOpt funname="delSalary(id,status)" title="删除" exp="isSys#ne#Y" urlclass="ace_button" urlStyle="background-color:#ec4758;" urlfont="fa-trash-o"></t:dgFunOpt>
            <t:dgToolBar title="common.add.param" langArg="薪酬" icon="icon-add" url="salaryController.do?addOrUpdate&parentId=${parentId}" funname="add"></t:dgToolBar>
            <t:dgToolBar title="common.edit.param" langArg="薪酬" icon="icon-edit" url="salaryController.do?addOrUpdate&parentId=${parentId}" funname="update"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>
<script>
    function delSalary(id, status) {
        var url = "salaryController.do?del&id=" + id;
        if ("Y" == status) {
            createdialog('删除确认 ', '将删除所有员工的该薪酬信息（不可恢复）。确定删除吗 ?', url, "salaryList");
        } else {
            createdialog('删除确认 ', '将删除所有员工的该薪酬信息（不可恢复）。确定删除吗 ?', url, "salaryList");
        }
    }

    function openAndCloseSalary(id, status) {
        var url = "salaryController.do?openAndClose&id=" + id;
        if ("Y" == status) {
            createdialog('停用确认 ', '将删除所有员工的该薪酬信息（不可恢复）。确定停用吗 ?', url, "salaryList");
        } else {
            createdialog('启用确认 ', '确定开启吗 ?', url, "salaryList");
        }
    }
</script>