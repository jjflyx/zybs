 /*
 Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.md or http://ckeditor.com/license
*/
CKEDITOR.dialog.add("hiddenfield",
function(d) {
    return {
        title: d.lang.forms.hidden.title,
        hiddenField: null,
        minWidth: 350,
        minHeight: 110,
        onShow: function() {
            delete this.hiddenField;
            var a = this.getParentEditor(),
            b = a.getSelection(),
            c = b.getSelectedElement();
            c && (c.data("cke-real-element-type") && "hiddenfield" == c.data("cke-real-element-type")) && (this.hiddenField = c, c = a.restoreRealElement(this.hiddenField), this.setupContent(c), b.selectElement(this.hiddenField))
        },
        onOk: function() {
            var a, b = this.hiddenfield,
            c = !b;
            c && (a = this.getParentEditor(), b = a.document.createElement("input"));
            b.setAttribute("type", "hidden");

            this.commitContent(b);

            a.createFakeElement(b, "cke_hidden", "hiddenfield");
            this.hiddenField ? (a.replace(this.hiddenField), a.getSelection().selectElement(b)) : a.insertElement(b);
            c && a.insertElement(b)

            /* 
            * 自带代码有错误
            * var a = this.getValueOf("info", "_cke_saved_name");
            alert(typeof(a));
            var b = this.getParentEditor(),
            a = 8 > CKEDITOR.document.$.documentMode ? '<input name="' + CKEDITOR.tools.htmlEncode(a) + '">': "input",
            a = CKEDITOR.env.ie && b.document.createElement(a);
            
            a.setAttribute("type", "hidden");
            this.commitContent(a);
            a = b.createFakeElement(a, "cke_hidden", "hiddenfield");
            this.hiddenField ? (a.replace(this.hiddenField), b.getSelection().selectElement(a)) : b.insertElement(a);
            return ! 0*/

        },
        contents: [{
            id: "info",
            label: d.lang.forms.hidden.title,
            title: d.lang.forms.hidden.title,
            elements: [{
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: d.lang.commonfield.fieldname,//标签属性 [name]
                    type: "text",
                    label: d.lang.forms.hidden.name,
                    validate: CKEDITOR.dialog.validate.notEmpty(d.lang.forms.hidden.name + d.lang.common.invalidValueNull),
                    "default": "",
                    accessKey: "N",
                    setup: function(a) {
                        this.setValue(a.getAttribute(d.lang.commonfield.fieldname) || "")
                    },
                    commit: function(a) {
                        this.getValue() ? a.setAttribute(d.lang.commonfield.fieldname, this.getValue()) : a.removeAttribute(d.lang.commonfield.fieldname)
                    }
                },
                {
                    id: d.lang.commonfield.labelname,//标签属性 [label_name]
                    type: "text",
                    label: d.lang.common.textlabel,
                    "default": "",
                    accessKey: "R",
                    style: "width:100%",
                    validate: CKEDITOR.dialog.validate.notEmpty(d.lang.common.textlabel + d.lang.common.invalidValueNull),
                    setup: function(a) {
                        this.setValue(a.hasAttribute(d.lang.commonfield.labelname) && a.getAttribute(d.lang.commonfield.labelname) || "")
                    },
                    commit: function(a) {
                        this.getValue() ? a.setAttribute(d.lang.commonfield.labelname, this.getValue()) : a.removeAttribute(d.lang.commonfield.labelname)
                    }
                }]
            },
            {
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: d.lang.commonfield.datatype,//标签 字符类型
                    type: "select",
                    label: d.lang.common.datatype,
                    items: [['字符', 'varchar2'], ['文本', 'text'], ['int型', 'int'], ['Number型', 'number'], ['clob', 'clob']],
                    "default": "varchar2",
                    accessKey: "A",
                    style: "width:100%",
                    //            	        	   validate: CKEDITOR.dialog.validate.integer(b.lang.common.invalidValue),
                    setup: function(a) {
                        this.setValue(a.hasAttribute(d.lang.commonfield.datatype) && a.getAttribute(d.lang.commonfield.datatype) || "")
                    },
                    commit: function(a) {
                        this.getValue() ? a.setAttribute(d.lang.commonfield.datatype, this.getValue()) : a.removeAttribute(d.lang.commonfield.datatype)
                    }
                },
                {
                    id: d.lang.commonfield.datelength,//标签 字符类型[datelength]
                    type: "text",
                    label: d.lang.common.datalength,
                    "default": "",
                    accessKey: "B",
                    style: "width:100%",
                    validate: CKEDITOR.dialog.validate.integer(d.lang.common.validateNumberFailed),
                    setup: function(a) {
                        this.setValue(a.hasAttribute(d.lang.commonfield.datelength) && a.getAttribute(d.lang.commonfield.datelength) || "")
                    },
                    commit: function(a) {
                        this.getValue() ? a.setAttribute(d.lang.commonfield.datelength, this.getValue()) : a.removeAttribute(d.lang.commonfield.datelength)
                    }
                }]
            },
            {
                id: d.lang.commonfield.fieldvalue,//标签 字符类型[value]
                type: "text",
                label: d.lang.forms.hidden.value,
                "default": "",
                accessKey: "V",
                setup: function(a) {
                    this.setValue(a.getAttribute(d.lang.commonfield.fieldvalue) || "")
                },
                commit: function(a) {
                    this.getValue() ? a.setAttribute(d.lang.commonfield.fieldvalue, this.getValue()) : a.removeAttribute(d.lang.commonfield.fieldvalue)
                }
            }]
        }]
    }
});
