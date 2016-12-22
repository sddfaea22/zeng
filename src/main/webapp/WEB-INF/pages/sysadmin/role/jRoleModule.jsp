<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title></title>
	<link rel="stylesheet" href="${ctx }/components/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css" />

	<script type="text/javascript" src="${ctx }/components/zTree/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="${ctx }/components/zTree/js/jquery.ztree.core-3.5.min.js"></script>
	<script type="text/javascript" src="${ctx }/components/zTree/js/jquery.ztree.excheck-3.5.min.js"></script>
	<script type="text/javascript">
		var zTreeObj;
		var setting = {
			check : {
				enable : true
			},
			data : {
				simpleData : {
					enable : true
				}
			}
		};
		
		
		$(function(){
			$.ajax({
				type:"GET",
				url:"${ctx}/sysadmin/roleAction_loadJSONTreeNodes.action?id=${id}",
				dataType:"json",
				success:initzTree
			});
		});
		
		function initzTree(data){
			//1.初始化树
			zTreeObj = $.fn.zTree.init($("#jkTree"), setting, data);
			//2.展开树上的所有结点
			zTreeObj.expandAll(true);
		}
		
		
		
		
		//获取所有选择的节点
		function submitCheckedNodes() {
			var nodes = new Array();
			nodes = zTreeObj.getCheckedNodes(true);		//取得选中的结点
			var str = "";
			for (i = 0; i < nodes.length; i++) {
				if (str != "") {
					str += ",";
				}
				str += nodes[i].id;
			}
			$('#moduleIds').val(str);
		}
	</script>
</head>

<body>
<form name="icform" method="post">
	<input type="hidden" name="id" value="${id}"/>
	<input type="hidden" id="moduleIds" name="moduleIds" value="" />
<div id="menubar">
<div id="middleMenubar">
<div id="innerMenubar">
  <div id="navMenubar">
<ul>
<li id="save"><a href="#" onclick="submitCheckedNodes();formSubmit('roleAction_module','_self');this.blur();">保存</a></li>
<li id="back"><a href="#" onclick="formSubmit('roleAction_list','_self');this.blur();">返回</a></li>
</ul>
  </div>
</div>
</div>
</div>
   
<div class="textbox" id="centerTextbox">
  <div class="textbox-header">
  <div class="textbox-inner-header">
  <div class="textbox-title">
    配置 [${name}] 角色的模块  
  </div> 
  </div>
  </div>
  
<%-- <div>


<div style="text-align:left">
	<c:forEach items="${moduleList}" var="o">
		<div style="padding:3px;">
		<input type="checkbox" name="moduleIds" value="${o.id}" class="input"
			<c:if test="${fn:contains(roleModuleStr,o.id)}">checked</c:if>
		>
		${o.name}
		</div>
	</c:forEach>
</div>
 
</div> --%>
<div>  
	<ul id="jkTree" class="ztree"></ul>  
</div>
 
 
</form>
</body>
</html>

