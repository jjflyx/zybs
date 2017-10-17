
function JTrim(s) {
	var r1, r2, s1, s2, s3;
	r1 = new RegExp("^ *");
	r2 = new RegExp(" *$");
	s1 = "" + s + "";
	s2 = s1.replace(r1, "");
	s3 = s2.replace(r2, "");
	r1 = null;
	r2 = null;
	return (s3);
}

//验证电子邮件的合法性
function isValidEmail(obj, str) {
	var s = JTrim(obj.value);
	var n = 0;
	var apos = s.indexOf("@");
	var dpos = s.lastIndexOf(".");
	var spos = s.indexOf(" ");
	var cpos = s.indexOf(",");
	if (cpos >= 0 || spos >= 0 || apos <= 0 || dpos <= 0) {
		n = 0;
	} else {
		if (dpos <= apos + 1) {
			n = 0;
		} else {
			if (s.charAt(apos + 1) == ".") {
				n = 0;
			} else {
				if (s.charAt(s.length - 1) == ".") {
					n = 0;
				} else {
					return true;
				}
			}
		}
	}
	if (str != "") {
		Dialog.alert("\u65e0\u6548\u7684" + str + "\uff01");
		obj.focus();
	}
	return false;
}

//验证密码合法性
function isValidPassword(obj, str) {
	var s = obj.value;
	if (s.length == 0) {
		if (str != "") {
			Dialog.alert(str + "\u4e0d\u53ef\u4e3a\u7a7a\uff01");
			obj.focus();
		}
		return false;
	}
	return true;
}

//验证密码是否相同
function isMatchPassword(obj1, obj2, str) {
	var s1 = obj1.value;
	var s2 = obj2.value;
	if (s1 != s2) {
		Dialog.alert(str + "\u4e0d\u5339\u914d\uff01");
		obj1.focus();
		return false;
	}
	return true;
}

//验证obj是否为空
function isValidValue(obj, str) {
	var s = JTrim(obj.value);
	var c;
	if (s.length == 0) {
		if (str != "") {
			Dialog.alert(str + "\u4e0d\u53ef\u4e3a\u7a7a\uff01");
			obj.focus();
		}
		return false;
	}
	return true;
}

//验证str是否包含/
function noSlash(obj, str) {
	var s = obj.value;
	var c;
	for (tmp = 0; tmp < s.length; tmp++) {
		c = s.charAt(tmp);
		if (c == "/") {
			if (str != "") {
				Dialog.alert(str + "\u4e0d\u53ef\u5305\u542b\u5b57\u7b26\"/\"\uff01");
				obj.focus();
			}
			return false;
		}
	}
	return true;
}
//验证obj是否包含str/
function noStr(obj, str) {
	var s = obj.value;
	var c;
	for (tmp = 0; tmp < s.length; tmp++) {
		c = s.charAt(tmp);
		if (c == str) {
			if (str != "") {
				Dialog.alert("\u4e0d\u53ef\u5305\u542b\u5b57\u7b26\"" + str + "\"\uff01");
				obj.focus();
			}
			return false;
		}
	}
	return true;
}

//验证obj是否包含中文/
function nochinaStr(obj) {
	var s = obj.value;
	var c;
	var re = /[^\x00-\xff]/g;
	for (tmp = 0; tmp < s.length; tmp++) {
		c = s.charAt(tmp);
		if (re.test(c)) {
			Dialog.alert("\u4e0d\u53ef\u5305\u542b\u4e2d\u6587\u5b57\u7b26");
			obj.focus();
			return false;
		}
	}
	return true;
}

//验证str是否为数字
function isDigits(obj, str) {
	var s = obj.value;
	var c;
	for (tmp = 0; tmp < s.length; tmp++) {
		c = s.charAt(tmp);
		if (c > "9" || c < "0") {
			if (str != "") {
				Dialog.alert(str + "\u5fc5\u987b\u662f\u6574\u6570\uff01");
				obj.focus();
			}
			return false;
		}
	}
	return true;
}
function isChats(str) {
	var c;
	if (str != "") {
		for (tmp = 0; tmp < str.length; tmp++) {
			c = str.charAt(tmp);
			if ((c >= "a" && c <= "z") || (c >= "A" && c <= "Z")) {
				continue;
			} else {
				return false;
			}
		}
	} else {
		return false;
	}
	return true;
}

