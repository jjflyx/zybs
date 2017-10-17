var leftOverHeight=0;
var rightOverHeight=0;
var oldFootHeight=0;
$(function(){
	oldFootHeight=$("#footbox").outerHeight();
	leftOverHeight=$("#headbox").outerHeight()+$("#footbox").outerHeight()+$("#leftTop").outerHeight()+$("#leftBottom").outerHeight();
	rightOverHeight=$("#headbox").outerHeight()+$("#footbox").outerHeight()+$("#rightTop").outerHeight()+$("#rightBottom").outerHeight();
	var pResizeTimer = null;
	autoReset();
	window.onresize = function(){
		autoReset();
//		if ( pResizeTimer ) clearTimeout (pResizeTimer);
//   		pResizeTimer = setTimeout ("autoReset()", 100);
	}
	window.onload = function(){
		autoReset()
//		if ( pResizeTimer ) clearTimeout (pResizeTimer);
//   		pResizeTimer = setTimeout ("autoReset()", 100);
	}
    /*$("#leftCTR span").click(function(){//点击主界面小箭头弹出或收回
		$("#leftArea").hide();
        //$(this).addClass("gnmenu_open");
        //$(this).removeClass("gnmenu_close");
		$(this).attr("title","展开面板");
        return false;
    }, function(){
		$("#leftArea").show();
        //$(this).addClass("gnmenu_close");
        //$(this).removeClass("gnmenu_open");
		$(this).attr("title","收缩面板");
        return false;
    })*/
});


function autoReset(){
	var ht = document.getElementsByTagName('html')[0];
		if (ht.style.overflow != "auto"){
    		ht.style.overflow = 'hidden';
			}
	if (document.documentElement.clientWidth < 1004){
		ht.style.overflowX = 'auto';
		$("#mainFrame").width(1004)
		}else{
			$("#mainFrame").width(document.documentElement.clientWidth)
		}
	leftOverHeight=$("#headbox").outerHeight()+$("#footbox").outerHeight()+$("#leftTop").outerHeight()+$("#leftBottom").outerHeight();
	rightOverHeight=$("#headbox").outerHeight()+$("#footbox").outerHeight()+$("#rightTop").outerHeight()+$("#rightBottom").outerHeight();
	 try {
			document.getElementById("leftFrame").contentWindow.scrollContent();
        } 
     catch (e) {}
	 try {
			document.getElementById("mainIframe").contentWindow.scrollContent();
        } 
     catch (e) {}
	 var currentheight =document.documentElement.clientHeight;
	  try {
			var leftHeight= currentheight-leftOverHeight-parseInt($("#leftdiv").css("paddingTop"))-parseInt($("#leftdiv").css("paddingBottom"));
			$("#leftdiv").height(leftHeight);
			$("#leftFrame").height(leftHeight);
        } 
     catch (e) {}
	 try {
			 var rightHeight= currentheight-rightOverHeight-parseInt($("#mainPanle").css("paddingTop"))-parseInt($("#mainPanle").css("paddingBottom"));
			 $("#mainPanle").height(rightHeight);
        } 
     catch (e) {}
}