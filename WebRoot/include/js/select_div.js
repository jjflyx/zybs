var ZfSelect = [];//一个模拟sel选项对象
var CurrentTR = null;//获得焦点的row对象
var selectObj = [];//真实下拉列表对象数组

var realValue = [];//真实值的隐藏表单对象数组
//自定义去空格函数Trim()
function String.prototype.Trim(){
	return this.replace(/(^\s*)|(\s*$)/g,'');
}

/**
 * 在第ID个控件下增加option选项
 * v:选项值
 * id:ZfSelect对象的N值
 */
function addOption(v,ID) { 
	var i=ID+1;
	if(!v.Trim()){
		return;
	}
	//--old---var tb=window.frames[ID].document.all['ZfSelect'+i];//获取id为ZfSelect+N的表格对象,换成一下方式
	var tb=document.getElementById(ZfSelect[ID][3]).contentWindow.document.all[ZfSelect[ID][2]];
	var c=tb.insertRow(tb.rows.length).insertCell();//插入一行一列的tb对象
	// 下拉列表中的显示值 c.innerHTML
	c.innerHTML=v.Trim();
	c.onmouseover=new Function("tb_MouseOver(this)");//给tb对象添加鼠标移入事件监听
	// c.onmouseout=new Function("out(this)");
	c.onclick=new Function("tb_MouseClick("+ID+",this)");//给tb对象添加鼠标单击事件监听
	v='';
}

/**
 * 增加inpnubox的接口,在页面中产生一个inputbox控件,下拉列表为空
*@name:名字
*@DefValue:默认值
*@width:宽度
*@selObj:下拉列表
*@realObj:保存真实值的隐藏域
*@root:工程名
*@isImportant:是否需要加红标记
*/
function addSelect_div(room,name,DefValue,width,selObj,realObj,root,isImportant,fsize) { 
	//alert(room);
	var i=getSelCount();//获取id为ZfContainer+N的对象的数量
	//alert(i+","+name);
	var j=i+1;
	var str='<table id="ZfContainer'+j+'" border="0" cellspacing=0 cellpadding=0 height="10px">' +
			'<tr>' +
			'<td class="selectBox">' +
			'<input name="'+name+'" value="'+DefValue+'" class="editBox" onblur="sel_BoxBlur(this)" onclick="sel_DropDown('+i+',\''+room+'\','+width+',\''+root+'\')" onkeyup="sel_showTip('+i+')" >' +
			'</td>    ' +
			'<td width="1" class="selectBox_b">' +
			'<span class="dropdownbutton" onmouseover="sel_ButtonOver(this)" onmouseout="sel_ButtonOut(this)" onclick="selClickDown('+i+',\''+room+'\','+width+',\''+root+'\')">&nbsp;&nbsp;&nbsp;</span>'
			+'</td> ';
	if(isImportant!=null && isImportant!=undefined && typeof(isImportant)=='boolean' && isImportant){
		if(fsize==null || fsize==undefined || isNaN(fsize)){
			fsize=20;
		}
		str=str+'<td><font color="red" style="font-weight:bolder;font-size: '+fsize+';position: relative;" >&nbsp*</font></td>' +
			'</tr>' +
			'</table>';
	}else{
		str=str+'</tr></table>';
	}
	//alert(str);
	//document.getElementById(room).innerHTML=str;//在页面里添加一个id为ZfContainer+N的表格
	var idname="#"+room;
	$(idname).html(str);
	ZfSelect[i]=["ZfContainer"+j,name,"ZfSelect"+j,"ZfIframe"+j,"ZfDiv"+j];
	var ZfContainer_s=GetSel_Obj(ZfSelect[i][0]);//获取id为ZfContainer+N的talbe对象
	if(isImportant!=null && isImportant!=undefined && typeof(isImportant)=='boolean' && isImportant){
		ZfContainer_s.width=(width+19);;
	}else{
		ZfContainer_s.width=width;
	}
	selectObj[i] = selObj; 
	realValue[i] = realObj;
}
/**
 * 在指定位置下插入一个iframe作为select的选择列表
 */
