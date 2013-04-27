<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="css/register.css" />
<link rel="stylesheet" href="css/main.css" />
<link rel="stylesheet" href="/zh_CN/css/head.css" />
<script src="js/web/jquery/jquery.js"></script>
<script src="js/web/jquery/jquery.blockUI.js"></script>
<script src="js/web/verify_email.js"></script>

<title>马上激活邮箱，完成注册吧！</title>
</head>

<body>
<div id="header" class="header-2">
	<div class="header-center">
		<div class="logo">
			<a ><img src="/zh_CN/images/logo-index.png" alt="duoduo logo"></a>
		</div>
		
		<!--点击注册 和 app 登录-->
		<div class="button-jump">
			<span class="jump-plug-in"><a href="/register.html">注册</a></span>
			<span class="jump-app"><a href="/account/default/login">登录</a></span>
		</div>			
		
		<ul id="nav">
			<li id="home"><a href="/">首页</a></li>
			<li id="nav-download"><a href="http://www.xxxxxx.com/#2">下载 </a></li>
			<li id="nav-on"><a href="/about/us">关于</a></li>
			<li id="nav-help"><a href="/help/us">帮助</a></li>
			<!--
			<li id="nav-support"><a href="javascript:alert('是得,您的满意,是我们最大的支持!')">支持</a></li>
			<li id="nav-blog"><a href="javascript:alert('^_^，原来还有博客哦！')">博客</a></li>
			-->
		</ul>

	</div>
</div>

<div id="stage" class="right-left">
	<div id="container" class="left-register left-verification left-activation">
	
		<div class="pic-top"></div>
		<div class="verification-li">
			<img src="images/pic-14.png" alt="" />
			<ul>
				<li>
					<p class="right-set">多多已经发送了一封验证激活邮件到：<%=request.getParameter("email")%></p>
				</li>
				<li class="color-verification">
					请您点击下面的按钮查看该邮件，完成注册吧！
				</li>
				<li class="verification-li-4">
					<input type="image" src="images/emil-pic.png" onclick="window.open('<%=request.getParameter("mailServer")%>')">
				</li>
			</ul>
		</div>
		
		<div class="verification-li-a">
			<span>还没有收到验证邮件？</span>
			<p>试试到的垃圾邮箱找找看？</p>
			<p>或者您可以再次<a href="javascript:reSendVerifyEmail('<%=request.getParameter("email")%>')">发送验证邮件</a></p>
			<p>如果邮箱地址填写有误，您可以重新<a href="register.html">填写邮箱</a></p>
		</div>

		<div class="pic-bottom"></div>
	</div>
	
	<!--右侧-->
	<div id="right-pane" class="right-register">
		<div class="sign-in">
			已有多多帐号？
			<span>
				<input name="" type="image" src="images/button-1.png" onclick="window.location='/account/default/login/'"/>
			<span>
		</div>
		
		<div class="cooperation-login">
			<span>使用合作网站帐号登录：</span>
			<ul>
				<!--<li><a href="/account/qq/oauth/"><img src="images/qq-pic.png" alt="qq" /></a><a href="/account/qq/oauth/">QQ</a></li>-->
				<li><a href="/account/weibo/oauth/"><img src="images/sina-pic.png" alt="sina" /></a><a href="/account/weibo/oauth/">新浪微博</a></li>
				<li><a href="/account/google/oauth/"><img src="images/mail-pic.png" alt="mail" /></a><a href="/account/google/oauth/">Gmail</a></li>
			</ul>
		</div>

		<div class="do">
			<span>“多多”可以为您做什么？</span>
			<ul>
				<li><span>·</span>第一杯多多，可以把任何互联网内容（文字、图片、视频、商品）同步到移动终端永久收藏、离线查看。</li>
				<li><span>·</span>第二杯多多，可以把收藏内容分享给朋友，让TA共享第一杯多多的美味。</li>
			</ul>
		</div>
	</div>
</div>
</body>
</html>