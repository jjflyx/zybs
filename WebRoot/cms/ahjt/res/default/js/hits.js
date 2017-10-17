// JavaScript Document
function tabitem(btn, n){
	var idname = new String(btn.id);
	var s = idname.indexOf("_");
	var e = idname.lastIndexOf("_") + 1;
	var tabName = idname.substr(0, s);
	var id = parseInt(idname.substr(e));
	for (i=0; i<n; i++){
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
status='fullscreen';
//status="width=0,height=0";
MsgBox=window.open("","print",status);
MsgBox.document.write   ("<html><head><title></title></head><body><div width=100%>");
MsgBox.document.write   ("<center>"+Title+"</center>");
MsgBox.document.write   ("   "+Item+"");
MsgBox.document.write   ("</div></body></html>");
MsgBox.document.close();
MsgBox.print();
MsgBox.close();
}

//导航切换下拉
/*鼠标点击时显示下拉
$(function(){
	$(".Nmenu_XMC").hide();
	$(".site_Nmenu").hover(function(){
	},function(){
		$(".site_Nmenu li").removeClass("hover");
		$(".Nmenu_XMC").hide();
	})
	$(".site_Nmenu li").hover(function(){
    //$(".site_Nmenu li").removeClass("hover")
		//$(this).addClass("hover");
		//$(this).css("cursor","pointer");
		$(this).find(".Nmlink").attr("href","#");
		$(this).find(".Nmlink").attr("target","");
		//$(".site_Nmenu li").find(".Nmenu_XMC").hide();
    //curXMC = $(this).find(".Nmenu_XMC");
		//curXMC.show()
  })
	$(".site_Nmenu li").click(function(){
  	$(".site_Nmenu li").find(".Nmenu_XMC").hide();
  	$(".site_Nmenu li").removeClass("hover")
		curNav=$(this).addClass("hover");
    curXMC = $(this).find(".Nmenu_XMC");
		curXMC.show()
  })
})*/
/* 鼠标指向时显示下拉*/
$(function(){
	$(".Nmenu_XMC").hide();
	$(".site_Nmenu").hover(function(){
	},function(){
		$(".site_Nmenu li").removeClass("hover");
		$(".Nmenu_XMC").hide();
	})
	$(".site_Nmenu li").hover(function(){
    $(".site_Nmenu li").removeClass("hover")
		curNav=$(this).addClass("hover");
    curXMC = $(this).find(".Nmenu_XMC");
		curXMC.show()
		//curXMC.animate({ opacity:'show', height:'show' },400)
    //setTimeout("curNav",2000)
  })
})

