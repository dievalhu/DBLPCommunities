<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<title>DBLP Communities</title>
		<script type="text/x-mathjax-config">
  			MathJax.Hub.Config({tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}});
		</script>
		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
		</script>
	</head>
	
	<body>
		
		<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li class="active"><a href="<spring:url value="/"/>">Home</a></li>
					<li><a href="<spring:url value="/algorithm"/>">About the algorithm</a></li>
					<li><a href="<spring:url value="/input"/>">About the input</a></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>

	<section>
		<div class="jumbotron">
			<div class="container">
				<h1>DBLP Communities</h1>
				<p><i>Beta version</i></p>
				<p>
					<spring:message code="info" />
				</p>
			</div>
		</div>
	</section>
		
		<section class="container">
			<div class="container">
				<h2>About the algorithm</h2>
				<p>This web page contains an implementation of a divisive community detection algorithm based on
				the following article.</p>
				<p>Filippo Radicchi, Claudio Castellano, Federico Cecconi, Vittorio Loreto, and Domenico Parisi. 
				<a href="http://arxiv.org/abs/cond-mat/0309488">Defining and identifying communities in networks.</a>
				<i>Proceedings of the National Academy of Sciences of the United States of America</i>, 
				101(9):2658-2663, 2004.</p>
				<p>This is a divisive hierarchical algorithm that starts out with one community comprising the whole network. As the
				algorithm progresses it keeps removing edges from the network, with the help of the concept of an edge clustering coefficient,
				resulting in increasingly more connected components (communities). When the algorithm uses either the <b>weak</b> or the <b>strong
				community</b> definition it will only permit an edge removal resulting in two disconnected components if both components 
				satisfy the definition. The <b>bounded size community</b> definition permits every edge removal that results in two disconnected 
				components as long as both components satisfy the lower bound on the size.</p>
			</div>
		</section>
		
	</body>
	
</html>