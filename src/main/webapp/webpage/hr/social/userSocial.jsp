<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>社保信息</title>
    <t:base type="jquery,easyui,tools"></t:base>
    <script type="text/javascript" src="plug-in/laydate/laydate.js"></script>
    <script>
        $(document).ready(function () {
            //时间选择器
            var start = laydate.render({
                elem: '#socialStart'
                , type: 'month'
                , trigger: 'click'
                , format: 'yyyyMM'
                , done: function (value, date, endDate) {
                    $("#socialStart").val(value);
                    $('#socialStart').blur();
                }
            });
        });
    </script>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="hrSocialController.do?saveUserSocial">
    <input type="hidden" name="userId" value='${userSocial.userId}'>
    <input type="hidden" name="deleteFlag" value='${userSocial.deleteFlag}'>
    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 起缴月: </label>
            </td>
            <td class="value" width="85%">
                <input id="socialStart" name="socialStart" type="text" value='${userSocial.socialStart}' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 社保缴费基数: </label>
            </td>
            <td class="value" width="85%">
                <c:if test="${insure == 'N'}">
                    <input id="socialBase" name="socialBase" type="text" class="form-control easyui-numberbox"
                           value='${val.socialBase}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <c:if test="${insure == 'Y'}">
                    <input id="socialBase" name="socialBase" type="text" class="form-control easyui-numberbox"
                           value='${userSocial.socialBase}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 社保公司缴费数额: </label>
            </td>
            <td class="value" width="85%">
                <c:if test="${insure == 'N'}">
                    <input id="socialComVal" name="socialComVal" type="text" class="form-control"
                           value='${val.socialCom}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <c:if test="${insure == 'Y'}">
                    <input id="socialComVal" name="socialComVal" type="text" class="form-control"
                           value='${userSocial.socialComVal}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>

                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 社保个人缴纳数额: </label>
            </td>
            <td class="value" width="85%">
                <c:if test="${insure == 'N'}">
                    <input id="socialUserVal" name="socialUserVal" type="text" class="form-control"
                           value='${val.socialUser}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <c:if test="${insure == 'Y'}">
                    <input id="socialUserVal" name="socialUserVal" type="text" class="form-control"
                           value='${userSocial.socialUserVal}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 公积金缴费基数: </label>
            </td>
            <td class="value" width="85%">
                <c:if test="${insure == 'N'}">
                    <input id="fundBase" name="fundBase" type="text" class="form-control"
                           value='${val.fundBase}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <c:if test="${insure == 'Y'}">
                    <input id="fundBase" name="fundBase" type="text" class="form-control"
                           value='${userSocial.fundBase}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 公积金公司缴纳数额: </label>
            </td>
            <td class="value" width="85%">
                <c:if test="${insure == 'N'}">
                    <input id="fundComVal" name="fundComVal" type="text" class="form-control"
                           value='${val.fundCom}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <c:if test="${insure == 'Y'}">
                    <input id="fundComVal" name="fundComVal" type="text" class="form-control"
                           value='${userSocial.fundComVal}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 公积金个人缴纳数额: </label>
            </td>
            <td class="value" width="85%">
                <c:if test="${insure == 'N'}">
                    <input id="fundUserVal" name="fundUserVal" type="text" class="form-control"
                           value='${val.fundUser}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <c:if test="${insure == 'Y'}">
                    <input id="fundUserVal" name="fundUserVal" type="text" class="form-control"
                           value='${userSocial.fundUserVal}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                </c:if>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>