<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="hrSocialList" checkbox="false" pagination="true" fitColumns="true" title="社保信息"
                    actionUrl="hrSocialController.do?userSocialList" idField="id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" width="120"></t:dgCol>
            <t:dgCol title="姓名" field="userName" query="true" width="120"></t:dgCol>
            <t:dgCol title="部门" field="departName" query="true" width="150"></t:dgCol>
            <t:dgCol title="入职日期" field="joinTime" width="150"></t:dgCol>
            <t:dgCol title="起缴月" field="socialStart" width="150"></t:dgCol>
            <t:dgCol title="社保缴费基数（元）" field="socialBase" width="150"></t:dgCol>
            <t:dgCol title="社保公司缴纳数额（元）" field="departName" width="150"></t:dgCol>
            <t:dgCol title="社保个人缴纳数额（元）" field="departName" width="150"></t:dgCol>
            <t:dgCol title="公积金缴费基数（元）" field="fundBase" width="150"></t:dgCol>
            <t:dgCol title="公积金公司缴纳数额（元）" field="fundComVal" width="150"></t:dgCol>
            <t:dgCol title="公积金个人缴纳数额（元）" field="fundUserVal" width="150"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <%--<t:dgDelOpt title="删除" url="hrSocialController.do?doDel&id={id}" urlStyle="background-color:#ec4758;" urlclass="ace_button" urlfont="fa-trash-o"/>--%>
            <%--<t:dgToolBar title="录入" icon="icon-add" url="hrLeaveController.do?addOrUpdate" funname="add"></t:dgToolBar>--%>
            <t:dgToolBar title="编辑" icon="icon-edit" url="hrSocialController.do?saveUserSocial" funname="update"></t:dgToolBar>
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