CKEDITOR.dialog.add("datepicker", function(editor){   
	return {
		title : '日历控件',    
        minWidth : 300,    
        minHeight : 150, 
		onShow: function() {
			 delete this.datepicker;
			 
	            var a = this.getParentEditor().getSelection().getSelectedElement();
	            alert(a);
	            a && a.is("input") && a.getAttribute("type")=='datepicker' && (this.datepicker = a, this.setupContent(a))
	    },
	    onOk:function(){
	    	editor.insertHtml('<script src='+CKEDITOR.plugins.get("datepicker").path+'my97/WdatePicker.js></script><style type="text/css">.Wdate{border: #999 1px solid;height: 20px;background: #fff url('+CKEDITOR.plugins.get("datepicker").path+'images/datePicker.gif) no-repeat right;}</style><input type="datepicker" id="d241" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" class="Wdate" style="width:150px"/>');
	    },
        contents : [
            {    
	            id: 'info',  
	            label: '日期空间',  
	            title: '日期空间',     
	                elements :[    
	                    {   
	                      id: 'date_picker',
	                      label: '日期空间',  
	                      title: '日期空间', 
	                      type: 'html',    
	                      html:'选择日期：<script>'+CKEDITOR.plugins.get("datepicker").path+'my97/WdatePicker.js</script><style type="text/css">.Wdate{border: #999 1px solid;height: 20px;background: #fff url('+CKEDITOR.plugins.get("datepicker").path+'images/datePicker.gif) no-repeat right;}</style><input type="datepicker" id="d241" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" class="Wdate" style="width:150px"/>', 
	                      "default": "",
			                accessKey: "N",
			                setup: function(a) {
			                    this.setValue(a.data("date_picker") || a.getAttribute("date_picker") || "")
			                },
			                commit: function(a) {
			                    a = a.element;
			                    this.getValue() ? a.data("date_picker", this.getValue()) : (a.data("date_picker", !1), a.removeAttribute("date_picker"))
			                }
	                    }    
	                ]    
            }    
       ]
	};
    /*return {     
        title : '日历控件',    
        minWidth : 300,    
        minHeight : 150,    
        contents : [    
            {    
            id: 'tab1',  
            label: '',  
            title: '',     
                elements :    
                [    
                    {      
                        type: 'html',    
                      html:'选择日期：<script>'+CKEDITOR.plugins.get("datepicker").path+'my97/WdatePicker.js</script><style type="text/css">.Wdate{border: #999 1px solid;height: 20px;background: #fff url('+CKEDITOR.plugins.get("datepicker").path+'images/datePicker.gif) no-repeat right;}</style><input type="text" id="d241" onfocus="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" class="Wdate" style="width:150px"/>'    
                    }    
                ]    
            }    
        ],  
        onOk: function() {
        	editor.insertHtml('<script src='+CKEDITOR.plugins.get("datepicker").path+'my97/WdatePicker.js></script><style type="text/css">.Wdate{border: #999 1px solid;height: 20px;background: #fff url('+CKEDITOR.plugins.get("datepicker").path+'images/datePicker.gif) no-repeat right;}</style><input type="text" id="d241" onfocus="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" class="Wdate" style="width:150px"/>');
        }
//        onOk: function(){  
//　　      		editor.insertHtml('<style type="text/css">.Wdate{border: #999 1px solid;height: 20px;background: #fff url('+CKEDITOR.plugins.get("datepicker").path+'/images/datePicker.gif) no-repeat right;}</style><input type="text" id="d241" onfocus="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" class="Wdate" style="width:150px"/>');  
//　　  	} 
    }   */ 
});    