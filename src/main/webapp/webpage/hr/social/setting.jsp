<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker,autocomplete"></t:base>
<style type="text/css">
    .value{
        padding: 10px auto 10px 10px;
    }
</style>

<div>
    <div class="easyui-layout" fit="true">
        <div region="center" style="padding:0;border:0">
            <t:datagrid name="socialType" title="社保信息" actionUrl="hrSocialController.do?data"
                        idField="id" treegrid="false" pagination="true" pageSize="5" onClick="querySocialByRowData"
                        onLoadSuccess="loadSuccess" queryMode="group" btnCls="bootstrap btn btn-info btn-xs">
                <t:dgCol title="common.code" field="id" hidden="true"></t:dgCol>
                <t:dgCol title="类型" field="name" width="300"></t:dgCol>
                <t:dgCol title="是否系统" field="isSys" hidden="true" width="300"></t:dgCol>
            </t:datagrid>
        </div>
    </div>
</div>

<div style="height:260px;" name="editPanel" id="editPanel" fit="true" class="easyui-panel">
    <table width="100%" id="socialRowList" toolbar="#socialRowListtb"></table>
    <div id="socialRowListtb" style="padding:3px; height: auto">
        <div style="border-bottom-width:0;height:auto;" class="datagrid-toolbar">
        <span style="float:left;">
            <a href="#" class="easyui-linkbutton" plain="true" icon="icon-add"
               onclick="addRow()">录入</a>
            <a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
               onclick="editRow()">编辑</a>
            <a href="#" class="easyui-linkbutton" plain="true" icon="icon-save"
               onclick="saveData()">保存</a>
            <a href="#" class="easyui-linkbutton" plain="true" icon="icon-undo"
               onclick="reject()">取消编辑</a>
            <%--<a href="#" class="easyui-linkbutton" plain="true" icon="icon-undo" onclick="calculator()">计算器</a>--%>
        </span>
            <div style="clear:both"></div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        //隐藏滚动条，固定高度，可根据表单字段数量调整
        $("body").css({"overflow": "hidden", "height": "600px"});
    });

    function loadSuccess() {
        var rows = $('#socialType').datagrid('getRows');
        if (rows.length > 0) {
            loadRowList(rows[0].id);
        }
    }

    function querySocialByRowData(rowIndex, rowData) {
        reject();//取消编辑
        loadRowList(rowData.id);
    }

    function deleteDialog(id) {
        //提示框
//            $.messager.confirm("确认", "确定要删除这条数据？", function (r) {
//                if (r) {
//                    var url = "jeecgListDemoController.do?doBatchDel&ids=" + id;
//                    $.ajax({
//                        url: url,
//                        type: "get",
//                        dataType: "json",
//                        success: function (data) {
//                            top.tip(data.msg);
//                            if (data.success) {
//                                $("#jeecgrowList").datagrid('reload');
//                            }
//                        }
//                    })
//                }
//            });
    }

    var pId;

    function loadRowList(parentId) {
        pId = parentId;
        storage = $.localStorage;
        if (!storage)storage = $.cookieStorage;
        $('#socialRowList').datagrid({
            idField: 'id',
            title: '编辑数据',
            url: 'hrSocialController.do?datagrid&field=id,name,base,companyProportion,userProportion,companyVal,userVal,status,isSys,&parentId=' + parentId,
            fit: true,
            loadMsg: '数据加载中...',
            pageSize: 10,
            pagination: true,
            pageList: [10, 20, 30],
            sortOrder: 'asc',
            rownumbers: true,
            singleSelect: false,
            fitColumns: true,
            striped: true,
            showFooter: true,
            frozenColumns: [[{field: 'ck', checkbox: 'true'}]],
            columns: [[{field: 'id', title: 'id', width: 140, hidden: true, sortable: true},
                {field: 'name', title: '项目', width: 80, rowspan: 2, editor: 'text', sortable: true},
                {field: 'base', title: '缴费基数（元）', width: 80, rowspan: 2, editor: {type: 'numberbox', options: {precision: 2}}, sortable: true},
                {title: '单位缴纳', colspan: "2"}, {title: '个人缴纳', colspan: "2"},
                {field: 'isSys', title: '是否系统', width: 60, rowspan: 2, hidden: true, sortable: true},
                {field: 'status', title: '是否启用', width: 60, rowspan: 2, sortable: true,
                    formatter: function (value, rec, index) {
                        if (value == undefined) return '';
                        if (value == 'Y') {
                            return '已启用';
                        }
                        if (value == 'N') {
                            return '已停用';
                        } else {
                            return value;
                        }
                    },
                    styler: function (value, rec, index) {
                        if (value == 'N') {
                            return 'background:red;color:#FFFFFF;'
                        }
                    }
                },
                {field: 'opt', title: '操作', rowspan: 2,
                    formatter: function (value, rec, index) {
                        if (!rec.id) {
                            return '';
                        }
                        var href = '';
                        if ($.inArray(rec.status, ['N']) >= 0) {
                            href += "<a href='#' class='ace_button' onclick=openAndCloseSocial('" + rec.id + "','" + rec.status + "') style='background-color:#18a689;' > <i class='fa fa-cog'></i>";
                            href += "启用</a>&nbsp;";
                        }
                        if ($.inArray(rec.status, ['Y']) >= 0) {
                            href += "<a href='#' class='ace_button' onclick=openAndCloseSocial('" + rec.id + "','" + rec.status + "') style='background-color:#18a689;' > <i class='fa fa-cog'></i>";
                            href += "停用</a>&nbsp;";
                        }
                        if ($.inArray(rec.isSys, ['Y']) < 0) {
                            href += "<a href='#' class='ace_button' onclick=delSocial('" + rec.id + "','" + rec.status + "') style='background-color:#ec4758;' > <i class='fa fa-trash-o'></i>";
                            href += "删除</a>&nbsp;";
                        }
                        return href;
                    }
                }],
                [{field: 'companyProportion', title: '比例（%）', width: 100, editor: {type:'numberbox',options:{precision:2}}, sortable: true},
                {field: 'companyVal', title: '费用（元/月）', width: 100, editor: {type:'numberbox',options:{precision:2}}, sortable: true},
                {field: 'userProportion', title: '比例（%）', width: 100, editor: {type:'numberbox',options:{precision:2}}, sortable: true},
                {field: 'userVal', title: '费用（元/月）', width: 100, editor: {type:'numberbox',options:{precision:2}}, sortable: true}
            ]],
            onLoadSuccess: function (data) {
                $("#socialRowList").datagrid("clearSelections");
                $(this).datagrid("fixRownumber");
                if (!false) {
                    if (data.total && data.rows.length == 0) {
                        var grid = $('#socialRowList');
                        var curr = grid.datagrid('getPager').data("pagination").options.pageNumber;
                        grid.datagrid({pageNumber: (curr - 1)});
                    }
                }
            }
        });
        $('#socialRowList').datagrid('getPager').pagination({
            beforePageText: '',
            afterPageText: '/{pages}',
            displayMsg: '{from}-{to}共 {total}条',
            showPageList: true,
            showRefresh: true
        });
        $('#socialRowList').datagrid('getPager').pagination({
            onBeforeRefresh: function (pageNumber, pageSize) {
                $(this).pagination('loading');
                $(this).pagination('loaded');
            }
        });
        try {
            restoreheader();
        } catch (ex) {
        }
    }

    //录入、添加行
    function addRow() {
        $('#socialRowList').datagrid('appendRow', {});
        var editIndex = $('#socialRowList').datagrid('getRows').length - 1;
        $('#socialRowList').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
    }

    //编辑行
    function editRow() {
        var rows = $('#socialRowList').datagrid("getChecked");
        if (rows.length == 0) {
            tip("请选择条目");
            return false;
        }
        for (var i = 0; i < rows.length; i++) {
            var index = $('#socialRowList').datagrid('getRowIndex', rows[i]);
            $('#socialRowList').datagrid('beginEdit', index);

            // 绑定事件, index为当前编辑行
            let editors = $('#socialRowList').datagrid('getEditors', index);
            let baseEditor = editors[1];//缴纳基数
            let comEditor = editors[2];//公司缴纳比例
            let comVal = editors[3];//公司缴纳
            let userEditor = editors[4];//个人缴纳比例
            let userVal = editors[5];//个人缴纳
            baseEditor.target.bind('change', function () {//缴纳基数
                if ("" != comEditor.target.val()) {
                    $(comVal.target).numberbox('setValue', $(this).val() * comEditor.target.val() * 0.01);
                }
                if ("" != userEditor.target.val()) {
                    $(userVal.target).numberbox('setValue', $(this).val() * userEditor.target.val() * 0.01);
                }
            });
            comEditor.target.bind('change', function () {//公司比例计算公司数额
                if ("" != baseEditor.target.val()) {
                    $(comVal.target).numberbox('setValue', $(this).val() * baseEditor.target.val() * 0.01);
                }
            });
            userEditor.target.bind('change', function () {//个人比例计算个人数额
                if ("" != baseEditor.target.val()) {
                    $(userVal.target).numberbox('setValue', $(this).val() * baseEditor.target.val() * 0.01);
                }
            });
        }
    }

    //保存数据
    function saveData() {
        if (!endEdit())
            return false;
        var rows = $('#socialRowList').datagrid("getChanges", "inserted");
        var uprows = $('#socialRowList').datagrid("getChanges", "updated");
        rows = rows.concat(uprows);
        if (rows.length <= 0) {
            tip("没有需要保存的数据！");
            return false;
        }
        var result = {};
        for (var i = 0; i < rows.length; i++) {
            for (var d in rows[i]) {
                result["childs[" + i + "]." + d] = rows[i][d];
            }
        }
        var addUrl = "hrSocialController.do?saveRows&pId=" + pId;
        $.ajax({
            url: "<%=basePath%>/" + addUrl,
            type: "post",
            data: result,
            dataType: "json",
            success: function (data) {
                tip(data.msg);
                if (data.success) {
                    reloadTable();
                }
            }
        })
    }

    //结束编辑
    function endEdit() {
        var editIndex = $('#socialRowList').datagrid('getRows').length - 1;
        for (var i = 0; i <= editIndex; i++) {
            if ($('#socialRowList').datagrid('validateRow', i)) {
                $('#socialRowList').datagrid('endEdit', i);
            } else {
                tip("请选择必填项(带有红色三角形状的字段)!");
                return false;
            }
        }
        return true;
    }

    //取消编辑
    function reject() {
        $('#socialRowList').datagrid('clearChecked');
        $('#socialRowList').datagrid('rejectChanges');
    }

    //删除
    function delSocial(id, status) {
        var url = "hrSocialController.do?del&id=" + id;
        if ("Y" == status) {
            createdialog('删除确认 ', '该社保已启用，删除将自动去掉该社保项目。确定删除该记录吗 ?', url, "socialRowList");
        } else {
            createdialog('删除确认 ', '确定删除该记录吗 ?', url, "socialRowList");
        }
    }

    //开启或关闭
    function openAndCloseSocial(id, status) {
        var url = "hrSocialController.do?openAndClose&id=" + id;
        if ("Y" == status) {
            createdialog('停用确认 ', '确定停用吗 ?', url, "socialRowList");
        } else {
            createdialog('启用确认 ', '确定开启吗 ?', url, "socialRowList");
        }
    }

    //计算器
    function calculator(){
        var url = "url:hrSocialController.do?openCalculator";
        $.dialog({
            content: url,
            zIndex: getzIndex(),
            lock: true,
            title: '计算器',
            opacity: 0.3,
            width: 400,
            height: 500,
            cache: false,
            cancelVal: '<t:mutiLang langKey="common.close"/>',
            cancel: true /*为true等价于function(){}*/
        });
    }

    function reloadTable() {
        try {
            $('#socialRowList').datagrid('reload');
            $('#socialRowList').treegrid('reload');
        } catch (ex) {
        }
    }

    function reloadsocialRowList() {
        $('#socialRowList').datagrid('reload');
    }

    function getsocialRowListSelected(field) {
        return getSelected(field);
    }

    function getSelected(field) {
        var row = $('#socialRowList').datagrid('getSelected');
        if (row != null) {
            value = row[field];
        } else {
            value = '';
        }
        return value;
    }

    function getsocialRowListSelections(field) {
        var ids = [];
        var rows = $('#socialRowList').datagrid('getSelections');
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i][field]);
        }
        ids.join(',');
        return ids
    }


    function getSelectRows() {
        return $('#socialRowList').datagrid('getChecked');
    }

    function saveHeader() {
        var columnsFields = null;
        var easyextends = false;
        try {
            columnsFields = $('#socialRowList').datagrid('getColumns');
            easyextends = true;
        } catch (e) {
            columnsFields = $('#socialRowList').datagrid('getColumnFields');
        }
        var cols = storage.get('socialRowListhiddenColumns');
        var init = true;
        if (cols) {
            init = false;
        }
        var hiddencolumns = [];
        for (var i = 0; i < columnsFields.length; i++) {
            if (easyextends) {
                hiddencolumns.push({field: columnsFields[i].field, hidden: columnsFields[i].hidden});
            } else {
                var columsDetail = $('#socialRowList').datagrid("getColumnOption", columnsFields[i]);
                if (init) {
                    hiddencolumns.push({
                        field: columsDetail.field,
                        hidden: columsDetail.hidden,
                        visible: (columsDetail.hidden == true ? false : true)
                    });
                } else {
                    for (var j = 0; j < cols.length; j++) {
                        if (cols[j].field == columsDetail.field) {
                            hiddencolumns.push({
                                field: columsDetail.field,
                                hidden: columsDetail.hidden,
                                visible: cols[j].visible
                            });
                        }
                    }
                }
            }
        }
        storage.set('socialRowListhiddenColumns', JSON.stringify(hiddencolumns));
    }

    function isShowBut() {
        var isShowSearchId = $('#isShowSearchId').val();
        if (isShowSearchId == "true") {
            $("#searchColums").hide();
            $('#isShowSearchId').val("false");
            $('#columsShow').remove("src");
            $('#columsShow').attr("src", "plug-in/easyui/themes/default/images/accordion_expand.png");
        } else {
            $("#searchColums").show();
            $('#isShowSearchId').val("true");
            $('#columsShow').remove("src");
            $('#columsShow').attr("src", "plug-in/easyui/themes/default/images/accordion_collapse.png");
        }
    }

    function restoreheader() {
        var cols = storage.get('socialRowListhiddenColumns');
        if (!cols)return;
        for (var i = 0; i < cols.length; i++) {
            try {
                if (cols.visible != false)$('#socialRowList').datagrid((cols[i].hidden == true ? 'hideColumn' : 'showColumn'), cols[i].field);
            } catch (e) {
            }
        }
    }

    function resetheader() {
        var cols = storage.get('socialRowListhiddenColumns');
        if (!cols)return;
        for (var i = 0; i < cols.length; i++) {
            try {
                $('#socialRowList').datagrid((cols.visible == false ? 'hideColumn' : 'showColumn'), cols[i].field);
            } catch (e) {
            }
        }
    }
</script>