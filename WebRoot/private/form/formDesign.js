var editor;
$(function() {
	customHeightSet();
	var a = $(document.body).height();
	editor = new baidu.editor.ui.Editor({
		minFrameHeight : a - 210
	});
	editor.render("form_define");
	//valid = $("#formDefForm").form();
	/*$("#btnPreView").click(function() {
		preView();
	});
	$("#btnNext").click(function() {
		nextStep();
	});
	$("#btnSaveForm").click(function() {
		saveForm();
	});*/
});
function next(){
	if (Verify.hasError()) {
		return;
	}
	$("#formhtml").val(editor.getContent());
	if($("#formhtml").val()==""){
		Dialog.alert("请输入表单内容");
	}else{
		$("#formhtml").val(encode64($("#formhtml").val()));
		form1.submit();
	}
}
function htmlDecode(a) {
	return a.replace(/\&quot\;/g, '"').replace(/\&\#39\;/g, "'");
}
function preview(){
	if(editor.getContent()==""){
		Dialog.alert("请输入表单内容");
	}else{
		$("#preformhtml").val(editor.getContent());
		/*var url=CONTEXTPATH+'/private/form/preview';
		var title='预览表单';
		var iWidth=1000; //弹出窗口的宽度;
		var iHeight=parent.document.body.clientHeight+100; //弹出窗口的高度;  
		window.open('','newWin','width='+iWidth+',height='+iHeight+',scrollbars=yes');*/
		preform.submit();
	}
}
function myclose() {
	if (confirm('你确定要关闭当前窗口吗？')) {
		window.close();
	}
}