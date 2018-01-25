<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>薪酬信息</title>
    <t:base type="jquery,easyui,tools"></t:base>
    <style>
        tr {
            height: 40px;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            if ("" == "${user.probationSalary}") {
                $("#probationSalary").val("02");
            } else {
                $("#probationSalary").val("${user.probationSalary}");
            }
        });

        //计算试用期工资
        function probation(name) {
            var v = document.getElementsByName(name)[0].value;
            var p = $("#probationSalary").val();
            if ("01" == p) {
                document.getElementsByName(name)[1].value = Math.floor(v * 0.8);
            } else if ("02" == p) {
                document.getElementsByName(name)[1].value = v;
            }
        }

        function change(value) {
            var input = $(".form-control");
            if ("01" == value) {
                for (var i = 0; i < input.length; i++) {
                    input[i + 1].value = Math.floor(input[i].value * 0.8);
                    i++;
                }
            } else if ("02" == value) {
                for (var i = 0; i < input.length; i++) {
                    input[i + 1].value = input[i].value;
                    i++;
                }
            }
        }
    </script>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" tiptype="1" usePlugin="password" layout="table" action="salaryController.do?saveUserSalary">
    <input type="hidden" name="id" value='${user.id}'>
    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label">姓名：</label>
            </td>
            <td width="25%" class="value">${user.name}</td>
            <td width="25%" align="right" nowrap>
                <label class="Validform_label">部门：</label>
            </td>
            <td width="25%" class="value">${orgName}</td>
        </tr>
        <tr>
            <td align="right" nowrap>
                <label class="Validform_label">入职时间：</label>
            </td>
            <td class="value"><fmt:formatDate value="${user.joinTime}" pattern="yyyy-MM-dd"/></td>
            <td align="right" nowrap>
                <label class="Validform_label">转正时间：</label>
            </td>
            <td class="value"><fmt:formatDate value="${user.formalDate}" pattern="yyyy-MM-dd"/></td>
        </tr>
        <tr>
            <td align="right" nowrap>
                <label class="Validform_label">在职状态：</label>
            </td>
            <td colspan="3" class="value">
                <c:if test="${user.jobStatus == '3'}">试用期</c:if>
                <c:if test="${user.jobStatus == '6'}">正式</c:if>
                <c:if test="${user.jobStatus == '9'}">离职</c:if>
            </td>
        </tr>
        <tr>
            <td align="right" nowrap>
                <label class="Validform_label">试用期工资：</label>
            </td>
            <td colspan="3" class="value">
                <select name="probationSalary" id="probationSalary" onchange="change(this.value)">
                    <option value="01">80%</option>
                    <option value="02">100%</option>
                    <option value="03">自定义</option>
                </select>
            </td>
        </tr>
        <c:forEach items="${parentList}" var="salary">
            <tr>
                <td colspan="2" nowrap style="text-align: center;">
                    <label class="Validform_label">${salary.name}</label>
                </td>
                <td colspan="2" nowrap style="text-align: center;">
                    <label class="Validform_label">试用期</label>
                </td>
            </tr>
            <c:if test="${salary.childs == '[]'}">
                <tr><td colspan="4" class="value" style="text-align: center;">无</td></tr>
            </c:if>
            <c:forEach items="${salary.childs}" var="child">
                <tr>
                    <td align="right">
                        <label class="Validform_label">${child.name}</label>
                    </td>
                    <td class="value">
                        <input name="${child.id}" type="text" class="form-control" onkeyup="probation(this.name)" onblur="probation(this.name)"
                               value='${child.value}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                    </td>
                    <td colspan="2" class="value">
                        <input name="${child.id}" type="text" class="form-control"
                               value='${child.probation}' datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore"/>
                    </td>
                </tr>
            </c:forEach>
        </c:forEach>
    </table>
</t:formvalid>
</body>