 /*
 Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.md or http://ckeditor.com/license
*/
CKEDITOR.dialog.add("checkbox",
function(d) {
    return {
        title: d.lang.forms.checkboxAndRadio.checkboxTitle,
        minWidth: 350,
        minHeight: 140,
        onShow: function() {
            delete this.checkbox;
            var a = this.getParentEditor().getSelection().getSelectedElement();
            a && "checkbox" == a.getAttribute("type") && (this.checkbox = a, this.setupContent(a))
        },
        onOk: function() {
            var a, b = this.checkbox;
            b || (a = this.getParentEditor(), b = a.document.createElement("input"), b.setAttribute("type", "checkbox"), a.insertElement(b));
            this.commitContent({
                element: b
            })
        },
        contents: [{
            id: "info",
            label: d.lang.forms.checkboxAndRadio.checkboxTitle,
            title: d.lang.forms.checkboxAndRadio.checkboxTitle,
            startupFocus: d.lang.commonfield.fieldname,//初始化显示焦点
            elements: [{
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: d.lang.commonfield.fieldname,//标签属性 [name]
                    type: "text",
                    label: d.lang.common.name,
                    "default": "",
                    accessKey: "N",
                    setup: function(a) {
                        this.setValue(a.getAttribute(d.lang.commonfield.fieldname) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(d.lang.commonfield.fieldname, this.getValue()) : a.removeAttribute(d.lang.commonfield.fieldname)
                    }
                },
                {
                    id: d.lang.commonfield.labelname,//标签属性 [label_name]
                    type: "text",
                    label: d.lang.common.textlabel,
                    validate: CKEDITOR.dialog.validate.notEmpty(d.lang.common.textlabel + d.lang.common.invalidValueNull),
                    "default": "",
                    accessKey: "N",
                    setup: function(a) {
                        this.setValue(a.getAttribute(d.lang.commonfield.labelname) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(d.lang.commonfield.labelname, this.getValue()) : a.removeAttribute(d.lang.commonfield.labelname)
                    }
                }]
            },
            {
                id: d.lang.commonfield.fieldvalue,//标签属性 [value]
                type: "text",
                label: d.lang.forms.checkboxAndRadio.value,
                "default": "",
                accessKey: "V",
                setup: function(a) {
                    a = a.getAttribute(d.lang.commonfield.fieldvalue);
                    this.setValue(CKEDITOR.env.ie && "on" == a ? "": a)
                },
                commit: function(a) {
                    var b = a.element,
                    c = this.getValue();
                    c && !(CKEDITOR.env.ie && "on" == c) ? b.setAttribute(d.lang.commonfield.fieldvalue, c) : CKEDITOR.env.ie ? (c = new CKEDITOR.dom.element("input", b.getDocument()), b.copyAttributes(c, {
                        value: 1
                    }), c.replace(b), d.getSelection().selectElement(c), a.element = c) : b.removeAttribute(d.lang.commonfield.fieldvalue)
                }
            },
            {
                id: "cmbSelected",
                type: "checkbox",
                label: d.lang.forms.checkboxAndRadio.selected,
                "default": "",
                accessKey: "S",
                value: "checked",
                setup: function(a) {
                    this.setValue(a.getAttribute("checked"))
                },
                commit: function(a) {
                    var b = a.element;
                    if (CKEDITOR.env.ie) {
                        var c = !!b.getAttribute("checked"),
                        e = !!this.getValue();
                        c != e && (c = CKEDITOR.dom.element.createFromHtml('<input type="checkbox"' + (e ? ' checked="checked"': "") + "/>", d.document), b.copyAttributes(c, {
                            type: 1,
                            checked: 1
                        }), c.replace(b), d.getSelection().selectElement(c), a.element = c)
                    } else this.getValue() ? b.setAttribute("checked", "checked") : b.removeAttribute("checked")
                }
            }]
        }]
    }
});
