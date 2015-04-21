<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<title>DBLP Communities</title>
		
		<script src="resource/vis/dist/vis.js"></script>
  		<link href="resource/vis/dist/vis.css" rel="stylesheet" type="text/css" />
		
		<style type="text/css">
			#visualization {
	            width: 1000px;
	            height: 800px;
	            border: 1px solid lightgray;
	        }
			#container {
				max-width: 800px;
				height: 600px;
				margin: auto;
			}
		</style>
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
			$("#settingsshow").on("click", function() {
			    $("#settingspanel").collapse('show');
			});
			$("#settingshide").on("click", function() {
			    $("#settingspanel").collapse('hide');
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
	
	<section>
		<div class="container">
			<a href="<spring:url value="/"/>"><button type="button" class="btn btn-primary">Search for another person</button></a>
			<br />
			<br />
		</div>
	</section>
	
	<section>
		<div class="container">
			<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
				<div class="panel panel-primary">
					<div class="panel-heading" role="tab" id="headingOne">
      					<h4 class="panel-title">
	       					<a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
	          					Run the algorithm again with different settings
	        				</a>
      					</h4>
    				</div>
    				<div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
     					<div class="panel-body">		
		  					<c:url value="/changeSettings" var="changeSettingsURL" />	
							<form:form modelAttribute="newSettings" action="${changeSettingsURL}" method="POST" class="form-horizontal">
								<form:errors path="*" cssClass="alert alert-danger" element="div" />
								<fieldset>
									<table>
										<tr>
											<td>
												<label class="control-label" for="person">
													<spring:message	code="thePerson" />
												</label>
											</td>
											<td>
												<select name="person">
													<option value="${person}">${name}</option>
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
												<select name="definition">
													<option <c:if test="${definition == 'strong'}">selected="selected"</c:if> value="strong">Strong community</option>
													<option <c:if test="${definition == 'weak'}">selected="selected"</c:if> value="weak">Weak community</option>
													<option <c:if test="${definition == 'strongweighted'}">selected="selected"</c:if> value="strongweighted">Strong weighted community</option>
													<option <c:if test="${definition == 'weakweighted'}">selected="selected"</c:if> value="weakweighted">Weak weighted community</option>
													<option <c:if test="${definition == 'bounded'}">selected="selected"</c:if> value="bounded">Bounded size community</option>
												</select>
											</td>
											<td>
												
											</td>
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
													<option value="0" <c:if test="${lowerValue eq 0}">selected="selected"</c:if> >0 %</option>
													<option value="0.01" <c:if test="${lowerValue eq 0.01}">selected="selected"</c:if> >1 %</option>
													<option value="0.02" <c:if test="${lowerValue eq 0.02}">selected="selected"</c:if>>2 %</option>
													<option value="0.03" <c:if test="${lowerValue eq 0.03}">selected="selected"</c:if>>3 %</option>
													<option value="0.04" <c:if test="${lowerValue eq 0.04}">selected="selected"</c:if>>4 %</option>
													<option value="0.05" <c:if test="${lowerValue eq 0.05}">selected="selected"</c:if>>5 %</option>
													<option value="0.06" <c:if test="${lowerValue eq 0.06}">selected="selected"</c:if>>6 %</option>
													<option value="0.07" <c:if test="${lowerValue eq 0.07}">selected="selected"</c:if>>7 %</option>
													<option value="0.08" <c:if test="${lowerValue eq 0.08}">selected="selected"</c:if>>8 %</option>
													<option value="0.09" <c:if test="${lowerValue eq 0.09}">selected="selected"</c:if>>9 %</option>
													<option value="0.10" <c:if test="${lowerValue eq 0.10}">selected="selected"</c:if>>10 %</option>
													<option value="0.11" <c:if test="${lowerValue eq 0.11}">selected="selected"</c:if>>11 %</option>
													<option value="0.12" <c:if test="${lowerValue eq 0.12}">selected="selected"</c:if>>12 %</option>
													<option value="0.13" <c:if test="${lowerValue eq 0.13}">selected="selected"</c:if>>13 %</option>
													<option value="0.14" <c:if test="${lowerValue eq 0.14}">selected="selected"</c:if>>14 %</option>
													<option value="0.15" <c:if test="${lowerValue eq 0.15}">selected="selected"</c:if>>15 %</option>
													<option value="0.16" <c:if test="${lowerValue eq 0.16}">selected="selected"</c:if>>16 %</option>
													<option value="0.17" <c:if test="${lowerValue eq 0.17}">selected="selected"</c:if>>17 %</option>
													<option value="0.18" <c:if test="${lowerValue eq 0.18}">selected="selected"</c:if>>18 %</option>
													<option value="0.19" <c:if test="${lowerValue eq 0.19}">selected="selected"</c:if>>19 %</option>
													<option value="0.20" <c:if test="${lowerValue eq 0.20}">selected="selected"</c:if>>20 %</option>
													<option value="0.21" <c:if test="${lowerValue eq 0.21}">selected="selected"</c:if>>21 %</option>
													<option value="0.22" <c:if test="${lowerValue eq 0.22}">selected="selected"</c:if>>22 %</option>
													<option value="0.23" <c:if test="${lowerValue eq 0.23}">selected="selected"</c:if>>23 %</option>
													<option value="0.24" <c:if test="${lowerValue eq 0.24}">selected="selected"</c:if>>24 %</option>
													<option value="0.25" <c:if test="${lowerValue eq 0.25}">selected="selected"</c:if>>25 %</option>
													<option value="0.26" <c:if test="${lowerValue eq 0.26}">selected="selected"</c:if>>26 %</option>
													<option value="0.27" <c:if test="${lowerValue eq 0.27}">selected="selected"</c:if>>27 %</option>
													<option value="0.28" <c:if test="${lowerValue eq 0.28}">selected="selected"</c:if>>28 %</option>
													<option value="0.29" <c:if test="${lowerValue eq 0.29}">selected="selected"</c:if>>29 %</option>
													<option value="0.30" <c:if test="${lowerValue eq 0.30}">selected="selected"</c:if>>30 %</option>
													<option value="0.31" <c:if test="${lowerValue eq 0.31}">selected="selected"</c:if>>31 %</option>
													<option value="0.32" <c:if test="${lowerValue eq 0.32}">selected="selected"</c:if>>32 %</option>
													<option value="0.33" <c:if test="${lowerValue eq 0.33}">selected="selected"</c:if>>33 %</option>
													<option value="0.34" <c:if test="${lowerValue eq 0.34}">selected="selected"</c:if>>34 %</option>
													<option value="0.35" <c:if test="${lowerValue eq 0.35}">selected="selected"</c:if>>35 %</option>
													<option value="0.36" <c:if test="${lowerValue eq 0.36}">selected="selected"</c:if>>36 %</option>
													<option value="0.37" <c:if test="${lowerValue eq 0.37}">selected="selected"</c:if>>37 %</option>
													<option value="0.38" <c:if test="${lowerValue eq 0.38}">selected="selected"</c:if>>38 %</option>
													<option value="0.39" <c:if test="${lowerValue eq 0.39}">selected="selected"</c:if>>39 %</option>
													<option value="0.40" <c:if test="${lowerValue eq 0.40}">selected="selected"</c:if>>40 %</option>
													<option value="0.41" <c:if test="${lowerValue eq 0.41}">selected="selected"</c:if>>41 %</option>
													<option value="0.42" <c:if test="${lowerValue eq 0.42}">selected="selected"</c:if>>42 %</option>
													<option value="0.43" <c:if test="${lowerValue eq 0.43}">selected="selected"</c:if>>43 %</option>
													<option value="0.44" <c:if test="${lowerValue eq 0.44}">selected="selected"</c:if>>44 %</option>
													<option value="0.45" <c:if test="${lowerValue eq 0.45}">selected="selected"</c:if>>45 %</option>
													<option value="0.46" <c:if test="${lowerValue eq 0.46}">selected="selected"</c:if>>46 %</option>
													<option value="0.47" <c:if test="${lowerValue eq 0.47}">selected="selected"</c:if>>47 %</option>
													<option value="0.48" <c:if test="${lowerValue eq 0.48}">selected="selected"</c:if>>48 %</option>
													<option value="0.49" <c:if test="${lowerValue eq 0.49}">selected="selected"</c:if>>49 %</option>
													<option value="0.50" <c:if test="${lowerValue eq 0.50}">selected="selected"</c:if>>50 %</option>
													<option value="0.51" <c:if test="${lowerValue eq 0.51}">selected="selected"</c:if>>51 %</option>
													<option value="0.52" <c:if test="${lowerValue eq 0.52}">selected="selected"</c:if>>52 %</option>
													<option value="0.53" <c:if test="${lowerValue eq 0.53}">selected="selected"</c:if>>53 %</option>
													<option value="0.54" <c:if test="${lowerValue eq 0.54}">selected="selected"</c:if>>54 %</option>
													<option value="0.55" <c:if test="${lowerValue eq 0.55}">selected="selected"</c:if>>55 %</option>
													<option value="0.56" <c:if test="${lowerValue eq 0.56}">selected="selected"</c:if>>56 %</option>
													<option value="0.57" <c:if test="${lowerValue eq 0.57}">selected="selected"</c:if>>57 %</option>
													<option value="0.58" <c:if test="${lowerValue eq 0.58}">selected="selected"</c:if>>58 %</option>
													<option value="0.59" <c:if test="${lowerValue eq 0.59}">selected="selected"</c:if>>59 %</option>
													<option value="0.60" <c:if test="${lowerValue eq 0.60}">selected="selected"</c:if>>60 %</option>
													<option value="0.61" <c:if test="${lowerValue eq 0.61}">selected="selected"</c:if>>61 %</option>
													<option value="0.62" <c:if test="${lowerValue eq 0.62}">selected="selected"</c:if>>62 %</option>
													<option value="0.63" <c:if test="${lowerValue eq 0.63}">selected="selected"</c:if>>63 %</option>
													<option value="0.64" <c:if test="${lowerValue eq 0.64}">selected="selected"</c:if>>64 %</option>
													<option value="0.65" <c:if test="${lowerValue eq 0.65}">selected="selected"</c:if>>65 %</option>
													<option value="0.66" <c:if test="${lowerValue eq 0.66}">selected="selected"</c:if>>66 %</option>
													<option value="0.67" <c:if test="${lowerValue eq 0.67}">selected="selected"</c:if>>67 %</option>
													<option value="0.68" <c:if test="${lowerValue eq 0.68}">selected="selected"</c:if>>68 %</option>
													<option value="0.69" <c:if test="${lowerValue eq 0.69}">selected="selected"</c:if>>69 %</option>
													<option value="0.70" <c:if test="${lowerValue eq 0.70}">selected="selected"</c:if>>70 %</option>
													<option value="0.71" <c:if test="${lowerValue eq 0.71}">selected="selected"</c:if>>71 %</option>
													<option value="0.72" <c:if test="${lowerValue eq 0.72}">selected="selected"</c:if>>72 %</option>
													<option value="0.73" <c:if test="${lowerValue eq 0.73}">selected="selected"</c:if>>73 %</option>
													<option value="0.74" <c:if test="${lowerValue eq 0.74}">selected="selected"</c:if>>74 %</option>
													<option value="0.75" <c:if test="${lowerValue eq 0.75}">selected="selected"</c:if>>75 %</option>
													<option value="0.76" <c:if test="${lowerValue eq 0.76}">selected="selected"</c:if>>76 %</option>
													<option value="0.77" <c:if test="${lowerValue eq 0.77}">selected="selected"</c:if>>77 %</option>
													<option value="0.78" <c:if test="${lowerValue eq 0.78}">selected="selected"</c:if>>78 %</option>
													<option value="0.79" <c:if test="${lowerValue eq 0.79}">selected="selected"</c:if>>79 %</option>
													<option value="0.80" <c:if test="${lowerValue eq 0.80}">selected="selected"</c:if>>80 %</option>
													<option value="0.81" <c:if test="${lowerValue eq 0.81}">selected="selected"</c:if>>81 %</option>
													<option value="0.82" <c:if test="${lowerValue eq 0.82}">selected="selected"</c:if>>82 %</option>
													<option value="0.83" <c:if test="${lowerValue eq 0.83}">selected="selected"</c:if>>83 %</option>
													<option value="0.84" <c:if test="${lowerValue eq 0.84}">selected="selected"</c:if>>84 %</option>
													<option value="0.85" <c:if test="${lowerValue eq 0.85}">selected="selected"</c:if>>85 %</option>
													<option value="0.86" <c:if test="${lowerValue eq 0.86}">selected="selected"</c:if>>86 %</option>
													<option value="0.87" <c:if test="${lowerValue eq 0.87}">selected="selected"</c:if>>87 %</option>
													<option value="0.88" <c:if test="${lowerValue eq 0.88}">selected="selected"</c:if>>88 %</option>
													<option value="0.89" <c:if test="${lowerValue eq 0.89}">selected="selected"</c:if>>89 %</option>
													<option value="0.90" <c:if test="${lowerValue eq 0.90}">selected="selected"</c:if>>90 %</option>
													<option value="0.91" <c:if test="${lowerValue eq 0.91}">selected="selected"</c:if>>91 %</option>
													<option value="0.92" <c:if test="${lowerValue eq 0.92}">selected="selected"</c:if>>92 %</option>
													<option value="0.93" <c:if test="${lowerValue eq 0.93}">selected="selected"</c:if>>93 %</option>
													<option value="0.94" <c:if test="${lowerValue eq 0.94}">selected="selected"</c:if>>94 %</option>
													<option value="0.95" <c:if test="${lowerValue eq 0.95}">selected="selected"</c:if>>95 %</option>
													<option value="0.96" <c:if test="${lowerValue eq 0.96}">selected="selected"</c:if>>96 %</option>
													<option value="0.97" <c:if test="${lowerValue eq 0.97}">selected="selected"</c:if>>97 %</option>
													<option value="0.98" <c:if test="${lowerValue eq 0.98}">selected="selected"</c:if>>98 %</option>
													<option value="0.99" <c:if test="${lowerValue eq 0.99}">selected="selected"</c:if>>99 %</option>
													<option value="1.0" <c:if test="${lowerValue eq 1}">selected="selected"</c:if>>100 %</option>
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
												<c:choose>
													<c:when test="${showNumEdgesValue}"><form:checkbox path="showNumEdges" checked="checked"/></c:when>
													<c:otherwise><form:checkbox path="showNumEdges"/></c:otherwise>
												</c:choose>
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
												<c:choose>
													<c:when test="${includeMainAuthorValue}"><form:checkbox path="includeMainAuthor" checked="checked"/></c:when>
													<c:otherwise><form:checkbox path="includeMainAuthor"/></c:otherwise>
												</c:choose>
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
											<td></td>
											<td>
												<input type="submit" id="btnDetect" class="btn btn-primary"	value="Detect" />
											</td>
											<td></td>
										</tr>
									</table>
								</fieldset>
							</form:form>
						</div>
    				</div>
				</div>
				
				<div class="panel panel-primary">
		  			<div class="panel-heading" role="tab" id="headingTwo">
      					<h4 class="panel-title">
	       					<a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo">
	          					Overview
	        				</a>
      					</h4>
    				</div>
    				<div id="collapseTwo" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingTwo">
			  			<div class="panel-body">
			  				<div class="row">
			  					<div class="col-xs-4">
			  						<p>Collaboration network of: ${name}</p>
									<p>Definition: ${definition}</p>
									<p>Lower bound: ${lower} %</p>		
									<p>${name} included in network: ${includeMainAuthor}</p>							
			  					</div>
			  					<div class="col-xs-4">			  						
									<p># coauthors: ${numCoauthors}</p>
									<p># communities: ${fn:length(communities)}</p>
									<p><a href="http://en.wikipedia.org/wiki/Modularity_%28networks%29">Modularity</a>: ${modularity}</p>
			  					</div>
			  					<div class="col-xs-4">			  						
									<p># edges: ${totalNumEdges}</p>
									<p># intra-community edges: ${numIntraCommunityEdges}</p>
									<p># inter-community edges: ${numInterCommunityEdges}</p>
									<c:if test="${not empty tooManyRequests}">
										<p>${tooManyRequests}</p>
									</c:if>
			  					</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="panel panel-primary">
		  			<div class="panel-heading" role="tab" id="headingThree">
      					<h4 class="panel-title">
	       					<a data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="true" aria-controls="collapseThree">
	          					Change layer in partition hierarchy
	        				</a>
      					</h4>
    				</div>
    				<div id="collapseThree" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingThree">
			  			<div class="panel-body">			
							<c:url value="/changeLayer" var="targetURL" />
							<form:form modelAttribute="layerInput" action="${targetURL}" method="POST" class="form-horizontal">
								<fieldset>
									<table>
										<tr>
											<td>
												<select name="layerId" class="form-control">
													<c:forEach items="${layerInfos}" var="layer">
														<option value="${layer.layerId}">${layer.numCommunities} communities</option>
													</c:forEach>
												</select>
												<form:hidden path="person" />
											</td>
											<td>
											<td>
												<input type="submit" id="btnDetect" class="btn btn-primary"	value="Change layer" />
											</td>
											<td>
												<button 
													type="button" 
													class="btn btn-info btn-xs" 
													data-toggle="popover" 
													title="Changing layer" 
													data-content="The layers reveal the course of the algorithm. The layer with the smallest number of communities is the first partition found by the algorithm, the layer with the next smallest number of communities is the second parition found by the algorithm, etc.">
													<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
												</button>
											</td>
										</tr>
									</table>
								</fieldset>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	
	<section>
		<div class="container">
			<h2>The communities</h2>
			<div class="row">
				<c:forEach items="${communities}" var="community">
					<c:if test="${fn:length(community.elements) gt 0}">
						<div class="col-sm-6 col-md-3" style="padding-bottom: 15px">
							<div class="thumbnail" style="background-color: #b0c4de">
								<p>
									<b>Community #${community.id}</b><br />
									<b>Size</b>: ${fn:length(community.elements)}
								</p>
								<ul>
									<c:forEach items="${community.elements}" var="elm">
										<li>${elm.name}</li>
									</c:forEach>
								</ul>
								<c:if test="${fn:length(community.elements) lt minNumNodesInCommunity}">
									<p>The size of the community is below the lower bound because it is or belongs to a connected component of size
									smaller than the lower bound.</p>
								</c:if>
								
								<c:if test="${showNumEdges}">
									<p>
										<b># edges to</b><br />
										<ul>
										<c:forEach items="${community.numEdgesTo}" var="neighbor">
											<c:if test="${neighbor.value gt 0}">
												<li>community ${neighbor.key}: ${neighbor.value}</li>
											</c:if>
										</c:forEach>
										</ul>
									</p>
								</c:if>
							</div>
						</div>
					</c:if>
				</c:forEach>
			</div>
  			</div>
		</div>
	</section>
	
	<section>
		<div class="container">
			<h2>The network</h2>
			
			<div id="visualization"></div>
			<script type="text/javascript">
				var network;
	
				var nodes = new vis.DataSet();
				var edges = new vis.DataSet();
				var gephiImported;
				
				loadJSON("<spring:url value="/json/${name}"/>",redrawAll);
				
				var container = document.getElementById('visualization');
				var data = {
				    nodes: nodes,
				    edges: edges
				};
				var options = {
				    nodes: {
				    	shape: 'dot',
			            radiusMin: 10,
			            radiusMax: 30,
			            fontSize: 12,
			            fontFace: "Tahoma",
			            scaleFontWithValue:true,
			            fontSizeMin:8,
			            fontSizeMax:20,
			            fontThreshold:12,
			            fontSizeMaxVisible:20
				    },
				    edges: {
				        width: 0.15,
				        inheritColor: "from"
				    },
				    tooltip: {
				        delay: 200,
				        fontSize: 12,
				        color: {
				            background: "#fff"
				        }
				    },
				    smoothCurves: {dynamic:false, type: "continuous"},
				    stabilize: false,
				    physics: {barnesHut: {gravitationalConstant: -10000, springConstant: 0.002, springLength: 150}},
				    hideEdgesOnDrag: true
				};
				
				network = new vis.Network(container, data, options);
				
				/**
				 * This function fills the DataSets. These DataSets will update the network.
				 */
				function redrawAll(gephiJSON) {
				    if (gephiJSON.nodes === undefined) {
				        gephiJSON = gephiImported;
				    }
				    else {
				        gephiImported = gephiJSON;
				    }
				
				    nodes.clear();
				    edges.clear();
				
				    var allowedToMove = true;
				    var parseColor = true;
				    var parsed = vis.network.gephiParser.parseGephi(gephiJSON, {allowedToMove:true, parseColor:true});
				
				    // add the parsed data to the DataSets.
				    nodes.add(parsed.nodes);
				    edges.add(parsed.edges);
				
				    var data = nodes.get(2); // get the data from node 2
				    nodeContent.innerHTML = syntaxHighlight(data); // show the data in the div
				    network.zoomExtent(); // zoom to fit
				}
				
				// from http://stackoverflow.com/questions/4810841/how-can-i-pretty-print-json-using-javascript
				function syntaxHighlight(json) {
				    if (typeof json != 'string') {
				        json = JSON.stringify(json, undefined, 2);
				    }
				    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
				    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
				        var cls = 'number';
				        if (/^"/.test(match)) {
				            if (/:$/.test(match)) {
				                cls = 'key';
				            } else {
				                cls = 'string';
				            }
				        } else if (/true|false/.test(match)) {
				            cls = 'boolean';
				        } else if (/null/.test(match)) {
				            cls = 'null';
				        }
				        return '<span class="' + cls + '">' + match + '</span>';
				    });
				}
				
				function loadJSON(path, success, error) {
				    var xhr = new XMLHttpRequest();
				    xhr.onreadystatechange = function() {
				        if (xhr.readyState === 4) {
				            if (xhr.status === 200) {
				                success(JSON.parse(xhr.responseText));
				            }
				            else {
				                error(xhr);
				            }
				        }
				    };
				    xhr.open("GET", path, true);
				    xhr.send();
				}
	
			</script>
		</div>
	</section>

</body>

</html>