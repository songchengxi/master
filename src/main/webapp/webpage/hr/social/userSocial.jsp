<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>请假信息</title>
    <t:base type="jquery,easyui,tools"></t:base>
    <script type="text/javascript" src="plug-in/laydate/laydate.js"></script>
    <script>
        $(document).ready(function () {
            //时间选择器
            var start = laydate.render({
                elem: '#startTime',
                type: 'datetime',
                calendar: true,
                trigger: 'click',
                done: function (value, date, endDate) {
                    $("#startTime").val(value);
                    $('#startTime').blur();
                    calculation();
                    end.config.min = {
                        year: date.year,
                        month: date.month - 1,
                        date: date.date,
                        hours: date.hours,
                        minutes: date.minutes,
                        seconds: date.seconds
                    }
                }
            });
        });
    </script>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="hrSocialController.do?saveUserSocial">
    <input type="hidden" name="id" value='${userSocial.id}'>
    <input type="hidden" name="companyId" value='${userSocial.companyId}'>
    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 员工: </label>
            </td>
            <td class="value" width="85%">
                <input id="userId" name="userId" type="hidden" value="${leave.userId}"/>
                <input id="userName" name="userName" class="inputxt" value="${leave.userName}" readonly="readonly" datatype="*" />
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-search"
                   onclick="openUserSelect()">选择</a>
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="roleRedo"
                   onclick="userClean()">清空</a>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="10%" nowrap><label class="Validform_label"> 请假类型: </label></td>
            <td class="value" width="10%">
                <t:dictSelect field="leaveType" type="select" extendJson="{class:'form-control'}" typeGroupCode="leaveType"
                              defaultVal="${leave.leaveType}" hasLabel="false" title="请假类型" datatype="*"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 开始时间: </label>
            </td>
            <td class="value" width="85%">
                <input id="startTime" name="startTime" type="text" class="Wdate"
                       value='<fmt:formatDate value="${leave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 结束时间: </label>
            </td>
            <td class="value" width="85%">
                <input id="endTime" name="endTime" type="text" class="Wdate"
                       value='<fmt:formatDate value="${leave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 时长: </label>
            </td>
            <td class="value" width="85%">
                <input id="duration" name="duration" type="text" class="form-control"
                       value='${leave.duration}' datatype="/^(-?\d+)(\.\d+)?$/"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 天数: </label>
            </td>
            <td class="value" width="85%">
                <input id="dayNum" name="dayNum" type="text" class="form-control"
                       value='${leave.dayNum}' datatype="/^(-?\d+)(\.\d+)?$/"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 原因: </label>
            </td>
            <td class="value" width="85%">
                <textarea id="reason" name="reason" rows="5" cols="50" class="form-control" ignore="ignore">${leave.reason}</textarea>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>