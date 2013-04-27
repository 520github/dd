<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="css/register.css" />
<link rel="stylesheet" href="css/header.css" />
<link rel="stylesheet" href="css/main.css" />
<script src="js/web/jquery/jquery.js"></script>
<script src="js/web/jquery/jquery.blockUI.js"></script>
<script src="js/web/verify_mobile.js"></script>
<title>马上输入验证码，完成注册吧！</title>
</head>

<body>
<jsp:include page="views/index/head.html" />

<div id="stage" class="right-left">
	<div id="container" class="left-register left-verification">
	
		<div class="pic-top"></div>
		<div class="verification-li">
			<img src="images/pic-12.png" alt="" />
			<ul>
				<li>马上输入验证码，完成注册吧！</li>
				<li>
					<p class="left-set">多多已经向你的手机</p>
					<p class="right-set"><%=request.getParameter("mobile")%></p>
				</li>
				<li class="color-verification">
					发送了免费验证码，请查看你的手机短信。
				</li>
				<li>
					<p class="left-set">短信中的验证码:</p>
					<p class="right-set"><input name="" type="text" id="mobileToken"/></p>
				</li>
				<li class="verification-li-4">
					<input type="image" src="images/confirm-2.png" onclick="verifySMSToken()">
				</li>
			</ul>
		</div>
		
		<div class="verification-li-a">
			<span>没有收到短信验证码？</span>
			<p>如果1分钟内没有收到手机验证短信，请<a href="javascript:reSendVerifySMS('<%=request.getParameter("mobile")%>')">重新发送验证码</a></p>
		</div>

		<div class="pic-bottom"></div>
	</div>
	
	<!--右侧-->
	<div id="right-pane" class="right-register">
		<div class="sign-in">
			已有多多帐号？
			<span>
				<input name="" type="image" src="images/button-1.png" />
			<span>
		</div>
		
		<div class="cooperation-login">
			<span>使用合作网站帐号登录：</span>
			<ul>
				<li><a href="#"><img src="images/qq-pic.png" alt="qq" /></a><a href="#">qq</a></li>
				<li><a href="#"><img src="images/sina-pic.png" alt="sina" /></a><a href="#">新浪微博</a></li>
				<li><a href="#"><img src="images/mail-pic.png" alt="mail" /></a><a href="#">goolge</a></li>
			</ul>
		</div>

		<div class="do">
			<span>在多多可以做什么？</span>
			<ul>
				<li><span>·</span>成为某一领域的专家，众多偻丝膜拜</li>
				<li><span>·</span>享爱朋友间精品内容的推荐举、分享</li>
				<li><span>·</span>轻松自如的找到自己真正感兴趣的话题</li>
			</ul>
		</div>
		
	</div>
</div>
</body>
</html>