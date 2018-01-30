<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div>
    <script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="plug-in/layui/layui.js"></script>
    <link rel="stylesheet" href="plug-in/layui/css/layui.css"  media="all">

    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>公司信息</legend>
    </fieldset>

    <form class="layui-form">
        <input type="hidden" name="id" value='${company.id}'>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">公司名称</label>
                <div class="layui-input-inline">
                    <input type="text" name="name" value='${company.name}' lay-verify="required" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label" style="width: 150px;">行业</label>
                <div class="layui-input-inline">
                    <input type="text" name="industry" value='${company.industry}' autocomplete="off" class="layui-input">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">办公地址</label>
                <div class="layui-input-inline">
                    <input type="text" name="address" value='${company.address}' lay-verify="required" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label" style="width: 150px;">电话</label>
                <div class="layui-input-inline">
                    <input type="text" name="phone" value='${company.phone}' lay-verify="required" autocomplete="off" class="layui-input">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">发票抬头</label>
                <div class="layui-input-inline">
                    <input type="text" name="invoiceTitle" value='${company.invoiceTitle}' autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label" style="width: 150px;">纳税人识别号（税号）</label>
                <div class="layui-input-inline">
                    <input type="text" name="taxNum" value='${company.taxNum}' lay-verify="required" autocomplete="off" class="layui-input">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">开户行</label>
                <div class="layui-input-inline">
                    <input type="text" name="bank" value='${company.bank}' lay-verify="required" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label" style="width: 150px;">银行账号</label>
                <div class="layui-input-inline">
                    <input type="text" name="bankAccount" value='${company.bankAccount}' lay-verify="required" autocomplete="off" class="layui-input">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">行号</label>
                <div class="layui-input-inline">
                    <input type="text" name="bankCode" value='${company.bankCode}' lay-verify="required" autocomplete="off" class="layui-input">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="*">保存</button>
            </div>
        </div>
    </form>
</div>
<script>
    $(function () {

    });

    layui.use('element', function(){
        var element = layui.element;

        //一些事件监听
        element.on('tab(salary)', function(data){

        });
    });

    layui.use(['form', 'layedit', 'laydate'], function(){
        var form = layui.form
                ,layer = layui.layer
                ,layedit = layui.layedit
                ,laydate = layui.laydate;

        //保存提交
        form.on('submit(*)', function (data) {
            var url = "companyController.do?saveCompany";
            $.ajax({
                url: url,
                type: "post",
                data: data.field,
                dataType: "json",
                success: function (data) {
                    layer.alert(data.msg, {
                        title: '提交结果'
                    });
                }
            });
            return false;
        });
    });
</script>