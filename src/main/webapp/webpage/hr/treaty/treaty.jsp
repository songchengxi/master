<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>合同信息</title>
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
                elem: '#treatyStart',
                calendar: true,
                trigger: 'click',
                done: function (value, date) {
                    $("#treatyStart").val(value);
                    $('#treatyStart').blur();
                    calculation();
                    end.config.min = {
                        year: date.year,
                        month: date.month - 1,
                        date: date.date
                    }
                }
            });
            var end = laydate.render({
                elem: '#treatyEnd',
                calendar: true,
                trigger: 'click',
                done: function (value, date) {
                    $("#treatyEnd").val(value);
                    $('#treatyEnd').blur();
                    calculation();
                    start.config.max = {
                        year: date.year,
                        month: date.month - 1,
                        date: date.date
                    }
                }
            });
            laydate.render({
                elem: '#signDate',
                calendar: true,
                trigger: 'click',
                done: function (value) {
                    $("#signDate").val(value);
                    $('#signDate').blur();
                }
            });
        });

        //计算期限
        function calculation() {
            var start = $("#treatyStart").val();//开始时间
            var end = $("#treatyEnd").val();//结束时间
            if (start != "" && end != "") {
                var date3 = new Date(end).getTime() - new Date(start).getTime();//时间差的毫秒数
                //计算出期限
                var year = Math.ceil(date3 / (365 * 24 * 3600 * 1000));
                $("#term").val(year);
                $("#term").blur();
            }
        }
    </script>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="hrTreatyController.do?save">
    <input type="hidden" name="id" value='${treaty.id}'>
    <input type="hidden" name="companyId" value='${treaty.companyId}'>
    <input type="hidden" name="deleteFlag" value='${treaty.deleteFlag}'>
    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 员工: </label>
            </td>
            <td class="value" width="85%">
                <input id="userId" name="userId" type="hidden" value="${treaty.userId}"/>
                <input id="userName" name="userName" class="inputxt" value="${userName}" readonly="readonly" datatype="*" />
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-search"
                   onclick="openUserSelect()">选择</a>
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="roleRedo"
                   onclick="userClean()">清空</a>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 合同编号: </label>
            </td>
            <td class="value" width="85%">
                <input id="treatyNo" name="treatyNo" type="text" class="form-control"
                       value='${treaty.treatyNo}' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="10%" nowrap><label class="Validform_label"> 合同类型: </label></td>
            <td class="value" width="10%">
                <t:dictSelect field="treatyType" type="select" extendJson="{class:'form-control'}" typeGroupCode="treatyType"
                              defaultVal="${treaty.treatyType}" hasLabel="false" title="合同类型" datatype="*"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="10%" nowrap><label class="Validform_label"> 签订类型: </label></td>
            <td class="value" width="10%">
                <t:dictSelect field="signType" type="select" extendJson="{class:'form-control'}" typeGroupCode="signType"
                              defaultVal="${treaty.signType}" hasLabel="false" title="签订类型" datatype="*"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 开始日期: </label>
            </td>
            <td class="value" width="85%">
                <input id="treatyStart" name="treatyStart" type="text" class="Wdate"
                       value='<fmt:formatDate value="${treaty.treatyStart}" pattern="yyyy-MM-dd"/>' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 结束日期: </label>
            </td>
            <td class="value" width="85%">
                <input id="treatyEnd" name="treatyEnd" type="text" class="Wdate"
                       value='<fmt:formatDate value="${treaty.treatyEnd}" pattern="yyyy-MM-dd"/>' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 期限: </label>
            </td>
            <td class="value" width="85%">
                <input id="term" name="term" type="text" class="form-control"
                       value='${treaty.term}' datatype="/^(-?\d+)(\.\d+)?$/"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 签订日期: </label>
            </td>
            <td class="value" width="85%">
                <input id="signDate" name="signDate" type="text" class="Wdate"
                       value='<fmt:formatDate value="${treaty.signDate}" pattern="yyyy-MM-dd"/>' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>