//验证str是否为 Double型
function isDouble(obj, str) {
	var s = obj.value;
	var c;
	if (str != null && str != "") {
		for (tmp = 0; tmp < s.length; tmp++) {
			c = s.charAt(tmp);
			if (c > "9" || c < "0") {
				if (tmp > 0 && c == ".") {
				} else {
					if (str != "") {
						Dialog.alert(str + "\u5fc5\u987b\u662f\u6570\u5b57\uff01");
						obj.focus();
						return false;
					}
				}
			}
		}
	}
	return true;
}
function isDouble1(obj, str) {
	var s = obj.value;
	if (s.length > 1 && s.charAt(0) == "-") {
		s = s.substring(1, s.length);
	}
	var c;
	if (str != null && str != "") {
		for (tmp = 0; tmp < s.length; tmp++) {
			c = s.charAt(tmp);
			if (c > "9" || c < "0") {
				if (tmp > 0 && c == ".") {
				} else {
					if (str != "") {
						Dialog.alert(str + "\u5fc5\u987b\u662f\u6570\u5b57\uff01");
						obj.focus();
						return false;
					}
				}
			}
		}
	}
	return true;
}

//是否是负数（浮点型）
function isNegative(obj, str) {
	var s = obj.value;
	if (s.length > 1 && s.charAt(0) == "-") {
		s = s.substring(1, s.length);
	}
	var c;
	if (str != null && str != "") {
		for (tmp = 0; tmp < s.length; tmp++) {
			c = s.charAt(tmp);
			if (c > "9" || c < "0") {
				if (tmp > 0 && c == ".") {
				} else {
					if (str != "") {
						Dialog.alert(str + "\u5fc5\u987b\u662f\u6570\u5b57\uff01");
						obj.focus();
						return false;
					}
				}
			}
		}
	}
	return true;
}

//验证不能为空，且是double类型
function nullAndDouble(obj, str) {
	var s = obj.value;
	if (isValidValue(obj, str)) {
		var c;
		if (str != null && str != "") {
			for (tmp = 0; tmp < s.length; tmp++) {
				c = s.charAt(tmp);
				if (c > "9" || c < "0") {
					if (tmp > 0 && c == ".") {
					} else {
						if (str != "") {
							Dialog.alert(str + "\u5fc5\u987b\u662f\u6570\u5b57\uff01");
							obj.focus();
							return false;
						}
					}
				}
			}
			return true;
		}
	}
}


//验证不能为空，且是整数类型
function nullAndNum(obj, str) {
	var s = obj.value;
	if (!isDigits(obj, str)) {
		return;
	}
	var c;
	if (str != null && str != "") {
		for (tmp = 0; tmp < s.length; tmp++) {
			c = s.charAt(tmp);
			if (c > "9" || c < "0") {
				if (tmp > 0 && c == ".") {
				} else {
					if (str != "") {
						Dialog.alert(str + "\u5fc5\u987b\u662f\u6570\u5b57\uff01");
						obj.focus();
						return false;
					}
				}
			}
		}
	}
	return true;
}


//验证str是否为 Double型
function isSaveStr(obj, str) {
	var s = obj.value;
	var c;
	if (str != null && str != "") {
		for (tmp = 0; tmp < s.length; tmp++) {
			c = s.charAt(tmp);
			if ((c >= "0" && c <= "9") || (c >= "A" && c <= "Z") || (c >= "a" && c <= "z")) {
			} else {
				Dialog.alert(str + "\u5fc5\u987b\u662f\u6570\u5b57\u6216\u82f1\u6587\u5b57\u6bcd\uff01");
				obj.focus();
				return false;
			}
		}
	}
	return true;
}

//提取STR中所有的数字
function parseDigits(str) {
	var c;
	var i = 0, j = 0;
	var sOK = "";
	for (; tmp < str.length; tmp++) {
		c = str.charAt(tmp);
		if (c > "9" || c < "0") {
			continue;
		}
		sOK += "" + c;
	}
	return sOK;
}
//过滤字符串中的的所有数字
function getNoDigits(str) {
	var c;
	var i = 0, tmp = 0;
	var sOK = "";
	for (; tmp < str.length; tmp++) {
		c = str.charAt(tmp);
		if (c < "9" && c > "0") {
			continue;
		}
		sOK += "" + c;
        //alert(sOK);
	}
	return sOK;
}    

