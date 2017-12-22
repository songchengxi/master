<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>转正</title>
    <t:base type="jquery,easyui,tools,DatePicker"></t:base>
    <script type="text/javascript" src="plug-in/My97DatePicker/WdatePicker.js"></script>
    <%--<script type="text/javascript" src="plug-in/laydate/laydate.js"></script>--%>
    <script>
        $(document).ready(function () {
//            top.laydate.render({
//                elem: '#formalDate'
//                , calendar: true
//                , trigger: 'click'
//                , done: function (value, date, endDate) {
//                    $("#formalDate").val(value);
//                    $('#formalDate').blur();
//                }
//            });
//            laydate.render({
//                elem: '#formalDate'
//                , calendar: true
//                , trigger: 'click'
//                , done: function (value, date, endDate) {
//                    $("#formalDate").val(value);
//                    $('#formalDate').blur();
//                }
//            });
        });
    </script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="hrUserController.do?updateFormal">
    <input type="hidden" name="id" value='${user.id}'>
    <table cellpadding="0" cellspacing="1" class="formtable">
        <%--<tr>--%>
            <%--<td align="right" width="25%" nowrap>--%>
                <%--<label class="Validform_label"> 转正时间: </label>--%>
            <%--</td>--%>
            <%--<td class="value" width="85%">--%>
                <%--<input id="formalDate" name="formalDate" type="text" class="form-control"--%>
                       <%--value='<fmt:formatDate value="${user.formalDate}" pattern="yyyy-MM-dd"/>' datatype="*"/>--%>
                <%--<span class="Validform_checktip">转正时间不能为空</span>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 转正时间: </label>
            </td>
            <td class="value" width="85%">
                <input name="formalDate" type="text" class="Wdate" onclick="WdatePicker()"
                       value='<fmt:formatDate value="${user.formalDate}" pattern="yyyy-MM-dd"/>' datatype="*"/>
                <span class="Validform_checktip">转正时间不能为空</span>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>
</html>