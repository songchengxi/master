<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<script type="text/javascript">
    var setting = {
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        async: {
            enable: true,
            url: "companyRoleController.do?setAuthority&roleId=${roleId}",
            dataFilter: filter
        },
        callback: {
            beforeAsync: function () {
            },
            onAsyncSuccess: function (event, treeId, treeNode, msg) {
                expandAll();
            },
            onAsyncError: function () {
            },
            onClick: function (event, treeId, treeNode) {
                if (null == treeNode.children) {
                    var roleId = $("#rid").val();
                    $('#operationListpanel').panel(
                            "refresh",
                            "companyRoleController.do?operationListForFunction&functionId=" + treeNode.id + "&roleId=" + roleId);
                    $('#dataRuleListpanel').panel(
                            "refresh",
                            "companyRoleController.do?dataRuleListForFunction&functionId=" + treeNode.id + "&roleId=" + roleId);
                } else {
                }
            }
        }
    };

    function filter(treeId, parentNode, childNodes) {
        if (!childNodes) return null;
        for (var i = 0, l = childNodes.length; i < l; i++) {
            childNodes[i].name = childNodes[i].text;
            if (childNodes[i].children != null) {
                childNodes[i].nodes = childNodes[i].children;
                filter(null, childNodes[i], childNodes[i].nodes);//递归设置子节点
            }
        }
        return childNodes;
    }

    $(function () {
        $.fn.zTree.init($("#functionid"), setting);
        $("#functionListPanel").panel({
            title: '<t:mutiLang langKey="menu.list"/>',
            tools: [{
                iconCls: 'icon-save',
                handler: function () {
                    mysubmit();
                }
            }]
        });
        $("#operationListpanel").panel({
            title: '<t:mutiLang langKey="operate.manage"/>',
            tools: [{
                iconCls: 'icon-save',
                handler: function () {
                    submitOperation();
                }
            }]
        });

        $("#dataRuleListpanel").panel({
            title: '数据规则权限',
            tools: [{
                iconCls: 'icon-save',
                handler: function () {
                    submitDataRule();
                }
            }]
        });
    });

    function mysubmit() {
        var roleId = $("#rid").val();
        var s = GetNode();
        doSubmit("companyRoleController.do?updateAuthority&rolefunctions=" + s + "&roleId=" + roleId);
    }

    function GetNode() {
        var zTree = $.fn.zTree.getZTreeObj("functionid");
        var node = zTree.getCheckedNodes(true);
        //加入实际被选中的节点
        var cnodes = '';
        for (var i = 0; i < node.length; i++) {
            cnodes += node[i].id + ',';
        }
        cnodes = cnodes.substring(0, cnodes.length - 1);
        return cnodes;
    }

    function expandAll() {
        var zTree = $.fn.zTree.getZTreeObj("functionid");
        zTree.expandAll(true);
    }

    function selecrAll() {
        var zTree = $.fn.zTree.getZTreeObj("functionid");
        zTree.checkAllNodes(true);
    }

    function reset() {
        $.fn.zTree.init($("#functionid"), setting);
    }

    function changeMode() {
        var zTree = $.fn.zTree.getZTreeObj("functionid");
        var typeMode = $("#typeMode").val();
        var type = typeMode == 1 ? {"Y": "", "N": ""} : {"Y": "ps", "N": "ps"};
        zTree.setting.check.chkboxType = type;
        $("#typeMode").val(typeMode % 2 + 1);
    }

    $('#selecrAllBtn').linkbutton({});
    $('#resetBtn').linkbutton({});
    $('#changeBtn').linkbutton({});
</script>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding: 1px;">
        <div class="easyui-panel" style="padding: 1px;" fit="true" border="false" id="functionListPanel">
            <input type="hidden" name="roleId" value="${roleId}" id="rid">
            <input type="hidden" id="typeMode" value="1"/>
            <a id="selecrAllBtn" onclick="selecrAll();"><t:mutiLang langKey="select.all"/></a>
            <a id="resetBtn" onclick="reset();"><t:mutiLang langKey="common.reset"/></a>
            <a id="changeBtn" onclick="changeMode();"><t:mutiLang langKey="common.typemode"/></a>
            <ul id="functionid" class="ztree"></ul>
        </div>
    </div>
    <div region="east" style="width: 300px; overflow: hidden;" split="true">
        <div class="easyui-layout" fit="true">
            <div region="center" style="padding: 1px; border: 0;">
                <div class="easyui-panel" style="padding: 1px;" fit="true" border="false" id="operationListpanel"></div>
            </div>
            <div region="east" style="width: 150px; overflow: hidden;" split="true">
                <div class="easyui-panel" style="padding: 1px;" fit="true" border="false" id="dataRuleListpanel"></div>
            </div>
        </div>
    </div>
</div>