//验证年月日的合法性
function isValidDate(sYear, sMonth, sDate) {
	if (sMonth == 2) {
		if (sDate > 29) {
			return false;
		} else {
			if (sDate == 29 && !((sYear % 4) == 0 && (sYear % 100) != 0 || (sYear % 400) == 0)) {
				return false;
			} else {
				return true;
			}
		}
	} else {
		if ((sMonth == 4 || sMonth == 6 || sMonth == 9 || sMonth == 11) && sDate == 31) {
			return false;
		} else {
			return true;
		}
	}
}

//选中 obj中值为 selectedValue
function selected(obj, selectedValue) { //默认选择 ，obj为选择菜单 ，selectedValue为默认值
	if (selectedValue == "") {
		return;
	}
	for (var tmp = 0; tmp < obj.length; tmp++) {     //如果选择菜单某项的值等于selectedValue，则该项为默认选项
		var value = obj.options[tmp].value;
		if (value == "") {
			value = obj.options[tmp].text;
		}
		if (value == selectedValue) {
			obj.options[tmp].selected = true;
			return;
		}
	}
}


//选中 obj中值为 selectedText ---xukai
function selected2(obj, selectedText) { //默认选择 ，obj为选择菜单 ，selectedValue为默认值
	if (selectedText == "") {
		return;
	}
	for (var tmp = 0; tmp < obj.length; tmp++) {     //如果选择菜单某项的值等于selectedValue，则该项为默认选项
		var value = obj.options[tmp].text;
		if (value == "") {
			value = obj.options[tmp].text;
		}
		if (value == selectedText) {
			obj.options[tmp].selected = true;
			return;
		}
	}
}

//选中单选框中值为 selectedValue
function checked(obj, selectedValue) { //默认选择 ，obj为选择菜单 ，selectedValue为默认值
	if (selectedValue == "") {
		return;
	}
	for (var tmp = 0; tmp < obj.length; tmp++) {     //如果选择菜单某项的值等于selectedValue，则该项为默认选项
		var value = obj[tmp].value;
		if (value == "") {
			value = obj[tmp].text;
		}
		if (value == selectedValue) {
			obj[tmp].checked = true;
			return;
		}
	}
}


//选中单选框中值为 selectedValue 徐凯
function checked2(obj, selectedValue) { //默认选择 ，obj为选择菜单 ，selectedValue为默认值
	if (selectedValue == "") {
		return;
	}
	if (obj.value == selectedValue) {
		obj.checked = true;
		return;
	}
}

