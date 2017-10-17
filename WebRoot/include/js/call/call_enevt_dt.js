/** *********************此JS用于呼叫中心事件的响应*********************************** */

var caller;// 来电号码
var GHOCX;
var iszContent=0;//转接内线用于保存的录音id
//自动设置坐席状态，用于未接来电后自动示闲，减轻话务员工作量。
//当坐席接通电话后，自动设置坐席状态不工作
var autoState=0;
/**
 * 初始化ocx控件，注册相应事件
 * 
 * @param
 * @author 杨坤 2014-4-23 下午2:46:27
 */
function doInitActiveX(ocx) {
	if (ocx) {
		GHOCX=ocx;
		
		// 坐席振铃
		ocx.attachEvent("OnTelephoneRing", handleTelephoneRing);
		// 添加应答成功弹屏事件
		ocx.attachEvent("OnAnswerConnected", handleAnswerConnected(GHOCX));
		//添加监听坐席状态事件
		ocx.attachEvent("OnAgentStateChanged", handleAgentStateChanged);
		//添加咨询接通事件，用来监控三方通话、转接
		ocx.attachEvent("OnConsultConnected", handleConsultConnected);
		//添加坐席形成三方事件
		ocx.attachEvent("OnAgentThirdParty", handleAgentThirdParty);
		//获取随路数据
		ocx.attachEvent("OnRecvAssociateData", handleOnRecvAssociateData);
		//挂机事件
		ocx.attachEvent("OnTelephoneHangup", handleTelephoneHangup);
		
	
	}
}
/**
 * 坐席振铃
 * 
 * @param caller=呼叫号码
 * @author 杨坤 2014-4-24 上午9:29:27
 */
function handleTelephoneRing() {
	alert("autoState=="+autoState);
	caller = arguments[0];
}

/**
 * 坐席接通后弹屏
 * 
 * @param GHOCX=坐席控件
 * @author 杨坤 2014-4-23 下午2:52:55
 */
function handleAnswerConnected(GHOCX) {
	
	return function() {// js闭包
		var appid = GHOCX.GetRecordAppID();// 获取录音
		var fromnum = caller;
		autoState=1;
		if(iszContent==0){//1.如果是正常来电
			showwindow("../private/slzx/slj_info/toadd?flowid=" + appid
					+ "&fromnum=" + fromnum + "", "受理件登记页面");
		}else{//2.如果是转接
			GHOCX.SetRecordAppID(iszContent);
			showwindow("../private/slzx/slj_info/toadd?flowid=" + iszContent
					+ "&fromnum=" + fromnum + "", "受理件登记页面");
			iszContent=0;
		}
		
	}
}
/**
 * 监听坐席状态
 * 
 * @param GHOCX=坐席控件,iAgentState=坐席状态
 * @author 杨坤 2014-4-23 下午2:52:55
 */
function handleAgentStateChanged(){
	iAgentState = arguments[0];
	switch (iAgentState) {
	case 3:
		call_button_show("sx");
		call_button_hide("sm");
		break;
	case 5:
		call_button_show("sx");
		call_button_hide("sm");
		break;
	case 6:
		call_button_show("sm");
		call_button_hide("sx");
		break;
	case 8:
		call_button_show("sm");
		call_button_hide("sx");
		break;
	default:
		break;
	}
}
/**
 * 咨询接通事件
 * 转接、三方通话,通过获取页面参数判断是转接还是三方通话,
 * 
 * @param GHOCX=坐席控件
 * @param appid=录音ID
 * @param brs=判断转接、三方通话是否成功
 * @author 杨坤 2014-5-8 下午15:43:55
 */
function handleConsultConnected(){
	//alert("咨询接通事件的方法是否成功+="+arguments[0]);
	var brs=arguments[0];
	var tmp_appid=$("#appid").val();

	if(brs==true&&$("#three").val()!=""&&$("#three").val()==0){//1.三方通话功能
		alert(11);
		var t=GHOCX.SetRecordAppID(tmp_appid);//2.设置录音id
		GHOCX.ThirdParty();//3.开始三方通话
	}
	//转接，判断转接方是否已经接听，如果接听自动挂机
	if(brs==true&&$("#shift").val()!=""&&$("#shift").val()==0){
		//设置录音id
		
		alert("随路数据+=="+iszContent);
		GHOCX.SetRecordAppID(iszContent);
		GHOCX.Onhook();//挂机
	}
}

/**
 * 坐席形成三方,设置录音ID
 * @param GHOCX=坐席控件,
 * @param brs坐席三方是否成功的返回值
 * @author 杨坤 
 * 2014-5-9 下午1:57:35
 */
function handleAgentThirdParty(){
	var brs=arguments[0];//1.获取是否成功的参数
	var tmp_appid=$("#appid").val();
	if(brs==true){//2.如果成功设置录音ID
		var t=GHOCX.SetRecordAppID(tmp_appid);//3.设置录音id
	}
}
/**
 * 获取随路数据
 * @param 
 * @author 杨坤 
 * 2014-5-19 下午2:24:22
 */
function handleOnRecvAssociateData(){	
	if(arguments[2]!=null||arguments[2]!=""){
		iszContent=arguments[2];//传入随路数据，然后用于打开页面中
	}
}

/**
 * 挂机事件
 * 挂机后初始化相应参数。
 * @param 
 * @author 杨坤 
 * 2014-5-20 下午1:25:30
 */
function handleTelephoneHangup(){
	iszContent=0;
	$("#three").val("");
	$("#shift").val("");
	if(autoState!=1){//未接来电后自动调用此方法
		 setTimeout("call_setAgentState(2)",5000);
	}
	Dialog.alert("挂机了!!");
}