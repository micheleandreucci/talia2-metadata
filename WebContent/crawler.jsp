<%@ page import="java.util.concurrent.TimeUnit"%>
<%@ page import="org.jsoup.select.Elements"%>
<%@ page import="org.jsoup.nodes.FormElement"%>
<%@ page import="org.jsoup.nodes.Element"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.MalformedURLException"%>
<%@ page import="org.jsoup.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="org.apache.commons.io.FileUtils"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.nio.file.Files"%>
<%@ page import="org.jsoup.nodes.Document"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.nio.file.attribute.BasicFileAttributes"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<!-- saved from url=(0076)file:///Users/paolobarletta/Desktop/Interreg%20MED%20-%20Our%20projects.htm# -->
<html lang="en" itemscope="" itemtype="https://schema.org/WebPage">

<head>
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet" />
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css"
	rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon"
	href="https://interreg-med.eu/typo3conf/ext/e_magineurs/progmed/Resources/Public/Images/favicon.ico"
	type="image/x-icon">
<meta name="robots" content="index, follow">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=1">
<meta property="og:title" content="Our deliveries">
<!--<link rel="stylesheet" type="text/css" href="./Interreg MED - Our deliveries_files/01b63b92f5.css" media="all">-->
<link rel="stylesheet" type="text/css" href="./styles/Main.css"
	media="all">
<link rel="stylesheet" type="text/css" href="./styles/page.css"
	media="all">
<!--<link rel="stylesheet" type="text/css" href="./Interreg MED - Our deliveries_files/print.css" media="print">-->
<!--<link rel="stylesheet" type="text/css" href="./Interreg MED - Our deliveries_files/jquery.fancybox.min.css" media="all">>
  <!--<script async="" src="./Interreg MED - Our deliveries_files/analytics.js"></script>-->
<!--<script src="./Interreg MED - Our deliveries_files/jquery.min.js" type="text/javascript"></script>>
  <!--<script src="./Interreg MED - Our deliveries_files/bd4d1f63e4.js" type="text/javascript"></script>
  <script src="./Interreg MED - Our deliveries_files/RsaEncryptionWithLib.min.js" type="text/javascript"></script>-->
<!-- <script type="text/javascript">
    /*<![CDATA[*/ /*TYPO3RsaEncryptionPublicKeyUrl*/
    var TYPO3RsaEncryptionPublicKeyUrl = 'https:\/\/interreg-med.eu\/index.php?eID=RsaPublicKeyGenerationController'; /*]]>*/
  </script> -->
<title>Interreg MED - Our deliveries</title>
<meta name="msapplication-config" content="none">
<!-- <link rel="dns-prefetch" href="https://fonts.googleapis.com/"> -->
<!-- <link rel="dns-prefetch" href="https://ajax.googleapis.com/"> -->
<link href="./styles/css" rel="stylesheet">

</head>

<body ng-app="deliverySearchApp">
	<div class="page program">
		<div class="color-1">
			<div class="wrapper">
				<header id="header" class="fixNavigation">
					<!--<div class="menu_accessible">
            <ul id="menu_accessible">
              <li><a href="https://interreg-med.eu/projects-results/our-projects/#home-news">Go to content</a></li>
              <li><a href="https://interreg-med.eu/projects-results/our-projects/#header">Go to menu</a></li>
              <li><a href="https://interreg-med.eu/projects-results/our-projects/#search">Go to search</a></li>
            </ul>
          </div>-->
					<div class="wrap-header content">
						<div class="logo">
							<a href="https://interreg-med.eu/"> <img alt="Home"
								src="./images/csm_logo_d5b9bb90ea.png" width="226" height="67">
							</a>
						</div>


					</div>

				</header>

				<div class="bloc-title">
					<h1>
						Smart Search <a style="float: right;" href="index.html"><span
							style="font-size: 80%" class="glyphicon glyphicon-home"></span></a>
					</h1>
				</div>

				<form action="login" method="get">

					<textarea rows="5" cols="50" name="description">
         </textarea>


				</form>


				<footer>
					<div class="wrap-footer">
						<div class="logo">
							<a href="https://interreg-med.eu/"> <img alt="Home"
								src="./images/csm_logo_d5b9bb90ea.png" width="226" height="67">
							</a>
						</div>
						<div class="menu-footer">
							<p class="no_glossary">Programme co-financed by the European
								Regional Development Fund</p>
							<nav>
								<ul class="list-footer">
									<li class="item-footer no_glossary"><a
										href="https://interreg-med.eu/special-pages/legal-notice-and-privacy-policy/">Legal
											notice and privacy policy</a></li>
									<li class="item-footer no_glossary"><a
										href="https://interreg-med.eu/special-pages/contact/">Contact</a></li>
									<li class="item-footer no_glossary"><a
										href="https://interreg-med.eu/special-pages/sitemap/">Sitemap</a></li>
								</ul>
							</nav>
						</div>

						<div class="menu-social">
							<div id="c48">
								<ul class="list-social">
									<li class="item-social"><a class="link-social"
										title="Facebook" target="_blank"
										href="https://www.facebook.com/InterregMED/"> <img
											src="./images/white-icon-fb.png" width="9" height="18" alt=""><img
											src="./images/icon-fb.png" width="20" height="20" alt="">
									</a></li>
									<li class="item-social"><a class="link-social"
										title="Twitter" target="_blank"
										href="https://twitter.com/MEDProgramme"> <img
											src="./images/white-icon-twitter.png" width="19" height="15"
											alt=""><img src="./images/icon-twitter.png" width="20"
											height="20" alt="">
									</a></li>
									<li class="item-social"><a class="link-social"
										title="Linkedin" target="_blank"
										href="http://www.linkedin.com/in/interreg-med-programme?authType=name&amp;authToken=RNP1">
											<img src="./images/white-icon-linkedin.png" width="19"
											height="18" alt=""><img
											src="./images/icon-linkedin.png" width="20" height="20"
											alt="">
									</a></li>
									<li class="item-social"><a class="link-social"
										title="Youtube" target="_blank"
										href="http://www.youtube.com/user/medprogramme"> <img
											src="./images/white-icon-yt.png" width="19" height="13"
											alt=""><img src="./images/icon-yt.png" width="20"
											height="20" alt="">
									</a></li>
									<li class="item-social"><a class="link-social"
										title="Flux RSS" target="_blank"
										href="https://interreg-med.eu/special-pages/rss-feed/"> <img
											src="./images/white-icon-rss.png" width="19" height="18"
											alt=""><img src="./images/icon-rss.png" width="20"
											height="20" alt="">
									</a></li>
								</ul>
							</div>
						</div>
					</div>
				</footer>
			</div>