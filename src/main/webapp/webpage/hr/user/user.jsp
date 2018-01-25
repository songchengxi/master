<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>员工信息</title>
    <t:base type="jquery,easyui,tools"></t:base>
    <style>
        tr {
            height: 40px;
        }
    </style>
    <script type="text/javascript" src="plug-in/laydate/laydate.js"></script>
    <script>
        function openDepartSelect() {
            $.dialog.setting.zIndex = getzIndex();
            var departIds = $("#departIds").val();
            $.dialog({
                content: 'url:orgController.do?departSelect&ids=' + departIds,
                zIndex: getzIndex(),
                title: '部门列表',
                lock: true,
                width: '400px',
                height: '350px',
                opacity: 0.4,
                button: [
                    {name: '<t:mutiLang langKey="common.confirm"/>', callback: callbackDepartSelect, focus: true},
                    {name: '<t:mutiLang langKey="common.cancel"/>', callback: function () {}}
                ]
            }).zindex();
        }

        function callbackDepartSelect() {
            var iframe = this.iframe.contentWindow;
            var treeObj = iframe.$.fn.zTree.getZTreeObj("departSelect");
            var nodes = treeObj.getCheckedNodes(true);
            if (nodes.length > 0) {
                var id = '', name = '';
                for (i = 0; i < nodes.length; i++) {
                    var node = nodes[i];
                    id += node.id + ",";
                    name += node.name + ",";
                }
                $('#departName').val(name);
                $('#departName').blur();
                $('#departIds').val(id);
            }
        }

        function departClean() {
            $('#departName').val('');
            $('#departIds').val('');
        }

        $(document).ready(function () {
            laydate.render({
                elem: '#birthday'
                , calendar: true
                , trigger: 'click'
                , done: function (value, date, endDate) {
                    $("#birthday").val(value);
                    $('#birthday').blur();
                    age(date.year);
                }
            });
            laydate.render({
                elem: '#joinTime'
                , calendar: true
                , trigger: 'click'
                , done: function (value, date, endDate) {
                    $("#joinTime").val(value);
                    $('#joinTime').blur();
                }
            });
            laydate.render({
                elem: '#formalDate'
                , calendar: true
                , trigger: 'click'
                , done: function (value, date, endDate) {
                    $("#formalDate").val(value);
                    $('#formalDate').blur();
                }
            });
        });

        //计算年龄
        function age(year) {
            var date = new Date();
            var age = date.getFullYear() - year;
            $("#age").val(age);
            $("#ageTd").html(age);
        }

        //新增岗位
        function addPost() {
            var url = "url:hrUserController.do?addOrUpdatePost";
            $.dialog({
                content: url,
                zIndex: getzIndex(),
                lock: true,
                title: '新增岗位信息',
                opacity: 0.3,
                width: 500,
                height: 300,
                cache: false,
                ok: function () {
                    iframe = this.iframe.contentWindow;
                    saveObj();
                    addPostVal();
                    return false;
                },
                cancelVal: '<t:mutiLang langKey="common.close"/>',
                cancel: true /*为true等价于function(){}*/
            });
        }

        function addPostVal() {
            $.ajax({
                type: "POST",
                url: "hrUserController.do?getPostByCompanyId",
                success: function (msg) {
                    var data = eval('(' + msg + ')');
                    var str = "<option value=''>---请选择---</option>";
                    $.each(data.data, function (i, item) {
                        str += "<option value=\"" + item.id + "\">" + item.postName + "</option>";
                    });
                    $("select[name='post']").html(str);
                }
            })
        }
    </script>
