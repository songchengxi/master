<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title><t:mutiLang langKey="common.add.param"/></title>
    <t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="div" dialog="true" action="salaryController.do?save">
    <input name="id" type="hidden" value="${salary.id}">
    <input name="isSys" type="hidden" value="${salary.isSys}">
    <input name="companyId" type="hidden" value="${salary.companyId}">
    <input name="status" type="hidden" value="${salary.status}">
    <fieldset class="step">
        <div class="form">
            <label class="Validform_label"> 分类名称: </label>
            <input name="name" class="inputxt" value="${salary.name}" datatype="s2-10">
            <span class="Validform_checktip">
                <t:mutiLang langKey="common.name.range" langArg="common.name,common.range2to10"/>
            </span>
        </div>
    </fieldset>
</t:formvalid>
</body>
</html>