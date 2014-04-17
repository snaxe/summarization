
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="style.css" rel="stylesheet">
</head>
<body>
<div class="maincontainer">
INPUT FILE PATH:<input type="text" id="ipfile" style="width: 35%;height: 30px;margin-top: 3%;"><br/>
<div  onclick="submit()" class="submit">SUMMARIZE</div>
<div id="summary">
</div>
<div onclick="pre()" id="pre" style="display:none;">Prev</div>
<div onclick="next()" id="next" style="display:none;">Next</div>
</div>
<script>
page=0;
function submit(){
	x=document.getElementById("ipfile");
	var xmlhttp;
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.onreadystatechange=function()
	{
	if (xmlhttp.readyState==4 && xmlhttp.status==200)
	  {
	  document.getElementById("summary").innerHTML=xmlhttp.responseText;
	  document.getElementById("next").removeAttribute("style");
	  document.getElementById("pre").removeAttribute("style");
	  document.getElementById("next").setAttribute("class", "next");
	  document.getElementById("pre").setAttribute("class", "pre");
	  
	  }
	}
	xmlhttp.open("GET","summarize?filename="+encodeURIComponent(x.value)+"&page="+page,true);
	xmlhttp.send();
}
prepage=page;
function next(){
	page++;
	var xmlhttp;
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.onreadystatechange=function()
	{
	if (xmlhttp.readyState==4 && xmlhttp.status==200)
	  {
	  document.getElementById("summary").innerHTML=xmlhttp.responseText;
	  if(xmlhttp.responseText.search("No more summaries left")!=-1){
		  page=prepage+1;
	  }
	  else
		  prepage=page;
	  }
	}
	xmlhttp.open("GET","summarize?filename="+encodeURIComponent(x.value)+"&page="+page,true);
	xmlhttp.send();
}
function pre(){
	page--;
	var xmlhttp;
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.onreadystatechange=function()
	{
	if (xmlhttp.readyState==4 && xmlhttp.status==200)
	  {
	  document.getElementById("summary").innerHTML=xmlhttp.responseText;
	  if(xmlhttp.responseText.search("No more summaries left")!=-1){
		  page=-1;
	  }
	  		
	  }
	}
	xmlhttp.open("GET","summarize?filename="+encodeURIComponent(x.value)+"&page="+page,true);
	xmlhttp.send();
}
</script>
</body>
</html>