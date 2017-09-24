 /*
 Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.md or http://ckeditor.com/license
*/
CKEDITOR.dialog.add("textarea", function(b) {
    return {
    	title: b.lang.forms.textarea.title,
    	minWidth: 350,
    	minHeight: 220,
    	onShow: function() {
            delete this.textarea;
            var a = this.getParentEditor().getSelection().getSelectedElement();
            a && "textarea" == a.getName() && (this.textarea = a, this.setupContent(a))
        },
        onOk: function() {
            var a, b = this.textarea, c = !b;
            c && (a = this.getParentEditor(), b = a.document.createElement("textarea"));
            this.commitContent(b);
            c && a.insertElement(b)
        },
        contents: [
                     {
                    	 id: "info",
                    	 label: b.lang.forms.textarea.title,
                    	 title: b.lang.forms.textarea.title,
                    	 elements: [
									{
										type: "hbox",
										widths: ["50%", "50%"],
										children: [
										           {id: b.lang.commonfield.fieldname,//标签属性[name]
										        	   type: "text",
										        	   label: b.lang.common.name,
										        	   "default": "",
										        	   accessKey: "C",
										        	   style: "width:100%",
										        	   validate: CKEDITOR.dialog.validate.notEmpty(b.lang.common.name+b.lang.common.invalidValueNull),
									                    setup: function(a) {
									                    	this.setValue(a.hasAttribute(b.lang.commonfield.fieldname) && a.getAttribute(b.lang.commonfield.fieldname) || "")
									                    },
									                    commit: function(a) {
									                    	this.getValue() ? a.setAttribute(b.lang.commonfield.fieldname, this.getValue()) : a.removeAttribute(b.lang.commonfield.fieldname)
									                    }
									                },
									                {
									                	id: b.lang.commonfield.fieldname,//标签属性[label_name]
									                	type: "text",
									                	label: b.lang.common.textlabel,
									                	"default": "",
									                	accessKey: "R",
									                	style: "width:100%",
									                	validate: CKEDITOR.dialog.validate.notEmpty(b.lang.common.textlabel+b.lang.common.invalidValueNull),
									                	setup: function(a) {
									                		this.setValue(a.hasAttribute(b.lang.commonfield.fieldname) && a.getAttribute(b.lang.commonfield.fieldname) || "")
									                    },
									                    commit: function(a) {
									                        this.getValue() ? a.setAttribute(b.lang.commonfield.fieldname,this.getValue()) : a.removeAttribute(b.lang.commonfield.fieldname)
									                    }
									                }
									               ]
									},
									{
			                        	type: "hbox",
			                        	widths: ["50%", "50%"],
			                        	children: [
			                        	           {id: b.lang.commonfield.datatype,//字段类型[datatype]
			                        	        	   type: "select",
			                        	        	   label: b.lang.common.datatype,
			                        	        	   items: [ ['字符', 'varchar2' ], [ '文本','text' ], [ 'int型','int' ], [ 'Number型','number' ],['clob','clob'] ],
			                        	        	   "default": "varchar2",
			                        	        	   accessKey: "A",
			                        	        	   style: "width:100%",
//			                        	        	   validate: CKEDITOR.dialog.validate.integer(b.lang.common.invalidValue),
							                            setup: function(a) {
							                            	this.setValue(a.hasAttribute(b.lang.commonfield.datatype) && a.getAttribute(b.lang.commonfield.datatype) || "")
							                            },
							                            commit: function(a) {
							                            	this.getValue() ? a.setAttribute(b.lang.commonfield.datatype, this.getValue()) : a.removeAttribute(b.lang.commonfield.datatype)
							                            }
						                            },
						                            {
						                            	id: b.lang.commonfield.datatype,//字符长度
						                            	type: "text",
						                            	label: b.lang.common.datalength,
						                            	"default": "",
						                            	accessKey: "B",
						                            	style: "width:100%",
						                            	validate: CKEDITOR.dialog.validate.integer(b.lang.common.validateNumberFailed),
						                            	setup: function(a) {
						                            		this.setValue(a.hasAttribute(b.lang.commonfield.datatype) && a.getAttribute(b.lang.commonfield.datatype) || "")
							                            },
							                            commit: function(a) {
								                            this.getValue() ? a.setAttribute(b.lang.commonfield.datatype, this.getValue()) : a.removeAttribute(b.lang.commonfield.datatype)
							                            }
						                            }
						                           ]
			                        },
			                        {
			                        	type: "hbox",
			                        	widths: ["50%", "50%"],
			                        	children: [
			                        	           {id: b.lang.commonfield.clos,//列数[clos]
			                        	        	   type: "text",
			                        	        	   label: b.lang.forms.textarea.cols,
			                        	        	   "default": "",
			                        	        	   accessKey: "C",
			                        	        	   style: "width:100%",
			                        	        	   validate: CKEDITOR.dialog.validate.integer(b.lang.common.validateNumberFailed),
							                            setup: function(a) {
							                            	this.setValue(a.hasAttribute(b.lang.commonfield.clos) && a.getAttribute(b.lang.commonfield.clos) || "")
							                            },
							                            commit: function(a) {
							                            	this.getValue() ? a.setAttribute(b.lang.commonfield.clos, this.getValue()) : a.removeAttribute(b.lang.commonfield.clos)
							                            }
						                            },
						                            {
						                            	id:  b.lang.commonfield.rows,//行数数[rows]
						                            	type: "text",
						                            	label: b.lang.forms.textarea.rows,
						                            	"default": "",
						                            	accessKey: "R",
						                            	style: "width:100%",
						                            	validate: CKEDITOR.dialog.validate.integer(b.lang.common.validateNumberFailed),
						                            	setup: function(a) {
						                            		this.setValue(a.hasAttribute(b.lang.commonfield.rows) && a.getAttribute(b.lang.commonfield.rows) || "")
							                            },
							                            commit: function(a) {
								                            this.getValue() ? a.setAttribute(b.lang.commonfield.rows, 
								                            this.getValue()) : a.removeAttribute(b.lang.commonfield.rows)
							                            }
						                            }
						                           ]
			                        }, 
			                        {
			                        	id: "value",
			                        	type: "textarea",
			                        	label: b.lang.forms.textfield.value,
			                        	"default": "",
			                        	setup: function(a) {
			                        		this.setValue(a.$.defaultValue)
				                        },
				                        commit: function(a) {
				                            a.$.value = a.$.defaultValue = this.getValue()
				                        }
			                        },{
                    	            	id: b.lang.commonfield.is_null,//是否为空
                    	            	type: "checkbox",
                    	            	label: b.lang.common.isnotnull,
                    	            	"default": "",
                    	            	accessKey: "N",
                    	            	setup: function(a) {
			                            this.setValue(a.getAttribute(b.lang.commonfield.is_null) || "")
				                        },
				                        commit: function(a) {
				                        	this.getValue() ? a.setAttribute(b.lang.commonfield.is_null,this.getValue()) : a.removeAttribute(b.lang.commonfield.is_null)
				                        }
			                        }
			                        ]
                     }
                     ]
        }
});
