/** *********************此js为工具类，实现点击呼叫按钮时的一些显示操作******************************** */


//实现弹屏功能
function showwindow(endtarget, WINname)
{
	
    var showw = window.open(endtarget, WINname, 'status=yes,toolbar=no,menubar=no,resizable=yes,scrollbars=yes,location=no,fullscreen=yes');
}

/**
 * 初始化时坐席控件的状态
 * @param 
 * @author 杨坤 
 * 2014-5-9 下午4:16:17
 */
function call_button_init(){
	$("a[name='init_button']").attr("disabled","disabled"); //使用attr设置属性    
	$("a[name='init_button']").css({'opacity':'0.4'});
}
/**
 * 点击签入按钮，释放其他按钮，禁用签入按钮
 * @param 
 * @author 杨坤 
 * 2014-5-9 下午5:03:37
 */
function call_button_qr(){
	$("a[name='init_button']").removeAttr("disabled"); //使用attr设置属性    
	$("a[name='init_button']").css({'opacity':'1'});
	
	$("#qr").attr("disabled","disabled"); //使用attr设置属性    
	$("#qr").css({'opacity':'0.4'});
}

/**
 * 
 * @param 点击签出按钮，释放签入按钮，禁用其他按钮
 * @author 杨坤 
 * 2014-5-9 下午5:07:44
 */
function call_button_qc(){
	$("#qr").removeAttr("disabled"); //使用attr设置属性    
	$("#qr").css({'opacity':'1'});
	
	$("a[name='init_button']").attr("disabled","disabled"); //使用attr设置属性    
	$("a[name='init_button']").css({'opacity':'0.4'});
}
/**
 * 隐藏控件
 * @param obj=对象名称
 * @author 杨坤 
 * 2014-5-9 下午5:23:12
 */
function call_button_hide(obj){
	$("#"+obj+"").hide();
}
/**
 * 显示控件
 * @param obj=对象名称
 * @author 杨坤 
 * 2014-5-9 下午5:24:20
 */
function call_button_show(obj){
	$("#"+obj+"").show();
}