function set_SelOptions(i,room,width,root){
	//alert("addOption="+i+","+ZfSelect[i][3]);
	var j=i+1;
	var fwidth=width-4;
	//framestr='<iframe src="#" id="ZfIframe'+j+'" name="ZfIfrme'+j+'" frameborder="0" class="selectContentiframe" style="width: '+fwidth+'px;" scrolling="no"></iframe>';
	//创建一个Iframe对象
	var frameNote=document.createElement("iframe");
	//frameNote.innerHTML=framestr;
	frameNote.attributes["id"].value=ZfSelect[i][3];
	frameNote.attributes["name"].value=ZfSelect[i][3];
	frameNote.attributes["class"].value='selectContentiframe';
	frameNote.attributes["scrolling"].value="no";
	
	frameNote.style.width=fwidth;
	document.getElementById(room).appendChild(frameNote);//在room对象中添加iframe对象
	str='<link href="/'+root+'/include/css/select_div.css" rel="stylesheet" type="text/css" />' +
		'<div class="selectContentDiv" id="ZfDiv'+j+'" style="OVERFLOW-y: auto;">' +
		'<table id="ZfSelect'+j+'" class="selectContent" cellspacing=0 cellpadding=0></table>' +
		'</div>';
	//alert(str);
    //--old---window.frames[i].document.write(str);//在frames+N的框架页面里添加一个id为ZfDiv+N的div，以及id为ZfSelect+N的Table
	document.getElementById(ZfSelect[i][3]).contentWindow.document.write(str);
	//alert(ZfSelect);
    GetFrame_Obj(ZfSelect[i][2],i).width=fwidth;//对齐ZfContainer表和ZfSelect的宽度
	GetFrame_Obj(ZfSelect[i][4],i).width=fwidth;//对齐ZfDiv和ZfSelect的宽度
	GetFrame_Obj(ZfSelect[i][4],i).height=200;//设置ZfDiv的高度
	//在id为ZfSelect+N的talbe对象中创建‘对应下拉列表选项数’的‘行数’，行内容为对应下拉选项text值
	for (g=0;g<selectObj[i].length;g++){
		addOption(selectObj[i][g].text,i);
	}
}
/**
 * 获取id为ZfContainer+N的对象的数量
 */
function getSelCount() { 
	var i=1; 
	while (eval("document.getElementById('ZfContainer"+i+"')")!=null) 
		i++;
	return i-1; 
}

/** 
 * 鼠标单击下拉按钮时事件,显示或隐藏下拉列表 show为true或者false
* 按下的时候为TRUE，
*/
function selClickDown(Index,room,width,root){
	if(event && !event.cancelBubble)
		event.cancelBubble=true;//屏蔽document.onclink事件
	//alert(2+"c="+Index);
	if(GetSel_Obj(ZfSelect[Index][3])!=null && GetSel_Obj(ZfSelect[Index][3])!=undefined){
		ZfContainerReset(Index);
		for(i=0;i<ZfSelect.length;i++){
			if(GetSel_Obj(ZfSelect[i][3])!=null && GetSel_Obj(ZfSelect[i][3])!=undefined)
				GetSel_Obj(ZfSelect[i][3]).style.display='none';
		}
		//alert("DropDown1");
		var show=GetSel_Obj(ZfSelect[Index][3]).style.display=='block';
		GetSel_Obj(ZfSelect[Index][3]).style.display=show?'none':'block';
	}else{
		//把已显示的列表隐藏
		for(i=0;i<ZfSelect.length;i++){
			if(GetSel_Obj(ZfSelect[i][3])!=null && GetSel_Obj(ZfSelect[i][3])!=undefined){
				GetSel_Obj(ZfSelect[i][3]).style.display='none';
			}
		}
		set_SelOptions(Index,room,width,root);
	}
	//setPosition(Index);
}
/**
 * 鼠标移入事件
 */
function tb_MouseOver(trObj){
	//alert("MouseOver");
	if(!CurrentTR)
		CurrentTR=trObj;
	CurrentTR.className="";
	CurrentTR.style.color="";
	CurrentTR.style.backgroundColor="";
	trObj.style.color="#FFFFFF";
	trObj.style.backgroundColor="#4B5FF7";
	trObj.className="sel_tr_OnFocus";
    // trObj.title=trObj.innerText
	CurrentTR=trObj;
}

