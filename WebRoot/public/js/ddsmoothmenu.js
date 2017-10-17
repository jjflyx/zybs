//ÏÂÀ­²Ëµ¥
$(function(){
	$("#smoothmenu1").hover(function(){
	},function(){
		$("#smoothmenu1 li").removeClass("hover");
	})
	$("#smoothmenu1 li").mouseenter(function(){
        //index = $(".name_menu li").index(this);
        curNav=$(this).addClass("hover").siblings().removeClass("hover");
        setTimeout("curNav",2000)
    })
  $("#smoothmenu1 li:last-child").find("ul").css({
  	"right":"-17px",
  	"margin-right":"0"
  })
})