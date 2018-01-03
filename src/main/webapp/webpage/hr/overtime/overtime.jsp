<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>请假信息</title>
    <t:base type="jquery,easyui,tools"></t:base>
    <script type="text/javascript" src="plug-in/laydate/laydate.js"></script>
    <script>
        function openUserSelect() {
            $.dialog.setting.zIndex = getzIndex();
            var userId = $("#userId").val();
            $.dialog({
                content: 'url:hrUserController.do?goUserSelect&id=' + userId,
                zIndex: getzIndex(),
                title: '员工列表',
                lock: true,
                width: '600px',
                height: '600px',
                opacity: 0.4,
                button: [
                    {name: '<t:mutiLang langKey="common.confirm"/>', callback: callbackUserSelect, focus: true},
                    {name: '<t:mutiLang langKey="common.cancel"/>', callback: function () {}}
                ]
            }).zindex();
        }

        function callbackUserSelect() {
            var iframe = this.iframe.contentWindow;
            var name = iframe.getuserListSelections('name');
            if (name != undefined && name != "") {
                $("#userName").val(name);
                $('#userName').blur();
            }
            var id = iframe.getuserListSelections('id');
            if (id !== undefined && id != "") {
                $('#userId').val(id);
            }
        }

        function userClean() {
            $('#userName').val('');
            $('#userId').val('');
        }

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
            var end = laydate.render({
                elem: '#endTime',
                type: 'datetime',
                calendar: true,
                trigger: 'click',
                done: function (value, date, endDate) {
                    $("#endTime").val(value);
                    $('#endTime').blur();
                    calculation();
                    start.config.max = {
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

        //计算时长
        function calculation() {
            var start = $("#startTime").val();//开始时间
            var end = $("#endTime").val();//结束时间
            if (start != "" && end != "") {
                var date3 = new Date(end).getTime() - new Date(start).getTime();//时间差的毫秒数
                //计算时长
                var hours = Math.floor(date3 / (3600 * 1000));
                $("#duration").val(hours);
            }
        }
    </script>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="hrOvertimeController.do?save">
    <input type="hidden" name="id" value='${overtime.id}'>
    <input type="hidden" name="companyId" value='${overtime.companyId}'>
    <input type="hidden" name="deleteFlag" value='${overtime.deleteFlag}'>
    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 员工: </label>
            </td>
            <td class="value" width="85%">
                <input id="userId" name="userId" type="hidden" value="${overtime.userId}"/>
                <input id="userName" name="userName" class="inputxt" value="${userName}" readonly="readonly" datatype="*"/>
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-search"
                   onclick="openUserSelect()">选择</a>
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="roleRedo"
                   onclick="userClean()">清空</a>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="10%" nowrap><label class="Validform_label"> 加班类型: </label></td>
            <td class="value" width="10%">
                <t:dictSelect field="type" type="select" extendJson="{class:'form-control'}" typeGroupCode="overtimeType"
                              defaultVal="${overtime.type}" hasLabel="false" title="请假类型" datatype="*"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 开始时间: </label>
            </td>
            <td class="value" width="85%">
                <input id="startTime" name="startTime" type="text"
                       value='<fmt:formatDate value="${overtime.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 结束时间: </label>
            </td>
            <td class="value" width="85%">
                <input id="endTime" name="endTime" type="text"
                       value='<fmt:formatDate value="${overtime.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 时长: </label>
            </td>
            <td class="value" width="85%">
                <input id="duration" name="duration" type="text" class="form-control"
                       value='${overtime.duration}' datatype="/^(-?\d+)(\.\d+)?$/"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 原因: </label>
            </td>
            <td class="value" width="85%">
                <textarea id="reason" name="reason" rows="5" cols="50" class="form-control" ignore="ignore">${overtime.reason}</textarea>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>