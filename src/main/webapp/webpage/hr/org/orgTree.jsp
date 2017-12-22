<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>组织架构</title>
    <t:base type="jquery,easyui"></t:base>
    <link rel="stylesheet" href="plug-in/ztree/css/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.core-3.5.min.js"></script>
    <script type="text/javascript" src="plug-in/ztree/js/ztreeCreator.js"></script>
    <script type="text/javascript" src="plug-in/lhgDialog/lhgdialog.min.js"></script>
    <script type="text/javascript" src="plug-in/tools/curdtools_zh-cn.js"></script>
</head>
<body>
<div class="easyui-layout" fit="true" scroll="no">
    <div data-options="region:'west',title:'组织管理',split:true" style="width:200px;overflow: auto;">
        <div id="orgTree" class="ztree"></div>
    </div>
    <div data-options="region:'center',border:false" style="text-align: center;">
        <iframe id="listFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
    </div>
    <div class="hidden">
        <div id="orgMenu" class="easyui-menu" data-options="onClick:menuHandler" style="width: 120px;">
            <div data-options="name:'add'">添加子级组织</div>
            <div data-options="name:'edit'">编辑当前组织</div>
            <div data-options="name:'remove'">删除当前组织</div>
            <div data-options="name:'fresh'">刷新</div>
        </div>
    </div>
</div>
</body>
</html>
<script>
    //加载树
    var orgTree;
    function loadTree() {
        var zNodes;
        jQuery.ajax({
            async: false,
            cache: false,
            type: 'POST',
            dataType: "json",
            url: 'orgController.do?getDepartTreeData',//请求的action路径
            error: function () {//请求失败处理函数
                alert('请求失败');
            },
            success: function (data) { //请求成功后处理函数。
                zNodes = data.obj;   //把后台封装好的简单Json格式赋给zNodes
            }
        });
        var ztreeCreator = new ZtreeCreator('orgTree', "orgController.do?getDepartTreeData", zNodes)
                .setCallback({onClick: zTreeOnLeftClick, onRightClick: zTreeOnRightClick})
                .initZtree({}, function (treeObj) {
                    orgTree = treeObj
                });

    }

    //左击
    function zTreeOnLeftClick(event, treeId, treeNode) {
        curSelectNode = treeNode;
        var parentId = treeNode.id;
        var url = "orgController.do?goUserList&id=" + curSelectNode.id;
        if (curSelectNode.parentId == "0") {
            return false;
        }
        $("#listFrame").attr("src", url);
    }

    //树右击事件
    function zTreeOnRightClick(e, treeId, treeNode) {
        if (treeNode) {
            orgTree.selectNode(treeNode);
            curSelectNode = treeNode;
            var isfolder = treeNode.isFolder;
            var h = $(window).height();
            var w = $(window).width();
            var menuWidth = 120;
            var menuHeight = 75;
            var menu = null;
            if (treeNode != null) {
                menu = $('#orgMenu');
            }
            var x = e.pageX, y = e.pageY;
            if (e.pageY + menuHeight > h) {
                y = e.pageY - menuHeight;
            }
            if (e.pageX + menuWidth > w) {
                x = e.pageX - menuWidth;
            }
            menu.menu('show', {
                left: x,
                top: y
            });
        }
    }

    //菜单对应项
    function menuHandler(item) {
        if ('add' == item.name) {
            addNode();
        } else if ('remove' == item.name) {
            delNode();
        } else if ('edit' == item.name) {
            editNode(1);
        } else if ('fresh' == item.name) {
            refreshNode();
        }
    }

    function refreshNode() {
        loadTree();
    }

    //添加节点
    function addNode() {
        var selectNode = getSelectNode();
        if (!selectNode)    return;
        var url = "orgController.do?add&id=" + selectNode.id;
        $("#listFrame").attr("src", url);
        refreshNode();
    }

    //编辑节点
    function editNode(type) {
        var selectNode = getSelectNode();
        if (!selectNode)    return;
        //根节点 不能编辑
        if (selectNode.parentId == "0" && type == 1) {
            tip('公司信息，不可编辑');
            return;
        }

        var url = "orgController.do?update&id=" + selectNode.id;
        $("#listFrame").attr("src", url);
    }

    //删除
    function delNode() {
        var selectNode = getSelectNode();
        var parentId = selectNode.parentId;
        if (parentId == "0") {
            tip('公司信息，不可删除');
            return;
        }
        var url = "orgController.do?del&id=" + selectNode.id;
        $.dialog.confirm('确定删除吗?', function (r) {
            jQuery.ajax({
                async: false,
                cache: false,
                type: 'GET',
                dataType: "json",
                url: url,//请求的action路径
                error: function () {//请求失败处理函数
                    alert('请求失败');
                },
                success: function (data) { //请求成功后处理函数。
                    if (data.success) {
                        orgTree.removeNode(selectNode);
                        $("#listFrame").attr("src", "about:blank");
                    } else {
                        alert(data.msg);
                    }
                }
            });
        });
    }

    //选择资源节点。
    function getSelectNode() {
        orgTree = $.fn.zTree.getZTreeObj("orgTree");
        var nodes = orgTree.getSelectedNodes();
        var node = nodes[0];
        return node;
    }
</script>
<script>
    $(function () {
        loadTree();
    });
</script>