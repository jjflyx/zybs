$(function(){
	tabClose();
	tabCloseEven();
});
function addTab2(subtitle,iframeurl){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(iframeurl),
			closable:false,
			//width:$('#mainPanle',parent).width(),
			//height:$('#mainPanle',parent).height(),
			icon:'icon-page'
		}); 
	}else{
		$('#tabs').tabs('select',subtitle);
		//var i=$(".tabs-selected").index();
		//alert(i)
		/*$("iframe[name='mainFrame']").each(function (j){
			if(i==j){
				var url=$(this).attr("src");
				$(this).attr("src",iframeurl);
			}
		});*/
//		refreshTab({tabTitle:subtitle,url:iframeurl});
	}
	//tabClose();
}
function addTab(subtitle,iframeurl){
	if(!$('#tabs').tabs('exists',subtitle)){
		 
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(iframeurl),
			closable:true,
			//width:$('#mainPanle',parent).width(),
			//height:$('#mainPanle',parent).height(),
			icon:'icon-page'
		}); 
	}else{
		$('#tabs').tabs('select',subtitle);
		var i=$(".tabs-selected").index();
		//alert(i)
		$("iframe[name='mainFrame']").each(function (j){
			if(i==j){
				var url=$(this).attr("src");
				$(this).attr("src",iframeurl);
			}
		});
//		refreshTab({tabTitle:subtitle,url:iframeurl});
	}
	tabClose();
}
function addWelcomeTab(subtitle,iframeurl){
	if(!$('#tabs').tabs('exists',subtitle)){
		 
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(iframeurl),
			closable:false
			//width:$('#mainPanle',parent).width(),
			//height:$('#mainPanle',parent).height(),
			//icon:'icon-page'
		}); 
	}else{
		$('#tabs').tabs('select',subtitle);
		var i=$(".tabs-selected").index();
		//alert(i)
		$("iframe[name='mainFrame']").each(function (j){
			if(i==j){
				var url=$(this).attr("src");
				$(this).attr("src",iframeurl);
			}
		});
//		refreshTab({tabTitle:subtitle,url:iframeurl});
	}
	tabClose();
}
function refreshTab(cfg){  
	
    var refresh_tab = cfg.tabTitle?$('#tabs').tabs('getTab',cfg.tabTitle):$('#tabs').tabs('getSelected');  
    if(refresh_tab && refresh_tab.find('iframe').length > 0){ 
    var refresh_url = cfg.url?cfg.url:_refresh_ifram.src;  	
    var _refresh_ifram = refresh_tab.find('iframe');  
    var obj;
    for(var i=0;i<_refresh_ifram.length;i++){
    	obj=_refresh_ifram[i];
    	if(checkref(obj.src,refresh_url)){
    		obj.contentWindow.location.href=refresh_url;  
    	}
    }
    }  
}  
function checkref(oldurl,newurl){
	var tempurl=newurl.substring(newurl.indexOf("/"),newurl.indexOf("?"));
	if(oldurl.indexOf(tempurl)>0){
		return true;
	}
		return false;	
}
function createFrame(url)
{
	var s = '<iframe name="mainFrame" scrolling="auto" allowtransparency="true" height="100%" width="100%" style="width:100%;height:100%;" frameborder="0"  src="'+url+'" ></iframe>';
	return s;
}

function tabClose()
{
	//alert(1);
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		//欢迎页标题
		var welcome=$(".tabs li:eq(0) a span").text();
		
		var subtitle = $(this).children("span").text();
		if(welcome!=subtitle){
			$('#tabs').tabs('close',subtitle);
		}
		
	});

	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
		
		var subtitle =$(this).children("span").text();
		$('#mm').data("currtab",subtitle);
		
		return false;
	});
}
//刷新某个指定的tap,并显示该页面
function resesTap(selectname,url){
	var refresh_tab = $('#tabs').tabs('getTab',selectname);
	if(refresh_tab){
		var refresh_url = url;  	
    	var _refresh_ifram = refresh_tab.find('iframe');  
    	var obj;
    	//alert("refresh_url="+refresh_url);
   	 	for(var i=0;i<_refresh_ifram.length;i++){
    		obj=_refresh_ifram[i];
    		//alert('刷新='+obj.src);
    		if(obj.src.indexOf(refresh_url)>0){
    			//alert('刷新');
    			obj.contentWindow.location.reload();
    		}
   	 	}
	}
	$('#tabs').tabs('resize',selectname);
}


/**2014-03-05 wanfly add，selectname,关闭当前显示的tab,并显示指定title为selectname的Tab
 * closename，需要关闭的tab的title值,
 * 当url和selectname都有值时，则关闭当前页面tab刷新指定页面tab
 * @param {Object} selectname
 * @param {Object} closename
 */
