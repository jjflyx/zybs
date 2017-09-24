/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	//设置默认语言
	config.language = 'zh-cn';
	//设置编辑器颜色
//	config.uiColor = '#AADC6E';
	// 换行方式
	 config.enterMode = CKEDITOR.ENTER_BR;
	 // 当输入：shift+Enter是插入的标签
	 config.shiftEnterMode = CKEDITOR.ENTER_BR;// 
	 config.startupFocus = true;
	//是否使用HTML实体进行输出 plugins/entities/plugin.js
	 config.entities = true;
	 //图片处理
	 config.pasteFromWordRemoveStyles = false;
	 config.filebrowserImageUploadUrl = "../img/ckeditorpic?type=image";
	//是否开启 图片和表格 的改变大小的功能
	 config.disableObjectResizing = true;
	//自定义日期选择器
 	 config.extraPlugins = 'datepicker';
 	 config.toolbar = 'Full';
	 //所有工具栏
	 config.toolbar_Full =
		 [
		         { name: 'document', items : [ 'Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates' ] },
		         { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
		         { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },
		         { name: 'forms', items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 
		         'HiddenField' ] },
		         '/',
		         { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
		         { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv',
		         '-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
		         { name: 'links', items : [ 'Link','Unlink','Anchor' ] },
		         { name: 'insert', items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe' ] },
		         '/',
		         { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
		         { name: 'colors', items : [ 'TextColor','BGColor' ] },
		         { name: 'tools', items : [ 'Maximize', 'ShowBlocks','-','About' ] },
		         { name: 'datepicker',items:['datepicker']}
		 ];
	
     	// The default plugins included in the basic setup define some buttons that
     	// we don't want too have in a basic editor. We remove them here.
     	config.removeButtons = 'Cut,Copy,Paste,Undo,Redo,Anchor,Underline,Strike,Subscript,Superscript';

     	// Let's have it basic on dialogs as well.
     	config.removeDialogTabs = 'link:advanced';
     	//防止 自定义属性添加后，点击查看源码在返回属性消失问题
     	config.allowedContent=true;
     	
};
CKEDITOR.scriptLoader.load(CKEDITOR.getUrl( 'lht/zh-cn.js' ));
/**
 * 给ckeditor编辑区添加事件
 * 
 * 
 * $('.ckeditor').attr('id')  页面加载时获得 ckeditor的Id
 */

//CKEDITOR.instances[$('.ckeditor').attr('id')].on("instanceReady", function () {  
	 
    //set keyup event  
//    this.document.on("keyup", KeyUp);  
    //and click event  
   // this.document.on("click", AutoSave);  
    //and select event  
    //this.document.on("select", AutoSave);  
//}); 
/**
 * 调用keyup 事件
 * @return
 */
function KeyUp() {  

	var content =  CKEDITOR.instances.content.getData();
	
//	 if(content.length==0){
//	     	ckLightHelp($('.ckeditor'), true);
//	     	hasError = true;
//	    }else{
//     	    ckLightHelp($('.ckeditor'), false);
//	  }
}     
