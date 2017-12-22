<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>新增岗位信息</title>
    <t:base type="jquery,easyui,tools,DatePicker"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="hrUserController.do?savePost">
    <input type="hidden" name="id" value='${post.id}'>
    <table cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 岗位名称: </label>
            </td>
            <td class="value" width="85%">
                <input id="postName" name="postName" type="text" class="form-control" value='${post.postName}' datatype="*"/>
                <span class="Validform_checktip">名称不能为空</span>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>
</html>