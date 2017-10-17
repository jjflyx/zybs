/**
 * 同步ajax请求
 */
MyJqueryAjax = function (v_url, data, func, dataType) {
	this.url = v_url; 
	this.data = data;
	this.func = func;//ajax请求回调方法
	this.request = function () {
		var v_response;
		$.ajax({
			async:false, //ajax同步请求,async=true异步请求
			url:v_url, //请求地址
			//contentType:"application/x-www-form-urlencoded;charset=GBK",
			data: data, //请求参数
			cache:false, //设置为 false 将不会从浏览器缓存中加载请求信息
			type:"POST", 
			dataType:dataType == null ? "text" : dataType, 
			error:function (XMLHttpRequest, textStatus, errorThrown) {
				alert("MyJqueryAjax Request Error!");
			}, 
			success:(func !== null && func != undefined) ? func : function (req) {
				v_response = req;
			}
		});
		return v_response;
	};
};
/**
 * 异步ajax请求
 */
MyJqueryAjax2 = function (v_url, data, func, dataType,errorFunc) {
	this.url = v_url;
	this.data = data;
	this.func = func; 
	this.request = function () {
		var v_response;
		$.ajax({
			async:true, //ajax异步请求,async=false同步请求
			url:v_url,
			data: data,
			cache:false,
			type:"POST",
			dataType:dataType == null ? "text" : dataType, 
			error: (errorFunc !== null && errorFunc != undefined) ? errorFunc: function (req) {
				alert("MyJqueryAjax2 Request Error!");
			}, 
			success:(func !== null && func != undefined) ? func: function (req) {
				v_response = req;
			}
			});
		return v_response;
	};
};

//调用方法
//var myAjax=new MyJqueryAjax2(url,null,function_name,"json").request();
