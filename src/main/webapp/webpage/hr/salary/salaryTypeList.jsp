<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div id="main_salaryType_list" class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="salaryTypeList" title="薪酬结构" actionUrl="salaryController.do?salaryTypeGrid"
                    idField="id" treegrid="false" pagination="false" onClick="querySalaryByRowData"
                    onLoadSuccess="loadSuccess" queryMode="group" btnCls="bootstrap btn btn-info btn-xs">
            <t:dgCol title="common.code" field="id" hidden="true"></t:dgCol>
            <t:dgCol title="分类名称" field="name" width="300"></t:dgCol>
            <t:dgCol title="是否系统" field="isSys" hidden="true" width="300"></t:dgCol>
            <t:dgCol title="common.operation" field="opt" width="100"></t:dgCol>
            <t:dgFunOpt funname="queryTypeValue(id,name)" title="common.type.view" urlclass="ace_button"  urlfont="fa-search"></t:dgFunOpt>
            <%--<t:dgDelOpt url="salaryController.do?delType&id={id}" exp="isSys#ne#Y" title="common.delete" urlclass="ace_button" urlStyle="background-color:#ec4758;" urlfont="fa-trash-o"></t:dgDelOpt>--%>
            <%--<t:dgToolBar title="common.add.param" langArg="薪酬分类" icon="fa fa-plus" url="salaryController.do?addOrUpdateType" funname="add"></t:dgToolBar>--%>
            <%--<t:dgToolBar title="common.edit" icon="fa fa-edit" url="salaryController.do?addOrUpdateType" funname="update"></t:dgToolBar>--%>
        </t:datagrid>
    </div>
</div>

<div data-options="region:'east',
	title:'mytitle',
	collapsed:true,
	split:true,
	border:false,
	onExpand : function(){
		li_east = 1;
	},
	onCollapse : function() {
	    li_east = 0;
	}"
     style="width: 600px; overflow: hidden;" id="eastPanel">
    <div class="easyui-panel" style="padding:0;border:0" fit="true" border="false" id="salaryListPanel"></div>
</div>

<script type="text/javascript">
    $(function() {
        var li_east = 0;
    });

    function querySalaryByRowData(rowIndex, rowData) {
        queryTypeValue(rowData.id, rowData.name);
    }

    function queryTypeValue(id, name) {
        var title = '薪酬分类: ' + name;
        if (li_east == 0) {
            $('#main_salaryType_list').layout('expand', 'east');
        }
        $('#main_salaryType_list').layout('panel', 'east').panel('setTitle', title);
        $('#salaryListPanel').panel("refresh", "salaryController.do?goSalary&parentId=" + id);
    }
    function loadSuccess() {
        $('#main_salaryType_list').layout('panel', 'east').panel('setTitle', "");
        $('#main_salaryType_list').layout('collapse', 'east');
        $('#salaryListPanel').empty();
    }
</script>
