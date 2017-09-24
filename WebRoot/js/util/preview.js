

jQuery(document).ready(function() {
    	jQuery.each(jQuery("select"),function(i,n){
			var load = jQuery(this).attr("loadselect");
			if(load!=null&&load!=''){
				eval(load);
			}else{
				var selectedvalue = jQuery(this).attr("selectedvalue");
				if(selectedvalue!=null&&selectedvalue!=''){
					jQuery(this).val(selectedvalue);
				}
			}
		});
    	
    	
    	//页面初始化时 选中checkbox选中的值
    	$("input:hidden[clas='checkbox']").each(function(){
  		  var key=$(this).prop("name");
  		  var hiddenvalue=$(this).val();
  		  if (hiddenvalue) {
			var valueArry=hiddenvalue.split(",");
			for (var i = 0; i < valueArry.length; i++) {
				$('input:checkbox[name='+key+'_name][value='+valueArry[i]+']').attr("checked","checked");
			}
		}
  	  });
    	//页面初始化时 选中radio选中的值
    	$("input:hidden[clas='radio']").each(function(){
  		  var key=$(this).prop("name");
  		  var hiddenvalue=$(this).val();
  		  if (hiddenvalue) {
			var valueArry=hiddenvalue.split(",");
			for (var i = 0; i < valueArry.length; i++) {
				$('input:radio[name=radioinput_'+key+'][value='+valueArry[i]+']').attr("checked","checked");
			}
			
			$('input:radio[name=radioinput_'+key+']:checked').click();
//			var $('input:radio[name=radioinput_'+key+']:checked').attr('onclick'));
		}
  	  });
    	
    	//页面初始化时 选中radio选中的值
    	$("input:hidden[clas='xyzt']").each(function(){
    		//信用主体
			var xyzt=$(this).attr('showxyzt');
			if(xyzt){
				$("input[name='"+$(this).attr("name")+"_name']").val(xyzt);
			}
  		 
  	  });
    if(typeof yhjg=="string"&&yhjg!=""){
    	//页面初始化时 设置用户所在机构的值
    	$("input:hidden[clas='userunit']").each(function(){
    		var xyzt=$(this).val(yhjg);
    		$("input[name='userunit_"+$(this).attr("name")+"']").val(yhjgname);
    	});
    }
});
	
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
			if(optionData.length==1&&typeid=="00020015"){
				$(obj).html("");
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
function initSelect(obj,typeid,values){
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
	$(obj).val(values);
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
//===================================================加载select 级联数据结束===========================

function loadFileClick_old(obj){
	var inputname=$(obj).attr("fileinputname");
	var d = new top.Dialog("filedialog");
    d.Width = 360;
    d.Height = 160;
    d.Title = "文件上传";
    d.URL = CONTEXTPATH+"/private/file/uploadOne";
    d.OKEvent = function(){ 
    	$('input[name="'+inputname+'"]').val($DW.$("#filename").val());
    	$('input[name="'+inputname+'_filename"]').val($DW.$("#filename").val());
    	$('input[name="'+inputname+'_filepath"]').val($DW.$("#filepath").val());
    	$('input[name="'+inputname+'_filesize"]').val($DW.$("#filesize").val());
    	$D.close();
    };
    d.CancelEvent = function(){ 
    	$D.close();
    };
    d.onLoad = function() {
    	
    }
    d.ButtonCenter=true;
    d.show(); 
    d.OKButton.value=" 确定";
    d.CancelButton.value=" 取消 ";
}
function loadFile(){
	var d = new top.Dialog("filedialog");
    d.Width = 360;
    d.Height = 160;
    d.Title = "文件上传";
    d.URL = CONTEXTPATH+"/private/file/uploadOne";
    d.OKEvent = function(){ 
    	$('input[name="filename"]').val($DW.$("#filename").val());
    	$('input[name="filepath"]').val($DW.$("#filepath").val());
    	$('input[name="filesize"]').val($DW.$("#filesize").val());
    	$D.close();
    };
    d.CancelEvent = function(){ 
    	$D.close();
    };
    d.onLoad = function() {
    	
    }
    d.ButtonCenter=true;
    d.show(); 
    d.OKButton.value=" 确定";
    d.CancelButton.value=" 取消 ";
}

//采矿权，探矿权多附件上传
function selectUpload(obj){
    var d = new Dialog("Add");
        d.Width = 400;
        d.Height = 125;
        d.Title = "文件上传";
        d.URL = CONTEXTPATH+"/private/file/uploadOne",
        d.OKEvent = function(){
           	addUpload(obj);
        };    	    		
        d.show();
}

function addUpload(obj) {
	var filename = $DW.$("#filename").val();
	var filepath = $DW.$("#filepath").val();
	var filesize = $DW.$("#filesize").val();
	var varItem = new Option(filename,filepath+"*"+filesize);
	obj.options.add(varItem);
	$D.close();
}
function setFile1(obj){
	var path="";
	var name="";
	var size="";
	for (var i = obj.length - 1; i >= 0; i--){
		var ss=obj[i].value.split("*");
		path+=ss[0]+";";
		name+=obj[i].text+";";
		size+=ss[1]+";";
	}
	form1.filepath.value=path;
	form1.filename.value=name;
	form1.filesize.value=size;
}
//删除附件
function del_fujian(obj){
	if (obj.length!=0) {//是否有附件
		for (var i = obj.length - 1; i >= 0; i--){
			if (obj[i].selected == true){//是否有选中的附件
				obj.remove(i);
				return;
			}/*else{
				Dialog.alert("请选择需要删除的附件！");
			}*/
		}
	}else{
		Dialog.alert("当前没有可删除的附件！");
	}
}



function loadFileClickOut(obj){
	var inputname=$(obj).attr("fileinputname");
	var d = new top.Dialog("filedialog");
	d.Width = 360;
	d.Height = 160;
	d.Title = "文件上传";
	d.URL = CONTEXTPATH+"/private/file/uploadOne";
	d.OKEvent = function(){
		if($DW.$("#filename").val()!=""&&$DW.$("#filepath").val()!=""){
			$('input[name="'+inputname+'"]').val($DW.$("#filename").val());
			setFileList($DW.$("#filepath").val(),$DW.$("#filename").val(),$DW.$("#filesize").val());
		}
		$D.close();
	};
	d.CancelEvent = function(){
		$D.close();
	};
	d.onLoad = function() {

	}
	d.ButtonCenter=true;
	d.show();
	d.OKButton.value=" 确定";
	d.CancelButton.value=" 取消 ";
}
//---多附件上传开始--------
function setFile(inputname){
	var name="";
	var path="";
	var size="";
	var id="";
	$('select[name="'+inputname+'"] option').each(function(i){
		name+=$(this).text()+";";
		id+=$(this).val().split("\|")[0]+";";
		path+=$(this).val().split("\|")[1]+";";
		size+=$(this).val().split("\|")[2]+";";
	});
	$('input[name="'+inputname+'_filename"]').val(name);
	$('input[name="'+inputname+'_fileid"]').val(id);
	$('input[name="'+inputname+'_filepath"]').val(path);
	$('input[name="'+inputname+'_filesize"]').val(size);
	
}
function loadFileClick(obj,type){
    var d = new Dialog("Add");
        d.Width = 400;
        d.Height = 125;
        d.Title = "文件上传";
        d.URL = CONTEXTPATH+"/private/file/uploadOne",
        d.OKEvent = function(){
        	var inputname=$(obj).attr("fileinputname");
        	var filename = $DW.$("#filename").val();
        	var filepath = $DW.$("#filepath").val();
        	var filesize = $DW.$("#filesize").val();
        	var varItem = " |"+filepath+"|"+filesize;
        	if(type==0){//单附件上传,删除其他的附件
        		$('select[name="'+inputname+'"] option').remove();
        	}
        	if(filename!=""&&filepath!=""){
        		$('select[name="'+inputname+'"]').append("<option value='"+varItem+"' selected>"+filename+"</option>");
        		setFile(inputname);
        	}
        	$D.close();
        };    	    		
        d.show();
}
//删除附件
function deleteFile(obj){
	var inputname=$(obj).attr("fileinputname");
	$('select[name="'+inputname+'"]').find("option:selected").remove();
	setFile(inputname);
}
//--多附件上传结束--------------

////////添加一行、删除一行封装方法///////
/**
 * 为table指定行添加一行
 *
 * tab 表id
 * row 行数，如：0->第一行 1->第二行 -2->倒数第二行 -1->最后一行
 * trHtml 添加行的html代码
 *
 */
function addTr(tab){
    //获取table最后一行 $("#tab tr:last")
    //获取table第一行 $("#tab tr").eq(0)
    //获取table倒数第二行 $("#tab tr").eq(-2)
    //指定行后
    var $tr=$("#"+tab+" tr:last");
    var subkey=tab.replace("table_","")
    var trHtml=$("#"+tab.replace("table","tr")).prop('outerHTML');
    trHtml=trHtml.replace("id=\"tr_"+subkey+"\"","").replace("name=\"radioinput_"+subkey+"\"","name=\"radioinput_"+subkey+length+"\"");
    $("#"+tab.replace("table","tr")+" input:hidden[clas='radio']").each(function(){
		  var key=$(this).prop("name");
	      var length = $("input[name="+key+"]").length+1;
		  trHtml=trHtml.split("name=\"radioinput_"+key+"\"").join("name=\"radioinput_"+key+length+"\"");
	});
    
    if($tr.size()==0){
       alert("指定的table id不存在！");
       return;
    }
    $tr.after(trHtml);
    
    $("#"+tab+" tr:last input").each(function(i){
    	if($(this).prop("name")==tab.replace("table","ckb")){
    		$(this).prop("disabled","");
    	}else if($(this).prop("type")=="text"||$(this).prop("type")=="hidden"){
    		$(this).prop("value","");
    	}
	  });
    //情况最后一个tr 下面的主键value
    $("#"+tab+" tr:last").find(":last-child").val("");
 }
 
function delTr(ckb){
   //获取选中的复选框，然后循环遍历删除
   var ckbs=$("input[name="+ckb+"]:checked");
   if(ckbs.size()==0){
      alert("要删除指定行，需选中要删除的行！");
      return;
   }
   
   ckbs.each(function(i,vars){
	   var size=$("input[name="+ckb+"]").size();
	   if(size==1){
		   
	   }else{
		   $(this).parent().parent().remove();
	   }
	  
   });
}
function delAllTr(ckb){
    //获取复选框，然后循环遍历删除
    var ckbs=$("input[name="+ckb+"]");
    if(ckbs.size()==0){
       alert("未找到要删除的行！");
       return;
    }
          ckbs.each(function(i){
       	   if(i!=0){
             	$(this).parent().parent().remove();
       	   }
          });
 }
/**
 * 全选
 * 
 * allCkb 全选复选框的id
 * items 复选框的name
 */
function allCheck(allCkb, items){
	  var checkobj=document.getElementById(allCkb);
	  var obj=document.getElementsByName(items);
	  fselectAll(checkobj, obj);
}
function fselectAll(checkobj, obj) {
		if (checkobj.checked) {
			if (obj.length == null) {
				obj.checked = true;
			} else {
				for (tmp = 0; tmp < obj.length; tmp++) {
					if(obj[tmp].disabled==true){
						obj[tmp].checked = false;
					}else{
						obj[tmp].checked = true;
					}
				}
			}
		} else {
			if (obj.length == null) {
				obj.checked = false;
			} else {
				for (tmp = 0; tmp < obj.length; tmp++) {
					obj[tmp].checked = false;
				}
			}
		}
	}

function getcheckbox(){
	  $("input:hidden[clas='checkbox']").each(function(){
		  var key=$(this).prop("name");
		  var spCodesTemp = "";
	      $('input:checkbox[name='+key+'_name]:checked').each(function(i){
		       if(0==i){
		        spCodesTemp = $(this).val();
		       }else{
		        spCodesTemp += (","+$(this).val());
		       }
	      });
	      $(this).val(spCodesTemp);
	  });
}
function getselect(){
	jQuery.each(jQuery("select"),function(i,n){
		var load = jQuery(this).attr("loadselect");
		var name = jQuery(this).attr("name");
		var iscascade = jQuery(this).attr("iscascade");
		if(name!=null&&name!=''&&iscascade!=null&&iscascade!=''){
			var iscascades = iscascade.split("_");
			var i=iscascades[iscascades.length-1];
			if(i>0){
				var val1=$("select[name='"+name+"']").val();
				if(val1==""){
					var val=$("select[iscascade='"+name+"_"+(i-1)+"']").val();
					var text=$("select[iscascade='"+name+"_"+(i-1)+"']").find("option:selected").text();;
					$("select[name='"+name+"']").append("<option value='"+val+"' selected>"+text+"</option>");
				}
			}
		}
	});
}
function getradio(){
	  $("input:hidden[clas='radio']").each(function(j){
		  var key=$(this).prop("name");
		  var spCodesTemp = "";
		  if(0==j){
			  spCodesTemp=$("input:radio[name='radioinput_"+key+"']:checked").val();
		  }else{
			  spCodesTemp=$("input:radio[name='radioinput_"+key+(j+1)+"']:checked").val();
		  }
	      $(this).val(spCodesTemp);
	  });
}
function userselector(label,singleSelect) {
	var d = new Dialog("add");
	d.Width = 750;
	d.Height = 360;
	d.Title = "信用主体选择";
	d.URL = CONTEXTPATH+"/private/jlxx/toAddfr?ctype="+singleSelect;
	if(typeof xytype_form=="string"&&xytype_form!=""){
		d.URL +="&xytype="+xytype_form
	}
	if(typeof tname_form=="string"&&tname_form!=""){
		d.URL +="&tname="+tname_form
	}
	if(typeof zttype_form=="string"&&zttype_form!=""){
		d.URL +="&zttype="+zttype_form
	}
	d.OKEvent = function() {
		if ($DW.doSave()) {
			var a = $DW.$("#checkids_id").val();
			var b = $DW.$("#checkids_name").val();
			$("input[name='"+label+"']").val(a);
			$("input[name='"+label+"_name']").val(b);
			d.close();
		}
	}
	d.show();
}
function addByName(name){
	var ss=0;
	  $("input[name='"+name+"']").each(function(){
		  if($(this).val()!=""){
			  ss=accAdd(ss,parseFloat($(this).val()));
		  }
	  });
	  return ss;
}
function accAdd(arg1,arg2){ 
	var r1,r2,m; 
	try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	m=Math.pow(10,Math.max(r1,r2)) 
	return (arg1*m+arg2*m)/m 
	} 
function getMaxByName(name){
	var ss="";
	  $("input[name='"+name+"']").each(function(){
		  if($(this).val()!=""&&ss<$(this).val()){
			  ss=$(this).val();
		  }
	  });
	  return ss;
}
function getMinByName(name){
	var ss="";
	  $("input[name='"+name+"']").each(function(){
		  if(($(this).val()!=""&&ss>$(this).val())||ss==""){
			  ss=$(this).val();
		  }
	  });
	  return ss;
}
//异步加载信用主体数据
function loadxyzt(tname,fname,sname,value){
	jQuery.ajax({
  		type : "post",
		dataType : "json",
		url : CONTEXTPATH+"/private/formyy/loadxyzt",
		data : {"tname":tname,"fname":fname,"sname":sname,"value":value},
		async:false,
		cache : false,
		success : function(xyzt) {
			$("input[name='xyzt']").val(xyzt["xyzt"]);
			$("input[name='xyname']").val(xyzt["xyname"]);
			$("input[name='xycode']").val(xyzt["xycode"]);
		},
		error : function() {
			Dialog.alert("访问数据失败,请核对是否存在或联系管理员！");
			$("input[name='xyzt']").val("");
			$("input[name='xyname']").val("");
			$("input[name='xycode']").val("");
		}
	});
}

function checkOnly(idname,keyname,tablename,name){
	var value=$("input[name='"+keyname+"']").val();
	var id=$("input[name='"+idname+"']").val();
	var bol=true;
	if(value){//没有值得时候不验证
		jQuery.ajax({
			type : 'POST',
			url : CONTEXTPATH+"/private/formyy/checkOnly",
			data : {"keyvalue":value,"id":id,"idname":idname,"keyname":keyname,"tablename":tablename},
			async:false,
			success : function(res) {
				if (res == "false") {
					Dialog.alert("当前输入的"+name+"已存在，请更换！", function() {
					});
					bol=false;
				}
			},
			fail : function(res) {
				Dialog.alert("系统错误?!");
				bol=false;
			}
		});
	}
	return bol;
}

function toAddXKZH(conName){
	var xyml = $("#xy_type").val();
	var ctype = 1;
	d = new Dialog("Addht");
	d.Width = 900;
	d.Height = 450;
	d.Title = "添加合同";
	d.URL = CONTEXTPATH + "/private/formyy/selectXKZH?type="+ctype+"&conName="+conName;
	d.OKEvent = function(){
		if($DW.doSave()){
			var ht_ids = $DW.$("#ids").val();
			var ht_names = $DW.$("#htnames").val();
			var xyzt_name = $DW.$("#xyzts").val();
			var xyzt_ids = $DW.$("#xyztids").val();
			var bjhs = $DW.$("#bjhs").val();
			$("input[name='xkzh']").val(ht_names);
			var names = xyzt_name.split("☆☆");
			$("input[name='xyname']").val(names[0]);
			$("input[name='xycode']").val(names[1]);
			$("input[name='xyzt']").val(xyzt_ids);
			$("input[name='bjh']").val(bjhs);
			$D.close();
		}
	}
	d.show();
}

function selectTKXKZH(conName){
	var xyml = $("#xy_type").val();
	var ctype = 1;
	d = new Dialog("Addht");
	d.Width = 900;
	d.Height = 450;
	d.Title = "添加合同";
	d.URL = CONTEXTPATH + "/private/formyy/selectTKXKZH?type="+ctype+"&conName="+conName;
	d.OKEvent = function(){
		if($DW.doSave()){
			var ht_ids = $DW.$("#ids").val();
			var ht_names = $DW.$("#htnames").val();
			var xyzt_name = $DW.$("#xyzts").val();
			var xyzt_ids = $DW.$("#xyztids").val();
			var bjhs = $DW.$("#bjhs").val();
			$("input[name='xkzh']").val(ht_names);
			var names = xyzt_name.split("☆☆");
			$("input[name='xyname']").val(names[0]);
			$("input[name='xycode']").val(names[1]);
			$("input[name='xyzt']").val(xyzt_ids);
			$("input[name='bjh']").val(bjhs);
			$D.close();
		}
	}
	d.show();
}


//这里是用于修改页面证明材料的上传
function loadFile1(){
var d = new top.Dialog("filedialog");
d.Width = 360;
d.Height = 160;
d.Title = "文件上传";
d.URL = CONTEXTPATH+"/private/file/uploadOne";
d.OKEvent = function(){ 
	$('input[name="filename1"]').val($DW.$("#filename").val());
	$('input[name="filepath1"]').val($DW.$("#filepath").val());
	$('input[name="filesize1"]').val($DW.$("#filesize").val());
	$D.close();
};
d.CancelEvent = function(){ 
	$D.close();
};
d.onLoad = function() {
	
}
d.ButtonCenter=true;
d.show(); 
d.OKButton.value=" 确定";
d.CancelButton.value=" 取消 ";
}

function loadFile2(){
var d = new top.Dialog("filedialog");
d.Width = 360;
d.Height = 160;
d.Title = "文件上传";
d.URL = CONTEXTPATH+"/private/file/uploadOne";
d.OKEvent = function(){ 
	$('input[name="filename2"]').val($DW.$("#filename").val());
	$('input[name="filepath2"]').val($DW.$("#filepath").val());
	$('input[name="filesize2"]').val($DW.$("#filesize").val());
	$D.close();
};
d.CancelEvent = function(){ 
	$D.close();
};
d.onLoad = function() {
	
}
d.ButtonCenter=true;
d.show(); 
d.OKButton.value=" 确定";
d.CancelButton.value=" 取消 ";
}