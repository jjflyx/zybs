function picScroll(){
		var aimgzoom = $("#imgzoom");
		var smallImgListWidth;
		var imgNum=$(".smallImgList li").length;
		var imgWidth=$(".smallImgList li").outerWidth();
		smallImgListWidth=imgNum*imgWidth;
		var sun=(smallImgListWidth-imgWidth*5)/376;
		var i=0;
		var timer;
		var popupLayer={
			showLayer:function(){
				var offset=$("#bigImgContent").offset();
				var offsetTop=offset.top;
				var popupHeight=$(".popup-layer").height();
				var bigHeight=$("#bigImgContent").height();
				$(".popup-layer").css("display","block").animate({
					  top:offsetTop+(bigHeight/2-popupHeight/2)+"px"
				},500);
			},
			hideLayer:function(){
				$(".popup-layer").css("display","none").animate({
					  top:"0"
				},500);
			}
		};
		$(".layer-close").click(function(){
			popupLayer.hideLayer();
		});
		
		var f_picSrc=$(".smallImgList li").eq(0).find("a").attr("rel");
		var f_img="<img src="+f_picSrc+"  alt=\"\" />";
		$("#aimgcon").html(f_img);
		aimgzoom.attr("href",f_picSrc);
		$(".smallImgList").css("width",smallImgListWidth+"px");		
		
		$(".smallImgList li").each(function(index){
			$(this).find("a").click(function(){
			   clearInterval(timer);
			   popupLayer.hideLayer();
			   $("#slideVideo").text("幻灯播放");
			   $(this).parent("div").parent("li").siblings('li').removeClass("thisimg").end().addClass("thisimg");
			   i=index;
			   loadPic();
			   smallPicBgRun();
				return false;
			});
			
		});
		
		$(".nextPic,.img-next").click(function(){
			clearInterval(timer);
			$("#slideVideo").text("幻灯播放");
			if (!$(".smallPicBg,.smallImgList").is(":animated")) {
			  if(i>=0&&i<(imgNum-1)){
				  i++;
				  loadPic();
				  smallPicBgRun();
			  }else if(i==(imgNum-1)){
				  popupLayer.showLayer();
				  return false;
			  }
			}
			return false;
		});
		
		$(".prevPic,.img-prev").click(function(){
			popupLayer.hideLayer();
			clearInterval(timer);
			$("#slideVideo").text("幻灯播放");
			if (!$(".smallPicBg,.smallImgList").is(":animated")) {
			  if(i>0&&i<=(imgNum-1)){
				  i--;
				  loadPic();
				  smallPicBgRun();
			  }else if(i==0){
				  return false;
			  }
		   }
		   return false;
		});
		
		$("#slideVideo").click(function(){
			if($(this).text()=="幻灯播放"){
				if(i==(imgNum-1)){			
					popupLayer.showLayer();
					return false;
				}
				else{
					$(this).text("停止播放");
					timer = setInterval(function(){
					   i++;
					   loadPic();
					   smallPicBgRun();
					   if(i>=(imgNum-1)){
						   clearInterval(timer);
						   setTimeout(function(){
							   popupLayer.showLayer();
						   },4000)			   
						   return false;
					   }
					} , 3000)
			   }
			}else{
				clearInterval(timer);
				popupLayer.hideLayer();
				$(this).text("幻灯播放");
			}			
		});
		scrollButton();
		
		function loadPic(){
			var picSrc=$(".smallImgList li").eq(i).find("a").attr("rel");
			var info=$(".smallImgList li").eq(i).find("a").attr("info");
			var pic="<img src="+picSrc+"  alt=\"\" />";
			var content=$(".imageDescription");		
			var loading="<span id=\"imgLoading\"><img src=\"../img/atlas/loading.gif\" alt=\"图片加载中\" title=\"图片加载中\" /></span>";
			$("#aimgcon").html(pic);
			aimgzoom.attr("href",picSrc);
			var ld_pic=$("#aimgcon>img");
			ld_pic.hide();
			content.html(info);
			$("#imgLoading").remove();
			$("#imgContent").append(loading);
		   
			ld_pic.load(function() {
			   $("#imgLoading").remove();
			   $(this).fadeIn("slow");
			});
		}
		
		function smallPicBgRun(){
			var bg=$(".smallPicBg");
			var ul=$(".smallImgList");
			bg.show();
			if(i<=2){
				bg.animate({ left: (imgWidth*i+4)+ "px" }, 500);
				ul.animate({ left: 0}, 500);

				$("#scrollButton").animate({ left: 0 }, 500);
			}else if(i>=3&&i<=(imgNum-3)){
				bg.css("left","228px");
				ul.animate({ left: -(imgWidth*(i-2))+ "px" }, 500);
				$("#scrollButton").animate({ left: (imgWidth*(i-2))/sun+ "px" }, 500);
			}else if(i>(imgNum-3)&&i<imgNum){
				bg.animate({ left: (imgWidth*(i-(imgNum-5))+4)+ "px" }, 500);
				ul.css({ left: -(smallImgListWidth-5*imgWidth)+ "px" });
			}
		}
		
		function scrollButton(){
		   var button=$("#scrollButton");
		   var top,left,move=false;	
		   button.click(function(){
		   }).mousedown(function(e){
			  clearInterval(timer);
			  $("#slideVideo").text("幻灯播放");
			  left=e.pageX-parseInt(button.css("left"));
			  button.fadeTo(50, 0.5);
			  move=true;
		   }).mouseup(function(){
			  button.fadeTo(50, 1.0);
		   });
		   $(document).mousemove(function(e){
			  if(move){
			  var x= e.pageX-left;
			  var scrollLength=-x*sun;
			  if(x>=0&&x<=376&&imgNum>5){
				 button.css({"left":x+"px"});	
				 $(".smallPicBg").hide();
				 $(".smallImgList").animate({ left: scrollLength + "px" },0);		 
			  }
			  else{
				  return false;
			  }
			}
		   }).mouseup(function(){
			  move=false;
		   }).click(function(){
			  button.fadeTo(50, 1.0);
		   });
	   }
	   
	   $(document).keydown(function(event){
		  switch(event.keyCode) {
			  case 39:
				 clearInterval(timer);
				  $("#slideVideo").text("幻灯播放");
				  if (!$(".smallPicBg,.smallImgList").is(":animated")) {
					if(i>=0&&i<(imgNum-1)){
						i++;
						loadPic();
						smallPicBgRun();
					}else if(i==(imgNum-1)){
						popupLayer.showLayer();
						return false;
					}
				  }
				 break;
			  case 37:
				  clearInterval(timer);
				  popupLayer.hideLayer();
				  $("#slideVideo").text("幻灯播放");
				  if (!$(".smallPicBg,.smallImgList").is(":animated")) {
					if(i>0&&i<=(imgNum-1)){
						i--;
						loadPic();
						smallPicBgRun();
					}else if(i==0){
						return false;
					}
				 }
				 break;
			  default:
		  }
	   }); 
	}