//select 选择框鼠标移开时消失
function selMouseOut(obj){
	with (document.all.zoom_show){
		style.display = "none";
	}
}
/**
 * 鼠标移入下拉图标事件，
 */
function sel_ButtonOver(BtnObj){
	//BtnObj.style.backgroundColor='#BAB4A7';
	BtnObj.style.backgroundImage='url("images/icon2.jpg")';
}
/**
 * 鼠标移出下拉图标事件，
 */
function sel_ButtonOut(BtnObj){
	//BtnObj.style.backgroundColor='#D4D0C8';
	BtnObj.style.backgroundImage='url("images/icon.jpg")';
}
/**
 *  使text显示点击的值
 * 点击每一项的时候调用的方法
*/
function piPei(Index,value) {
	for(i=0;i<selectObj[Index].length;i++) {
		var test = selectObj[Index].options[i].text;
		//alert("'"+value.length+","+test.length+"'"+value.indexOf("├"));
		var ti=test,tv=value;
		if(test.indexOf("├")>0 && value.indexOf("├")>0){
			ti=test.substring(test.indexOf("├"));
			tv=value.substring(value.indexOf("├"));
			//alert(ti+"'"+tv);
		}
		if(ti==tv) {
			realValue[Index].value=selectObj[Index].options[i].value;
		}
	}
	//alert("z获取的值："+realValue[Index].value);
}
/**
 * 模拟选项鼠标单击事件监听
*/
function tb_MouseClick(Index,trObj){ 
	//alert("MouseClick");
	with(GetSel_Obj(ZfSelect[Index][1])){
		value=trObj.innerText; //使text显示点击的值
		style.backgroundColor='#0A246A';
		style.color='#FFFFFF';
		focus();
		piPei(Index,value);
		for (g=0;g<selectObj[Index].length;g++){
			//alert("enter...");
			if (selectObj[Index].options[g].text==value){ 
				//alert(1);
				value=value;
				break;
			}
		}
	}
	GetSel_Obj(ZfSelect[Index][3]).style.display='none';
	//sel_DropDown(Index);
}
/*
function GoURL(trObj){
	var url=trObj.getAttribute("url");
	if(url&&url!="")
		window.open(url);
}
*/
/**
 *  离开输入框的时候 失去焦点事件
 */
function sel_BoxBlur(InputBox){
	//alert("BoxBlur");
	with(InputBox.style){
		backgroundColor="";
		color='';
	}
}

/** 
 * 鼠标单击"模拟文本框"事件 show为true或者false
* 按下的时候为TRUE，
*/
function sel_DropDown(Index,room,width,root){
	//alert(1);
	selClickDown(Index,room,width,root);
	//event.cancelBubble=true;
	ZfContainerReset(Index);
	GetSel_Obj(ZfSelect[Index][3]).style.display='block';
}

/**
 * 整个页面所有单击事件时，隐藏下拉模拟列表
 */
function document::onclick(){
	for(i=0;i<ZfSelect.length;i++){
		if(GetSel_Obj(ZfSelect[i][3])!=null && GetSel_Obj(ZfSelect[i][3])!=undefined)
			GetSel_Obj(ZfSelect[i][3]).style.display='none';
	}
}

function window::onresize(){
	//alert("onresize");
	for(i=0;i<ZfSelect.length;i++)
		setPosition(i);
}
/**
 *  键盘按下的时候调用的事件,显示包含有输入框内相同字符的项
 * ZfContainer.rows.length为下拉列表中值的个数 这里为8和2
 */
