UE.plugins["input"] = function() {
	var me = this;
	me.commands["hidedomain"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				if (old) {
					old.setAttribute("external", getExternal(data));
				} else {
					insertControl(cmdName, data, '<input type="text"/>');
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["textfield"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				//系统参数
				var setsysconfig=setSysConfig(data, cmdName);
				//验证规则
				var setverifytext = setVerifyText(data, cmdName);
				
				var readonlytext = setReadonlyText(data, cmdName);
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = '<input type="text" '+setsysconfig+' '+setverifytext+' ' +readonlytext+' />';
				} else {
					insertControl(cmdName, data, '<input type="text" '+setsysconfig+' '+setverifytext+' ' +readonlytext+' />');
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["textarea"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				//系统参数
				var setsysconfig=setSysConfig(data, cmdName);
				//验证规则
				var setverifytext = setVerifyText(data, cmdName);
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = '<textarea cols="15" rows="3" '+setsysconfig+' '+setverifytext+'></textarea>';
				} else {
					insertControl(cmdName, data,
							'<textarea cols="15" rows="3" '+setsysconfig+' '+setverifytext+'></textarea>');
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["checkbox"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				var text = [ "<input type=\"hidden\" clas=\"checkbox\"/>" ];
				for ( var i = 0, c; c = data[i++];) {
					if (c.id == "options") {
						var options = c.val.split(/\n/g);
						for ( var j = 0, option; option = options[j++];) {
							var vn = option.split("/");
							if(vn.length==2){
								text.push('<input type="checkbox" value="'
										+ vn[1] + '"/>' + vn[0] + " ");
							}else{
								text.push('<input type="checkbox" value="'
										+ option + '"/>' + option + " ");
							}
						}
					}
				}
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = text.join("");
				} else {
					insertControl(cmdName, data, text.join(""));
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["radioinput"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				var text = [ "<input type=\"hidden\" clas=\"radio\"/>" ];
				for ( var i = 0, c; c = data[i++];) {
					if (c.id == "options") {
						var options = c.val.split(/\n/g);
						for ( var j = 0, option; option = options[j++];) {
							var vn = option.split("/");
							if(vn.length==2){
								text.push('<input type="radio" value="'
										+ vn[1] + '"/>' + vn[0] + " ");
							}else{
								text.push('<input type="radio" value="'
										+ option + '"/>' + option + " ");
							}
						}
					}
				}
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = text.join("");
				} else {
					insertControl(cmdName, data, text.join(""));
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["selectinput"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				//系统参数
				var setsysconfig=setSysConfig(data, cmdName);
				//验证规则
				var setverifytext = setVerifyText(data, cmdName);
				//级联属性
				var setcascade=setCascade(data, cmdName);
				
				var text = [ "<select "+setsysconfig+" "+setverifytext+" "+setcascade+" >" ];
				for ( var i = 0, c; c = data[i++];) {
					//是否选择系统参数
//					if(c.id == "loadselect" && c.val){
//						text = [ '<select loadselect="loadSelect(this,\''+c.val+'\')">' ];
//					}
					if (c.id == "options") {
						if(c.val){
							var options = c.val.split(/\n/g);
							for ( var j = 0, option; option = options[j++];) {
								var vn = option.split("/");
								if(vn.length==2){
									text.push('<option value="' + vn[1] + '">'
											+ vn[0] + "</option>");
								}else{
									text.push('<option value="' + option + '">'
											+ option + "</option>");
								}
							}
						}
					}
					
					if(c.id == "loadselect"){
						text.push('<option value=" ">请选择</option>');
					}
				}
				text.push("</select>");
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = text.join("");
				} else {
					insertControl(cmdName, data, text.join(""));
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function(cmd) {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["dictionary"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				if (old) {
					old.setAttribute("external", getExternal(data));
				} else {
					var text = '<input type="text" />';
					insertControl(cmdName, data, text);
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["userunit"] = {
			execCommand : function(cmdName, data, old) {
				if (data) {
					if (old) {
						old.setAttribute("external", getExternal(data));
					} else {
						var text = '<input type="hidden" clas="userunit"/><input class="readonlyText" type="text" readonly="readonly" />';
						insertControl(cmdName, data, text);
					}
				} else {
					openDialog(cmdName);
				}
			},
			queryCommandState : function() {
				return this.highlight ? -1 : 0;
			}
		};
	me.commands["userselector"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				//验证规则
				var setverifytext = setVerifyText(data, cmdName);
				
				var text = setUserText(data, cmdName,setverifytext,'readonly="readonly"');
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = text;
				} else {
					insertControl(cmdName, data, text);
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["roleselector"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				var text = setText(data, cmdName);
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = text;
				} else {
					insertControl(cmdName, data, text);
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["positionselector"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				var text = setText(data, cmdName);
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = text;
				} else {
					insertControl(cmdName, data, text);
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["depselector"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				var text = setText(data, cmdName);
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = text;
				} else {
					insertControl(cmdName, data, text);
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["attachement"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
//				var text = setText(data, cmdName);
				//验证规则
				var setverifytext = setVerifyText(data, cmdName);
				var content = "选择";
				var buttonClick="";
				var name='';
				var infoput='';
				var isMore='';
				var type='';
				
				for ( var i = 0, c; c = data[i++];) {
					if (c.id == "name") {
						name = c.val;
					}
					if (c.id == "buttoncontent") {
						content = c.val;
						buttonClick=' onclick="loadFileClick(this,type);" ';
						break;
					}
					if (c.id == "isMore") {
						type=c.val;
						if(c.val==1){
							isMore =' style="width: 300px;height: 65px;" multiple size="3"';
						}else{
							isMore =' style="width: 300px;" multiple size="1"';
						}
					}
				}
				buttonClick=buttonClick.replace("type", type);
				
				//加入隐藏文件内容
				if(name){
					infoput='<input type="hidden" name="'+name+'_fileid" value="" /><input type="hidden" name="'+name+'_filename" value="" /><input type="hidden" name="'+name+'_filepath" value=""/><input type="hidden" name="'+name+'_filesize" value=""/>';
				}
				//'<input type="text"  readonly="readonly" '+setverifytext+'/>'
				var text= '<select '+setverifytext+isMore+' class="Noprint"></select>'
						+infoput+'<button type="button" fileinputname="'+name+'" class="btn-text btn-'+ cmdName + '" '+buttonClick+'>' + content + '</button>'
						+'&nbsp;<button type="button" fileinputname="'+name+'" class="btn-text btn-attachement-del" onclick="deleteFile(this);">删除</button>';
				
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = text;
				} else {
					insertControl(cmdName, data, text);
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["datepicker"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				var text=[""];
				//验证规则
				var setverifytext = setVerifyText(data, cmdName);
				text.push('<input type="text" onclick="WdatePicker()" readonly="readonly" '+setverifytext+'/>');
				if (old) {
					old.setAttribute("external", getExternal(data));
					old.innerHTML = text.join("");
				} else {
					
					insertControl(cmdName, data, text.join(""));
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["officecontrol"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				if (old) {
					old.setAttribute("external", getExternal(data));
				} else {
					insertControl(cmdName, data, '<input type="text" />');
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["ckeditor"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				if (old) {
					old.setAttribute("external", getExternal(data));
				} else {
					insertControl(cmdName, data, '<input type="text" />');
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	me.commands["subtable"] = {
		execCommand : function(cmdName, data, old) {
			if (data) {
				var rows = 0, cols = 0,tablename = "", tablekey = "", model = 0;
				for ( var i = 0, c; c = data[i++];) {
					if (c.id == "tablerows") {
						rows = c.val;
					} else {
						if (c.id == "tablecols") {
							cols = c.val;
						} else {
							if (c.id == "tablename") {
								tablename = c.val;
							} else {
								if (c.id == "tablekey") {
									tablekey = c.val;
								} else {
									if (c.val == 1) {
										if (c.id == "inlinemodel") {
											model = 0;
										} else {
											if (c.id == "blockmodel") {
												model = 1;
											} else {
												if (c.id == "windowmodel") {
													model = 2;
												}
											}
										}
									}
								}
							}
						}
					}
				}
				var external = getExternal(data);
				var html = [ '<div tablename="' + tablename + '" tablekey="'
						+ tablekey
						+ '" type="subtable" name="editable-input" class="'
						+ cmdName + '" external="' + external + '">' ];
				html.push(getSubtableContent(rows,cols, model,tablekey));
				html.push("</div>");
				if (old) {
					var externalStr = old.getAttribute("external").replace(
							/\&quot\;/g, '"').replace(/\&\#39\;/g, "'");
					var externalObj = eval("(" + externalStr + ")");
					if (externalObj) {
						var tablerows = externalObj.tablerows, oldModel;
						if (externalObj.inlinemodel == 1) {
							oldModel = 0;
						} else {
							if (externalObj.blockmodel == 1) {
								oldModel = 1;
							} else {
								if (externalObj.windowmodel == 1) {
									oldModel = 2;
								}
							}
						}
						if (tablerows == rows && oldModel == model) {
							old.setAttribute("external", external);
							old.setAttribute("tablename", tablename);
							var hh=old.innerHTML+"";
							var pattern1 = new RegExp(old.getAttribute("tablekey"),"gm"); 
							old.innerHTML=hh.replace(pattern1,tablekey);
							old.setAttribute("tablekey", tablekey);
						} else {
							old.outerHTML = html.join("");
						}
					} else {
						old.outerHTML = html.join("");
					}
				} else {
					me.execCommand("insertHtml", html.join(""));
				}
			} else {
				openDialog(cmdName);
			}
		},
		queryCommandState : function() {
			return this.highlight ? -1 : 0;
		}
	};
	function getSubtableContent(rows,cols, modelIndex,id) {
		var text = [ "" ];
		switch (modelIndex) {
		case 0:
			text.push('<table id="table_'+id+'" width="100%" class="listTable" border="0" cellpadding="2" cellspacing="0"><tbody>');
			text.push('<tr class="toolBar"><td colspan="'+ (parseInt(cols)+1)+ '">');
			text.push("<a class=\"link add\" href=\"javascript:addTr('table_"+id+"');\">添加</a>&nbsp;&nbsp;");
			text.push("<a class=\"link del\" href=\"javascript:delTr('ckb_"+id+"');\">删除</a></td></tr>");
			text.push('<tr class="headRow">');
			text.push("<th width=\"60\"><input type=\"checkbox\" id=\"allCkb_"+id+"\" onclick=\"allCheck('allCkb_"+id+"', 'ckb_"+id+"')\"/></th>");
			for ( var i = 0; i < cols; i++) {
				text.push("<th>列" + (i + 1) + "</th>");
			}
			//text.push("<th>删除</th>");
			text.push('</tr>');
			text.push('<tr id="tr_'+id+'" class="listRow" formtype="edit">');
			text.push("<td><input type=\"checkbox\" name=\"ckb_"+id+"\" disabled=\"disabled\"/></td>");
			for ( var i = 0; i < cols; i++) {
				text.push("<td></td>");
			}
			//text.push("<td width=\"60\"><input type=\"button\" class=\"button button_gray\" disabled value=\" 删除 \" onclick=\"delTr('row_"+id+"_1')\" /></td>");
			text.push("</tr>");
			/*for ( var j = 0; j < rows; j++) {
			}*/
			text.push("</table>");
			break;
		case 1:
			text.push('<div formtype="edit"><table class="blocktable" id="table_'+id+'">');
			for ( var j = 0; j < rows; j++) {
				text.push('<tr>');
				for ( var i = 0; i < cols; i++) {
					text.push('<th style="width:' + (100 * 0.3) / cols + '%;">列'
							+ (i + 1) + '</th><td style="width:' + (100 * 0.7)
							/ cols + '%;"></td>');
				}
				text.push("</tr>");
			}
			text.push("</table></div>");
			break;
		}
		return text.join("");
	}
	var setUserText = function(data, cmdName,setverifytext,readonlytext) {
		var content = "选择";
		for ( var i = 0, c; c = data[i++];) {
			if (c.id == "buttoncontent") {
				content = c.val;
				break;
			}
		}
		return '<input type="hidden" /><input type="text" '+setverifytext+' ' +readonlytext+' /><button type="button"  class="btn-text btn-'
				+ cmdName + '">' + content + '</button>';
	};
	var setText = function(data, cmdName) {
		var content = "选择";
		for ( var i = 0, c; c = data[i++];) {
			if (c.id == "buttoncontent") {
				content = c.val;
				break;
			}
		}
		return '<input type="hidden" /><input type="text" /><button type="button"  class="btn-text btn-'
				+ cmdName + '">' + content + '</button>';
	};
	
	//设置下拉框显示系统参数
	var setSysConfig = function(data, cmdName) {
		var content = "";
		var typeid="";
		var cascade="";
		var name="";
		for ( var i = 0, c; c = data[i++];) {
			if(c.id == "name" && c.val){
				name=c.val
			}
			if(c.id == "loadselect" && c.val){
				typeid=c.val
			}
			if(c.id == "selectcascade" && c.val){
				cascade=c.val;
				break;
			}
		}
		
		if (typeid) {
			content='loadselect="loadSelect(this,\''+typeid+'\'';
			if (cascade) {
				content=content+','+cascade+')" iscascade="'+name+'_0" ';
			}else{
				content=content+')"  ';
			}
		}
		return content;
	};
	//设置下拉框级联显示数据
	var setCascade = function(data, cmdName) {
		var content = "";
		var typeid='';
		for ( var i = 0, c; c = data[i++];) {
			
			if(c.id == "loadselect" && c.val){
				typeid=c.val;
			}
			if(c.id == "selectcascade" && c.val){
				
				content='onchange="selectCascade(this,\''+typeid+'\')"';
				break;
			}
		}
		return content;
	};
	//设置只读属性
	
	var setReadonlyText = function(data, cmdName) {
		var content = "";
		for ( var i = 0, c; c = data[i++];) {
			if (c.id == "isReadonly" && c.val==1) {
				content='class="readonlyText" readonly="readonly"';
				break;
			}
		}
		return  content ;
	};
	//设置元素 表单验证类型
	var setVerifyText = function(data, cmdName) {
		var content = "";
		var rule=[];
		for ( var i = 0, c; c = data[i++];) {
			if (c.id == "empty" && c.val==1) {
//				rule.push("");
			}else if (c.id == "NotNull" && c.val==1) {
				rule.push(c.id);
			}else if (c.id == "Number" && c.val==1) {
				rule.push(c.id);
			}else if (c.id == "Time" && c.val==1) {
				rule.push(c.id);
			}else if (c.id == "IsInt" && c.val==1) {
				rule.push(c.id);
			}else if (c.id == "Email" && c.val==1) {
				rule.push(c.id);
			}else if (c.id == "isPhone" && c.val==1) {
				rule.push(c.id);
			}else if (c.id == "PASSWORD" && c.val==1) {
				rule.push(c.id);
			}
		}
		content=rule.join("&&");
		if (content) {
			
			content='verify="'+content+'"';
		}
		return  content ;
	};	
	var openDialog = function(cmdName) {
		me.curInputType = cmdName;
		var title = me.options.labelMap[cmdName]
				|| me.getLang("labelMap." + cmdName) || "";
		var className = "edui-for-" + cmdName;
		var dialogUI = me.ui._dialogs[cmdName + "dialog"];
		if (!dialogUI) {
			iframeUrl = "~/dialogs/input/" + cmdName + ".html";
			var dialog = new baidu.editor.ui.Dialog(utils.extend({
				iframeUrl : me.ui.mapUrl(iframeUrl),
				editor : me,
				className : "edui-for-" + cmdName + "dialog",
				title : title
			}, {
				buttons : [ {
					className : "edui-okbutton",
					label : me.getLang("ok"),
					onclick : function() {
						dialog.close(true);
					}
				}, {
					className : "edui-cancelbutton",
					label : me.getLang("cancel"),
					onclick : function() {
						dialog.close(false);
					}
				} ]
			}));
			dialogUI = dialog;
			dialog.render();
		}
		dialogUI.title = title;
		dialogUI.className = className;
		dialogUI.clearContent();
		dialogUI.open();
	};
	var insertControl = function(cmdName, data, text) {
		var html = [ '<span name="editable-input" style="display:inline-block;padding:2px;" class="'
				+ cmdName + '" external="' ];
		html.push(getExternal(data));
		html.push('">');
		html.push(text);
		html.push("</span>");
		editor.execCommand("insertHtml", html.join(""));
	};
	var getExternal = function(d) {
		var external = {};
		for ( var i = 0, c; c = d[i++];) {
			if (c.prenode) {
				if (typeof external[c.prenode] == "undefined") {
					external[c.prenode] = {};
				}
				(external[c.prenode])[c.id] = c.val;
			} else {
				external[c.id] = c.val;
			}
		}
		external = utils.json2str(external);
		return utils.htmlEncode(external);
	};
};