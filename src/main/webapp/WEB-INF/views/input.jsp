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
				<h2>About the input</h2>
				<p><b>Person.</b> You may get several hits on your person search. Choose the desired person.</p>
				
				<p>
					<b>Community definitions.</b> 
					<ul>
						<li>Given an input	graph $G = (V,E)$, a <b>strong community</b> is a set of vertices $W \subseteq V$ with 
						the property that every vertex $v \in W$ has strictly more neighbors in $W$ than in $\overline{W}$.</li>
						<li>Given an input	graph $G = (V,E)$, a <b>weak community</b>	is a set of vertices $W \subseteq V$ with 
						the property that the sum of the neighbors of the vertices in $W$ that fall within $W$ is greater than 
						the sum of the neighbors of the vertices in $W$ that fall within $\overline{W}$.</li>
						<li>Given an input	graph $G = (V,E)$, a <b>strong weighted community</b> (<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>) is a set of vertices $W \subseteq V$ with 
						the property that every vertex $v \in W$ has a strictly higher total edge weight to vertices in $W$ than to vertices
						in $\overline{W}$. That is, if for every $v \in W$, we have that $\sum_{u \in N(v), u \in W} w(uv) > \sum_{u \in N(v), u \notin W} w(uv)$.</li>
						<li>Given an input	graph $G = (V,E)$, a <b>weak weighted community</b> is a set of vertices $W \subseteq V$ with 
						the property that $\sum_{u \in W} \sum_{v \in N(u), v \in W} w(uv) > \sum_{u \in W} \sum_{v \in N(u), v \notin W} w(uv)$.</li>
						<li>Given an input	graph $G = (V,E)$, a <b>bounded community</b> (<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>) is a set of vertices $W \subseteq V$
						such that $|W| \geq |V| \cdot \ell$, where $\ell \in [0,1]$ is the lower bound.</li>
					</ul>
					<p><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> The strong weighted community definition and 
					the bounded community definition usually don't give good results and we therefore discourage the use of these definitions when 
					you want to find good communities. They are included for the sake of the curious investigator and to be a continuous reminder
					of what doesn't work.</p>
				</p>
				<p>
					<b>Lower bound.</b>
					You can enforce the requirement that every community found by the algorithm must have a size equal to a certain
					percentage of the total number of vertices in the collaboration network.
				</p>
				<p>
					<b>Change layers.</b> The algorithm is a divisive hierarchical algorithm that may discover several layers of communities.
					A new layer is discovered by the algorithm when a given community is split into two disconnected parts by removing a sufficient
					amount of edges from the community.
				</p>
			</div>
		</section>
		
	</body>
	
</html>