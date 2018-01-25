<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="birthdayList" checkbox="false" pagination="true" fitColumns="true" title="员工生日"
                    actionUrl="hrUserController.do?birthdayList" idField="id" fit="true" queryMode="group">
            <t:dgCol title="姓名" field="name" width="150"></t:dgCol>
            <t:dgCol title="部门" field="departName" width="150"></t:dgCol>
            <t:dgCol title="入职日期" field="joinTime" formatter="yyyy-MM-dd" width="150"></t:dgCol>
            <t:dgCol title="在职状态" field="jobStatus" dictionary="jobStatus" width="120"></t:dgCol>
            <t:dgCol title="出生日期" field="birthday" formatter="yyyy-MM-dd" width="150"></t:dgCol>
            <t:dgCol title="年龄" field="age" width="150"></t:dgCol>
            <t:dgCol title="距离生日还剩" field="diff" width="150" extendParams="styler:fmtype"></t:dgCol>
            <%--<t:dgCol title="还剩" field="diff" width="150" style="background-color:#3a87ad_50,background-color:#f89406_100"></t:dgCol>--%>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript">
    //列表字段颜色 demo,逻辑判断函数
    function fmtype(val, row, index) {
        //可添加更多CSS样式
        var s1 = 'background-color:#f89406;color:#FFF;';
        var s2 = 'background-color:#3a87ad;color:#FFF;';
        var s3 = 'background-color:#21B9BB;';
        if (val < 7) {
            return s1;
        } else {
            return s2;
        }
    }
</script>