function closeNowTab(selectname,url,closename){
	//alert('关闭1');
	if(url && selectname && closename){//关闭指定页面tab，刷新指定页面tab
		$('#tabs').tabs('close',closename);
		selectTap(selectname);
		resesTap(selectname,url);
	}else if(url && selectname){//关闭当前页面tab,刷新指定页面tab
		var tab = $('#tabs').tabs('select');
		var nowseledtext=$(".tabs-selected").children("a").children("span")[0].lastChild.nodeValue;
		if (tab){
			$('#tabs').tabs('close',nowseledtext);
			selectTap(selectname);
			resesTap(selectname,url);
		}
	}else if(selectname && closename){//关掉指定页面tab，打开其指定页面tab
		//alert('关闭');
		$('#tabs').tabs('close',closename);
		selectTap(selectname);
	}else if(closename){
		$('#tabs').tabs('close',closename);
	}else if(selectname){//关闭当前页面tab,打开指定页面tab
		var tab = $('#tabs').tabs('select');
		var nowseledtext=$(".tabs-selected").children("a").children("span")[0].lastChild.nodeValue;
		if (tab){
			$('#tabs').tabs('close',nowseledtext);
			selectTap(selectname);
		}
	}else{//关闭当前页面tab
		var tab = $('#tabs').tabs('select');
		var nowseledtext=$(".tabs-selected").children("a").children("span")[0].lastChild.nodeValue;
		if (tab){
			$('#tabs').tabs('close',nowseledtext);
		}
	}
}
function closeTab(closename){
	$('#tabs').tabs('close',closename);
}
//显示指定title的tab
function selectTap(name){
	$("#tabs").tabs('select',name);
}

/**
 * tab 标签点击刷新
 */
$(function (){
	//alert($('#tabs').tabs('select'));
	$("ul.tabs").on('click','li',function (){
		var welcome=$(".tabs li:eq(0) a span").text();
		var thhisname=$(this).find('span').html();
		if(welcome!='受理件登记'){
			if(welcome===thhisname){
				var i=$(this).index();
				//alert("i="+ i);
				$("iframe[name='mainFrame']").each(function (j){
					if(i==j){
						var url=$(this).attr("src");
						this.contentWindow.location.href=url;
						//$(this).attr("src",url);
					}
				});
			}
		}
		
	});
	
});

function getSelectedTab(){
	  var selectTabIndex=$("li.tabs-selected").index();
	  var tabName= $(".tabs li:eq("+selectTabIndex+") a span").text();
	  return tabName;
}

//绑定右键菜单事件
function tabCloseEven()
{
    //刷新页面
    $("#mm-tabrefresh").click(function(){
            var currtab_title = $('#mm').data("currtab");
            var currtab = $('#tabs').tabs('getTab',currtab_title);
            var oCurrtab = $(currtab);
            var oLis = oCurrtab.find(".tabs").find("li");
            var tabIndex = -1;
            for(var i=0;i<oLis.length;i++){
                var li = oLis[i];
                if(li.innerText == currtab_title){
                    tabIndex = i;
                    break;
                }
            }
            var oIframes = oCurrtab.find("iframe");
            if(tabIndex != 0){
                
                oIframes[tabIndex-1].src = oIframes[tabIndex-1].src;
            }else{
            	oIframes[tabIndex].src = oIframes[tabIndex].src;
//                window.location.reload();
            }
    });	
  //关闭当前
	$('#mm-tabclose').click(function(){
		var currtab_title = $('#mm').data("currtab");
		//欢迎页标题
		var welcome=$(".tabs li:eq(0) a span").text();
		if (currtab_title != welcome) {
			$('#tabs').tabs('close',currtab_title);
		}
		
	});
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		//欢迎页标题
		var welcome=$(".tabs li:eq(0) a span").text();
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			if (t != welcome && t!='') {
				$('#tabs').tabs('close',t);
			}
		});	
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
		//欢迎页标题
		var welcome=$(".tabs li:eq(0) a span").text();
		var currtab_title = $('#mm').data("currtab");
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			if(t!=currtab_title && t!=welcome)
				$('#tabs').tabs('close',t);
		});	
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		//欢迎页标题
		var welcome=$(".tabs li:eq(0) a span").text();
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length==0){
			//msgShow('系统提示','后边没有啦~~','error');
			msgShow('信息提示','右边没有啦','warning');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			if (t !=welcome) {
				$('#tabs').tabs('close',t);
			}
			
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		//欢迎页标题
		var welcome=$(".tabs li:eq(0) a span").text();
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length<=1){
			msgShow('信息提示','左边没有啦','warning');
			return false;
		}
		prevall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			if (t!=welcome) {
				$('#tabs').tabs('close',t);
			}
			
		});
		return false;
	});

	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	});
}

//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}

function clockon() {
    var now = new Date();
    var year = now.getFullYear(); //getFullYear getYear
    var month = now.getMonth();
    var date = now.getDate();
    var day = now.getDay();
    var hour = now.getHours();
    var minu = now.getMinutes();
    var sec = now.getSeconds();
    var week;
    month = month + 1;
    if (month < 10) month = "0" + month;
    if (date < 10) date = "0" + date;
    if (hour < 10) hour = "0" + hour;
    if (minu < 10) minu = "0" + minu;
    if (sec < 10) sec = "0" + sec;
    var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    week = arr_week[day];
    var time = "";
    time = year + "年" + month + "月" + date + "日" + " " + hour + ":" + minu + ":" + sec + " " + week;

    $("#bgclock").html(time);

    var timer = setTimeout("clockon()", 200);
}
