<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="${ctx}/static/images/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/static/jquery/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	$('.menu').height($(window).height()-51-27-26);
	$('.sidebar').height($(window).height()-51-27-26);
	$('.page').height($(window).height()-51-27-26);
	$('.page iframe').width($(window).width()-15-168);
	
	//menu on and off
	$('.btn').click(function(){
		$('.menu').toggle();
		
		if($(".menu").is(":hidden")){
			$('.page iframe').width($(window).width()-15+5);
			}else{
			$('.page iframe').width($(window).width()-15-168);
				}
		});
		
	//
	$('.subMenu a[href="#"]').click(function(){
		$(this).next('ul').toggle();
		return false;
		});
})

function meunClick(url){
	$("#page_main").load(url);
}
</script>
</head>

<body>
<div id="wrap">
	<div id="header">
    <div class="logo fleft"></div>
    <div class="nav fleft">
    	<ul>
        	<div class="nav-left fleft"></div>
            <li class="first">我的面板</li>
        	<li>设置</li>
            <li>模块</li>
            <li>内容</li>
            <li>用户</li>
            <li>扩展</li>
            <li>应用</li>
            <div class="nav-right fleft"></div>
        </ul>
    </div>
    <a class="logout fright" href="login.html"> </a>
    <div class="clear"></div>
    <div class="subnav">
    	<div class="subnavLeft fleft"></div>
        <div class="fleft"></div>
        <div class="subnavRight fright"></div>
    </div>
    </div><!--#header -->
    <div id="content">
    <div class="space"></div>
    <div class="menu fleft">
    	<ul>
        	<li class="subMenuTitle">系统管理</li>
            <li class="subMenu"><a href="#">优惠券管理</a>
            	<ul>
                	<li><a href="#" onclick="meunClick('m/coupon/index')">优惠券列表</a></li>
                    <li><a href="#">优惠券使用列表</a></li>
                </ul>
            </li>
            <li class="subMenu"><a href="main.html" target="right">加油站管理</a>
            	<ul>
                	<li><a href="#">加油站管理</a></li>
                </ul>
            </li>
            <li class="subMenu"><a href="http://www.baidu.com" target="right">订单管理</a></li>
            <li class="subMenu"><a href="http://www.baidu.com" target="right">用户管理</a></li>
            <li class="subMenu"><a href="http://www.baidu.com" target="right">标题标题标题</a></li>
            <li class="subMenu"><a href="http://www.baidu.com" target="right">标题标题标题</a></li>
            <li class="subMenu"><a href="http://www.baidu.com" target="right">标题标题标题</a></li>
            <li class="subMenu"><a href="http://www.baidu.com" target="right">标题标题标题</a></li>
        </ul>
    </div>
	<div id="page_main" style="width: 1000px;padding-left: 200px">航云宝管理系统</div>
</div>
</body>
</html>
