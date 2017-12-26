<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="hrUserList" checkbox="false" pagination="true" fitColumns="true" title="员工信息" idField="id"
                    actionUrl="hrUserController.do?datagrid" fit="true" queryMode="group" onLoadSuccess="loadSuccess">
            <t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="姓名" field="name" query="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="性别" field="sex" query="true" queryMode="single" replace="男_0,女_1" width="120"></t:dgCol>
            <t:dgCol title="政治面貌" field="politics" width="120"></t:dgCol>
            <t:dgCol title="联系电话" field="phone" width="120"></t:dgCol>
            <t:dgCol title="部门" field="userOrgList.tsDepart.departname" query="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="岗位" field="post" dictionary="hr_post,id,name" formatterjs="post" width="120"></t:dgCol>
            <t:dgCol title="员工性质" field="quality" queryMode="single" dictionary="quality" width="120"></t:dgCol>
            <t:dgCol title="入职日期" field="joinTime" formatter="yyyy-MM-dd" query="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="在职状态" field="jobStatus" query="true" queryMode="single" dictionary="jobStatus" width="120"></t:dgCol>
            <t:dgCol title="转正时间" field="formalDate" formatter="yyyy-MM-dd" width="120"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <t:dgDelOpt title="删除" url="hrUserController.do?doDel&id={id}" urlclass="ace_button" urlStyle="background-color:#ec4758;" urlfont="fa-trash-o"/>
            <t:dgFunOpt funname="updateFormal(name,id)" title="转正" exp="jobStatus#eq#3" urlclass="ace_button" urlStyle="background-color:#18a689;" urlfont="fa-cog"></t:dgFunOpt>
            <t:dgToolBar title="新增员工" icon="icon-add" url="hrUserController.do?addOrUpdate" funname="add"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="icon-edit" url="hrUserController.do?addOrUpdate" funname="update"></t:dgToolBar>
            <t:dgToolBar title="查看" icon="icon-search" url="hrUserController.do?addOrUpdate" funname="detail"></t:dgToolBar>
            <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
            <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
            <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
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

        $("#searchColums input[name='userOrgList.tsDepart.departname']").combotree({
            url: 'orgController.do?selOrgTree',
            width: 155,
            onSelect: function (node) {

            }
        });
    });

    function loadSuccess() {
        postData = "";
    }

    var postData;
    function post(value, row, index) {
        var name;
        if (postData == undefined || postData == "") {
            $.ajax({
                type: "POST",
                async: false,
                url: "hrUserController.do?getPostByCompanyId",
                success: function (msg) {
                    postData = eval('(' + msg + ')');
                }
            });
        }
        $.each(postData.data, function (i, item) {
            if (value == item.id) {
                name = item.postName;
            }
        });
        return name;
    }

    //转正
    function updateFormal(name, id) {
        var url = "url:hrUserController.do?goUpdateFormal&id=" + id;
        $.dialog({
            content: url,
            zIndex: getzIndex(),
            lock: true,
            title: '[ ' + name + ' ] 转正',
            opacity: 0.3,
            width: 500,
            height: 300,
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

    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'hrUserController.do?upload', "hrUserList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("hrUserController.do?exportXls", "hrUserList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("hrUserController.do?exportXlsByT", "hrUserList");
    }
</script>