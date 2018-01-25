<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="div" dialog="true" action="salaryController.do?save">
    <input name="id" type="hidden" value="${salary.id }">
    <input name="isSys" type="hidden" value="${salary.isSys}">
    <input name="companyId" type="hidden" value="${salary.companyId}">
    <input name="status" type="hidden" value="${salary.status}">
    <input name="parent.id" type="hidden" value="${parentId}">
    <input name="code" type="hidden" value="${code}">
    <fieldset class="step">
        <div class="form">
            <label class="Validform_label"> 薪酬分类: </label>
            <input readonly="true" class="inputxt" value="${parentName}">
        </div>
        <div class="form">
            <label class="Validform_label"> 薪酬名称: </label>
            <input name="name" class="inputxt" value="${salary.name }" datatype="s1-10">
            <span class="Validform_checktip">名称范围在1~10位字符</span>
        </div>
    </fieldset>
</t:formvalid>
</body>
</html>
