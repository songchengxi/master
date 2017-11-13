<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html >
<html>
<head>
<title>公司集合</title>
<t:base type="jquery"></t:base>
<link rel="stylesheet" type="text/css" href="plug-in/ztree/css/zTreeStyle.css">
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="plug-in/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>
<script type="text/javascript">
	var setting = {
	  check: {
			enable: true,
		  	chkStyle: "radio",  //单选框
			chkboxType: { "Y": "", "N": "" }
		},
		data: {
			simpleData: {
				enable: true
			}
		},callback: {
//			onExpand: zTreeOnExpand
		}
	};
	
	//加载展开方法
	function zTreeOnExpand(event, treeId, treeNode){
		 var treeNodeId = treeNode.id;
		 $.post(
			'companyController.do?getCompanyInfo',
			{parentid:treeNodeId,companyId:$("#companyId").val()},
			function(data){
				var d = $.parseJSON(data);
				if (d.success) {
					var dbDate = eval(d.msg);
					var tree = $.fn.zTree.getZTreeObj("companySelect");

					if (!treeNode.zAsync){
						tree.addNodes(treeNode, dbDate);
						treeNode.zAsync = true;
					} else{
						tree.reAsyncChildNodes(treeNode, "refresh");
					}
					//tree.addNodes(treeNode, dbDate);
				}
			}
		);
	}
	
	//首次进入加载level为1的
	$(function(){
		$.post(
			'companyController.do?getCompanyInfo',
		    {companyId:$("#companyId").val()},
			function(data){
				var d = $.parseJSON(data);
				if (d.success) {
					var dbDate = eval(d.msg);
					$.fn.zTree.init($("#companySelect"), setting, dbDate);
				}
			}
		);
	});
</script>
</head>
<body style="overflow-y: auto" scroll="no">
<input id="companyId" name="companyId" type="hidden" value="${companyId}">
<ul id="companySelect" class="ztree" style="margin-top: 30px;"></ul>
</body>
</html>