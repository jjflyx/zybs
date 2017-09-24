 /*
 Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.md or http://ckeditor.com/license
*/
CKEDITOR.dialog.add("textfield",
function(b) {
    function e(a) {
        var a = a.element,
        c = this.getValue();
        c ? a.setAttribute(this.id, c) : a.removeAttribute(this.id)
    }
    function f(a) {
        this.setValue(a.hasAttribute(this.id) && a.getAttribute(this.id) || "")
    }
    var g = {
        email: 1,
        password: 1,
        search: 1,
        tel: 1,
        text: 1,
        url: 1
    };
    return {
        title: b.lang.forms.textfield.title,
        minWidth: 350,
        minHeight: 150,
        onShow: function() {
            delete this.textField;
            var a = this.getParentEditor().getSelection().getSelectedElement();
            if (a && "input" == a.getName() && (g[a.getAttribute("type")] || !a.getAttribute("type"))) this.textField = a,
            this.setupContent(a)
        },
        onOk: function() {
            var a = this.getParentEditor(),
            c = this.textField,
            b = !c;
            b && (c = a.document.createElement("input"), c.setAttribute("type", "text"));
            c = {
                element: c
            };
            b && a.insertElement(c.element);
            this.commitContent(c);
            b || a.getSelection().selectElement(c.element)
        },
        onLoad: function() {
            this.foreach(function(a) {
                if (a.getValue && (a.setup || (a.setup = f), !a.commit)) a.commit = e
            })
        },
        contents: [{
            id: "info",
            label: b.lang.forms.textfield.title,
            title: b.lang.forms.textfield.title,
            elements: [{
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: b.lang.commonfield.fieldname,//标签属性 [name]
                    type: "text",
                    label: b.lang.forms.textfield.name,
                    "default": "",
                    accessKey: "N",
                    setup: function(a) {
                        this.setValue(a.getAttribute(b.lang.commonfield.fieldname) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(b.lang.commonfield.fieldname, this.getValue()) : a.removeAttribute("name")
                    }
                },
                {
                    id: b.lang.commonfield.labelname,//标签属性 [label_name]
                    type: "text",
                    label: b.lang.common.textlabel,
                    validate: CKEDITOR.dialog.validate.notEmpty(b.lang.common.textlabel + b.lang.common.invalidValueNull),
                    "default": "",
                    accessKey: "N",
                    setup: function(a) {
                        this.setValue(a.getAttribute(b.lang.commonfield.labelname) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(b.lang.commonfield.labelname, this.getValue()) : a.removeAttribute(b.lang.commonfield.labelname)
                    }
                }]
            },
            {
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: b.lang.commonfield.fieldvalue,//标签属性[value]
                    type: "text",
                    label: b.lang.forms.textfield.value,
                    "default": "",
                    accessKey: "V",
                    commit: function(a) {
                        if (CKEDITOR.env.ie && !this.getValue()) {
                            var c = a.element,
                            d = new CKEDITOR.dom.element("input", b.document);
                            c.copyAttributes(d, {
                                value: 1
                            });
                            d.replace(c);
                            a.element = d
                        } else e.call(this, a)
                    }
                },
                {
                    id: b.lang.commonfield.validformat,//标签属性[valid_format]
                    type: "text",
                    label: b.lang.common.invalidReg,
                    "default": "",
                    accessKey: "R",
                    style: "width:100%",
                    validate: CKEDITOR.dialog.validate.notEmpty(b.lang.common.invalidReg + b.lang.common.invalidValueNull),
                    setup: function(a) {
                        this.setValue(a.hasAttribute(b.lang.commonfield.validformat) && a.getAttribute(b.lang.commonfield.validformat) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(b.lang.commonfield.validformat, this.getValue()) : a.removeAttribute("data_format")
                    }
                }]
            },
            {
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: b.lang.commonfield.datatype,//数据类型[datatype]
                    type: "select",
                    label: b.lang.common.datatype,
                    items: [['字符', 'varchar2'], ['文本', 'text'], ['int型', 'int'], ['Number型', 'number'], ['clob', 'clob']],
                    "default": "varchar2",
                    accessKey: "A",
                    style: "width:100%",
                    //              	        	   validate: CKEDITOR.dialog.validate.integer(b.lang.common.invalidValue),
                    setup: function(a) {
                        this.setValue(a.hasAttribute(b.lang.commonfield.datatype) && a.getAttribute(b.lang.commonfield.datatype) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(b.lang.commonfield.datatype, this.getValue()) : a.removeAttribute(b.lang.commonfield.datatype)
                    }
                },
                {
                    id: b.lang.commonfield.datelength,//字符宽度[datelength]
                    type: "text",
                    label: b.lang.forms.textfield.charWidth,
                    "default": "",
                    accessKey: "C",
                    style: "width:100%x",
                    validate: CKEDITOR.dialog.validate.integer(b.lang.common.validateNumberFailed)
                }]
            },
            {
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: "type",
                    type: "select",
                    label: b.lang.forms.textfield.type,
                    "default": "text",
                    accessKey: "M",
                    items: [[b.lang.forms.textfield.typeEmail, "email"], [b.lang.forms.textfield.typePass, "password"], [b.lang.forms.textfield.typeSearch, "search"], [b.lang.forms.textfield.typeTel, "tel"], [b.lang.forms.textfield.typeText, "text"], [b.lang.forms.textfield.typeUrl, "url"]],
                    setup: function(a) {
                        this.setValue(a.getAttribute("type"))
                    },
                    commit: function(a) {
                        var c = a.element;
                        if (CKEDITOR.env.ie) {
                            var d = c.getAttribute("type"),
                            e = this.getValue();
                            d != e && (d = CKEDITOR.dom.element.createFromHtml('<input type="' + e + '"></input>', b.document), c.copyAttributes(d, {
                                type: 1
                            }), d.replace(c), a.element = d)
                        } else c.setAttribute("type", this.getValue())
                    }
                },
                {
                    id: b.lang.commonfield.maxlength,//标签属性[maxLength]
                    type: "text",
                    label: b.lang.forms.textfield.maxChars,
                    "default": "",
                    accessKey: "M",
                    style: "width:100%",
                    validate: CKEDITOR.dialog.validate.integer(b.lang.common.validateNumberFailed)
                }],
                onLoad: function() {
                    CKEDITOR.env.ie7Compat && this.getElement().setStyle("zoom", "100%")
                }
            },
            {
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: b.lang.commonfield.is_primary,//标签属性[is_primary]
                    type: "checkbox",
                    label: b.lang.common.isprimary,
                    "default": "",
                    accessKey: "N",
                    setup: function(a) {
                        this.setValue(a.getAttribute(b.lang.commonfield.is_primary) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(b.lang.commonfield.is_primary, this.getValue()) :  a.removeAttribute(b.lang.commonfield.is_primary)
                    }
                },
                {
                    id: b.lang.commonfield.is_null,//标签属性[is_null]
                    type: "checkbox",
                    label: b.lang.common.isnotnull,
                    "default": "",
                    accessKey: "N",
                    setup: function(a) {
                        this.setValue(a.getAttribute(b.lang.commonfield.is_null) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(b.lang.commonfield.is_null, this.getValue()) : a.removeAttribute(b.lang.commonfield.is_null)
                    }
                }]
            }]
        }]
    }
});