//选中OBJ中所有的先项
function selectAll(checkobj, obj) {
	if (checkobj.checked) {
		if (obj.length == null) {
			obj.checked = true;
		} else {
			for (tmp = 0; tmp < obj.length; tmp++) {
				obj[tmp].checked = true;
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


//获取单先框或多选择框选中的数目,i 不带空格
function setSelectno(selObj, values) {
	if (selObj.length == null) {
		if (values.indexOf("," + selObj.value + ",") != -1) {
			selObj.checked = true;
		}
		return;
	} else {
		if (values.length == 0) {
			return;
		} else {
			for (tmp = 0; tmp < selObj.length; tmp++) {
				if (values.indexOf("," + selObj[tmp].value + ",") != -1) {
					selObj[tmp].checked = true;
				}
			}
		}
	}
	return;
}

//获取单先框或多选择框选中的数目
function setSelect(selObj, values) {
	if (selObj.length == null) {
		if (values.indexOf(", " + selObj.value + ", ") != -1) {
			selObj.checked = true;
		}
		return;
	} else {
		if (values.length == 0) {
			return;
		} else {
			for (tmp = 0; tmp < selObj.length; tmp++) {
				if (values.indexOf(", " + selObj[tmp].value + ", ") != -1) {
					selObj[tmp].checked = true;
				}
			}
		}
	}
	return;
}
function checkboxed(obj, values) {
	if (values == "") {
		return;
	} else {
		values = values.substring(0, values.length - 1);//去掉最后一个";"符号
		var strs = new Array(); //定义一数组
		strs = values.split(";");
		for (var tmp = 0; tmp < strs.length; tmp++) {
			var t = strs[tmp];
			if (t == obj[t].id) {
				obj[t].checked = true;
			}
		}
	}
}


//获取单先框或多选择框选中的数目
function getSelectCount(selObj) {
	var count = 0;
	if (selObj.length == null) {
		if (selObj.checked == true) {
			count = 1;
		}
	} else {
		for (tmp = 0; tmp < selObj.length; tmp++) {
			if (selObj[tmp].checked == true) {
				count++;
			}
		}
	}
	return count;
}

//获取第一个选择项的值
function getfirstSelect(selObj) {
	var value = "";
	if (selObj.length == null) {
		if (selObj.checked == true) {
			value = selObj.value;
		}
	} else {
		for (tmp = 0; tmp < selObj.length; tmp++) {
			if (selObj[tmp].checked == true) {
				value = selObj[tmp].value;
				break;
			}
		}
	}
	return value;
}

//转到指定页面
function itemGo(gourl) {
	window.location = gourl;
}

//转到自定义页面，并定义其动作。
function itemToAction(gourl, action) {
	document.form1.action = gourl;
	document.form1.doAction.value = action;
	document.form1.submit();
}

//转到新增页面
function itemToAdd(gourl) {
	document.form1.action = gourl;
	document.form1.doAction.value = "toadd";
	document.form1.submit();
}
//转到修改页面增加列表判断
function itemToAddList(gourl) {
	if (getSelectCount(document.form1.id) >= 1) {
		document.form1.action = gourl;
		document.form1.doAction.value = "toadd";
		document.form1.submit();
	} else {
		Dialog.alert("\u8bf7\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}

//转到修改页面
function itemTodoAction(gourl, doAction) {
	document.form1.action = gourl;
	document.form1.doAction.value = doAction;
	document.form1.submit();
}

//转到修改页面
function itemToDelete(gourl) {
	if (confirm("\u786e\u5b9a\u8981\u5220\u9664\u5417?")) {
		document.form1.action = gourl;
		document.form1.doAction.value = "delete";
		document.form1.submit();
	}
}
function itemToDelete1(gourl) {
	if (confirm("\u786e\u5b9a\u8981\u5220\u9664\u5417?")) {
		document.form1.action = gourl;
		document.form1.doAction.value = "delete1";
		document.form1.submit();
	}
}

//提交新增
function itemAdd(gourl) {
	document.form1.action = gourl;
	document.form1.doAction.value = "add";
	document.form1.submit();
}

//转到修改页面
function itemToUpdate(gourl) {
	document.form1.action = gourl;
	document.form1.doAction.value = "toupdate";
	document.form1.submit();
}

//转到修改页面
function itemToUpdate1(gourl) {
	document.form1.action = gourl;
	document.form1.doAction.value = "toupdate1";
	document.form1.submit();
}
//转到浏览页面
function itemToDetail(gourl) {
	document.form1.action = gourl;
	document.form1.doAction.value = "detail";
	document.form1.submit();
}

//提交修改
function itemUpdate(gourl) {
	document.form1.action = gourl;
	document.form1.doAction.value = "update";
	document.form1.submit();
}

//转到修改页面增加列表判断
function itemToUpdateList(gourl) {
	if (getSelectCount(document.form1.id) >= 1) {
		document.form1.action = gourl;
		document.form1.doAction.value = "toupdate";
		document.form1.submit();
	} else {
		Dialog.alert("\u8bf7\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}
function itemToUpdateListyd(gourl) {
	if (getSelectCount(document.form1.id) >= 1) {
		document.form1.action = gourl;
		document.form1.doAction.value = "listpage";
		document.form1.submit();
	} else {
		Dialog.alert("\u8bf7\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}

//转到修改页面增加列表判断
function itemToUpdateList1(gourl) {
	if (getSelectCount(document.form1.indexNO) >= 1) {
		document.form1.action = gourl;
		document.form1.doAction.value = "toupdate";
		document.form1.submit();
	} else {
		Dialog.alert("\u8bf7\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}
//转到修改页面增加列表判断
function itemToUpdateList2(gourl) {
	if (getSelectCount(document.form1.indexNO) >= 1) {
		document.form1.action = gourl;
		document.form1.doAction.value = "toupdate1";
		document.form1.submit();
	} else {
		Dialog.alert("\u8bf7\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}
//转到修改页面增加列表判断
function itemToUpdateList3(gourl) {
	if (getSelectCount(document.form1.indexNO) >= 1) {
		document.form1.action = gourl;
		document.form1.doAction.value = "toupdate2";
		document.form1.submit();
	} else {
		Dialog.alert("\u8bf7\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}
//提交删除
function itemDel(gourl) {
	if (confirm("\u786e\u5b9a\u8981\u5220\u9664\u5417")) {
		document.form1.action = gourl;
		document.form1.doAction.value = "delete";
		document.form1.deltype.value = "0";
		document.form1.submit();
	}
}

//提交删除增加列表判断
function itemDelList(gourl, type) {
	if (getSelectCount(document.form1.id) >= 1) {
		if (confirm("\u786e\u5b9a\u8981\u5220\u9664\u5417?")) {
			document.form1.action = gourl;
			try {
				getIds();
				document.form1.deltype.value = type;
			}
			catch (err) {
			}
			document.form1.doAction.value = "delete";
			document.form1.submit();
		}
	} else {
		Dialog.alert("\u81f3\u5c11\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}


//提交批量更新操作
function itemDelList2(msg, mathod) {
	if (getSelectCount(document.form1.id) >= 1) {
		if (confirm(msg)) {
			try {
				getIds();
			}
			catch (err) {
			}
			document.form1.doAction.value = mathod;
			document.form1.submit();
		}
	} else {
		Dialog.alert("\u81f3\u5c11\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}
//提交删除增加列表判断
function itemDelList1(gourl, type) {
	if (getSelectCount(document.form1.indexNO) >= 1) {
		if (confirm("\u786e\u5b9a\u8981\u5220\u9664\u5417?")) {
			document.form1.action = gourl;
			try {
				getIds1();
				document.form1.deltype.value = type;
			}
			catch (err) {
			}
			document.form1.doAction.value = "delete";
			document.form1.submit();
		}
	} else {
		Dialog.alert("\u81f3\u5c11\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}

//判断输入字符长度
function valueLength(obj, name, len) {
	if (obj.value.length > len) {
		Dialog.alert(name + "\u957f\u5ea6\u4e0d\u53ef\u4ee5\u8d85\u8fc7" + len);
		return false;
	}
	return true;
}
//得到选择的字符串
function getIds1() {
	var ids = "";
	var obj = document.form1.indexNO;
	if (document.form1.indexNO.length == null) {
		if (document.form1.indexNO.checked) {
			ids = document.form1.indexNO.value;
		}
	} else {
		for (var tmp = 0; tmp < obj.length; tmp++) {
			if (obj[tmp].checked) {
				ids += obj[tmp].value + ",";
			}
		}
	}
	document.form1.checkids.value = ids;
}

//得到选择的字符串
function getIds() {
	var ids = "";
	var obj = document.form1.id;
	if (document.form1.id.length == null) {
		if (document.form1.id.checked) {
			ids = document.form1.id.value;
		}
	} else {
		for (var tmp = 0; tmp < obj.length; tmp++) {
			if (obj[tmp].checked) {
				ids += obj[tmp].value + ",";
			}
		}
	}
	document.form1.checkids.value = ids;
}

//得到选择的字符串
function getValues(obj) {
	var ids = "";
	if (obj.length == null) {
		if (obj.checked) {
			ids = obj.value;
		}
	} else {
		for (var tmp = 0; tmp < obj.length; tmp++) {
			if (obj[tmp].checked) {
				ids += obj[tmp].value + ",";
			}
		}
	}
	document.form1.checkids.value = ids;
}


//得到选择的字符串
function getSelectValues(obj, obj2) {
	var ids = "";
	if (obj.length == null) {
		if (obj.checked) {
			ids = obj.value;
		}
	} else {
		for (var tmp = 0; tmp < obj.length; tmp++) {
			if (obj[tmp].checked) {
				ids += obj[tmp].value + "\u3001";
			}
		}
	}
	if (ids.length > 0) {
		ids = ids.substr(0, ids.length - 1);
	}
	obj2.value = ids;
}

//判断其值是否被选中
function isSelect(value) {
	var obj = document.form1.id;
	for (var tmp = 0; tmp < obj.length; tmp++) {
		if (obj[tmp].checked && obj[tmp].value == value) {
			return true;
		}
	}
	return false;
}

//打开一个弹出窗口
function openWin(endtarget, WINname, WINwidth, WINheight) {
	var showw = window.open(endtarget, WINname, "status=yes,toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,width=" + (WINwidth + 22) + ",height=" + WINheight);
	showw.focus();
}


//得到下拉框的级别
function gettree(id) {
	var line = " ";
	var node = " \u251c";
	var restr = "";
	var count = id.length / 4;
	for (tmp = 1; tmp < count; tmp++) {
		restr += line;
	}
	return restr += node;
}

//得到下拉框的级别html代码方式
function gettreecode(id) {
	var line = "&nbsp;";
	var node = "&nbsp;\u251c";
	var restr = "";
	var count = id.length / 4;
	for (var tmp = 1; tmp < count; tmp++) {
		restr += line;
	}
	return restr += node;
}
function gettreecode1(id) {
	var line = " ";
	var node = " \u251c";
	var restr = "";
	var count = id.length / 4;
	for (tmp = 1; tmp < count; tmp++) {
		restr += line;
	}
	return restr += node;
}
//得到资源的级别
function gettreemap1(id) {
	var line = "&nbsp;";
	var restr = "";
	var count = id.length / 4;
	for (tmp = 1; tmp < count; tmp++) {
		restr += line;
	}
	return restr;
}
//得到资源的级别
function gettreemap(id, domain) {
	var line = "<img src='" + domain + "/images/private/sys/line.png' border=0>";
	var node = "<img src='" + domain + "/images/private/sys/mfc.gif' border=0><img src='" + domain + "/images/private/sys/node.png' border=0>";
	var restr = "";
	var count = id.length / 4;
	for (tmp = 1; tmp < count; tmp++) {
		restr += line;
	}
	return restr += node;
}

//得到资源状态
function getstate(state, online) {
	if (state == online) {
		return "\u221a";
	} else {
		return "\xd7";
	}
}


//
function getnum(f, c) {
	var t = Math.pow(10, c);
	return Math.round(f * t) / t;
}
function ForDight(Dight, How) {
	Dight = Math.round(Dight * Math.pow(10, How)) / Math.pow(10, How);
	return Dight;
}

function cancle(obja, action) {
	var obj = obja;
	obj.doAction.value = action;
	obj.submit();
}

 //提交删除增加列表判断
function itemJyList(gourl, type) {
	if (getSelectCount(document.form1.id) >= 1) {
		document.form1.action = gourl;
		try {
			getIds();
			document.form1.enabled.value = type;
		}
		catch (err) {
		}
		document.form1.doAction.value = "jinyong";
		document.form1.submit();
	} else {
		Dialog.alert("\u81f3\u5c11\u9009\u62e9\u4e00\u6761\u8bb0\u5f55\uff01");
	}
}
   //关闭子窗口，并更新父窗口
function closeWin(location) {
	this.location = location;
	hasClosed = true;
	window.opener.location = "javascript:reloadPage();";
	window.opener = null;
	window.close();
}
function reloadPage() {
	history.go(0);
	document.execCommand("refresh");
	document.location = document.location;
	document.location.reload();
}

   //统一弹窗2
function openWin3(url, title, iWidth, iHeight) {
		var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
		var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
   		var params='height='+iHeight+', width='+iWidth+',top='+iTop+', left='+iLeft+', scrollbars=yes, resizable=yes';
   		var showw = window.open(url, title, params);
   		showw.focus();
}

    
  //统一弹窗
function openwindow(url, title, params) {
	window.open(url, title, params);
}

/**
 * 封装js Map
 */
function Map() {
	var struct = function(key, value) {
		this.key = key;
		this.value = value;
	}
	
	var put = function(key, value) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key == key || this.arr[i].key === key) {
				this.arr[i].value = value;
				return;
			}
		}
		this.arr[this.arr.length] = new struct(key, value);
	}

	var get = function(key) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key == key || this.arr[i].key === key) {
				return this.arr[i].value;
			}
		}
		return null;
	}

	var remove = function(key) {
		var v;
		for (var i = 0; i < this.arr.length; i++) {
			v = this.arr.pop();
			if (v.key === key) {
				continue;
			}
			this.arr.unshift(v);
		}
	}

	var size = function() {
		return this.arr.length;
	}

	var isEmpty = function() {
		return this.arr.length <= 0;
	}
	this.arr = new Array();
	this.get = get;
	this.put = put;
	this.remove = remove;
	this.size = size;
	this.isEmpty = isEmpty;
}  
/**
 * 将json 转换为map
 * @param json_map
 */
function getCustomeMap(json_map){
	var map = new Map();  
	var jsonObj = eval('(' + json_map + ')');
	for (var key in jsonObj){
		map.put(key,jsonObj[key]);
	}
	return map;
}

/**
 * 在Dialog.alert回调函数中使用，关闭当前弹出层，刷新父页面,只适用有loadata初始函数的父页面
 * @return
 */
function refreshloadDataTab(){
	if (window.opener) {
		window.opener.loadData();
		window.close();
	}else{
		loadData();
	}
}

//此方法只为了返回到当前页
function refreshDqTab(){
	if (window.opener) {
		window.opener.reloadData();
		window.close();
	}else{
		reloadData();
	}
}

function refreshloadGridTab(){
	if (window.opener) {
		window.opener.loadGrid();
		window.close();
	}else{
		loadGrid();
	}
}
/**
 * 禁用按钮
 * @param id
 */
function disabledBtn(id){
	$('#'+id).attr('disabled','disabled');
	$('#'+id).attr('style','background-color:gray');
}

/**
 * 启用按钮
 * @param id
 */
function unDisabledBtn(id){
	$('#'+id).removeAttr('disabled');
	$('#'+id).removeAttr('style');
}

/**
 * 页面浏览
 * @param id
 */
function detail(id){
	var url=CONTEXTPATH+'/private/dbzx/todetail?id='+id+'&sys_random=' + Math.random();
		window.open(url);
		
}

/**
 * 播放录音
 * @param id
 */
function playLuYin(id){
	alert("录音播放功能暂未开放，参数传 工单编号");
}

/**
 * 增加提交遮罩层div
 */
function appendDiv(){
		var html="<div id='ajaxLoadingDiv' style='position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:960;text-align:center;display:none;'><div style='background-color:#333;position:absolute;left:0px;top:0px;opacity:0.1;filter:alpha(opacity=0);width:100%;height:100%;z-index:960;'></div>";
		html+="<span style='display:inline-block;height:100%;vertical-align:middle;'></span>";
		html+="<img src='"+CONTEXTPATH+"/images/loading.gif' style='z-index:99999'></img></div>";
		$(this.document.body).append(html);
}
$(function (){
	appendDiv();
});
/**
 * ajax加载效果-用于提交时操作
 */
$(document).ajaxStart(function(){
	$("#ajaxLoadingDiv").attr('style','position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:960;text-align:center;display:block;');
});
$(document).ajaxStop(function(){
	$("#ajaxLoadingDiv").attr('style','position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:960;text-align:center;display:none;');
});


/**
 * ajax加载效果-手动调用
 */
function showLoading(){
	$("#ajaxLoadingDiv").attr('style','position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:960;text-align:center;display:block;');
}
function hideLoading(){
	$("#ajaxLoadingDiv").attr('style','position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:960;text-align:center;display:none;');
}


//判断输入手机号码或电话号码
  function isValiPhone(obj, name)
  {
 	 var re=/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{6,11}$)/;
 	 
      if (!obj.value.match(re))
      {
          Dialog.alert(name + '不合法');
          return false;
      }
      return true;
  }
  
//判断输入11手机号码
  function isValiTel(obj, name)
  {
 	 var re=/(^[0-9]{11}$)/;
 	 
      if (!obj.value.match(re))
      {
          Dialog.alert(name + '不合法');
          return false;
      }
      return true;
  }
  
  
//判断email
  function isValiEmail(obj, name)
  {
 	 var re=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
 	 
      if (!obj.value.match(re))
      {
          Dialog.alert(name + '不合法');
          return false;
      }
      return true;
  }