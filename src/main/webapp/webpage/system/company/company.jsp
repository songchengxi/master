<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title></title>
    <t:base type="jquery,easyui,tools"></t:base>
    <script type="text/javascript">
        //编写自定义JS代码
    </script>
</head>

<body>
<t:formvalid formid="formobj" layout="div" dialog="true" action="companyController.do?saveCompany">
    <input id="id" name="id" type="hidden" value="${page.id }">
    <fieldset class="step">
        <div class="form">
            <label class="Validform_label">公司名称: </label>
            <input name="name" class="inputxt" type="text" value="${page.name }">
            <span class="Validform_checktip"></span>
        </div>
        <div class="form">
            <label class="Validform_label">行业: </label>
            <input name="industry" class="inputxt" type="text" value="${page.industry }">
            <span class="Validform_checktip"></span>
        </div>
        <div class="form">
            <label class="Validform_label">办公地址: </label>
            <input name="address" class="inputxt" type="text" value="${page.address }">
            <span class="Validform_checktip"></span>
        </div>
        <div class="form">
            <label class="Validform_label">发票抬头: </label>
            <input name="invoiceTitle" class="inputxt" type="text" value="${page.invoiceTitle }">
            <span class="Validform_checktip"></span>
        </div>
        <div class="form">
            <label class="Validform_label">税号: </label>
            <input name="taxNum" class="inputxt" type="text" value="${page.taxNum }">
            <span class="Validform_checktip"></span>
        </div>
        <div class="form">
            <label class="Validform_label">开户行: </label>
            <input name="bank" class="inputxt" type="text" value="${page.bank }">
            <span class="Validform_checktip"></span>
        </div>
        <div class="form">
            <label class="Validform_label">银行账号: </label>
            <input name="bankAccount" class="inputxt" type="text" value="${page.bankAccount }">
            <span class="Validform_checktip"></span>
        </div>
        <div class="form">
            <label class="Validform_label">电话: </label>
            <input name="phone" class="inputxt" type="text" value="${page.phone }">
            <span class="Validform_checktip"></span>
        </div>
        <div class="form">
            <label class="Validform_label">注册地址: </label>
            <input name="registerAddress" class="inputxt" type="text" value="${page.registerAddress }">
            <span class="Validform_checktip"></span>
        </div>
        <div class="form">
            <label class="Validform_label">备注: </label>
            <input name="remark" class="inputxt" type="text" value="${page.remark }">
            <span class="Validform_checktip"></span>
        </div>
    </fieldset>
</t:formvalid>
<script type="text/javascript">
    $(function () {
        //查看模式情况下,删除和上传附件功能禁止使用
        if (location.href.indexOf("load=detail") != -1) {
            $(".jeecgDetail").hide();
        }

        if (location.href.indexOf("mode=read") != -1) {
            //查看模式控件禁用
            $("#formobj").find(":input").attr("disabled", "disabled");
        }
        if (location.href.indexOf("mode=onbutton") != -1) {
            //其他模式显示提交按钮
            $("#sub_tr").show();
        }
    });

    var neibuClickFlag = false;
    function neibuClick() {
        neibuClickFlag = true;
        $('#btn_sub').trigger('click');
    }
</script>
</body>
</html>