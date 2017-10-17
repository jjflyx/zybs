/** *********************此JS用于呼叫中心与坐席通讯对接****************************************** */
document
		.write("<script language=javascript src='../include/js/call/call_util.js'></script>");


// 获取ocx控件对象
var GHOCX;
function getOCX(obj){
	GHOCX=obj;
}
/**
 * 坐席签入功能。用于链接呼叫中心服务器。
 * 
 * @param loginname=用户名
 *            用于坐席登录
 * @author 杨坤 2014-4-23 上午11:36:41
 */
function call_connect(loginname) {
	//alert(loginname);
	$.getJSON("../include/js/call/call_config.json",
			function(json) {// 1.通过json配置文件获取服务器相应数据。
				var rsCon = GHOCX.ConnectToACD(json.serverIP, json.port);// serverIP=服务器IP,port=服务器端口号
				switch (rsCon) {// 2.判断连接是否成功
				case 1:
					var rsLogin = GHOCX.Login(loginname, loginname, loginname,
							1, 2, 0);// 3.连接服务器成功，坐席签入
					if (rsLogin == true) {// 4.判断是否签入成功。
						call_button_qr();
					
						Dialog.alert("签入成功！");
					} else {
						Dialog.alert("签入失败！");
					}
					break;
				case 2:
					Dialog.alert("错误代码2：" + "坐席已连接！");
					break;
				case 0:
					Dialog.alert("错误代码0：" + "连接失败！");
					break;
				default:
					Dialog.alert("错误代码-1：" + "未知状态！");
					break;
				}
			});
}

/**
 * 签出签出功能。用于断开呼叫中心服务器。
 * 
 * @param
 * @author 杨坤 2014-4-23 下午1:28:35
 */
function call_DisConnect() {
	var rsLogout = GHOCX.Logout();// 1.坐席登出。
	if (rsLogout == true) {
		var rsDisCon = GHOCX.DisConnectToACD();// 2.登出成功后断开与服务器的连接
		switch (rsDisCon) {
		case 1:
			call_button_qc();
			Dialog.alert("签出成功！");
			break;
		case 0:
			Dialog.alert("错误代码0：" + "断开连接失败！");
			break;
		case -1:
			Dialog.alert("错误代码-1：" + "未连接！");
			break;
		default:
			Dialog.alert("错误代码-2：" + "未知状态！");
			break;
		}
	} else {
		Dialog.alert("坐席签出失败！");
	}
}

/**
 * 坐席示忙、示闲
 * 
 * @param number=4示忙，number=2示闲
 * @author 杨坤 2014-4-23 下午1:45:54
 */
function call_setAgentState(number) {
	var rs;
	switch (number) {
	case 2:// 示闲
		rs=GHOCX.SetAgentState(number);
		if(rs==true){
			autoState=0;
			call_button_show("sm");
			call_button_hide("sx");
		}else{
			Dialog.alert("示闲失败！");
		}
		break;
	case 4:// 示忙
		rs=GHOCX.SetAgentState(number);
		if(rs==true){
			autoState=0;
			call_button_show("sx");
			call_button_hide("sm");
		}else{
			Dialog.alert("示忙失败！");
		}
		break;
	default:
		break;
	}
}

/**
 * 坐席外呼
 * 
 * @param phoneNumber=外呼号码
 * @author 杨坤 2014-4-23 下午2:08:35
 */
function call_Dial(phoneNumber) {
	var state=GHOCX.GetAgentState();
	if(state!==4){
		Dialog.alert("外呼前请先示忙！");
		return;
	}else{

		if (phoneNumber == "" || phoneNumber == null) {
			Dialog.alert("请输入要外呼的号码！");
			return;
		} else {
			var rsDial = GHOCX.Dial(phoneNumber);
			if (rsDial == true) {
				Dialog.alert("外呼成功！正在呼叫......");
			} else {
				Dialog.alert("外呼失败！");
			}
		}
	}
 
}
/**
 * 三方通话
 * 
 * @param phoneNumber=三方通话号码
 * @author 杨坤 2014-4-23 下午2:08:35
 */
function call_Three(phoneNumber){
	
	//1.获取当前通话ID
	var appid=GHOCX.GetRecordAppID();
	//2.调用咨询方法
	var rsCon=GHOCX.Consult(phoneNumber);
	//alert("调用咨询方法==="+rsCon);
	//3.判断咨询方法是否成功
	if(rsCon==true){
		//alert("进入rsCOn==true");
		$("#three").val("0");//设置三方通话参数
		$("#appid").val(appid);//设置录音ID
		$("#shift").val("");
	}else{
		Dialog.alert("三方通话失败！");
	}
}

/**
 * 转接
 * @param 
 * @author 杨坤 
 * 2014-5-19 下午2:13:31
 */
function call_BlindTransfer(phoneNumber){
	//1.获取当前通话ID
	var appid=GHOCX.GetRecordAppID();
	//2.调用咨询方法
	var rsCon=GHOCX.Consult(phoneNumber);
	alert("咨询方法是否成功=="+rsCon);
	if(rsCon==true){
		//3.发送随路数据给坐席
		GHOCX.SendAssociateData(phoneNumber,appid);
		//4.设置转接的参数
		$("#shift").val("0");//设置三方通话参数
		$("#three").val("");
		$("#appid").val(appid);//设置录音ID
	}else{
		Dialog.alert("转接失败！");
	}
}

/**
 * 取消转接、三方通话
 * @param 
 * @author 杨坤 
 * 2014-5-9 下午4:04:51
 */
function call_CancelConsult(){
	var rs=GHOCX.CancelConsult();//取回通话
	if(rs==true){
		Dialog.alert("取回通话成功！");
	}else{
		Dialog.alert("取回通话失败！请等待......");
	}
}


/**
 * 静音
 * @param 
 * @author 杨坤 
 * 2014-5-9 下午4:10:15
 */
function call_Hold(){
	var rs=GHOCX.Hold();//静音
	if(rs==true){
		call_button_show("th");
		call_button_hide("jy");
	}
}

/**
 * 取消静音，恢复通话
 * @param 
 * @author 杨坤 
 * 2014-5-9 下午4:11:16
 */
function call_UnHold(){
	var rs=GHOCX.UnHold();//恢复通话
	if(rs==true){
		call_button_show("jy");
		call_button_hide("th");
	}
}


