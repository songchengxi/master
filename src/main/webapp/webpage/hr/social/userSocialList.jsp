<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="hrSocialList" checkbox="false" pagination="true" fitColumns="true" title="社保信息"
                    actionUrl="hrSocialController.do?userSocialList" idField="id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" rowspan="2" width="120"></t:dgCol>
            <t:dgCol title="姓名" field="userName" rowspan="2" query="true" width="120"></t:dgCol>
            <t:dgCol title="部门" field="departName" rowspan="2" query="true" width="150"></t:dgCol>
            <t:dgCol title="入职日期" field="joinTime" rowspan="2" formatter="yyyy-MM-dd" width="150"></t:dgCol>
            <t:dgCol title="在职状态" field="jobStatus" rowspan="2" dictionary="jobStatus" width="150"></t:dgCol>
            <t:dgCol title="是否参保" field="insure" rowspan="2" hidden="true" replace="否_N" width="150"></t:dgCol>
            <t:dgCol title="起缴月" field="socialStart" rowspan="2" width="150"></t:dgCol>
            <t:dgCol title="社保" colspan="3"></t:dgCol>
            <t:dgCol title="公积金" colspan="3"></t:dgCol>
            <t:dgCol title="操作" field="opt" rowspan="2" newColumn="true" width="100"></t:dgCol>
            <t:dgCol title="缴费基数（元）" field="socialBase" width="150"></t:dgCol>
            <t:dgCol title="公司缴纳（元/月）" field="socialComVal" width="150"></t:dgCol>
            <t:dgCol title="个人缴纳（元/月）" field="socialUserVal" width="150"></t:dgCol>
            <t:dgCol title="缴费基数（元）" field="fundBase" width="150"></t:dgCol>
            <t:dgCol title="公司缴纳（元/月）" field="fundComVal" width="150"></t:dgCol>
            <t:dgCol title="个人缴纳（元/月）" field="fundUserVal" width="150"></t:dgCol>
            <t:dgFunOpt funname="edit(userName,insure,id)" title="参保" exp="insure#eq#N" urlclass="ace_button" urlStyle="background-color:#18a689;" urlfont="fa-cog"></t:dgFunOpt>
            <t:dgFunOpt funname="edit(userName,insure,id)" title="编辑" exp="insure#eq#Y" urlclass="ace_button" urlStyle="background-color:#18a689;" urlfont="fa-cog"></t:dgFunOpt>
            <%--<t:dgToolBar title="编辑" icon="icon-edit" url="hrSocialController.do?goUserSocial" funname="update"></t:dgToolBar>--%>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript" src="plug-in/laydate/laydate.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $(".Wdate").removeAttr("onclick");

        //同时绑定多个
        lay('.Wdate').each(function(){
            laydate.render({
                elem: this
                ,trigger: 'click'
                ,calendar: true
            });
        });
    });

    function update(){

    }

    //参保|编辑
    function edit(userName, insure, id) {
        var title = "";
        if ("Y" == insure) {
            title = '[ ' + userName + ' ] 社保信息';
        } else {
            title = '[ ' + userName + ' ] 参保';
        }
        var url = "url:hrSocialController.do?goUserSocial&insure=" + insure + "&id=" + id;
        $.dialog({
            content: url,
            zIndex: getzIndex(),
            lock: true,
            title: title,
            opacity: 0.3,
            width: 600,
            height: 400,
            cache: false,
            ok: function () {
                iframe = this.iframe.contentWindow;
                saveObj();
                return false;
            },
            cancelVal: '<t:mutiLang langKey="common.close"/>',
            cancel: true /*为true等价于function(){}*/
        });
    }
</script>