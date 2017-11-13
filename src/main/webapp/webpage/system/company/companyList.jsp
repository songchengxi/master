<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="companyList" checkbox="false" pagination="true" fitColumns="true" title="公司信息"
                    actionUrl="companyController.do?companyGrid" idField="id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="公司名称" field="name" width="120"></t:dgCol>
            <t:dgCol title="行业" field="industry" width="120"></t:dgCol>
            <t:dgCol title="办公地址" field="address" width="120"></t:dgCol>
            <t:dgCol title="发票抬头" field="invoiceTitle" width="120"></t:dgCol>
            <t:dgCol title="税号" field="taxNum" width="120"></t:dgCol>
            <t:dgCol title="开户行" field="bank" width="120"></t:dgCol>
            <t:dgCol title="银行账号" field="bankAccount" width="120"></t:dgCol>
            <t:dgCol title="电话" field="phone" width="120"></t:dgCol>
            <t:dgCol title="注册地址" field="registerAddress" width="120"></t:dgCol>
            <t:dgCol title="备注" field="remark" hidden="true" queryMode="single" width="120"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <t:dgDelOpt title="删除" url="companyController.do?doDel&id={id}" urlclass="ace_button"
                        urlfont="fa-trash-o"/>
            <t:dgToolBar title="录入" icon="icon-add" url="companyController.do?goAdd" funname="add"></t:dgToolBar>
            <t:dgToolBar title="编辑" icon="icon-edit" url="companyController.do?goUpdate"
                         funname="update"></t:dgToolBar>
            <%--<t:dgToolBar title="批量删除" icon="icon-remove" url="companyController.do?doBatchDel"--%>
                         <%--funname="deleteALLSelect"></t:dgToolBar>--%>
            <t:dgToolBar title="查看" icon="icon-search" url="companyController.do?goUpdate"
                         funname="detail"></t:dgToolBar>
            <%--<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
            <%--<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>--%>
            <%--<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
        </t:datagrid>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {

    });

    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'companyController.do?upload', "companyList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("companyController.do?exportXls", "companyList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("companyController.do?exportXlsByT", "companyList");
    }
</script>