function sel_showTip(Index){
	//alert("Index="+Index+","+ZfSelect.length);
	var inputbox=GetSel_Obj(ZfSelect[Index][1]);
	var ZfContainer=GetFrame_Obj(ZfSelect[Index][2],Index);
	var num=0;
	for(i=0;i<ZfSelect.length;i++){
		if(GetSel_Obj(ZfSelect[i][3])!=null && GetSel_Obj(ZfSelect[i][3])!=undefined)
			GetSel_Obj(ZfSelect[i][3]).style.display='none';
	}
	GetSel_Obj(ZfSelect[Index][3]).style.display='block';
	//显示被筛选出来的选项
	for(i=0;i<ZfContainer.rows.length;i++){
		//alert(ZfContainer.rows[i].cells[0].innerText);
		//如果包含输入的字符，则显示相关的选项，如果输入的字符等于默认值则显示全部选项
		if(selectObj[Index].options[selectObj[Index].selectedIndex].text==inputbox.value 
				|| ZfContainer.rows[i].cells[0].innerText.indexOf(inputbox.value)>=0){
			ZfContainer.rows[i].style.display="";
			num++;
		}else {
			ZfContainer.rows[i].style.display="none";
		}
	}
}
/**
 * 把每一个选项显示出来
 */
function ZfContainerReset(Index){
	//alert("ZfSelect[Index][2]="+ZfSelect[Index][2]);
	var ZfContainer=GetFrame_Obj(ZfSelect[Index][2],Index);//GetSel_Obj(ZfSelect[Index][2]);
	for(i=0;i<ZfContainer.rows.length;i++)
	{
		ZfContainer.rows[i].style.display="";
	}
	if(CurrentTR){
		CurrentTR.className="";
		CurrentTR.style.color="";
		CurrentTR.style.backgroundColor="";
	}
}
// 公用方法
function setPosition(Index){
	/* var s=GetSel_Obj(ZfSelect[Index][0])
	var ZfContainer=GetSel_Obj(ZfSelect[Index][2])
	var l=s.offsetLeft;
	var t=s.offsetTop;
	
	while(s=s.offsetParent){l+=s.offsetLeft;t+=s.offsetTop}
	with(ZfContainer.style){left=l+1;top=t+GetSel_Obj(ZfSelect[Index][0]).offsetHeight}
	*/
}
/**
 * 获取对应id的对象
 */
function GetSel_Obj(id){
	return document.getElementById(id);
}
/**
 *获取对应frame内的ID对象 
 */
function GetFrame_Obj(id,frame){
	//alert("GetFrame_Obj="+id+","+frame);
	return document.getElementById(ZfSelect[frame][3]).contentWindow.document.getElementById(id);
}
/*
// 鼠标进入该区域的时候调用的事件
// r.text为列表的显示值

function selectmove(){
	//alert("selectmove");
	var r;
	if(window.event.srcElement.isTextEdit) {
		r=window.event.srcElement.createTextRange();
	}else{
		var el=window.event.srcElement.parentTextEdit;
		//var el=window.event.srcElement.persontype.options[forms[0]1.persontype.selectedIndex]
		r=el.createTextRange();
	}
	r.moveToPoint(window.event.x, window.event.y);
	r.expand("word");
	var str = r.text;

	if(str.length > 0 ) {
		with(zoom_show.style) {
   			display = "";
   			top = event.y + 10;
   			left = event.x + 10;
		}
		zoom_show.innerText = str;
	} else {
		zoom_show.style.display = "none";
	}
}

function checkSel_Value(){//检查值是否在列表中，并且将选中的值赋给realValue
	var flag = false;
	for(i=0;i<selectObj[getSelCount()].length;i++) {
       var test = selectObj[getSelCount()][i].text;
       if(GetSel_Obj(ZfSelect[getSelCount()-1][1]).value==test) {
           realValue[getSelCount()].value=selectObj[getSelCount()][i].value;
           flag = true;
           break;
      }
	}
	if(!flag){
   		alert('<fmt:message key="eventSummary2.noipaddr.list"/>');
      	return false;
	}
	return true;
}
*/
/**功能:下拉列表根据隐藏域值自动选择
*参数:sel_element:下拉列表名hid_element：隐藏域值hid_element_value
*/
function SelectInd(sel_element,hid_element_value){
	for(i=0;i<(sel_element.length);i++){
		if(sel_element.options[i].value==hid_element_value){
			sel_element.selectedIndex=i;
			break;
		}
	}
}