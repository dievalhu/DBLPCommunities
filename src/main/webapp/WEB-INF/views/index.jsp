<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
	<script src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
	<script type='text/javascript'>
	 $(document).ready(function(){
         $("#introstuff").hide();
         $("#hide").click(function(){$("#introstuff").hide();});
         $("#show").click(function(){$("#introstuff").show();});
		});
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
	
	<section>
		<div class="container">
				<h3>Introduction</h3>
				<p>This web page offers community detection in the DBLP database using a divisive hierarchical algorithm based on the following article ...</p>
				<div id="introstuff">
					<p>Filippo Radicchi, Claudio Castellano, Federico Cecconi, Vittorio Loreto, and Domenico Parisi. 
						<a href="http://arxiv.org/abs/cond-mat/0309488">Defining and identifying communities in networks.</a>
						<i>Proceedings of the National Academy of Sciences of the United States of America</i>, 
						101(9):2658-2663, 2004.
					</p>
					<p>The algorithm is a <a href="http://en.wikipedia.org/wiki/Hierarchical_clustering">divisive hierarchical</a> algorithm that starts with
					the whole network as one community and that repeatedly removes edges from the graph in order to split it into connected components that
					are viewed as communities.</p>
					<p>The algorithm uses the concept of an <b>edge clustering coefficient</b> (ecc) to decide which edges to remove. The ecc of an edge is determined
					by the number of triangles it is part of. An edge with a small ecc is an edge that is part of few triangles and such an edge is deemed to be a
					good inter-community edge. At every iteration the edge with the smallest ecc is removed from the network. The algorithm stops when it can remove
					no more edges, either because there are no more edges or because it will violate the specified requirements.</p>
					<p>The community definitions used by the algorithm are the following.
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
					</p>
					<p><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> The strong weighted community definition and 
					the bounded community definition usually don't give good results and we therefore discourage the use of these definitions when 
					you want to find good communities. They are included for the sake of the curious investigator and to be a continuous reminder
					of what doesn't work.</p>
					<p>When either the weak or strong community definition are used an edge removal that splits a component into two connected components will only be permitted
					if both components satisfy the definition. If one, however, uses the bounded community definition, the split will be permitted as long a both components
					contains at least the number of vertices specified by the lower bound.</p>
				</div>
				<button id="show">Show introduction</button>
        		<button id="hide">Hide introduction</button>
		</div>
	</section>
	
	<section>
		<div class="container">
			<h2>Step 1: Find a person whose collaboration network you will do
				community detection on</h2>
			
			<c:if test="${not empty noHits}">
				<div class="alert alert-danger" role="alert">
	 	 			<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
	  				<span class="sr-only">Error:</span>
	  				${noHits}
				</div>
			</c:if>
			
			<form:form modelAttribute="newPerson" class="form-horizontal">
				<fieldset>
					<table style="text-align: center">
						<tr>
							<td>
								<form:input id="personName" path="personName" type="text" class="form-control input-lg" size="100" placeholder="Search for a person in the DBLP database" />
							</td>						
						</tr>
						<tr>
							<td>
								<form:errors path="personName" cssClass="text-danger" />
							</td>
						</tr>
						<tr>
							<td>
								<input type="submit" id="btnDetect" class="btn btn-primary"	value="Search" />
							</td>
						</tr>
					</table>
				</fieldset>
			</form:form>
		</div>
	</section>

	<section>
		<div class="container">
			<p></p>
			<p>
				The data provided on the <a href="http://dblp.uni-trier.de/">DBLP</a>
				webpage are released under the <a
					href="http://opendatacommons.org/licenses/by/summary/">Open
					Data Commons Attribution License (ODC-BY 1.0)</a>.
			</p>
		</div>
	</section>

</body>

</html>