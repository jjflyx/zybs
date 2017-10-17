var editor;
$(function() {
	customHeightSet();
	var a = $(document.body).height();
	editor = new baidu.editor.ui.Editor({
		minFrameHeight : a - 210
	});
	editor.render("definehtml");
});
function baocun(){
	$("#queryhtml").val(encode64(editor.getContent()));
	var req=new MyJqueryAjax($("#form1").attr("action"),$("#form1").serialize(),null,null).request();
	if(req=="true"){ 
		hideLoading();
			Dialog.alert("保存成功！",function (){
				refreshloadDataTab();
			});
	}else{
		hideLoading();
		Dialog.alert("保存失败！");
	}
}
function htmlDecode(a) {
	return a.replace(/\&quot\;/g, '"').replace(/\&\#39\;/g, "'");
}
function toquery(){
	form1.action=CONTEXTPATH+'/private/form/toquery';
	form1.submit();
}
function myclose() {
	if (confirm('你确定要关闭当前窗口吗？')) {
		window.close();
	}
}