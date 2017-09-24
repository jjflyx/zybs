jQuery(document).ready(function() {
    	jQuery.each(jQuery("select"),function(i,n){
			var load = jQuery(this).attr("loadselect");
			if(load!=null&&load!=''){
				eval(load);
			}
		});
})
//异步显示 select选择的系统参数信息
function loadSelect(obj,typeid,cascade){
	jQuery.ajax({
  		type : "post",
		dataType : "json",
		url : CONTEXTPATH+"/private/cs/value/getAjaxCsValue",
		data : {"typeid":typeid,"cascade":cascade},
		async:false,
		cache : false,
		success : function(optionData) {
			var a = [ "" ];
			if(optionData){
				$.each(optionData,function(){
					a.push('<option value="'+this.value+'">'+this.name+'</option>');
				});
			}
			$(obj).append(a.join(""));
			//默认选择数据  selectedvalue 从后台追加的 
			var selectedvalue=$(obj).attr("selectedvalue");
			if(selectedvalue){
				//判断是否有级联属性
				var iscascade=$(obj).attr("iscascade");
				if(iscascade){
					var name=$(obj).attr("name");
					onloadCascade(name,typeid,selectedvalue);//初始化级联数据
				}else{
					$(obj).val(selectedvalue);
					if($(obj).val()==null){
						$(obj).append('<option value="'+selectedvalue+'" selected>'+selectedvalue+'</option>');
					}
				}
			}
		},
		error : function() {
			Dialog.alert("访问数据失败！");
		}
	});
}

//===================================================加载select 级联数据===========================
//加载select 级联数据
function selectCascade(obj,typeid){
	
	var values=$(obj).val().replace(/\s+/g,"");//去空格
	var name=$(obj).attr("name");
	var verify=$(obj).attr("verify");
	var selectindex=$(obj).attr("iscascade");
	//获取当前选择的级联框是第几个，删除后面所有的子级联框
	var indexnum=selectindex.split("_");
	var num=indexnum[indexnum.length-1];
	
	//判断当前点击的级联框是否有name
	if(!name){
		var namenum=selectindex.match(/(\S*)_/);
		name=namenum[1];
		
		$(obj).attr("mame",name);
	}
	
	if(values){
		delChildSelect(name,num);
		ajaxSelectCascade(obj,typeid,values,name,verify);
	}else{
		delChildSelect(name,num);
		
	}
}
//删除子级联数据框
function delChildSelect(name,num){
	$("select[iscascade*='"+name+"_']").each(function(i){
		if(i>num){
			$(this).remove();
		}
	});
}
//加载子级联数据
function ajaxSelectCascade(obj,typeid,values,name,verify){
	jQuery.ajax({
  		type : "post",
		dataType : "json",
		url : CONTEXTPATH+"/private/cs/value/getAjaxCascadeCsValue",
		data : {"typeid":typeid,"value":values},
		async:false,
		cache : false,
		success : function(optionData) {
			if(optionData.length>0){
				var cascadesize=$("select[iscascade*='"+name+"_']").size();
				var vtmp="";
				if(verify){
					vtmp='verify="'+verify+'"';
				}
				var a = [ '<select '+vtmp+' name="'+name+'" iscascade="'+name+'_'+cascadesize+'" onchange="selectCascade(this,\''+typeid+'\')" style="margin-left:4px;" >'];
				a.push('<option value="">请选择</option>');
				$.each(optionData,function(){
					a.push('<option value="'+this.value+'">'+this.name+'</option>');
				});
				a.push('</select>');
				$(obj).after(a.join(""));
				$(obj).removeAttr("name");
			}
		},
		error : function() {
			Dialog.alert("访问数据失败！");
		}
	});
}

//初始化时加载选择的级联数据
function onloadCascade(name,typeid,selectedvalue){
	var lastcode=ajaxSelectedCascade(typeid,selectedvalue);
	if (lastcode) {
		var forlength=lastcode.length/4;
		for (var i = 1; i <= forlength; i++) {
			var code=lastcode.substring(0,i*4);
			var value=ajaxCodeCascade(typeid,code);
			var obj=$("select[name='"+name+"']");
			$(obj).val(value);
			selectCascade(obj,typeid);
		}
	}
}

//获取修改前选中的级联数据
function ajaxSelectedCascade(typeid,values){
	var codes;
	jQuery.ajax({
  		type : "post",
		dataType : "json",
		url : CONTEXTPATH+"/private/cs/value/getAjaxSelectedValue",
		data : {"typeid":typeid,"value":values},
		async:false,
		cache : false,
		success : function(optionData) {
			if(optionData){
				codes=optionData.code;
			}
		},
		error : function() {
			Dialog.alert("访问数据失败！");
		}
	});
	return codes;
}
//通过code和typeid获取信息
function ajaxCodeCascade(typeid,code){
	var result;
	jQuery.ajax({
  		type : "post",
		dataType : "json",
		url : CONTEXTPATH+"/private/cs/value/getAjaxCodeValue",
		data : {"typeid":typeid,"code":code},
		async:false,
		cache : false,
		success : function(optionData) {
			if(optionData){
				result=optionData.value;
			}
		},
		error : function() {
			Dialog.alert("访问数据失败！");
		}
	});
	return result;
}