</head>
<body scroll="no">
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="hrUserController.do?save">
    <input type="hidden" name="id" value='${user.id}'>
    <input type="hidden" name="companyId" value='${user.companyId}'>
    <input type="hidden" name="deleteFlag" value='${user.deleteFlag}'>
    <input type="hidden" name="formalDate" value='${user.formalDate}'>
    <input type="hidden" name="jobStatus" value='${user.jobStatus}'>
    <input type="hidden" name="probationSalary" value='${user.probationSalary}'>
    <input type="hidden" name="fixSalary" value='${user.fixSalary}'>
    <input type="hidden" name="rewardSalary" value='${user.rewardSalary}'>
    <input id="age" name="age" type="hidden" class="form-control" value='${user.age}'/>
    <table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
        <tr>
            <td colspan="2" nowrap style="text-align: center;">
                <label class="Validform_label">基本信息</label>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 姓名: </label>
            </td>
            <td class="value" width="85%">
                <input id="name" name="name" type="text" class="form-control"
                       value='${user.name}' datatype="*"/>
                <span class="Validform_checktip">姓名不能为空</span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 民族: </label>
            </td>
            <td class="value" width="85%">
                <input id="nation" name="nation" type="text" class="form-control" datatype="*"
                       value='${user.nation}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="10%" nowrap><label class="Validform_label"> 性别: </label></td>
            <td class="value" width="10%">
                <t:dictSelect field="sex" type="radio" extendJson="{class:'form-control'}" typeGroupCode="sex"
                              defaultVal="${user.sex}" hasLabel="false" title="性别"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 出生日期: </label>
            </td>
            <td class="value" width="85%">
                <input id="birthday" name="birthday" type="text"
                       value='<fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/>' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 年龄: </label>
            </td>
            <td class="value" width="85%" id="ageTd">${user.age}</td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 证件类型: </label>
            </td>
            <td class="value" width="85%">
                <t:dictSelect field="idType" type="list" extendJson="{class:'form-control'}" typeGroupCode="idType"
                              defaultVal="${user.idType}" hasLabel="false" title="证件类型"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 证件号码: </label>
            </td>
            <td class="value" width="85%">
                <input id="idNumber" name="idNumber" type="text" class="form-control" ignore="ignore"
                       value='${user.idNumber}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 婚姻状况: </label>
            </td>
            <td class="value" width="85%">
                <input id="marriageStatus" name="marriageStatus" type="text" class="form-control"
                       ignore="ignore" value='${user.marriageStatus}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 籍贯: </label>
            </td>
            <td class="value" width="85%">
                <input id="nativePlace" name="nativePlace" type="text" class="form-control" ignore="ignore"
                       value='${user.nativePlace}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 政治面貌: </label>
            </td>
            <td class="value" width="85%">
                <t:dictSelect field="politics" type="list" extendJson="{class:'form-control'}" typeGroupCode="politicsType"
                              defaultVal="${user.politics}" hasLabel="false" title="政治面貌"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 电子邮箱: </label>
            </td>
            <td class="value" width="85%">
                <input id="email" name="email" type="text" class="form-control" ignore="ignore"
                       value='${user.email}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 联系电话: </label>
            </td>
            <td class="value" width="85%">
                <input id="phone" name="phone" type="text" class="form-control" ignore="ignore"
                       value='${user.phone}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 地址: </label>
            </td>
            <td class="value" width="85%">
                <input id="address" name="address" type="text" class="form-control" ignore="ignore"
                       value='${user.address}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <%--<tr>--%>
            <%--<td align="right" width="25%" nowrap>--%>
                <%--<label class="Validform_label"> 照片: </label>--%>
            <%--</td>--%>
            <%--<td class="value" width="85%">--%>
                <%--<input id="photo" name="photo" type="text" class="form-control" ignore="ignore"--%>
                       <%--value='${user.photo}'/>--%>
                <%--<span class="Validform_checktip"></span>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 学历: </label>
            </td>
            <td class="value" width="85%">
                <input id="education" name="education" type="text" class="form-control" ignore="ignore"
                       value='${user.education}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 专业: </label>
            </td>
            <td class="value" width="85%">
                <input id="major" name="major" type="text" class="form-control" ignore="ignore"
                       value='${user.major}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 毕业院校: </label>
            </td>
            <td class="value" width="85%">
                <input id="school" name="school" type="text" class="form-control" ignore="ignore"
                       value='${user.school}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td colspan="2" nowrap style="text-align: center;">
                <label class="Validform_label">工作信息</label>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 员工性质: </label>
            </td>
            <td class="value" width="85%">
                <t:dictSelect field="quality" type="list" extendJson="{class:'form-control'}" typeGroupCode="quality"
                              defaultVal="${user.quality}" hasLabel="false" title="员工性质" datatype="*"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 入职日期: </label>
            </td>
            <td class="value" width="85%">
                <input id="joinTime" name="joinTime" type="text"
                       value='<fmt:formatDate value="${user.joinTime}" pattern="yyyy-MM-dd"/>' datatype="*"/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 试用期: </label>
            </td>
            <td class="value" width="85%">
                <t:dictSelect field="period" type="list" extendJson="{class:'form-control'}" typeGroupCode="period"
                              defaultVal="${user.period}" hasLabel="false" title="试用期" datatype="*"></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <%--<tr>--%>
            <%--<td align="right" width="25%" nowrap>--%>
                <%--<label class="Validform_label"> 转正时间: </label>--%>
            <%--</td>--%>
            <%--<td class="value" width="85%">--%>
                <%--<input id="formalDate" name="formalDate" type="text"--%>
                       <%--value='<fmt:formatDate value="${user.formalDate}" pattern="yyyy-MM-dd"/>' datatype="*"/>--%>
                <%--<span class="Validform_checktip"></span>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td align="right" width="25%" nowrap>--%>
                <%--<label class="Validform_label"> 合同期限: </label>--%>
            <%--</td>--%>
            <%--<td class="value" width="85%">--%>
                <%--<input id="contractTerm" name="contractTerm" type="text"--%>
                       <%--value='<fmt:formatDate value="${user.contractTerm}" pattern="yyyy-MM-dd"/>' datatype="*"/>--%>
                <%--<span class="Validform_checktip"></span>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 部门: </label>
            </td>
            <td class="value" width="85%">
                <input id="departIds" name="departIds" type="hidden" value="${departIds}"/>
                <input id="departName" name="departName" class="inputxt" value="${departName}" readonly="readonly" datatype="*" />
                <a class="easyui-linkbutton" plain="true" icon="icon-search"
                   onclick="openDepartSelect()">选择</a>
                <a class="easyui-linkbutton" plain="true" icon="icon-redo" id="roleRedo"
                   onclick="departClean()">清空</a>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 岗位: </label>
            </td>
            <td class="value" width="85%">
                <t:dictSelect field="post" defaultVal="${user.post}" dictTable="hr_post" dictCondition="where company_id='${companyId}'"
                              dictField="id" dictText="name" title="岗位" datatype="*"></t:dictSelect>
                <a class="easyui-linkbutton" plain="true" icon="icon-add"
                   onclick="addPost()">新增</a>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 工号: </label>
            </td>
            <td class="value" width="85%">
                <input id="jobNumber" name="jobNumber" type="text" class="form-control" ignore="ignore"
                       value='${user.jobNumber}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 职称: </label>
            </td>
            <td class="value" width="85%">
                <input id="title" name="title" type="text" class="form-control" ignore="ignore"
                       value='${user.title}'/>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
        <%--<tr>--%>
            <%--<td align="right" width="25%" nowrap>--%>
                <%--<label class="Validform_label"> 基本工资: </label>--%>
            <%--</td>--%>
            <%--<td class="value" width="85%">--%>
                <%--<input id="baseSalary" name="baseSalary" type="text" class="form-control"--%>
                       <%--datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore" value='${user.baseSalary}'/>--%>
                <%--<span class="Validform_checktip"></span>--%>
            <%--</td>--%>
        <%--</tr>--%>

        <%--<tr>--%>
            <%--<td align="right" width="25%" nowrap>--%>
                <%--<label class="Validform_label"> 在职状态: </label>--%>
            <%--</td>--%>
            <%--<td class="value" width="85%">--%>
                <%--<input id="jobStatus" name="jobStatus" type="text" class="form-control" ignore="ignore"--%>
                       <%--value='${user.jobStatus}'/>--%>
                <%--<span class="Validform_checktip"></span>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td align="right" width="25%" nowrap>--%>
                <%--<label class="Validform_label"> 工龄: </label>--%>
            <%--</td>--%>
            <%--<td class="value" width="85%">--%>
                <%--<input id="workAge" name="workAge" type="text" class="form-control"--%>
                       <%--datatype="/^(-?\d+)(\.\d+)?$/" ignore="ignore" value='${user.workAge}'/>--%>
                <%--<span class="Validform_checktip"></span>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td align="right" width="25%" nowrap>--%>
                <%--<label class="Validform_label"> 简历: </label>--%>
            <%--</td>--%>
            <%--<td class="value" width="85%">--%>
                <%--<input id="resume" name="resume" type="text" class="form-control" ignore="ignore"--%>
                       <%--value='${user.resume}'/>--%>
                <%--<span class="Validform_checktip"></span>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td align="right" width="25%" nowrap>
                <label class="Validform_label"> 备注: </label>
            </td>
            <td class="value" width="85%">
                <textarea name="remark" id="remark" class="form-control" cols="50" rows="5" ignore="ignore">${user.remark}</textarea>
                <span class="Validform_checktip"></span>
            </td>
        </tr>
    </table>
</t:formvalid>
</body>