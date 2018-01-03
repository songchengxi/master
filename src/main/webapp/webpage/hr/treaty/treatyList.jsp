<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="hrTreatyList" checkbox="false" pagination="true" fitColumns="true" title="合同信息"
                    actionUrl="hrTreatyController.do?data" idField="id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="姓名" field="userId" dictionary="hr_user,id,name" width="120"></t:dgCol>
            <t:dgCol title="姓名" field="userName" hidden="true" query="true"></t:dgCol>
            <t:dgCol title="合同编号" field="treatyNo" width="150"></t:dgCol>
            <t:dgCol title="合同类型" field="treatyType" dictionary="treatyType" query="true" width="150"></t:dgCol>
            <t:dgCol title="签订类型" field="signType" dictionary="signType" query="true" width="150"></t:dgCol>
            <t:dgCol title="开始时间" field="treatyStart" formatter="yyyy-MM-dd" width="120"></t:dgCol>
            <t:dgCol title="结束时间" field="treatyEnd" formatter="yyyy-MM-dd" width="120"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <t:dgDelOpt title="删除" url="hrTreatyController.do?doDel&id={id}" urlStyle="background-color:#ec4758;" urlclass="ace_button" urlfont="fa-trash-o"/>
            <t:dgToolBar title="录入" icon="icon-add" url="hrTreatyController.do?addOrUpdate" funname="add"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="icon-edit" url="hrTreatyController.do?addOrUpdate" funname="update"></t:dgToolBar>
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
</script>