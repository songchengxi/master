<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <t:datagrid name="socialBillList" checkbox="false" pagination="true" fitColumns="true" title="参保账单"
                    actionUrl="hrSocialController.do?billData" idField="id" fit="true" queryMode="group">
            <t:dgCol title="主键" field="id" hidden="true" width="120"></t:dgCol>
            <t:dgCol title="月份" field="month" width="120"></t:dgCol>
            <t:dgCol title="人数" field="count" width="120"></t:dgCol>
            <t:dgCol title="社保公司缴费数额" field="socialComVal" width="120"></t:dgCol>
            <t:dgCol title="社保个人缴纳数额" field="socialUserVal" width="120"></t:dgCol>
            <t:dgCol title="公积金公司缴纳数额" field="fundComVal" width="120"></t:dgCol>
            <t:dgCol title="公积金个人缴纳数额" field="fundUserVal" width="120"></t:dgCol>
            <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
            <t:dgFunOpt funname="detail(month)" title="详细" urlclass="ace_button" urlStyle="background-color:#18a689;" urlfont="fa-cog"></t:dgFunOpt>
            <t:dgFunOpt funname="rebuild(month)" title="重新生成" urlclass="ace_button" urlStyle="background-color:#18a689;" urlfont="fa-cog"></t:dgFunOpt>
            <t:dgToolBar title="生成账单" icon="icon-edit" url="hrSocialController.do?goAddBill" funname="add"></t:dgToolBar>
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

    //详细信息
    function detail(month) {
        addOneTab(month + "社保详细信息", "hrSocialController.do?goBillDetail&month=" + month);
    }

    //重新生成账单
    function rebuild(month){
        var url = "hrSocialController.do?rebuild&month=" + month;
        createdialog(month + ' 账单重新生成', '将重新生成 [' + month + '] 的社保账单。确定吗 ?', url, "socialBillList");
        reloadTable();
    }

</script>