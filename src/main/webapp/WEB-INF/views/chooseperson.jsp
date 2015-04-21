s<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
    	$('[data-toggle="tooltip"]').tooltip();   
	});
	$(document).ready(function(){
	    $('[data-toggle="popover"]').popover({
	        placement : 'top'
	    });
	});
</script>
</head>

<body>

	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li class="active"><a href="<spring:url value="/"/>">Home</a></li>
					<li><a href="<spring:url value="/algorithm"/>">About the
							algorithm</a></li>
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
		<h2>Step 2: Choose algorithm settings</h2>
		<c:url value="/person" var="theURL" />
		<form:form modelAttribute="input" action="${theURL}" method="POST" class="form-horizontal">
			<form:errors path="*" cssClass="alert alert-danger" element="div" />
			<fieldset>
				<table>
					<tr>
						<td>
							<label class="control-label" for="person"> 
								<spring:message	code="choosePerson.label" />
							</label>
						</td>
						<td>
							<select name="person">
								<c:forEach items="${hits}" var="hit">
									<option value="urlpt=${hit.urlPt}&name=${hit.author}">${hit.author}</option>
								</c:forEach>
							</select>
						</td>
						<td>
							<form:errors path="person" cssClass="text-danger" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="control-label" for="definition">
								<spring:message	code="choosePerson.definition" />
							</label>
						</td>
						<td>
							<select name="definition"">
								<option value="strong">Strong community</option>
								<option value="weak" selected="selected">Weak community</option>
								<option value="strongweighted">Strong weighted community</option>
								<option value="weakweighted">Weak weighted community</option>
								<option value="bounded">Bounded size community</option>
							</select>
						</td>
						<td></td>
						<td>
							<form:errors path="definition" cssClass="text-danger" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="control-label" for="lower">
								<spring:message	code="choosePerson.lower" />
							</label>
						</td>
						<td>
							<select name="lower">
								<option value="0" selected="selected">0 %</option>
								<option value="0.01">1 %</option>
								<option value="0.02">2 %</option>
								<option value="0.03">3 %</option>
								<option value="0.04">4 %</option>
								<option value="0.05">5 %</option>
								<option value="0.06">6 %</option>
								<option value="0.07">7 %</option>
								<option value="0.08">8 %</option>
								<option value="0.09">9 %</option>
								<option value="0.10" selected="selected">10 %</option>
								<option value="0.11">11 %</option>
								<option value="0.12">12 %</option>
								<option value="0.13">13 %</option>
								<option value="0.14">14 %</option>
								<option value="0.15">15 %</option>
								<option value="0.16">16 %</option>
								<option value="0.17">17 %</option>
								<option value="0.18">18 %</option>
								<option value="0.19">19 %</option>
								<option value="0.20">20 %</option>
								<option value="0.21">21 %</option>
								<option value="0.22">22 %</option>
								<option value="0.23">23 %</option>
								<option value="0.24">24 %</option>
								<option value="0.25">25 %</option>
								<option value="0.26">26 %</option>
								<option value="0.27">27 %</option>
								<option value="0.28">28 %</option>
								<option value="0.29">29 %</option>
								<option value="0.30">30 %</option>
								<option value="0.31">31 %</option>
								<option value="0.32">32 %</option>
								<option value="0.33">33 %</option>
								<option value="0.34">34 %</option>
								<option value="0.35">35 %</option>
								<option value="0.36">36 %</option>
								<option value="0.37">37 %</option>
								<option value="0.38">38 %</option>
								<option value="0.39">39 %</option>
								<option value="0.40">40 %</option>
								<option value="0.41">41 %</option>
								<option value="0.42">42 %</option>
								<option value="0.43">43 %</option>
								<option value="0.44">44 %</option>
								<option value="0.45">45 %</option>
								<option value="0.46">46 %</option>
								<option value="0.47">47 %</option>
								<option value="0.48">48 %</option>
								<option value="0.49">49 %</option>
								<option value="0.50">50 %</option>
								<option value="0.51">51 %</option>
								<option value="0.52">52 %</option>
								<option value="0.53">53 %</option>
								<option value="0.54">54 %</option>
								<option value="0.55">55 %</option>
								<option value="0.56">56 %</option>
								<option value="0.57">57 %</option>
								<option value="0.58">58 %</option>
								<option value="0.59">59 %</option>
								<option value="0.60">60 %</option>
								<option value="0.61">61 %</option>
								<option value="0.62">62 %</option>
								<option value="0.63">63 %</option>
								<option value="0.64">64 %</option>
								<option value="0.65">65 %</option>
								<option value="0.66">66 %</option>
								<option value="0.67">67 %</option>
								<option value="0.68">68 %</option>
								<option value="0.69">69 %</option>
								<option value="0.70">70 %</option>
								<option value="0.71">71 %</option>
								<option value="0.72">72 %</option>
								<option value="0.73">73 %</option>
								<option value="0.74">74 %</option>
								<option value="0.75">75 %</option>
								<option value="0.76">76 %</option>
								<option value="0.77">77 %</option>
								<option value="0.78">78 %</option>
								<option value="0.79">79 %</option>
								<option value="0.80">80 %</option>
								<option value="0.81">81 %</option>
								<option value="0.82">82 %</option>
								<option value="0.83">83 %</option>
								<option value="0.84">84 %</option>
								<option value="0.85">85 %</option>
								<option value="0.86">86 %</option>
								<option value="0.87">87 %</option>
								<option value="0.88">88 %</option>
								<option value="0.89">89 %</option>
								<option value="0.90">90 %</option>
								<option value="0.91">91 %</option>
								<option value="0.92">92 %</option>
								<option value="0.93">93 %</option>
								<option value="0.94">94 %</option>
								<option value="0.95">95 %</option>
								<option value="0.96">96 %</option>
								<option value="0.97">97 %</option>
								<option value="0.98">98 %</option>
								<option value="0.99">99 %</option>
								<option value="1.0">100 %</option>
							</select>
						</td>
						<td>
							<button 
								type="button" 
								class="btn btn-info btn-xs" 
								data-toggle="popover" 
								title="Choosing a lower bound" 
								data-content="Ensure that every community found by the algorithm contains at least a certain percentage of all the vertices in the network. 0 % corresponds to the original algorithm by Radicchi et al. However, you may end up with communities below the lower bound if the whole community is only connected to the main author and he/she is not included in the network.">
								<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
							</button>
						</td>
						<td>
							<form:errors path="lower" cssClass="text-danger" />
						</td>
					</tr>
					
					<tr>
						<td>
							<label class="control-label" for="showNumEdges">
								<spring:message	code="choosePerson.showNumEdges" />
							</label>
						</td>
						<td>
							<form:checkbox path="showNumEdges"/>
						</td>
						<td>
							<button 
								type="button" 
								class="btn btn-info btn-xs" 
								data-toggle="popover" 
								title="Show detailed edge information" 
								data-content="Check this box if you want to know exactly how many edges there are between every pair of communities.">
								<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
							</button>
						</td>
						<td>
							<form:errors path="showNumEdges" cssClass="text-danger" />
						</td>
					</tr>
					
					<tr>
						<td>
							<label class="control-label" for="includeMainAuthor">
								<spring:message	code="choosePerson.includeMainAuthor" />
							</label>
						</td>
						<td>
							<form:checkbox path="includeMainAuthor"/>
						</td>
						<td>
							<button 
								type="button" 
								class="btn btn-info btn-xs" 
								data-toggle="popover" 
								title="Include main author" 
								data-content="Check this box if you want to include the main author in the network, both in computations and in the results that are shown on the web page.">
								<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
							</button>
						</td>
						<td>
							<form:errors path="includeMainAuthor" cssClass="text-danger" />
						</td>
					</tr>
					
					<tr>
						<td>
							
						</td>
						<td>
							<input type="submit" id="btnDetect" class="btn btn-primary"	value="Detect" />
						</td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</table>
			</fieldset>
		</form:form>
	</section>

</body>

</html>