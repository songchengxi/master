<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>生成账单</title>
    <t:base type="jquery,easyui,tools,DatePicker"></t:base>
    <script type="text/javascript" src="plug-in/laydate/laydate.js"></script>
    <script>
        $(document).ready(function () {
            laydate.render({
                elem: '#month'
                , type: 'month'
                , calendar: true
                , trigger: 'click'
                , format: 'yyyy-MM'
                , max: '${max}'
                , done: function (value, date, endDate) {
                    $("#month").val(value);
                    $('#month').blur();
                }
            });
        });
    </script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="hrSocialController.do?addBill">
    <table cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 账单月份: </label>
            </td>
            <td class="value" width="85%">
                <input id="month" name="month" type="text" class="form-control" datatype="*"/>
                <span class="Validform_checktip">请选择账单月份</span>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>
</html>