// JavaScript Document
function tabitem(btn, n){
	var idname = new String(btn.id);
	var s = idname.indexOf("_");
	var e = idname.lastIndexOf("_") + 1;
	var tabName = idname.substr(0, s);
	var id = parseInt(idname.substr(e));
	for (i=1; i<n+1; i++){
		document.getElementById(tabName + "_content_" + i).style.display = "none";
		document.getElementById(tabName + "_title_" + i).className = '';
	};
	document.getElementById(tabName+"_content_"+id).style.display = "block";
	btn.className = "on";
};

function showMenu(id)
{
document.getElementById('menubox'+id).style.display='block';
document.getElementById('a'+id).className="a_hover";
}
function hidenMenu(id)
{
document.getElementById('menubox'+id).style.display='none';
document.getElementById('a'+id).className="normal";
}

// 字号缩放
function fontZoomA(){
 document.getElementById('fontzoom').style.fontSize='14px';
 document.getElementById('fontzoom').style.lineHeight='25px';
 allChanges("13px","23px");
}
function fontZoomB(){
 document.getElementById('fontzoom').style.fontSize='16px';
 document.getElementById('fontzoom').style.lineHeight='28px';
 allChanges("16px","28px");
}
function fontZoomC(){
 document.getElementById('fontzoom').style.fontSize='20px';
 document.getElementById('fontzoom').style.lineHeight='30px';
 allChanges("20px","30px");
}

function allChanges(_fontSize,_lineHeight){
	 var allSpan=document.getElementById('fontzoom').getElementsByTagName('span');
	 var allDiv=document.getElementById('fontzoom').getElementsByTagName('div');
	 var allFont=document.getElementById('fontzoom').getElementsByTagName('font');
	 for(var i=0;i<allSpan.length;i++){
		 allSpan.item(i).style.fontSize=_fontSize;
		 allSpan.item(i).style.lineHeight=_lineHeight;
	 }
	 for(var i=0;i<allDiv.length;i++){
		 allDiv.item(i).style.fontSize=_fontSize;
		 allDiv.item(i).style.lineHeight=_lineHeight;
	 }
	 for(var i=0;i<allFont.length;i++){
		 allFont.item(i).style.fontSize=_fontSize;
		 allFont.item(i).style.lineHeight=_lineHeight;
	 }
 }

//页面内容打印
function printPage(){
Item=document.getElementById('fontzoom').innerHTML;
Title=document.getElementById('pagetitle').innerHTML;
Pother=document.getElementById('pageother').innerHTML;
status='fullscreen';
//status="width=0,height=0";
MsgBox=window.open("","print",status);
MsgBox.document.write   ("<html><head><title></title></head><body><div width=100%>");
MsgBox.document.write   ("<center>"+Title+"</center>");
MsgBox.document.write   ("<center>"+Pother+"</center>");
MsgBox.document.write   ("   "+Item+"");
MsgBox.document.write   ("</div></body></html>");
MsgBox.document.close();
MsgBox.print();
MsgBox.close();
}
function closeWindow(){
	var browserName=navigator.appName;
	if (browserName=="Netscape") {
		window.open('','_self',''); window.close();
	} else if (browserName=="Microsoft Internet Explorer") {
		window.opener = "whocares"; window.close(); 
	}
}

//导航切换下拉
/*鼠标点击时显示下拉*/
$(function(){
	$(".menuul li:first").css("margin-left","0px")
	$(".zzjg li:first").css("border-top","0px")
	//$(".zzjg li:last").css("border-bottom","0px")
	$(".zzjg li").each(function() {
		$(this).css("z-index",100-$(this).index())
    });
	$(".listul li:nth-child(5n)").after("<hr class='listline'>")
})


function closeAD(ID){
	$("#"+ID).hide();
}
function a(x,y){
	l = $('.top_linktext').offset().left;
	w = $('.top_linktext').width();
	$('.sitekf').css('left',(l + w + x) + 'px');
	$('.sitekf').css('top',y + 'px');
	z = $('.ad_1').width();
	$('.ad_1').css('left',(l - x - z) + 'px');
	$('.ad_1').css('top',y + 'px');
}
function b(){
	h = $(window).height();
	t = $(document).scrollTop();
	if(t > h){
		$('#gotopli').fadeIn('slow');
	}else{
		$('#gotopli').fadeOut('slow');
	}
}
$(document).ready(function(e) {		
	var clientWidth=window.document.documentElement.clientWidth;
	if(clientWidth>1260){
		try{
		a(10,200);//kf的div距浏览器顶部和页面内容区域右侧的距离
		}catch (e) {}
	}
	b();
	$('#gotopli').click(function(){
		$(document).scrollTop(0);	
	})
});
$(window).scroll(function(e){
	b();
})
$(window).resize(function(){
	var clientWidth=window.document.documentElement.clientWidth;
	if(clientWidth>1260){
		try{
		a(10,200);//kf的div距浏览器顶部和页面内容区域右侧的距离
		}catch (e) {}
	}
});