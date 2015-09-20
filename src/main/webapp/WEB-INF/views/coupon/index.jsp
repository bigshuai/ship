<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/static/images/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/static/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/static/jquery/jquery.page.js"></script>
<style>
.tcdPageCode{padding: 15px 20px;text-align: left;color: #ccc;}
.tcdPageCode a{display: inline-block;color: #428bca;display: inline-block;height: 25px;	line-height: 25px;	padding: 0 10px;border: 1px solid #ddd;	margin: 0 2px;border-radius: 4px;vertical-align: middle;}
.tcdPageCode a:hover{text-decoration: none;border: 1px solid #428bca;}
.tcdPageCode span.current{display: inline-block;height: 25px;line-height: 25px;padding: 0 10px;margin: 0 2px;color: #fff;background-color: #428bca;	border: 1px solid #428bca;border-radius: 4px;vertical-align: middle;}
.tcdPageCode span.disabled{	display: inline-block;height: 25px;line-height: 25px;padding: 0 10px;margin: 0 2px;	color: #bfbfbf;background: #f2f2f2;border: 1px solid #bfbfbf;border-radius: 4px;vertical-align: middle;}
</style>
<script type="text/javascript">  
    $(document).ready(function(){  
   	  	$(".tcdPageCode").createPage({
   	        pageCount:6,
   	        current:1,
   	        backFn:function(p){
   	            console.log(p);
   	        }
   	    });
    });   
</script>
</head>

<body>
	<div id="contentWrap">
		<div class="" style="">
			<div id="coupon_query_id">
				<table>
					<tr>
						<td><span>ID:</span><input type="text" style="width: 120px"></td>
						<td><span>名称:</span><input type="text" style="width: 120px"></td>
					</tr>
					<tr>
						<td colspan="2"><button type="button">查询</button></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="pageColumn" style="margin-top: 50px">
			<table id="alltable" class="tablesorter" width="900">
				<thead>
					<tr>
						<th>id</th>
						<th>名称</th>
						<th>描述</th>
						<th>面额</th>
						<th>所属加油站</th>
						<th>类型</th>
						<th>状态</th>
						<th>有效天数</th>
						<th>有效期</th>
						<th>创建时间</th>
					</tr>
				</thead>
				<tbody>
				    <c:forEach items="${rl.dataList}" var="coupon">
					<tr>
						<td>${coupon.id}</td>
						<td>${coupon.name}</td>
						<td>${coupon.desc}</td>
						<td>${coupon.limitValue} - ${coupon.faceValue}</td>
						<td>${coupon.osId}</td>
						<td>${coupon.type}</td>
						<td>${coupon.status}</td>
						<td>${coupon.effectiveDay}</td>
						<td>${coupon.startTime}  -  ${coupon.endTime}</td>
						<td>${coupon.createTime}</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="tcdPageCode"></div>
		</div>
	</div>
</body>
</html>