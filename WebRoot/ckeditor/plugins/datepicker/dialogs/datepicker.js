CKEDITOR.dialog.add("datepicker",
function(editor) {

    return {
        title: editor.lang.common.datepicker,
        minWidth: 380,
        minHeight: 150,
        onShow: function() {
            delete this.datepicker;
            var a = this.getParentEditor().getSelection().getSelectedElement();
            a && a.is("input") && a.getAttribute("type") == 'datepicker' && (this.datepicker = a, this.setupContent(a))
        },
        onOk: function() {
            var a = this.getParentEditor(),
            c = this.datepicker,
            b = !c;
            b && (c = a.document.createElement("input"), c.setAttribute("type", "datepicker"), c.setAttribute("class", "Wdate1"), c.setAttribute("onclick", "WdatePicker()"), c.setAttribute("readonly", "readonly"));
            c = {
                element: c
            };
            b && a.insertElement(c.element);
            this.commitContent(c);
            b || a.getSelection().selectElement(c.element)
        },
        contents: [{
            id: 'info',
            label: editor.lang.common.datepicker,
            title: editor.lang.common.datepicker,
            elements: [{
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: editor.lang.commonfield.fieldname,//标签属性 [name]
                    type: "text",
                    label: editor.lang.forms.textfield.name,
                    "default": "",
                    accessKey: "V",
                    style: "width:100%",
                    setup: function(a) {
                        this.setValue(a.getAttribute(editor.lang.commonfield.fieldname) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(editor.lang.commonfield.fieldname, this.getValue()) : a.removeAttribute(editor.lang.commonfield.fieldname)
                    }
                },
                {
                    id: editor.lang.commonfield.labelname,//标签属性 [labelname]
                    type: "text",
                    label: editor.lang.common.textlabel,
                    validate: CKEDITOR.dialog.validate.notEmpty(editor.lang.common.textlabel + editor.lang.common.invalidValueNull),
                    "default": "",
                    accessKey: "N",
                    style: "width:100%",
                    setup: function(a) {
                        this.setValue(a.getAttribute(editor.lang.commonfield.labelname) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(editor.lang.commonfield.labelname, this.getValue()) : a.removeAttribute(editor.lang.commonfield.labelname)
                    }
                }]
            },
            {
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: editor.lang.commonfield.timeformat,//标签属性，日期格式[]
                    type: "select",
                    label: editor.lang.common.timeformat,
                    items: [['yyyy-MM-dd', 'yyyy-MM-dd'], ['yyyy-MM-dd HH:mm:ss', 'yyyy-MM-dd HH:mm:ss']],
                    "default": "yyyy-MM-dd",
                    accessKey: "V",
                    style: "width:100%",
                    setup: function(a) {
                        this.setValue(a.getAttribute(editor.lang.commonfield.timeformat) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(editor.lang.commonfield.timeformat, this.getValue()) : a.removeAttribute(editor.lang.commonfield.timeformat)
                    }
                },
                {
                    id: editor.lang.commonfield.validformat,//标签属性，正则表达式[validformat]
                    type: "select",
                    label: editor.lang.common.invalidReg,
                    items: [['任何字符', 'Any'], ['不为空', 'NotNull'], ['数字', 'Number'], ['整数', 'IsInt'], ['日期', 'DateTime'], ['电子邮件', 'Email'], ['手机号码', 'isPhone'], ['密码', 'PASSWORD']],
                    "default": "Any",
                    accessKey: "A",
                    style: "width:100%",
                    setup: function(a) {
                        this.setValue(a.hasAttribute(editor.lang.commonfield.validformat) && a.getAttribute(editor.lang.commonfield.validformat) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(editor.lang.commonfield.validformat, this.getValue()) : a.removeAttribute(editor.lang.commonfield.validformat)
                    }
                }]
            },
            {
                type: "hbox",
                widths: ["50%", "50%"],
                children: [{
                    id: editor.lang.commonfield.is_null,//标签属性是否可为空
                    type: "checkbox",
                    label: editor.lang.common.isnotnull,
                    "default": "",
                    accessKey: "N",
                    setup: function(a) {
                        this.setValue(a.getAttribute(editor.lang.commonfield.is_null) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(editor.lang.commonfield.is_null, this.getValue()) : a.removeAttribute(editor.lang.commonfield.is_null)
                    }
                },
                {
                    id: editor.lang.commonfield.istoday,//日期是否显示今天
                    type: "checkbox",
                    label: editor.lang.common.istoday,
                    "default": "",
                    accessKey: "N",
                    setup: function(a) {
                        this.setValue(a.getAttribute(editor.lang.commonfield.istoday) || "")
                    },
                    commit: function(a) {
                        a = a.element;
                        this.getValue() ? a.setAttribute(editor.lang.commonfield.istoday, this.getValue()) : a.removeAttribute(editor.lang.commonfield.istoday)
                    }
                }]
            }

            ]
        }]
    };
});