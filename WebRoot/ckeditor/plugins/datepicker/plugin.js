CKEDITOR.plugins.add('datepicker',{           
        requires : ['dialog'],    
        init : function (editor){    
            var pluginName = 'datepicker';    
            //加载自定义窗口  
            CKEDITOR.dialog.add('datepicker',this.path + "dialogs/datepicker.js");
            //给自定义插件注册一个调用命令    
            editor.addCommand( pluginName, new CKEDITOR.dialogCommand( 'datepicker' ) );    
            //注册一个按钮，来调用自定义插件    
            editor.ui.addButton('datepicker',{    
                //editor.lang.mine是在zh-cn.js中定义的一个中文项，    
                //这里可以直接写英文字符，不过要想显示中文就得修改zh-cn.js    
                label :editor.lang.common.datepicker,    
                command : pluginName,
                icon:this.path + 'images/datePicker.gif'
            });
            //为编辑区的文本框双击事件绑定一个事件，即显示弹出窗  
            editor.on( 'doubleclick', function( evt ) {  
                var element = evt.data.element;  
                 if (element.is('input')) {  
                    var type = element.getAttribute('type') || 'datepicker';  
                    if (type=='datepicker'){  
                        evt.data.dialog =pluginName;  
                    }  
                }  
            })
        }
    }); 
//加载my97日历空间js，在dialog页面可以点击选择日期
CKEDITOR.scriptLoader.load( CKEDITOR.plugins.get("datepicker").path+'my97/WdatePicker.js' );
