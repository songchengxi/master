<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<link rel="stylesheet" href="plug-in/Validform/css/style.css" type="text/css">
<link rel="stylesheet" href="plug-in/Validform/css/tablefrom.css" type="text/css">
<script type="text/javascript" src="plug-in/Validform/js/Validform_v5.3.1_min_zh-cn.js"></script>
<script type="text/javascript" src="plug-in/Validform/js/Validform_Datatype_zh-cn.js"></script>
<script type="text/javascript" src="plug-in/Validform/js/datatype_zh-cn.js"></script>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding:0;border:0">
        <table width="100%" id="userSalaryList" toolbar="#userSalaryListtb"></table>
        <div id="userSalaryListtb" style="padding:3px; height: auto">
            <div name="searchColums" id="searchColums">
                <input id="isShowSearchId" type="hidden" value="false"/>
                <input id="_sqlbuilder" name="sqlbuilder" type="hidden"/>
                <form onkeydown='if(event.keyCode==13){userSalaryListsearch();return false;}' id='userSalaryListForm'>
                    <span style="display:-moz-inline-box;display:inline-block;">
                        <span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 90px;
                        text-align:right;text-overflow:ellipsis;-o-text-overflow:ellipsis; overflow: hidden;
                        white-space:nowrap;">姓名：</span>
                        <input onkeypress="EnterPress(event)" onkeydown="EnterPress()" type="text" name="name"
                               style="width: 120px" class="inuptxt"/>
                    </span>
                </form>
            </div>
            <div style="border-bottom-width:0;height:auto;" class="datagrid-toolbar">
        <span style="float:left;">
        <a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
           onclick="update('编辑','salaryController.do?goUpdateUserSalary','userSalaryList',null,null)">编辑</a>
        </span>
        <span style="float:right">
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="userSalaryListsearch()">查询</a>
        <a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="searchReset()">重置</a>
        </span>
                <div style="clear:both"></div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var columns = [];
    $(function () {
        $.ajax({
            url: 'salaryController.do?getCompanySalary',
            type: 'POST',
            dataType: 'JSON',
            async: false,
            success: function (data) {
                columns = eval(data.obj);
            },
            error: function (data) {
                $.messager.alert('错误', data.msg);
            }
        });
    });

    $(function () {
        storage = $.localStorage;
        if (!storage)storage = $.cookieStorage;
        $('#userSalaryList').datagrid({
            idField: 'id',
            title: '薪酬信息',
            url: 'salaryController.do?userSalaryData&field=id,name,salaryStr,',
            fit: true,
            queryParams: {},
            loadMsg: '数据加载中...',
            pageSize: 10,
            pagination: true,
            pageList: [10, 20, 30],
            sortOrder: 'asc',
            rownumbers: true,
            singleSelect: true,
            fitColumns: true,
            striped: true,
            showFooter: true,
            frozenColumns: [[]],
            columns: [
                columns
            ],
            loadFilter: function (data) {
                //过滤数据
                var value = {
                    total: data.total,
                    rows: []
                };
                var x = 0;
                $.each(data.rows, function (i, item) {
                    var str = JSON.parse("[" + item.salaryStr + "]");
                    $.each(str, function (i, salaryItem) {
                        var salaryId = salaryItem.salaryId;
                        item[salaryId] = salaryItem.value;
                    });
                    value.rows[x++] = item;
                });
                return value;
            },
            onLoadSuccess: function (data) {
                $("#userSalaryList").datagrid("clearSelections");
                $(this).datagrid("fixRownumber");
                if (!false) {
                    if (data.total && data.rows.length == 0) {
                        var grid = $('#userSalaryList');
                        var curr = grid.datagrid('getPager').data("pagination").options.pageNumber;
                        grid.datagrid({pageNumber: (curr - 1)});
                    }
                }
            },
            onClickRow: function (rowIndex, rowData) {
                rowid = rowData.id;
                gridname = 'userSalaryList';
            }
        });
        $('#userSalaryList').datagrid('getPager').pagination({
            beforePageText: '',
            afterPageText: '/{pages}',
            displayMsg: '{from}-{to}共 {total}条',
            showPageList: true,
            showRefresh: true
        });
        $('#userSalaryList').datagrid('getPager').pagination({
            onBeforeRefresh: function (pageNumber, pageSize) {
                $(this).pagination('loading');
                $(this).pagination('loaded');
            }
        });
        try {
            restoreheader();
        } catch (ex) {
        }
    });

    function reloadTable() {
        try {
            $('#userSalaryList').datagrid('reload');
            $('#userSalaryList').treegrid('reload');
        } catch (ex) {
        }
    }

    function reloaduserSalaryList() {
        $('#userSalaryList').datagrid('reload');
    }

    function getuserSalaryListSelected(field) {
        return getSelected(field);
    }

    function getSelected(field) {
        var row = $('#userSalaryList').datagrid('getSelected');
        if (row != null) {
            value = row[field];
        } else {
            value = '';
        }
        return value;
    }

    function getuserSalaryListSelections(field) {
        var ids = [];
        var rows = $('#userSalaryList').datagrid('getSelections');
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i][field]);
        }
        ids.join(',');
        return ids
    }

    function getSelectRows() {
        return $('#userSalaryList').datagrid('getChecked');
    }

    function saveHeader() {
        var columnsFields = null;
        var easyextends = false;
        try {
            columnsFields = $('#userSalaryList').datagrid('getColumns');
            easyextends = true;
        } catch (e) {
            columnsFields = $('#userSalaryList').datagrid('getColumnFields');
        }
        var cols = storage.get('userSalaryListhiddenColumns');
        var init = true;
        if (cols) {
            init = false;
        }
        var hiddencolumns = [];
        for (var i = 0; i < columnsFields.length; i++) {
            if (easyextends) {
                hiddencolumns.push({field: columnsFields[i].field, hidden: columnsFields[i].hidden});
            } else {
                var columsDetail = $('#userSalaryList').datagrid("getColumnOption", columnsFields[i]);
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
        storage.set('userSalaryListhiddenColumns', JSON.stringify(hiddencolumns));
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
        var cols = storage.get('userSalaryListhiddenColumns');
        if (!cols)return;
        for (var i = 0; i < cols.length; i++) {
            try {
                if (cols.visible != false)$('#userSalaryList').datagrid((cols[i].hidden == true ? 'hideColumn' : 'showColumn'), cols[i].field);
            } catch (e) {
            }
        }
    }

    function resetheader() {
        var cols = storage.get('userSalaryListhiddenColumns');
        if (!cols)return;
        for (var i = 0; i < cols.length; i++) {
            try {
                $('#userSalaryList').datagrid((cols.visible == false ? 'hideColumn' : 'showColumn'), cols[i].field);
            } catch (e) {
            }
        }
    }

    function userSalaryListsearch() {
        try {
            if (!$("#userSalaryListForm").Validform({tiptype: 3}).check()) {
                return false;
            }
        } catch (e) {
        }
        if (true) {
            var queryParams = $('#userSalaryList').datagrid('options').queryParams;
            $('#userSalaryListtb').find('*').each(function () {
                queryParams[$(this).attr('name')] = $(this).val();
            });
            $('#userSalaryList').datagrid({
                url: 'salaryController.do?userSalaryData&field=id,name,salaryStr,',
                pageNumber: 1
            });
        }
    }

    function dosearch(params) {
        var jsonparams = $.parseJSON(params);
        $('#userSalaryList').datagrid({
            url: 'salaryController.do?userSalaryData&field=id,name,salaryStr,',
            queryParams: jsonparams
        });
    }

    function userSalaryListsearchbox(value, name) {
        var queryParams = $('#userSalaryList').datagrid('options').queryParams;
        queryParams[name] = value;
        queryParams.searchfield = name;
        $('#userSalaryList').datagrid('reload');
    }

    $('#userSalaryListsearchbox').searchbox({
        searcher: function (value, name) {
            userSalaryListsearchbox(value, name);
        }, menu: '#userSalaryListmm', prompt: '请输入查询关键字'
    });

    function EnterPress(e) {
        var e = e || window.event;
        if (e.keyCode == 13) {
            userSalaryListsearch();
        }
    }

    function searchReset() {
        $("#userSalaryListtb").find(":input").val("");
        var queryParams = $('#userSalaryList').datagrid('options').queryParams;
        $('#userSalaryListtb').find('*').each(function () {
            queryParams[$(this).attr('name')] = $(this).val();
        });
        $('#userSalaryListtb').find("input[type='checkbox']").each(function () {
            $(this).attr('checked', false);
        });
        $('#userSalaryListtb').find("input[type='radio']").each(function () {
            $(this).attr('checked', false);
        });
        $('#userSalaryList').datagrid({
            url: 'salaryController.do?userSalaryData&field=id,name,salaryStr,',
            pageNumber: 1
        });
    }
</script>