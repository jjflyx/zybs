function initTT(datas)  {
	$('#tt').datagrid(
			{
				width:"auto",
				height:"auto",
				singleSelect : true,
				rownumbers:true,//行号
				columns : [ [
						{
							title : '数据库名',
							field : 'fieldname',
							width : 80,
							align : 'left',
							editor : 'text'
						},
						{
							title : '中文名称',
							field : 'fieldlabel',
							width : 120,
							align : 'left',
							editor : 'text'
						},
						{
							title : '字段类型',
							field : 'fieldtype',
							width : 60,
							align : 'left',
							editor : 'text'
						},
						{
							title : '字段长度',
							field : 'fieldsize',
							width : 50,
							align : 'left',
							editor : 'numberbox'
						},
						{
							title : '显示格式',
							field : 'showformat',
							width : 200,
							align : 'left',
							editor : 'text'
						},
						{
							title : '主键',
							field : 'isprimary',
							width : 40,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							title : '公开',
							field : 'ispublic',
							width : 50,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							title : '必填',
							field : 'isrequired',
							width : 40,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							title : '列表',
							field : 'islist',
							width : 40,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							title : '查询',
							field : 'isquery',
							width : 40,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							field : 'action',
							title : '操作',
							width : 70,
							align : 'center',
							formatter : function(value, row, index) {
								if(row.isdesignshow==1||row.isdesignshow==2){
									return ;
								}
								if (row.editing) {
									var s = "<a href=\"#\" onclick=\"saverow('tt',"
											+ index + ")\">保存</a> ";
									var c = "<a href=\"#\" onclick=\"cancelrow('tt',"
										+ index + ")\">取消</a> ";
									return s + c;
								} else {
									if(row.status!=1){
										var e = "<a href=\"#\" onclick=\"editrow('tt',"
											+ index + ")\">编辑</a> ";
										var d = "<a href=\"#\" onclick=\"deleterow('tt',"
											+ index + ")\">删除</a> ";
										return e + d;
									}
								}
							}
						} ] ],
				toolbar : [ {
					text : '增加',
					iconCls : 'icon-add',
					handler : function(){
						addrow('tt');
					}
				}, {
					text : '保存',
					iconCls : 'icon-save',
					handler : function(){
						saveall('tt');
					}
				}, {
					text : '取消',
					iconCls : 'icon-cancel',
					handler : function(){
						cancelall('tt');
					}
				} , {
		            text: '上移', iconCls: 'icon-up', handler: function () {
		                MoveUp('tt');
		            }
		        }, {
		            text: '下移', iconCls: 'icon-down', handler: function () {
		                MoveDown('tt');
		            }
		        }],
				onBeforeEdit : function(index, row) {
					row.editing = true;
					$('#tt').datagrid('refreshRow', index);
					editcount++;
				},
				onAfterEdit : function(index, row) {
					row.editing = false;
					$('#tt').datagrid('refreshRow', index);
					editcount--;
				},
				onCancelEdit : function(index, row) {
					row.editing = false;
					$('#tt').datagrid('refreshRow', index);
					editcount--;
				}
			}).datagrid('loadData', datas).datagrid('acceptChanges');
}
var editcount = 0;
function editrow(id,index) {
	$('#'+id).datagrid('beginEdit', index);
}
function deleterow(id,index) {
	$.messager.confirm('确认', '是否真的删除?', function(r) {
		if (r) {
			$('#'+id).datagrid('deleteRow', index);
		}
	});
}
function saverow(id,index) {
	$('#'+id).datagrid('endEdit', index);
}
function cancelrow(id,index) {
	$('#'+id).datagrid('cancelEdit', index);
}
function addrow(id) {
	if (editcount > 0) {
		$.messager.alert('警告', '当前还有' + editcount + '记录正在编辑，不能增加记录。');
		return;
	}
	$('#'+id).datagrid('appendRow', {
		//no : '',
		//birthday : ''
	});
}
function saveall(id) {
	$('#'+id).datagrid('acceptChanges');
}
function cancelall(id) {
	$('#'+id).datagrid('rejectChanges');
}
function initSub(id,datas)  {
	$('#'+id).datagrid(
			{
				width:"auto",
				height:"auto",
				singleSelect : true,
				rownumbers:true,//行号
				columns : [ [
						{
							title : '数据库名',
							field : 'fieldname',
							width : 80,
							align : 'left',
							editor : 'text'
						},
						{
							title : '中文名称',
							field : 'fieldlabel',
							width : 120,
							align : 'left',
							editor : 'text'
						},
						{
							title : '字段类型',
							field : 'fieldtype',
							width : 60,
							align : 'left',
							editor : 'text'
						},
						{
							title : '字段长度',
							field : 'fieldsize',
							width : 50,
							align : 'left',
							editor : 'numberbox'
						},
						{
							title : '显示格式',
							field : 'showformat',
							width : 200,
							align : 'left',
							editor : 'text'
						},
						{
							title : '主键',
							field : 'isprimary',
							width : 40,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							title : '公开',
							field : 'ispublic',
							width : 40,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							title : '必填',
							field : 'isrequired',
							width : 40,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							title : '列表',
							field : 'islist',
							width : 40,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							title : '查询',
							field : 'isquery',
							width : 40,
							align : 'center',
							formatter:function(val,rec){
								if(val==0){
									val="";
								}
								return val;
							},
							editor : {
								type : 'checkbox',
								options : {
									on : '1',
									off : ''
								}
							}
						},
						{
							title : '外键字段',
							field : 'foreignkey',
							width : 40,
							align : 'left',
							editor : 'text'
						},
						{
							title : '关联表',
							field : 'foreigntable',
							width : 40,
							align : 'left',
							editor : 'text'
						},
						{
							field : 'action',
							title : '操作',
							width : 70,
							align : 'center',
							formatter : function(value, row, index) {
								if(row.isdesignshow==1||row.isdesignshow==2){
									return ;
								}
								if (row.editing) {
									var s = "<a href=\"#\" onclick=\"saverow('"+id+"',"
											+ index + ")\">保存</a> ";
									var c = "<a href=\"#\" onclick=\"cancelrow('"+id+"',"
										+ index + ")\">取消</a> ";
									return s + c;
								} else {
									var e = "<a href=\"#\" onclick=\"editrow('"+id+"',"
										+ index + ")\">编辑</a> ";
									var d = "<a href=\"#\" onclick=\"deleterow('"+id+"',"
										+ index + ")\">删除</a> ";
									return e + d;
								}
							}
						} ] ],
						toolbar : [ {
							text : '增加',
							iconCls : 'icon-add',
							handler : function(){
								addrow(id);
							}
						}, {
							text : '保存',
							iconCls : 'icon-save',
							handler : function(){
								saveall(id);
							}
						}, {
							text : '取消',
							iconCls : 'icon-cancel',
							handler : function(){
								cancelall(id);
							}
						}, {
				            text: '上移', iconCls: 'icon-up', handler: function () {
				                MoveUp(id);
				            }
				        }, {
				            text: '下移', iconCls: 'icon-down', handler: function () {
				                MoveDown(id);
				            }
				        } ],
				onBeforeEdit : function(index, row) {
					row.editing = true;
					$('#'+id).datagrid('refreshRow', index);
					editcount++;
				},
				onAfterEdit : function(index, row) {
					row.editing = false;
					$('#'+id).datagrid('refreshRow', index);
					editcount--;
				},
				onCancelEdit : function(index, row) {
					row.editing = false;
					$('#'+id).datagrid('refreshRow', index);
					editcount--;
				}
			}).datagrid('loadData', datas).datagrid('acceptChanges');
}
function MoveUp(id) {
    var row = $("#"+id).datagrid('getSelected');
    var index = $("#"+id).datagrid('getRowIndex', row);
    mysort(index, 'up', id);
     
}
//下移
function MoveDown(id) {
    var row = $("#"+id).datagrid('getSelected');
    var index = $("#"+id).datagrid('getRowIndex', row);
    mysort(index, 'down', id);
     
}
 
 
function mysort(index, type, gridname) {
    if ("up" == type) {
        if (index != 0) {
            var toup = $('#' + gridname).datagrid('getData').rows[index];
            var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
            $('#' + gridname).datagrid('getData').rows[index] = todown;
            $('#' + gridname).datagrid('getData').rows[index - 1] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index - 1);
            $('#' + gridname).datagrid('selectRow', index - 1);
        }
    } else if ("down" == type) {
        var rows = $('#' + gridname).datagrid('getRows').length;
        if (index != rows - 1) {
            var todown = $('#' + gridname).datagrid('getData').rows[index];
            var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
            $('#' + gridname).datagrid('getData').rows[index + 1] = todown;
            $('#' + gridname).datagrid('getData').rows[index] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index + 1);
            $('#' + gridname).datagrid('selectRow', index + 1);
        }
    }
 
}
function changeshow(i){
	var l_class=$('#legend'+i).attr("class");
	if(l_class=="titimg"){
		$('#legend'+i).attr("class","titimg2");
		$('#subdiv'+i).hide();
		$('#conBody'+i).hide();
	}else{
		$('#legend'+i).attr("class","titimg");
		$('#subdiv'+i).show();
		$('#conBody'+i).show();
	}
}
function myclose() {
	if (confirm('你确定要关闭当前窗口吗？')) {
		window.close();
	}
}
function up(){
	form1.action=CONTEXTPATH+'/private/form/toadd';
	form1.submit();
}
function preview(){
  	var url=CONTEXTPATH+'/private/form/preview?mid='+$("#formdesid").val()+'&sys_random=' + Math.random();
	var title='预览表单';
	var iWidth=1000; //弹出窗口的宽度;
	var iHeight=parent.document.body.clientHeight+100; //弹出窗口的高度;  
	openWin3(url,title,iWidth,iHeight);
}
