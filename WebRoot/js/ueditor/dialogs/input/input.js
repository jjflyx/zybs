//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
var curWwwPath=window.document.location.href;
//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
var pathName=window.document.location.pathname;
var pos=curWwwPath.indexOf(pathName);
//获取主机地址，如： http://localhost:8083
var localhostPaht=curWwwPath.substring(0,pos);
//获取带"/"的项目名，如：/uimcardprj
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);

var oldElement, valid, cmdName = editor.curInputType, language = UE.I18N[editor.options.lang]["inputDialog"];

//手动增加 公用的zh-cn变量
$.extend(lang._static,UE.I18N[editor.options.lang]["ruleInfo"]);

var isEmpty = function(b, a) {
	return b === null || b === undefined || (!a ? b === "" : false);
};
function initComplete() {
	
	$(".button-td").bind("mouseenter mouseleave", function() {
		$(this).toggleClass("button-td-hover");
	});
	if (cmdName == "subtable") {
		editInitData();
	} else {
		$("[eid='type']").bind("change", typeChange);
		$("[eid='value']").bind("change", valueChange);
		$("#isQuery").bind("click", conditionChange);
		$("[eid='searchFrom']").bind("change", searchFromChange);
		typeChange.call($("[eid='type']")[0]);
		
		if(cmdName == "selectinput"){
			$("[eid='loadselect']").live("change",loadselectChange);
		}
		
		if (checkName(cmdName)) {
			editInitData();
		} else {
			editInitData();
		}
	}
}
function checkName(a) {
	if (!isEmpty(a)) {
		if (a == "textfield" || a == "textarea" || a == "checkbox"
				|| a == "radioinput" || a == "selectinput") {
			return true;
		}
	}
	return false;
}
function conditionChange() {
	$(".condition_tr").toggleClass("hidden");
}
function searchFromChange() {
	var a = $(this).val();
	if (a == "fromForm") {
		$(".searchValue-td").addClass("hidden");
	} else {
		$(".searchValue-td").removeClass("hidden");
	}
}
function editInitData() {
	oldElement = null;
	if (!editor.curInputElement) {
		return;
	}
	var externalStr = $(editor.curInputElement).attr("external");
	externalStr = utils.htmlDecode(externalStr);
	var external = eval("(" + externalStr + ")");
	oldElement = editor.curInputElement;
	editor.curInputElement = null;
	bind(external);
}
function bind(c) {
	for ( var a in c) {
		
		var b = c[a];
		if (typeof b == "object" && b != null) {
			bind(b);
		} else {
			if (b === 1) {
				$("[eid='" + a + "']").attr("checked", "checked");
				if (a == "isQuery") {
					conditionChange();
				}
				if (editor.canEditColumnNameAndType) {
					if (a == "displayDate") {
						$("[eid='" + a + "']").attr("disabled", "disabled");
					}
				}
			} else {
				b = b.toString().replace(/#newline#/g, "\n");
				$("[eid='" + a + "']:visible").val(b);
				if (a == "type") {
					typeChange.call($("[eid='" + a + "']")[0]);
				}
				if (a == "value") {
					valueChange.call($("[eid='" + a + "']")[0]);
				}
				if (a == "searchFrom") {
					searchFromChange.call($("[eid='" + a + "']")[0]);
				}
				if (a == "loadselect") {
					loadselectChange.call($("[eid='" + a + "']")[0]);
				}
				if (editor.canEditColumnNameAndType) {
					if (a == "name" || a == "type" || a == "length"
							|| a == "intLen" || a == "decimalLen"
							|| a == "format") {
						$("[eid='" + a + "']").attr("disabled", "disabled");
					}
				}
			}
		}
	}
}
function loadselectChange(){
	var values=$(this).val();
	if(values!=''){
		$("textarea[eid='options']").parent().parent().hide();
		
		//获取当前元素的父节点之后的所有兄弟节点进行显示
		$(this).parent().nextAll().each(function (i){
			$(this).show();
		});
		$(this).parent().removeAttr("colspan");
	}else{
		//获取当前元素的父节点之后的所有兄弟节点进行隐藏
		$(this).parent().nextAll().each(function (i){
			$(this).hide();
		});
		$(this).parent().attr("colspan","3");
		$("textarea[eid='options']").parent().parent().show();
	}
}
function valueChange() {
	var a = $(this).val();
	$("tr[class^='valuefrom']").each(function() {
		if ($(this).attr("class").indexOf(a) > -1) {
			$(this).removeClass("hidden");
		} else {
			$(this).addClass("hidden");
		}
	});
}
function typeChange() {
	$(".dbformat_td").html(getFormatHtml($(this).val()));
	if (cmdName == "attachement") {
		$("[eid='length']").val("2000");
	}
}
function getFormatHtml(b) {
	var a = [ "" ];
	switch (b) {
	case "varchar":
		a.push(language.length
						+ ':<input eid="length" prenode="dbType" style="width:60px;" type="text" value="50" validate="{number:true}"/>');
		break;
	case "number":
		a.push(language.integer
						+ ':<input eid="intLen" prenode="dbType" style="width:30px;" type="text" value="14" validate="{number:true}"/><br/>'
						+ language.decimal
						+ ':<input eid="decimalLen" prenode="dbType" style="width:30px;" type="text" value="0" validate="{number:true}"/>');
		break;
	case "date":
		a.push('<select eid="format" prenode="dbType">'
				+ '<option value="yyyy">yyyy</option>'		
				+ '<option value="yyyy-MM-dd">yyyy-MM-dd</option>'
				+ '<option value="yyyy-MM-dd HH:mm:ss">yyyy-MM-dd HH:mm:ss</option>'
				+ '<option value="yyyy-MM-dd HH:mm:00">yyyy-MM-dd HH:mm:00</option>'
				+ '</select><label for="displayDate">'
				//+ '<input id="defaultDate" eid="defaultDate" prenode="dbType" type="checkbox" />'
				//+ language.defaultDate 
				+ '<input id="maxDate" eid="maxDate" prenode="dbType" type="checkbox" />'
				+ language.maxDate 
				+ '<input id="minDate" eid="minDate" prenode="dbType" type="checkbox" />'
				+ language.minDate + "</label>");
		break;
	}
	return a.join("");
}
dialog.onok = function() {
	var b = valid.valid();
	if (!b) {
		return false;
	}
	var c = [];
	var a = $("#dictType").val();
	$("#inputPanel").find("*:visible").each(
			function() {
				var g = $(this).attr("eid"), f = $(this).val(), d = $(this)
						.attr("prenode");
				if ($(this).attr("type")) {
					if ($(this).attr("type") == "checkbox"
							|| $(this).attr("type") == "radio") {
						f = (!!$(this).attr("checked")) ? 1 : 0;
					}
				}
				if ((g && f) || (g && f == 0)) {
					var e = {
						id : g,
						val : f
					};
					if (d) {
						e.prenode = d;
					}
					c.push(e);
				}
			});
	if (!isEmpty(a)) {
		c.push({
			id : "dictType",
			val : a
		});
	}
	editor.execCommand(cmdName, c, oldElement);
};

//异步查询系统后台参数配置
function ajaxSysConfig(){
    
	jQuery.ajax({
		type : 'POST',
		url : projectName+"/private/cs/type/getAjaxCsType",
//		data : $("#form1").serialize(),
		dataType:"json",
		async:false,
		success : function(data) {
			var a = [ "" ];
			a.push('<select eid="loadselect"> <option value="">请选择</option>');
			if(data){
				for(var objNum in data){
					var obj=data[objNum];
					a.push('<option value="'+obj.id+'">'+obj.name+'</option>');
				}
			}
			a.push('</select>');
			$("#sysConfig").html(a.join(""));
			
			
		},error : function(req) {
			alert("系统错误?!");
		}
	});
}
$(function() {
	//加载系统后台参数
	ajaxSysConfig();
	
	$focus($G("fieldName"));
	initComplete();
	valid = $("#inputPanel").form();
});