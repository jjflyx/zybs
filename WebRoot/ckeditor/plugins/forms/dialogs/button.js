 /*
 Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.md or http://ckeditor.com/license
*/
CKEDITOR.dialog.add("button",
function(b) {
    function d(a) {
        var b = this.getValue();
        b ? (a.attributes[this.id] = b, "name" == this.id && (a.attributes["data-cke-saved-name"] = b)) : (delete a.attributes[this.id], "name" == this.id && delete a.attributes["data-cke-saved-name"])
    }
    return {
        title: b.lang.forms.button.title,
        minWidth: 350,
        minHeight: 150,
        onShow: function() {
            delete this.button;
            var a = this.getParentEditor().getSelection().getSelectedElement();
            a && a.is("input") && a.getAttribute("type") in {
                button: 1,
                reset: 1,
                submit: 1
            } && (this.button = a, this.setupContent(a))
        },
        onOk: function() {
            var a = this.getParentEditor(),
            b = this.button,
            d = !b,
            c = b ? CKEDITOR.htmlParser.fragment.fromHtml(b.getOuterHtml()).children[0] : new CKEDITOR.htmlParser.element("input");
            this.commitContent(c);
            var e = new CKEDITOR.htmlParser.basicWriter;
            c.writeHtml(e);
            c = CKEDITOR.dom.element.createFromHtml(e.getHtml(), a.document);
            d ? a.insertElement(c) : (c.replace(b), a.getSelection().selectElement(c))
        },
        contents: [{
            id: "info",
            label: b.lang.forms.button.title,
            title: b.lang.forms.button.title,
            elements: [

            {
                id: b.lang.commonfield.fieldname,//标签属性 [name]
                type: "text",
                label: b.lang.common.name,
                "default": "",
                setup: function(a) {
                    this.setValue(a.getAttribute(b.lang.commonfield.fieldname) || "")
                },
                commit: d
            },
            {
                id:  b.lang.commonfield.fieldvalue,//标签属性 [value]
                type: "text",
                label: b.lang.forms.button.text,
                accessKey: "V",
                "default": "",
                setup: function(a) {
                    this.setValue(a.getAttribute(b.lang.commonfield.fieldvalue) || "")
                },
                commit: d
            },
            {
                id: b.lang.commonfield.type,//标签属性 [type]
                type: "select",
                label: b.lang.forms.button.type,
                "default": "button",
                accessKey: "T",
                items: [[b.lang.forms.button.typeBtn, "button"], [b.lang.forms.button.typeSbm, "submit"], [b.lang.forms.button.typeRst, "reset"]],
                setup: function(a) {
                    this.setValue(a.getAttribute(b.lang.commonfield.type) || "")
                },
                commit: d
            }]
        }]
    }
});
