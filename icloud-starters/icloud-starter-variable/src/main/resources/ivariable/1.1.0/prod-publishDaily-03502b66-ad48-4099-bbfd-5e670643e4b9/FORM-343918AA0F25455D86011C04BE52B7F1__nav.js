typeof __loadSchema === 'function' && __loadSchema({
    "content": {
        "schemaType": "superform",
        "schemaVersion": "5.0",
        "pages": [{
            "utils": [{
                "name": "fusion",
                "type": "npm",
                "content": {"package": "@ali/vu-fusion", "version": "2.0.2", "exportName": "fusion"}
            }, {
                "name": "router",
                "type": "npm",
                "content": {"package": "@ali/vu-router", "version": "1.2.1", "exportName": "router"}
            }, {
                "name": "routerSpa",
                "type": "npm",
                "content": {"package": "@ali/vu-router-spa", "version": "1.4.5", "exportName": "routerSpa"}
            }, {
                "name": "spm",
                "type": "npm",
                "content": {"package": "@ali/vu-spm", "version": "1.1.11", "exportName": "spm"}
            }, {
                "name": "switchSchema",
                "type": "npm",
                "content": {"package": "@ali/vu-switch-schema", "version": "1.0.8", "exportName": "switchSchema"}
            }, {
                "name": "legaoBuiltin",
                "type": "npm",
                "content": {"package": "@ali/vu-legao-builtin", "version": "1.5.6", "exportName": "legaoBuiltin"}
            }, {
                "name": "dataSource",
                "type": "npm",
                "content": {"package": "@ali/vu-dataSource", "version": "1.0.4", "exportName": "dataSource"}
            }, {
                "name": "toolkit",
                "type": "npm",
                "content": {"package": "@ali/vu-toolkit", "version": "1.2.0", "exportName": "toolkit"}
            }, {
                "name": "formatter",
                "type": "npm",
                "content": {"package": "@ali/vu-formatter", "version": "2.0.0", "exportName": "formatter"}
            }, {
                "name": "logic",
                "type": "npm",
                "content": {"package": "@ali/vu-logic", "version": "1.0.1", "exportName": "logic"}
            }, {"type": "npm", "content": {"package": "@ali/vm-router", "version": "1.0.14"}}, {
                "type": "npm",
                "content": {"package": "@ali/vm-schema-nav", "version": "1.0.0"}
            }],
            "componentsMap": [{
                "package": "@ali/vc-html",
                "version": "2.0.0",
                "componentName": "Html"
            }, {"package": "@ali/vc-deep", "version": "2.15.2", "componentName": "Drawer"}, {
                "package": "@ali/vc-deep",
                "version": "2.15.2",
                "componentName": "TextField"
            }, {"package": "@ali/vc-deep", "version": "2.15.2", "componentName": "Form"}, {
                "package": "@ali/vc-deep",
                "version": "2.15.2",
                "componentName": "Dialog"
            }, {
                "package": "@ali/vc-deep",
                "version": "2.15.2",
                "componentName": "NumberField"
            }, {
                "package": "@ali/vc-page",
                "version": "1.3.2",
                "componentName": "RootHeader"
            }, {"package": "@ali/vc-deep", "version": "2.15.2", "componentName": "TablePc"}, {
                "package": "@ali/vc-deep",
                "version": "2.15.2",
                "componentName": "Tab"
            }, {
                "package": "@ali/vc-deep",
                "version": "2.15.2",
                "componentName": "TabsLayout"
            }, {
                "package": "@ali/vc-deep",
                "version": "2.15.2",
                "componentName": "PageSection"
            }, {
                "package": "@ali/vc-page",
                "version": "1.3.2",
                "componentName": "RootContent"
            }, {
                "package": "@ali/vc-page",
                "version": "1.3.2",
                "componentName": "RootFooter"
            }, {"package": "@ali/vc-page", "version": "1.3.2", "componentName": "Page"}],
            "componentsTree": [{
                "componentName": "Page",
                "id": "node_oclhfyjw0s1",
                "props": {
                    "templateVersion": "1.0.0",
                    "containerStyle": {},
                    "pageStyle": {"backgroundColor": "#f2f3f5"},
                    "className": "page_lhfzxjns"
                },
                "methods": {
                    "__initMethods__": {
                        "type": "JSExpression",
                        "value": "function (exports, module) { \"use strict\";\n\nexports.__esModule = true;\nexports.helloPage = helloPage;\nexports.onTabChange = onTabChange;\nexports.onAddBtnClick = onAddBtnClick;\nexports.onAddOk = onAddOk;\nexports.onEditOk = onEditOk;\nexports.onListFetchData = onListFetchData;\nexports.onEditBtnClick = onEditBtnClick;\nexports.editShow = editShow;\nexports.infoActionRender = infoActionRender;\nexports.onInfoActionClick = onInfoActionClick;\nexports.onDelActionClick = onDelActionClick;\n\nfunction _newArrowCheck(innerThis, boundThis) { if (innerThis !== boundThis) { throw new TypeError(\"Cannot instantiate an arrow function\"); } }\n\nfunction helloPage() {// 你可以这么调用其他函数\n  //this.func1();\n  // 你可以这么调用组件的函数\n  // this.$('textField_xxx').getValue();\n  // 你可以这么使用「数据源面板」定义的「变量」\n  // this.state.xxx\n  // 你可以这么发送一个在「数据源面板」定义的「远程 API」\n  // this.dataSourceMap['xxx'].load(data)\n  // API 详见：https://lab.lowcode-engine.cn/help/docs/api/js-api\n}\n/**\n * 选项卡切换时触发\n * @param activeIndex\n * @param key\n */\n\n\nfunction onTabChange(activeIndex, key) {\n  console.log(activeIndex, key);\n  var sqlDialectConfigTips = \"sql样例:{\\\"sql\\\":\\\"SELECT 1 FROM DUAL where 1=${1} and 2=#{2} and 3=#{3} and 4=#{4} and 5=#{5}\\\"}\\n\";\n  var dubboDialectConfigTips = \"dubbo样例:{\\\"method_type\\\":\\\"[\\\\n    \\\\\\\"java.util.map\\\\\\\"\\\\n  ]\\\",\\\"interfacename\\\":\\\"cn.isqing.icloud.policy.engine.dubbo.api.DemoApi\\\",\\\"method_name\\\":\\\"getMap\\\",\\\"version\\\":\\\"1.0.0\\\",\\\"group\\\":\\\"demo\\\",\\\"params\\\":\\\" [\\\\n    {\\\\n      \\\\\\\"1\\\\\\\": #{1},\\\\n      \\\\\\\"2\\\\\\\": #{2},\\\\n      \\\\\\\"5\\\\\\\": #{5}\\\\n    }\\\\n  ]\\\"}\";\n\n  switch (activeIndex) {\n    case 1:\n      this.state.domain = \"ds\";\n      this.state.addFieldInfo = [{\n        \"label\": \"name\",\n        \"fieldId\": \"f_name\",\n        \"fieldName\": \"name\",\n        \"labelTipsText\": \"数据源名称\",\n        \"placeholder\": \"请输入数据源名称\"\n      }, {\n        \"label\": \"type\",\n        \"fieldId\": \"f_type\",\n        \"fieldName\": \"type\",\n        \"labelTipsText\": \"数据源类型:1[sql]、2[dubbo]\",\n        \"placeholder\": \"请输入类型数字标识\"\n      }, {\n        \"label\": \"isActive\",\n        \"fieldId\": \"f_isActive\",\n        \"fieldName\": \"isActive\",\n        \"labelTipsText\": \"是否生效 0否 1是\",\n        \"placeholder\": \"请输入0/1\"\n      }, {\n        \"label\": \"config\",\n        \"fieldId\": \"f_config\",\n        \"fieldName\": \"config\",\n        \"labelTipsText\": \"数据源配置\",\n        \"placeholder\": \"请输入json字符串\"\n      }];\n      this.state.addJsonObjField = [\"config\"];\n      this.state.listTile = [{\n        \"title\": \"ID\",\n        \"dataKey\": \"id\"\n      }, {\n        \"title\": \"名称\",\n        \"dataKey\": \"name\"\n      }, {\n        \"title\": \"类型\",\n        \"dataKey\": \"type\"\n      }, {\n        \"title\": \"生效\",\n        \"dataKey\": \"isActive\"\n      }];\n      this.state.urls = {\n        listUrl: \"datasource/list\",\n        editUrl: \"datasource/edit\",\n        addUrl: \"datasource/add\",\n        delUrl: \"datasource/del\",\n        infoUrl: \"datasource/text\"\n      };\n      break;\n\n    case 2:\n      this.state.domain = \"c\";\n      this.state.addFieldInfo = [{\n        \"label\": \"name\",\n        \"fieldId\": \"f_name\",\n        \"fieldName\": \"name\",\n        \"labelTipsText\": \"组件名字\",\n        \"placeholder\": \"请输入中文\"\n      }, {\n        \"label\": \"dataSourceType\",\n        \"fieldId\": \"f_dataSourceType\",\n        \"fieldName\": \"dataSourceType\",\n        \"labelTipsText\": \"组件的数据源类型:1[sql]、2[dubbo]\",\n        \"placeholder\": \"请输入类型数字标识\"\n      }, {\n        \"label\": \"dataSourceId\",\n        \"fieldId\": \"f_dataSourceId\",\n        \"fieldName\": \"dataSourceId\",\n        \"labelTipsText\": \"数字数据源主键id\",\n        \"placeholder\": \"请输入数字数据源主键id\"\n      }, {\n        \"label\": \"dialectConfig\",\n        \"fieldId\": \"f_dialectConfig\",\n        \"fieldName\": \"dialectConfig\",\n        \"labelTipsText\": sqlDialectConfigTips + dubboDialectConfigTips,\n        \"placeholder\": \"请输入json字符串\"\n      }, {\n        \"label\": \"dependCids\",\n        \"fieldId\": \"f_dependCids\",\n        \"fieldName\": \"dependCids\",\n        \"labelTipsText\": \"依赖的组件主键数组\",\n        \"placeholder\": \"请输入json数组如[1,2]\"\n      }, {\n        \"label\": \"dependInputParams\",\n        \"fieldId\": \"f_dependInputParams\",\n        \"fieldName\": \"dependInputParams\",\n        \"labelTipsText\": \"依赖的入参 map<占位标识,jsonpath>\",\n        \"placeholder\": \"请输入json字符串\"\n      }, {\n        \"label\": \"dependCRes\",\n        \"fieldId\": \"f_dependCRes\",\n        \"fieldName\": \"dependCRes\",\n        \"labelTipsText\": \"依赖的组件结果 map<占位标识,jsonpath>\",\n        \"placeholder\": \"请输入json字符串\"\n      }, {\n        \"label\": \"dependConstants\",\n        \"fieldId\": \"f_dependConstants\",\n        \"fieldName\": \"dependConstants\",\n        \"labelTipsText\": \"依赖的系统配置常量 map<占位标识,jsonpath>\",\n        \"placeholder\": \"请输入json字符串\"\n      }, {\n        \"label\": \"selfConstants\",\n        \"fieldId\": \"f_selfConstants\",\n        \"fieldName\": \"selfConstants\",\n        \"labelTipsText\": \"自身常量属性 map<占位标识,常量值>\",\n        \"placeholder\": \"请输入json字符串\"\n      }, {\n        \"label\": \"dependSystemVars\",\n        \"fieldId\": \"f_dependSystemVars\",\n        \"fieldName\": \"dependSystemVars\",\n        \"labelTipsText\": \"依赖的系统内置变量 map<占位标识,系统变量标识[如uuid]>\",\n        \"placeholder\": \"请输入json字符串\"\n      }, {\n        \"label\": \"resJudge\",\n        \"fieldId\": \"f_resJudge\",\n        \"fieldName\": \"resJudge\",\n        \"labelTipsText\": \"执行结果判断 [jsonpath1,jsonpath2]\",\n        \"placeholder\": \"请输入json数组\"\n      }];\n      this.state.addJsonObjField = [\"dependCids\", \"dependInputParams\", \"dependCRes\", \"dependConstants\", \"selfConstants\", \"dependSystemVars\", \"resJudge\"];\n      this.state.listTile = [{\n        \"title\": \"ID\",\n        \"dataKey\": \"id\"\n      }, {\n        \"title\": \"名称\",\n        \"dataKey\": \"name\"\n      }, {\n        \"title\": \"数据源类型\",\n        \"dataKey\": \"dataSourceType\"\n      }, {\n        \"title\": \"数据源ID\",\n        \"dataKey\": \"dataSourceId\"\n      }];\n      this.state['cListReq'] = {\n        \"pageInfo\": {\n          \"pageNum\": 1,\n          \"pageSize\": 20,\n          \"needTotal\": true,\n          \"needList\": true,\n          \"fromId\": 0\n        },\n        \"condition\": {\n          \"id\": null\n        }\n      };\n      this.state.urls = {\n        listUrl: \"component/list\",\n        editUrl: \"component/edit\",\n        addUrl: \"component/add\",\n        delUrl: \"component/del\",\n        infoUrl: \"component/text\"\n      };\n      break;\n\n    case 3:\n      this.state.domain = \"cc\";\n      this.state.addFieldInfo = [{\n        \"label\": \"group\",\n        \"fieldId\": \"f_group\",\n        \"fieldName\": \"group\",\n        \"labelTipsText\": \"配置组:\\n12:渲染器\",\n        \"placeholder\": \"请输入配置组标识\"\n      }, {\n        \"label\": \"key\",\n        \"fieldId\": \"f_key\",\n        \"fieldName\": \"key\",\n        \"labelTipsText\": \"key\",\n        \"placeholder\": \"请输入key\"\n      }, {\n        \"label\": \"value\",\n        \"fieldId\": \"f_value\",\n        \"fieldName\": \"value\",\n        \"labelTipsText\": \"value 最长2000 超长应该拆分多条 按顺序添加\",\n        \"placeholder\": \"请输入value\"\n      }, {\n        \"label\": \"sort\",\n        \"fieldId\": \"f_sort\",\n        \"fieldName\": \"sort\",\n        \"labelTipsText\": \"排序值\",\n        \"placeholder\": \"请输入排序数字\"\n      }];\n      this.state.addJsonObjField = [];\n      this.state.listTile = [{\n        \"title\": \"ID\",\n        \"dataKey\": \"id\"\n      }, {\n        \"title\": \"分组\",\n        \"dataKey\": \"group\"\n      }, {\n        \"title\": \"key\",\n        \"dataKey\": \"key\"\n      }, {\n        \"title\": \"value\",\n        \"dataKey\": \"value\"\n      }, {\n        \"title\": \"排序\",\n        \"dataKey\": \"sort\"\n      }];\n      this.state.urls = {\n        listUrl: \"common_config/list\",\n        editUrl: \"common_config/edit\",\n        addUrl: \"common_config/add\",\n        delUrl: \"common_config/del\",\n        infoUrl: null\n      };\n      break;\n\n    case 4:\n      this.state.domain = \"v\";\n      var varType = \"STRING(1, \\\"java.lang.String\\\", \\\"String\\\"),\\n\" + \"    BIG_DECIMAL(2, \\\"java.math.BigDecimal\\\", \\\"BigDecimal\\\"),\\n\" + \"    BIG_INTEGER(3, \\\"java.math.BigInteger\\\", \\\"BigInteger\\\"),\\n\" + \"    LIST(4, \\\"java.util.List\\\", \\\"List\\\"),\\n\" + \"    MAP(5, \\\"java.util.Map\\\", \\\"Map\\\")\";\n      this.state.addFieldInfo = [{\n        \"label\": \"name\",\n        \"fieldId\": \"f_name\",\n        \"fieldName\": \"name\",\n        \"labelTipsText\": \"变量名称\",\n        \"placeholder\": \"请输入变量名称\"\n      }, {\n        \"label\": \"cid\",\n        \"fieldId\": \"f_cid\",\n        \"fieldName\": \"cid\",\n        \"labelTipsText\": \"来源组件的主键\",\n        \"placeholder\": \"请输入组件主键\"\n      }, {\n        \"label\": \"cresPath\",\n        \"fieldId\": \"f_cresPath\",\n        \"fieldName\": \"cresPath\",\n        \"labelTipsText\": \"结果集提取jsonpath\",\n        \"placeholder\": \"请输入jsonpath\"\n      }, {\n        \"label\": \"type\",\n        \"fieldId\": \"f_type\",\n        \"fieldName\": \"type\",\n        \"labelTipsText\": \"变量类型数字标识:\" + varType,\n        \"placeholder\": \"变量类型数字标识\"\n      }, {\n        \"label\": \"typePath\",\n        \"fieldId\": \"f_typePath\",\n        \"fieldName\": \"typePath\",\n        \"labelTipsText\": \"变量类型全路径:\" + varType,\n        \"placeholder\": \"请输入变量类型包路径\"\n      }, {\n        \"label\": \"domain\",\n        \"fieldId\": \"f_domain\",\n        \"fieldName\": \"domain\",\n        \"labelTipsText\": \"域 隔离数据的第一层标识\",\n        \"placeholder\": \"请输入域标识\"\n      }, {\n        \"label\": \"rendererId\",\n        \"fieldId\": \"f_rendererId\",\n        \"fieldName\": \"rendererId\",\n        \"labelTipsText\": \"渲染器主键\",\n        \"placeholder\": \"请输入渲染器主键\"\n      }, {\n        \"label\": \"busiCode\",\n        \"fieldId\": \"f_busiCode\",\n        \"fieldName\": \"busiCode\",\n        \"labelTipsText\": \"抽象业务编码\",\n        \"placeholder\": \"请输入业务编码\"\n      }, {\n        \"label\": \"note\",\n        \"fieldId\": \"f_note\",\n        \"fieldName\": \"note\",\n        \"labelTipsText\": \"备注\",\n        \"placeholder\": \"请输入备注\"\n      }];\n      this.state.addJsonObjField = [];\n      this.state.listTile = [{\n        \"title\": \"ID\",\n        \"dataKey\": \"id\"\n      }, {\n        \"title\": \"名称\",\n        \"dataKey\": \"name\"\n      }, {\n        \"title\": \"值表达式\",\n        \"dataKey\": \"cresPath\"\n      }, {\n        \"title\": \"类型\",\n        \"dataKey\": \"type\"\n      }, {\n        \"title\": \"类型包路径\",\n        \"dataKey\": \"typePath\"\n      }, {\n        \"title\": \"域\",\n        \"dataKey\": \"domain\"\n      }, {\n        \"title\": \"渲染器ID\",\n        \"dataKey\": \"rendererId\"\n      }, {\n        \"title\": \"业务编码\",\n        \"dataKey\": \"busiCode\"\n      }, {\n        \"title\": \"备注\",\n        \"dataKey\": \"note\"\n      }];\n      this.state.urls = {\n        listUrl: \"variable/list\",\n        editUrl: \"variable/edit\",\n        addUrl: \"variable/add\",\n        delUrl: \"variable/del\",\n        infoUrl: null\n      };\n      break;\n  }\n\n  this.state[this.state.domain + 'ListReq'] = {\n    \"pageInfo\": {\n      \"pageNum\": 1,\n      \"pageSize\": 20,\n      \"needTotal\": true,\n      \"needList\": true,\n      \"fromId\": 0\n    },\n    \"condition\": {\n      \"id\": null\n    }\n  };\n  this.dataSourceMap['list'].load();\n}\n\nfunction onAddBtnClick() {\n  this.$('dialog_add').show();\n}\n/**\n * dialog onOk\n */\n\n\nfunction onAddOk() {\n  var _this = this;\n\n  var that = this;\n  this.$('form_add').submit(function (data, error) {\n    var _this2 = this;\n\n    _newArrowCheck(this, _this);\n\n    if (data) {\n      console.log(data);\n      this.dataSourceMap['add'].load(data).then(function () {\n        _newArrowCheck(this, _this2);\n\n        if (that.state.res.success) {\n          this.utils.toast({\n            type: 'success',\n            title: '提交成功'\n          });\n        } else {\n          this.utils.toast({\n            type: 'error',\n            title: that.state.res.msg\n          });\n        }\n\n        this.$('dialog_add').hide();\n        this.state['listReq'].pageInfo.pageNum = 1;\n        this.dataSourceMap['list'].load();\n        this.$('form_add').reset();\n      }.bind(this)).catch(function (reason) {\n        _newArrowCheck(this, _this2);\n\n        this.utils.toast({\n          type: 'error',\n          title: reason\n        });\n      }.bind(this));\n    }\n  }.bind(this));\n}\n\nfunction onEditOk() {\n  var _this3 = this;\n\n  var that = this;\n  this.$('form_edit').submit(function (data, error) {\n    var _this4 = this;\n\n    _newArrowCheck(this, _this3);\n\n    if (data) {\n      console.log(data);\n      this.dataSourceMap['edit'].load(data).then(function () {\n        _newArrowCheck(this, _this4);\n\n        if (that.state.res.success) {\n          this.utils.toast({\n            type: 'success',\n            title: '提交成功'\n          });\n        } else {\n          this.utils.toast({\n            type: 'error',\n            title: that.state.res.msg\n          });\n        }\n\n        this.$('dialog_edit').hide();\n        this.state['listReq'].pageInfo.pageNum = 1;\n        this.dataSourceMap['list'].load();\n        this.$('form_edit').reset();\n      }.bind(this)).catch(function (reason) {\n        _newArrowCheck(this, _this4);\n\n        this.utils.toast({\n          type: 'error',\n          title: reason\n        });\n      }.bind(this));\n    }\n  }.bind(this));\n}\n/**\n * tablePc onFetchData\n * @param params.currentPage 当前页码\n * @param params.pageSize 每页显示条数\n * @param params.searchKey 搜索关键字\n * @param params.orderColumn 排序列\n * @param params.orderType 排序方式（desc,asc）\n * @param params.from 触发来源（order,search,pagination）\n */\n\n\nfunction onListFetchData(params) {\n  // 如果是搜索的话翻页重置到 1\n  if (params.from === 'search') {\n    params.currentPage = 1;\n  }\n\n  this.state['listReq'].pageInfo.pageNum = params.currentPage;\n  this.dataSourceMap['list'].load();\n}\n\nfunction onEditBtnClick(rowData) {\n  var _this5 = this;\n\n  var that = this;\n\n  if (this.state.urls.infoUrl !== null) {\n    this.dataSourceMap[\"info\"].load({\n      id: rowData.id\n    }).then(function () {\n      _newArrowCheck(this, _this5);\n\n      editShow(rowData, that);\n    }.bind(this)).catch(function (reason) {\n      _newArrowCheck(this, _this5);\n\n      this.utils.toast({\n        type: 'error',\n        title: reason\n      });\n    }.bind(this));\n  } else {\n    editShow(rowData, that);\n  }\n}\n\nfunction editShow(rowData, that) {\n  var _this6 = this;\n\n  that.state.editInfo.id = rowData.id;\n  var arr = [];\n  that.state.addFieldInfo.forEach(function (one) {\n    _newArrowCheck(this, _this6);\n\n    var clone = Object.assign({}, one);\n    var key = clone.fieldName;\n    console.log(\"key:\" + key);\n    console.log(\"info:\", that.state.infoInfo);\n\n    if (rowData[key] !== null) {\n      clone[\"value\"] = rowData[key];\n    } else if (that.state.infoInfo.content !== null && that.state.infoInfo.content.hasOwnProperty(key) && that.state.infoInfo.content[key] !== null) {\n      clone[\"value\"] = stringify(that.state.infoInfo.content[key]);\n    }\n\n    arr.push(clone);\n  }.bind(this));\n  that.state.editInfo.editFieldInfo = arr;\n  that.$('dialog_edit').show();\n} //定义一个函数，接受一个对象作为参数\n\n\nfunction stringify(obj) {\n  //如果对象是字符串，直接返回\n  if (typeof obj === \"string\") {\n    return obj;\n  } //否则，使用JSON.stringify方法将对象转为json字符串，并返回\n  else {\n    return JSON.stringify(obj, null, 4);\n  }\n}\n\nfunction infoActionRender(title, rowData) {\n  if (this.state.urls.infoUrl == null) {\n    return false;\n  }\n\n  return title; // return false 则不显示\n}\n\nfunction onInfoActionClick(rowData) {\n  this.dataSourceMap[\"info\"].load({\n    id: rowData.id\n  });\n  this.$('drawer_info').show();\n}\n\nfunction onDelActionClick(rowData) {\n  var _this7 = this;\n\n  this.dataSourceMap[\"del\"].load({\n    id: rowData.id\n  }).then(function () {\n    _newArrowCheck(this, _this7);\n\n    if (this.state.res.success) {\n      this.utils.toast({\n        type: 'success',\n        title: '提交成功'\n      });\n      this.state['listReq'].pageInfo.pageNum = 1;\n      this.dataSourceMap['list'].load();\n    } else {\n      this.utils.toast({\n        type: 'error',\n        title: that.state.res.msg\n      });\n    }\n  }.bind(this)).catch(function (reason) {\n    _newArrowCheck(this, _this7);\n\n    this.utils.toast({\n      type: 'error',\n      title: reason\n    });\n  }.bind(this));\n}\n }",
                        "extType": "function"
                    }
                },
                "lifeCycles": {
                    "constructor": {
                        "type": "JSExpression",
                        "value": "function constructor() {\nvar module = { exports: {} };\nvar _this = this;\nthis.__initMethods__(module.exports, module);\nObject.keys(module.exports).forEach(function(item) {\n  if(typeof module.exports[item] === 'function'){\n    _this[item] = module.exports[item];\n  }\n});\n\n}",
                        "extType": "function"
                    }
                },
                "hidden": false,
                "title": "",
                "isLocked": false,
                "condition": true,
                "conditionGroup": "",
                "dataSource": {
                    "globalConfig": {},
                    "online": [{
                        "id": "",
                        "name": "urlParams",
                        "description": "URL 上 querystring 解析后的对象",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "URI",
                        "isReadonly": true
                    }, {
                        "id": "",
                        "name": "list",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "http://172.16.2.119/ivariable/datasource/list",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": {"type": "JSExpression", "value": "state.dsListReq", "extType": "variable"},
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = {\n    \"currentPage\": 1,\n    \"totalCount\": 0,\n    \"data\": []\n  };\n\n  if (response.success) {\n    console.log(\"请求成功----------\");\n    content = {\n      \"currentPage\": 1,\n      \"totalCount\": response.data.total,\n      \"data\": response.data.list\n    };\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  // 通过 config.url 可以更改  url \n  vars.data = this.state.listReq;\n  config.url = this.state.urlPre + this.state.urls.listUrl;\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(JSON.parse(JSON.stringify(vars)));\n  console.log(JSON.parse(JSON.stringify(config)));\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": true,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({list: data}); return data; }",
                            "extType": "function"
                        }
                    }, {
                        "id": "",
                        "name": "listReq",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {
                            "type": "JSExpression",
                            "value": "{\r\n    \"pageInfo\": {\r\n        \"pageNum\": 1,\r\n        \"pageSize\": 100,\r\n        \"needTotal\": true,\r\n        \"needList\": true,\r\n        \"fromId\": 0\r\n    },\r\n    \"condition\": {\r\n        \"id\": null\r\n    }\r\n}",
                            "extType": "variable"
                        }
                    }, {
                        "id": "",
                        "name": "tabs",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {
                            "type": "JSExpression",
                            "value": "[\r\n  {\"title\":\"数据源\",\"primaryKey\":\"tab_1\",\"closeable\":false,\"disabled\":false,\"customKey\":{},\"defaultActived\": true},\r\n  {\"title\":\"组件\",\"primaryKey\":\"tab_2\",\"closeable\":false,\"disabled\":false,\"customKey\":{}},\r\n  {\"title\":\"公共配置\",\"primaryKey\":\"tab_3\",\"closeable\":false,\"disabled\":false,\"customKey\":{}},\r\n  {\"title\":\"变量\",\"primaryKey\":\"tab_4\",\"closeable\":false,\"disabled\":false,\"customKey\":{}}\r\n]",
                            "extType": "variable"
                        }
                    }, {
                        "id": "",
                        "name": "dsListTitle",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {
                            "type": "JSExpression",
                            "value": "[\r\n  {\"title\":\"ID\",\"dataKey\":\"id\"},\r\n  {\"title\":\"名称\",\"dataKey\":\"name\"},\r\n  {\"title\":\"类型\",\"dataKey\":\"type\"},\r\n  {\"title\":\"生效\",\"dataKey\":\"isActive\"}\r\n]",
                            "extType": "variable"
                        }
                    }, {
                        "id": "",
                        "name": "res",
                        "description": "信息",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {
                            "type": "JSExpression",
                            "value": "{\"success\":true,\"msg\":\"ok\"}",
                            "extType": "variable"
                        }
                    }, {
                        "id": "",
                        "name": "addFieldInfo",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {
                            "type": "JSExpression",
                            "value": "[\r\n  {\"label\":\"name\",\"fieldId\":\"f_name\",\"fieldName\":\"name\"},\r\n  {\"label\":\"type\",\"fieldId\":\"f_type\",\"fieldName\":\"type\"},\r\n  {\"label\":\"isActive\",\"fieldId\":\"f_isActive\",\"fieldName\":\"isActive\"},\r\n  {\"label\":\"config\",\"fieldId\":\"f_config\",\"fieldName\":\"config\"}\r\n]",
                            "extType": "variable"
                        }
                    }, {
                        "id": "",
                        "name": "domain",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {"type": "JSExpression", "value": "\"ds\"", "extType": "variable"}
                    }, {
                        "id": "",
                        "name": "add",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "../datasource/add",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": [],
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = response.data;\n\n  if (content == null) {\n    content = [];\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  this.state.res = response;\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  config.url = this.state.urlPre + this.state.urls.addUrl;\n\n  for (var key in vars.data) {\n    // 如果键存在于字符串数组中\n    if (this.state.addJsonObjField.includes(key)) {\n      // 则将值转换成 JSON 对象，并赋值给结果对象的同名键\n      vars.data[key] = JSON.parse(vars.data[key]);\n    }\n  }\n\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(vars, config, \"请求参数------\"); // 可以查看还有哪些参数可以修改。\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "onError": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function onError(error) {\n  console.log(error); // 可以在这里做弹框提示等操作\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": false,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({add: data}); return data; }",
                            "extType": "function"
                        }
                    }, {
                        "id": "",
                        "name": "addJsonObjField",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {"type": "JSExpression", "value": "[\"config\"]", "extType": "variable"}
                    }, {
                        "id": "",
                        "name": "editInfo",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "initialData": {"type": "JSExpression", "value": "{\"id\":0}", "extType": "variable"}
                    }, {
                        "id": "",
                        "name": "edit",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "../datasource/edit",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": [],
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = response.data;\n\n  if (content == null) {\n    content = [];\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  this.state.res = response;\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  config.url = this.state.urlPre + this.state.urls.editUrl;\n\n  for (var key in vars.data) {\n    // 如果键存在于字符串数组中\n    if (this.state.addJsonObjField.includes(key)) {\n      // 则将值转换成 JSON 对象，并赋值给结果对象的同名键\n      vars.data[key] = JSON.parse(vars.data[key]);\n    }\n  }\n\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(vars, config, \"请求参数------\"); // 可以查看还有哪些参数可以修改。\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "onError": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function onError(error) {\n  console.log(error); // 可以在这里做弹框提示等操作\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": false,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({edit: data}); return data; }",
                            "extType": "function"
                        }
                    }, {
                        "id": "",
                        "name": "urls",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {
                            "type": "JSExpression",
                            "value": "{\r\n        listUrl: \"datasource/list\",\r\n        editUrl: \"datasource/edit\",\r\n        addUrl: \"datasource/add\",\r\n        delUrl: \"datasource/del\",\r\n        infoUrl: \"datasource/text\"\r\n      }",
                            "extType": "variable"
                        }
                    }, {
                        "id": "",
                        "name": "urlPre",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "initialData": {"type": "JSExpression", "value": "\"../\"", "extType": "variable"}
                    }, {
                        "id": "",
                        "name": "info",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "../datasource/text",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": [],
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = response.data;\n\n  if (content == null) {\n    content = [];\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  this.state.res = response;\n  this.state.infoInfo = data;\n  this.state.infoString = \"<div><pre>\" + JSON.stringify(data.content, null, 4) + \"</div></pre>\";\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  config.url = this.state.urlPre + this.state.urls.infoUrl + \"?id=\" + vars.data.id;\n\n  for (var key in vars.data) {\n    // 如果键存在于字符串数组中\n    if (this.state.addJsonObjField.includes(key)) {\n      // 则将值转换成 JSON 对象，并赋值给结果对象的同名键\n      vars.data[key] = JSON.parse(vars.data[key]);\n    }\n  }\n\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(vars, config, \"请求参数------\"); // 可以查看还有哪些参数可以修改。\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "onError": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function onError(error) {\n  console.log(\"-----\", error); // 可以在这里做弹框提示等操作\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": false,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({info: data}); return data; }",
                            "extType": "function"
                        }
                    }, {
                        "id": "",
                        "name": "infoInfo",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "initialData": {"type": "JSExpression", "value": "\"\"", "extType": "variable"}
                    }, {
                        "id": "",
                        "name": "cListTitle",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {
                            "type": "JSExpression",
                            "value": "[\r\n          {\"title\":\"ID\",\"dataKey\":\"id\"},\r\n          {\"title\":\"名称\",\"dataKey\":\"name\"},\r\n          {\"title\":\"数据源类型\",\"dataKey\":\"dataSourceType\"},\r\n          {\"title\":\"数据源ID\",\"dataKey\":\"dataSourceId\"}\r\n        ]",
                            "extType": "variable"
                        }
                    }, {
                        "id": "",
                        "name": "ccListTitle",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {
                            "type": "JSExpression",
                            "value": "[\r\n  {\"title\":\"ID\",\"dataKey\":\"id\"},\r\n  {\"title\":\"分组\",\"dataKey\":\"group\"},\r\n  {\"title\":\"key\",\"dataKey\":\"key\"},\r\n  {\"title\":\"value\",\"dataKey\":\"value\"},\r\n  {\"title\":\"排序\",\"dataKey\":\"sort\"},\r\n]",
                            "extType": "variable"
                        }
                    }, {
                        "id": "",
                        "name": "vListTitle",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "isModified": false,
                        "initialData": {
                            "type": "JSExpression",
                            "value": "[\r\n          {\"title\":\"ID\",\"dataKey\":\"id\"},\r\n          {\"title\":\"名称\",\"dataKey\":\"name\"},\r\n          {\"title\":\"值表达式\",\"dataKey\":\"cresPath\"},\r\n          {\"title\":\"类型\",\"dataKey\":\"type\"},\r\n          {\"title\":\"类型包路径\",\"dataKey\":\"typePath\"},\r\n          {\"title\":\"域\",\"dataKey\":\"domain\"},\r\n          {\"title\":\"渲染器ID\",\"dataKey\":\"rendererId\"},\r\n          {\"title\":\"业务编码\",\"dataKey\":\"busiCode\"},\r\n          {\"title\":\"备注\",\"dataKey\":\"note\"},\r\n        ]",
                            "extType": "variable"
                        }
                    }, {
                        "id": "",
                        "name": "infoString",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "VALUE",
                        "initialData": {"type": "JSExpression", "value": "\"\"", "extType": "variable"}
                    }, {
                        "id": "",
                        "name": "del",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "../datasource/del",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": [],
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = response.data;\n\n  if (content == null) {\n    content = [];\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  this.state.res = response;\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  config.url = this.state.urlPre + this.state.urls.delUrl + \"?id=\" + vars.data.id;\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(vars, config, \"请求参数------\"); // 可以查看还有哪些参数可以修改。\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "onError": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function onError(error) {\n  console.log(error); // 可以在这里做弹框提示等操作\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": false,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({del: data}); return data; }",
                            "extType": "function"
                        }
                    }],
                    "offline": [],
                    "sync": true,
                    "list": [{
                        "id": "list",
                        "name": "list",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "http://172.16.2.119/ivariable/datasource/list",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": {"type": "JSExpression", "value": "state.dsListReq", "extType": "variable"},
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = {\n    \"currentPage\": 1,\n    \"totalCount\": 0,\n    \"data\": []\n  };\n\n  if (response.success) {\n    console.log(\"请求成功----------\");\n    content = {\n      \"currentPage\": 1,\n      \"totalCount\": response.data.total,\n      \"data\": response.data.list\n    };\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  // 通过 config.url 可以更改  url \n  vars.data = this.state.listReq;\n  config.url = this.state.urlPre + this.state.urls.listUrl;\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(JSON.parse(JSON.stringify(vars)));\n  console.log(JSON.parse(JSON.stringify(config)));\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": true,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({list: data}); return data; }",
                            "extType": "function"
                        }
                    }, {
                        "id": "add",
                        "name": "add",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "../datasource/add",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": [],
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = response.data;\n\n  if (content == null) {\n    content = [];\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  this.state.res = response;\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  config.url = this.state.urlPre + this.state.urls.addUrl;\n\n  for (var key in vars.data) {\n    // 如果键存在于字符串数组中\n    if (this.state.addJsonObjField.includes(key)) {\n      // 则将值转换成 JSON 对象，并赋值给结果对象的同名键\n      vars.data[key] = JSON.parse(vars.data[key]);\n    }\n  }\n\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(vars, config, \"请求参数------\"); // 可以查看还有哪些参数可以修改。\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "onError": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function onError(error) {\n  console.log(error); // 可以在这里做弹框提示等操作\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": false,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({add: data}); return data; }",
                            "extType": "function"
                        }
                    }, {
                        "id": "edit",
                        "name": "edit",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "../datasource/edit",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": [],
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = response.data;\n\n  if (content == null) {\n    content = [];\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  this.state.res = response;\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  config.url = this.state.urlPre + this.state.urls.editUrl;\n\n  for (var key in vars.data) {\n    // 如果键存在于字符串数组中\n    if (this.state.addJsonObjField.includes(key)) {\n      // 则将值转换成 JSON 对象，并赋值给结果对象的同名键\n      vars.data[key] = JSON.parse(vars.data[key]);\n    }\n  }\n\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(vars, config, \"请求参数------\"); // 可以查看还有哪些参数可以修改。\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "onError": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function onError(error) {\n  console.log(error); // 可以在这里做弹框提示等操作\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": false,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({edit: data}); return data; }",
                            "extType": "function"
                        }
                    }, {
                        "id": "info",
                        "name": "info",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "../datasource/text",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": [],
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = response.data;\n\n  if (content == null) {\n    content = [];\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  this.state.res = response;\n  this.state.infoInfo = data;\n  this.state.infoString = \"<div><pre>\" + JSON.stringify(data.content, null, 4) + \"</div></pre>\";\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  config.url = this.state.urlPre + this.state.urls.infoUrl + \"?id=\" + vars.data.id;\n\n  for (var key in vars.data) {\n    // 如果键存在于字符串数组中\n    if (this.state.addJsonObjField.includes(key)) {\n      // 则将值转换成 JSON 对象，并赋值给结果对象的同名键\n      vars.data[key] = JSON.parse(vars.data[key]);\n    }\n  }\n\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(vars, config, \"请求参数------\"); // 可以查看还有哪些参数可以修改。\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "onError": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function onError(error) {\n  console.log(\"-----\", error); // 可以在这里做弹框提示等操作\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": false,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({info: data}); return data; }",
                            "extType": "function"
                        }
                    }, {
                        "id": "del",
                        "name": "del",
                        "description": "",
                        "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                        "protocal": "REMOTE",
                        "isModified": false,
                        "options": {
                            "url": "../datasource/del",
                            "shouldFetch": true,
                            "method": "POST",
                            "params": [],
                            "isSync": false,
                            "loadType": "",
                            "fit": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function fit(response) {\n  var content = response.data;\n\n  if (content == null) {\n    content = [];\n  }\n\n  var error = {\n    message: response.msg\n  };\n  var success = response.success;\n  var data = {\n    content: content,\n    success: success,\n    error: error\n  };\n  console.log(data, \"--------------\");\n  this.state.res = response;\n  return data;\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "willFetch": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function willFetch(vars, config) {\n  // 通过 vars.data 可以更改查询参数\n  // 通过 config.header 可以更改 header\n  config.url = this.state.urlPre + this.state.urls.delUrl + \"?id=\" + vars.data.id;\n  config.header['Content-Type'] = 'application/json'; // 修改 Content-Type\n\n  console.log(vars, config, \"请求参数------\"); // 可以查看还有哪些参数可以修改。\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            },
                            "onError": {
                                "type": "JSExpression",
                                "value": "function main(){\n    \n    \"use strict\";\n\nvar __compiledFunc__ = function onError(error) {\n  console.log(error); // 可以在这里做弹框提示等操作\n};\n    return __compiledFunc__.apply(this, arguments);\n  }",
                                "extType": "function"
                            }
                        },
                        "isInit": false,
                        "dpType": "REMOTE",
                        "type": "legao",
                        "requestHandler": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.dataSourceHandler"
                        },
                        "dataHandler": {
                            "type": "JSExpression",
                            "value": "function(data, err) { this.setState({del: data}); return data; }",
                            "extType": "function"
                        }
                    }]
                },
                "css": "body{background-color:#f2f3f5}",
                "children": [{
                    "componentName": "Drawer",
                    "id": "node_oclhiiibvs8",
                    "props": {
                        "title": {
                            "type": "JSExpression",
                            "value": "this.utils.getLocale() === 'en_US' ? (\"Drawer Title\" == 'null' ? null :\"Drawer Title\") : (\"详情\" == 'null' ? null : \"详情\")"
                        },
                        "hasMask": true,
                        "closeable": ["esc", "mask"],
                        "placement": "right",
                        "footer": true,
                        "footerAlign": "right",
                        "footerActions": "cancel",
                        "confirmText": {
                            "type": "JSExpression",
                            "value": "this.utils.getLocale() === 'en_US' ? (\"Confirm\" == 'null' ? null :\"Confirm\") : (\"确定\" == 'null' ? null : \"确定\")"
                        },
                        "cancelText": {
                            "type": "JSExpression",
                            "value": "this.utils.getLocale() === 'en_US' ? (\"Cancel\" == 'null' ? null :\"Cancel\") : (\"取消\" == 'null' ? null : \"取消\")"
                        },
                        "confirmStyle": "primary",
                        "confirmState": "确定",
                        "fieldId": "drawer_info",
                        "visible": false,
                        "height": ""
                    },
                    "title": "侧抽屉",
                    "hidden": true,
                    "isLocked": false,
                    "condition": true,
                    "conditionGroup": "",
                    "children": [{
                        "componentName": "Html",
                        "id": "node_oclhjunbbw6",
                        "props": {
                            "content": {
                                "type": "JSExpression",
                                "value": "state.infoString",
                                "extType": "variable"
                            }, "__style__": {}, "fieldId": "html_lhk03td9"
                        },
                        "title": "HTML",
                        "hidden": false,
                        "isLocked": false,
                        "condition": true,
                        "conditionGroup": ""
                    }]
                }, {
                    "componentName": "Dialog",
                    "id": "node_oclhg2iq2v1e",
                    "props": {
                        "title": {
                            "type": "JSExpression",
                            "value": "this.utils.getLocale() === 'en_US' ? (\"Dialog Title\" == 'null' ? null :\"Dialog Title\") : (\"新增\" == 'null' ? null : \"新增\")"
                        },
                        "hasMask": true,
                        "closeable": ["mask", "esc"],
                        "autoFocus": false,
                        "footer": true,
                        "footerAlign": "right",
                        "footerActions": "cancel,ok",
                        "confirmText": {
                            "type": "JSExpression",
                            "value": "this.utils.getLocale() === 'en_US' ? (\"Confirm\" == 'null' ? null :\"Confirm\") : (\"确定\" == 'null' ? null : \"确定\")"
                        },
                        "cancelText": {
                            "type": "JSExpression",
                            "value": "this.utils.getLocale() === 'en_US' ? (\"Cancel\" == 'null' ? null :\"Cancel\") : (\"取消\" == 'null' ? null : \"取消\")"
                        },
                        "confirmStyle": "primary",
                        "confirmState": "确定",
                        "__style__": {},
                        "fieldId": "dialog_add",
                        "popupOutDialog": true,
                        "onOk": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.execEventFlow.bind(this, [this.onAddOk])",
                            "events": [{"type": "JSExpression", "value": "onAddOk.bind(this)"}]
                        },
                        "visible": false
                    },
                    "title": "普通型",
                    "hidden": true,
                    "isLocked": false,
                    "condition": true,
                    "conditionGroup": "",
                    "children": [{
                        "componentName": "Form",
                        "id": "node_oclhg2iq2v1r",
                        "props": {
                            "labelAlign": "top",
                            "size": "medium",
                            "behavior": "NORMAL",
                            "autoValidate": true,
                            "scrollToFirstError": true,
                            "autoUnmount": true,
                            "fieldOptions": {},
                            "__style__": {},
                            "fieldId": "form_add"
                        },
                        "title": "表单容器",
                        "hidden": false,
                        "isLocked": false,
                        "condition": true,
                        "conditionGroup": "",
                        "children": [{
                            "componentName": "TextField",
                            "id": "node_oclhg2iq2v1x",
                            "props": {
                                "htmlType": "textarea",
                                "__category__": "form",
                                "__useMediator": "value",
                                "label": {"type": "JSExpression", "value": "this.item.label", "extType": "variable"},
                                "value": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "labelAlign": "left",
                                "labelColSpan": 4,
                                "labelTextAlign": "right",
                                "placeholder": {
                                    "type": "JSExpression",
                                    "value": "this.item.placeholder",
                                    "extType": "variable"
                                },
                                "tips": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "size": "medium",
                                "behavior": "NORMAL",
                                "labelTipsTypes": "text",
                                "labelTipsText": {
                                    "type": "JSExpression",
                                    "value": "this.item.labelTipsText",
                                    "extType": "variable"
                                },
                                "rows": 1,
                                "maxLength": 200,
                                "addonBefore": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "addonAfter": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "validation": [],
                                "__style__": {},
                                "fieldId": "textField_lhh200ii",
                                "labelColOffset": 0,
                                "wrapperColSpan": 0,
                                "wrapperColOffset": 0,
                                "labelTipsIcon": "help",
                                "state": "",
                                "hasLimitHint": false,
                                "autoHeight": true,
                                "hasClear": false,
                                "trim": true,
                                "useI18nInput": false,
                                "autoFocus": true,
                                "cutString": false,
                                "fieldName": {
                                    "type": "JSExpression",
                                    "value": "this.item.fieldName",
                                    "extType": "variable"
                                },
                                "labelTips": {
                                    "type": "JSExpression",
                                    "value": "this.item.labelTipsText",
                                    "extType": "variable"
                                }
                            },
                            "title": "文本框",
                            "hidden": false,
                            "isLocked": false,
                            "condition": true,
                            "conditionGroup": "",
                            "loop": {"type": "JSExpression", "value": "state.addFieldInfo", "extType": "variable"},
                            "loopArgs": ["item", "index"]
                        }]
                    }]
                }, {
                    "componentName": "Dialog",
                    "id": "node_oclhiiibvs1",
                    "props": {
                        "title": {
                            "type": "JSExpression",
                            "value": "this.utils.getLocale() === 'en_US' ? (\"Dialog Title\" == 'null' ? null :\"Dialog Title\") : (\"修改\" == 'null' ? null : \"修改\")"
                        },
                        "hasMask": true,
                        "closeable": ["mask", "esc"],
                        "autoFocus": false,
                        "footer": true,
                        "footerAlign": "right",
                        "footerActions": "cancel,ok",
                        "confirmText": {
                            "type": "JSExpression",
                            "value": "this.utils.getLocale() === 'en_US' ? (\"Confirm\" == 'null' ? null :\"Confirm\") : (\"确定\" == 'null' ? null : \"确定\")"
                        },
                        "cancelText": {
                            "type": "JSExpression",
                            "value": "this.utils.getLocale() === 'en_US' ? (\"Cancel\" == 'null' ? null :\"Cancel\") : (\"取消\" == 'null' ? null : \"取消\")"
                        },
                        "confirmStyle": "primary",
                        "confirmState": "确定",
                        "__style__": {},
                        "fieldId": "dialog_edit",
                        "popupOutDialog": true,
                        "onOk": {
                            "type": "JSExpression",
                            "value": "this.utils.legaoBuiltin.execEventFlow.bind(this, [this.onEditOk])",
                            "events": [{"type": "JSExpression", "value": "onEditOk.bind(this)"}]
                        },
                        "visible": false
                    },
                    "title": "普通型",
                    "hidden": true,
                    "isLocked": false,
                    "condition": true,
                    "conditionGroup": "",
                    "children": [{
                        "componentName": "Form",
                        "id": "node_oclhiiibvs2",
                        "props": {
                            "labelAlign": "top",
                            "size": "medium",
                            "behavior": "NORMAL",
                            "autoValidate": true,
                            "scrollToFirstError": true,
                            "autoUnmount": true,
                            "fieldOptions": {},
                            "__style__": {},
                            "fieldId": "form_edit"
                        },
                        "title": "表单容器",
                        "hidden": false,
                        "isLocked": false,
                        "condition": true,
                        "conditionGroup": "",
                        "children": [{
                            "componentName": "NumberField",
                            "id": "node_oclhiiibvs4",
                            "props": {
                                "__category__": "form",
                                "__useMediator": "value",
                                "label": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"NumberField\" == 'null' ? null :\"NumberField\") : (\"id\" == 'null' ? null : \"id\")"
                                },
                                "value": {"type": "JSExpression", "value": "state.editInfo.id", "extType": "variable"},
                                "labelAlign": "left",
                                "labelColSpan": 4,
                                "labelTextAlign": "left",
                                "placeholder": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"Please enter a number\" == 'null' ? null :\"Please enter a number\") : (\"请输入数字\" == 'null' ? null : \"请输入数字\")"
                                },
                                "tips": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "size": "medium",
                                "behavior": "DISABLED",
                                "labelTipsTypes": "none",
                                "labelTipsText": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "type": "normal",
                                "innerBefore": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "innerAfter": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "step": 1,
                                "editable": true,
                                "validation": [],
                                "__style__": {},
                                "fieldId": "numberField_lhit7rv1",
                                "labelColOffset": 0,
                                "wrapperColSpan": 0,
                                "wrapperColOffset": 0,
                                "labelTipsIcon": "",
                                "precision": 0,
                                "fieldName": "id"
                            },
                            "title": "数字输入框",
                            "hidden": false,
                            "isLocked": false,
                            "condition": true,
                            "conditionGroup": ""
                        }, {
                            "componentName": "TextField",
                            "id": "node_oclhiiibvs3",
                            "props": {
                                "htmlType": "textarea",
                                "__category__": "form",
                                "__useMediator": "value",
                                "label": {"type": "JSExpression", "value": "this.item.label", "extType": "variable"},
                                "value": {"type": "JSExpression", "value": "this.item.value", "extType": "variable"},
                                "labelAlign": "left",
                                "labelColSpan": 4,
                                "labelTextAlign": "left",
                                "placeholder": {
                                    "type": "JSExpression",
                                    "value": "this.item.placeholder",
                                    "extType": "variable"
                                },
                                "tips": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "size": "medium",
                                "behavior": "NORMAL",
                                "labelTipsTypes": "text",
                                "labelTipsText": {
                                    "type": "JSExpression",
                                    "value": "this.item.labelTipsText",
                                    "extType": "variable"
                                },
                                "rows": 1,
                                "maxLength": 200,
                                "addonBefore": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "addonAfter": {
                                    "type": "JSExpression",
                                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                                },
                                "validation": [],
                                "__style__": {},
                                "fieldId": "textField_lhit7ruy",
                                "labelColOffset": 0,
                                "wrapperColSpan": 0,
                                "wrapperColOffset": 0,
                                "labelTipsIcon": "help",
                                "state": "",
                                "hasLimitHint": false,
                                "autoHeight": true,
                                "hasClear": false,
                                "trim": true,
                                "useI18nInput": false,
                                "autoFocus": true,
                                "cutString": false,
                                "fieldName": {
                                    "type": "JSExpression",
                                    "value": "this.item.fieldName",
                                    "extType": "variable"
                                },
                                "labelTips": {
                                    "type": "JSExpression",
                                    "value": "this.item.labelTipsText",
                                    "extType": "variable"
                                }
                            },
                            "title": "文本框",
                            "hidden": false,
                            "isLocked": false,
                            "condition": true,
                            "conditionGroup": "",
                            "loop": {
                                "type": "JSExpression",
                                "value": "state.editInfo.editFieldInfo",
                                "extType": "variable"
                            },
                            "loopArgs": ["item", "index"]
                        }]
                    }]
                }, {
                    "componentName": "RootHeader",
                    "id": "node_oclhfyjw0s2",
                    "props": {},
                    "hidden": false,
                    "title": "",
                    "isLocked": false,
                    "condition": true,
                    "conditionGroup": ""
                }, {
                    "componentName": "RootContent",
                    "id": "node_oclhfyjw0s3",
                    "props": {"contentMargin": "20", "contentPadding": "20", "contentBgColor": "white"},
                    "hidden": false,
                    "title": "",
                    "isLocked": false,
                    "condition": true,
                    "conditionGroup": "",
                    "children": [{
                        "componentName": "PageSection",
                        "id": "node_oclhg2iq2vu",
                        "props": {
                            "showHeader": false,
                            "title": {
                                "type": "JSExpression",
                                "value": "this.utils.getLocale() === 'en_US' ? (\"Title\" == 'null' ? null :\"Title\") : (\"卡片标题\" == 'null' ? null : \"卡片标题\")"
                            },
                            "tooltip": {
                                "type": "JSExpression",
                                "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                            },
                            "subTitle": {
                                "type": "JSExpression",
                                "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                            },
                            "extra": {
                                "type": "JSExpression",
                                "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"\" == 'null' ? null : \"\")"
                            },
                            "showHeadDivider": true,
                            "withPadding": true,
                            "__style__": {},
                            "fieldId": "pageSection_lhh200hm"
                        },
                        "title": "无标题",
                        "hidden": false,
                        "isLocked": false,
                        "condition": true,
                        "conditionGroup": "",
                        "children": [{
                            "componentName": "TabsLayout",
                            "id": "node_oclhiiibvsh",
                            "props": {
                                "shape": "capsule",
                                "size": "medium",
                                "contentPadding": "20px 20px",
                                "excessMode": "slide",
                                "tabPosition": "top",
                                "__style__": {},
                                "fieldId": "tabsLayout_lhit7rvc",
                                "onTabChange": {
                                    "type": "JSExpression",
                                    "value": "this.utils.legaoBuiltin.execEventFlow.bind(this, [this.onTabChange])",
                                    "events": [{"type": "JSExpression", "value": "onTabChange.bind(this)"}]
                                },
                                "isItemsVariable": false,
                                "items": [{
                                    "title": {
                                        "type": "JSExpression",
                                        "value": "this.utils.getLocale() === 'en_US' ? (\"Tab Item\" == 'null' ? null :\"Tab Item\") : (\"数据源\" == 'null' ? null : \"数据源\")"
                                    },
                                    "primaryKey": "tab_lhiiidbj",
                                    "defaultActived": true,
                                    "closeable": false,
                                    "disabled": false,
                                    "__sid": "item_lhilcm9v"
                                }, {
                                    "title": {
                                        "type": "JSExpression",
                                        "value": "this.utils.getLocale() === 'en_US' ? (\"Tab Item\" == 'null' ? null :\"Tab Item\") : (\"组件\" == 'null' ? null : \"组件\")"
                                    },
                                    "primaryKey": "tab_lhiiidbk",
                                    "closeable": false,
                                    "disabled": false,
                                    "__sid": "item_lhilcm9w"
                                }, {
                                    "title": {
                                        "type": "JSExpression",
                                        "value": "this.utils.getLocale() === 'en_US' ? (\"Tabs Layout\" == 'null' ? null :\"Tabs Layout\") : (\"公共配置\" == 'null' ? null : \"公共配置\")"
                                    },
                                    "primaryKey": "tab_lhiiidbp",
                                    "closeable": false,
                                    "disabled": false,
                                    "__sid": "item_lhilcmal"
                                }, {
                                    "title": {
                                        "type": "JSExpression",
                                        "value": "this.utils.getLocale() === 'en_US' ? (\"Tabs Layout\" == 'null' ? null :\"Tabs Layout\") : (\"变量\" == 'null' ? null : \"变量\")"
                                    },
                                    "primaryKey": "tab_lhiiidbq",
                                    "closeable": false,
                                    "disabled": false,
                                    "__sid": "item_lhilcmba"
                                }],
                                "unmountInactiveTabs": false,
                                "traceable": false,
                                "forceRender": false,
                                "needBadge": false,
                                "defaultActiveKey": "tab_lhiiidbj"
                            },
                            "title": "胶囊型",
                            "hidden": false,
                            "isLocked": false,
                            "condition": true,
                            "conditionGroup": "",
                            "children": [{
                                "componentName": "Tab",
                                "id": "node_oclhiiibvsi",
                                "props": {
                                    "primaryKey": "tab_lhiiidbj",
                                    "title": {
                                        "type": "JSExpression",
                                        "value": "this.utils.getLocale() === 'en_US' ? (\"Tab Item\" == 'null' ? null :\"Tab Item\") : (\"数据源\" == 'null' ? null : \"数据源\")"
                                    },
                                    "closeable": false,
                                    "disabled": false
                                },
                                "hidden": false,
                                "title": "",
                                "isLocked": false,
                                "condition": true,
                                "conditionGroup": "",
                                "children": [{
                                    "componentName": "TablePc",
                                    "id": "node_oclhfyjw0s12",
                                    "props": {
                                        "noPadding": false,
                                        "columns": {
                                            "type": "JSExpression",
                                            "value": "state.dsListTitle",
                                            "extType": "variable"
                                        },
                                        "dataSourceType": "data",
                                        "data": {"type": "JSExpression", "value": "state.list", "extType": "variable"},
                                        "primaryKey": "id",
                                        "actionTitle": {
                                            "type": "JSExpression",
                                            "value": "this.utils.getLocale() === 'en_US' ? (\"Action\" == 'null' ? null :\"Action\") : (\"操作\" == 'null' ? null : \"操作\")"
                                        },
                                        "actionType": "link",
                                        "actionFixed": "none",
                                        "maxWebShownActionCount": 3,
                                        "actionColumn": [{
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"Detail\" == 'null' ? null :\"Detail\") : (\"详情\" == 'null' ? null : \"详情\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {
                                                "type": "JSExpression",
                                                "value": "onInfoActionClick.bind(this)"
                                            },
                                            "render": {"type": "JSExpression", "value": "infoActionRender.bind(this)"},
                                            "__sid": "item_lhilcm97"
                                        }, {
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"action\" == 'null' ? null :\"action\") : (\"编辑\" == 'null' ? null : \"编辑\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {"type": "JSExpression", "value": "onEditBtnClick.bind(this)"},
                                            "__sid": "item_lhilcm98"
                                        }, {
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"action\" == 'null' ? null :\"action\") : (\"删除\" == 'null' ? null : \"删除\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {
                                                "type": "JSExpression",
                                                "value": "onDelActionClick.bind(this)"
                                            },
                                            "__sid": "item_lhkbsozx"
                                        }],
                                        "showActionBar": true,
                                        "actionBar": [{
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"Action\" == 'null' ? null :\"Action\") : (\"新增\" == 'null' ? null : \"新增\")"
                                            },
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {"type": "JSExpression", "value": "onAddBtnClick.bind(this)"},
                                            "__sid": "item_lhh0z0wx"
                                        }],
                                        "showLinkBar": true,
                                        "linkBar": [],
                                        "showSearch": true,
                                        "searchBarPlaceholder": {
                                            "type": "JSExpression",
                                            "value": "this.utils.getLocale() === 'en_US' ? (\"Please Input\" == 'null' ? null :\"Please Input\") : (\"请搜索\" == 'null' ? null : \"请搜索\")"
                                        },
                                        "theme": "split",
                                        "hasHeader": true,
                                        "rowSelection": {"mode": "multiple", "selectedRowKeys": ""},
                                        "isPagination": true,
                                        "pagination": {
                                            "paginationPosition": "right",
                                            "size": "medium",
                                            "type": "normal",
                                            "shape": "arrow-only",
                                            "pageSizeSelector": false,
                                            "pageSizeList": [5, 10, 20],
                                            "pageSize": 10,
                                            "pageSizePosition": "end",
                                            "pageShowCount": 5,
                                            "hideOnlyOnePage": false,
                                            "showJump": true,
                                            "showMiniPager": false
                                        },
                                        "indent": 16,
                                        "hasExpandedRowCtrl": true,
                                        "expandedRowIndent": "[]",
                                        "getExpandedColProps": "function getExpandedColProps(record, index) {  }",
                                        "mobileMode": "normal",
                                        "mobileExpandViewMode": "normal",
                                        "__router": {
                                            "type": "JSExpression",
                                            "value": "this.utils.router",
                                            "extType": "variable"
                                        },
                                        "fieldId": "table_list",
                                        "loading": false,
                                        "actionWidth": 0,
                                        "actionHidden": false,
                                        "showCustomColumn": false,
                                        "showCustomBarItem": false,
                                        "fixedHeader": false,
                                        "stickyHeader": false,
                                        "useVirtual": false,
                                        "setLoadingComponent": false,
                                        "setEmptyContent": false,
                                        "showRowSelector": false,
                                        "isExpand": false,
                                        "isTree": false,
                                        "expandedRowRender": "",
                                        "openRowKeys": "",
                                        "onRowOpen": "",
                                        "mobileMargin": 0,
                                        "onFetchData": {
                                            "type": "JSExpression",
                                            "value": "this.utils.legaoBuiltin.execEventFlow.bind(this, [this.onListFetchData])",
                                            "events": [{"type": "JSExpression", "value": "onListFetchData.bind(this)"}]
                                        }
                                    },
                                    "title": "表单",
                                    "hidden": false,
                                    "isLocked": false,
                                    "condition": true,
                                    "conditionGroup": ""
                                }]
                            }, {
                                "componentName": "Tab",
                                "id": "node_oclhiiibvsj",
                                "props": {
                                    "primaryKey": "tab_lhiiidbk",
                                    "title": {
                                        "type": "JSExpression",
                                        "value": "this.utils.getLocale() === 'en_US' ? (\"Tab Item\" == 'null' ? null :\"Tab Item\") : (\"组件\" == 'null' ? null : \"组件\")"
                                    },
                                    "closeable": false,
                                    "disabled": false
                                },
                                "hidden": false,
                                "title": "",
                                "isLocked": false,
                                "condition": true,
                                "conditionGroup": "",
                                "children": [{
                                    "componentName": "TablePc",
                                    "id": "node_oclhiiibvs1b",
                                    "props": {
                                        "noPadding": false,
                                        "columns": {
                                            "type": "JSExpression",
                                            "value": "state.cListTitle",
                                            "extType": "variable"
                                        },
                                        "dataSourceType": "data",
                                        "data": {"type": "JSExpression", "value": "state.list", "extType": "variable"},
                                        "primaryKey": "id",
                                        "actionTitle": {
                                            "type": "JSExpression",
                                            "value": "this.utils.getLocale() === 'en_US' ? (\"Action\" == 'null' ? null :\"Action\") : (\"操作\" == 'null' ? null : \"操作\")"
                                        },
                                        "actionType": "link",
                                        "actionFixed": "none",
                                        "maxWebShownActionCount": 3,
                                        "actionColumn": [{
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"Detail\" == 'null' ? null :\"Detail\") : (\"详情\" == 'null' ? null : \"详情\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {
                                                "type": "JSExpression",
                                                "value": "onInfoActionClick.bind(this)"
                                            },
                                            "render": {"type": "JSExpression", "value": "infoActionRender.bind(this)"},
                                            "__sid": "item_lhilcm97"
                                        }, {
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"action\" == 'null' ? null :\"action\") : (\"编辑\" == 'null' ? null : \"编辑\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {"type": "JSExpression", "value": "onEditBtnClick.bind(this)"},
                                            "__sid": "item_lhilcm98"
                                        }, {
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"action\" == 'null' ? null :\"action\") : (\"删除\" == 'null' ? null : \"删除\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {
                                                "type": "JSExpression",
                                                "value": "onDelActionClick.bind(this)"
                                            },
                                            "__sid": "item_lhkbsozz"
                                        }],
                                        "showActionBar": true,
                                        "actionBar": [{
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"Action\" == 'null' ? null :\"Action\") : (\"新增\" == 'null' ? null : \"新增\")"
                                            },
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {"type": "JSExpression", "value": "onAddBtnClick.bind(this)"},
                                            "__sid": "item_lhh0z0wx"
                                        }],
                                        "showLinkBar": true,
                                        "linkBar": [],
                                        "showSearch": true,
                                        "searchBarPlaceholder": {
                                            "type": "JSExpression",
                                            "value": "this.utils.getLocale() === 'en_US' ? (\"Please Input\" == 'null' ? null :\"Please Input\") : (\"请搜索\" == 'null' ? null : \"请搜索\")"
                                        },
                                        "theme": "split",
                                        "hasHeader": true,
                                        "rowSelection": {"mode": "multiple", "selectedRowKeys": ""},
                                        "isPagination": true,
                                        "pagination": {
                                            "paginationPosition": "right",
                                            "size": "medium",
                                            "type": "normal",
                                            "shape": "arrow-only",
                                            "pageSizeSelector": false,
                                            "pageSizeList": [5, 10, 20],
                                            "pageSize": 10,
                                            "pageSizePosition": "end",
                                            "pageShowCount": 5,
                                            "hideOnlyOnePage": false,
                                            "showJump": true,
                                            "showMiniPager": false
                                        },
                                        "indent": 16,
                                        "hasExpandedRowCtrl": true,
                                        "expandedRowIndent": "[]",
                                        "getExpandedColProps": "function getExpandedColProps(record, index) {  }",
                                        "mobileMode": "normal",
                                        "mobileExpandViewMode": "normal",
                                        "__router": {
                                            "type": "JSExpression",
                                            "value": "this.utils.router",
                                            "extType": "variable"
                                        },
                                        "fieldId": "tablePc_lhit7rve",
                                        "loading": false,
                                        "actionWidth": 0,
                                        "actionHidden": false,
                                        "showCustomColumn": false,
                                        "showCustomBarItem": false,
                                        "fixedHeader": false,
                                        "stickyHeader": false,
                                        "useVirtual": false,
                                        "setLoadingComponent": false,
                                        "setEmptyContent": false,
                                        "showRowSelector": false,
                                        "isExpand": false,
                                        "isTree": false,
                                        "expandedRowRender": "",
                                        "openRowKeys": "",
                                        "onRowOpen": "",
                                        "mobileMargin": 0,
                                        "onFetchData": {
                                            "type": "JSExpression",
                                            "value": "this.utils.legaoBuiltin.execEventFlow.bind(this, [this.onListFetchData])",
                                            "events": [{"type": "JSExpression", "value": "onListFetchData.bind(this)"}]
                                        }
                                    },
                                    "title": "表单",
                                    "hidden": false,
                                    "isLocked": false,
                                    "condition": true,
                                    "conditionGroup": ""
                                }]
                            }, {
                                "componentName": "Tab",
                                "id": "node_oclhiiibvs18",
                                "props": {
                                    "title": {
                                        "type": "JSExpression",
                                        "value": "this.utils.getLocale() === 'en_US' ? (\"Tabs Layout\" == 'null' ? null :\"Tabs Layout\") : (\"公共配置\" == 'null' ? null : \"公共配置\")"
                                    },
                                    "primaryKey": "tab_lhiiidbp",
                                    "closeable": false,
                                    "disabled": false,
                                    "__sid": "item_lhilcmal"
                                },
                                "hidden": false,
                                "title": "",
                                "isLocked": false,
                                "condition": true,
                                "conditionGroup": "",
                                "children": [{
                                    "componentName": "TablePc",
                                    "id": "node_oclhiiibvs1c",
                                    "props": {
                                        "noPadding": false,
                                        "columns": {
                                            "type": "JSExpression",
                                            "value": "state.ccListTitle",
                                            "extType": "variable"
                                        },
                                        "dataSourceType": "data",
                                        "data": {"type": "JSExpression", "value": "state.list", "extType": "variable"},
                                        "primaryKey": "id",
                                        "actionTitle": {
                                            "type": "JSExpression",
                                            "value": "this.utils.getLocale() === 'en_US' ? (\"Action\" == 'null' ? null :\"Action\") : (\"操作\" == 'null' ? null : \"操作\")"
                                        },
                                        "actionType": "link",
                                        "actionFixed": "none",
                                        "maxWebShownActionCount": 3,
                                        "actionColumn": [{
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"Detail\" == 'null' ? null :\"Detail\") : (\"详情\" == 'null' ? null : \"详情\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {
                                                "type": "JSExpression",
                                                "value": "onInfoActionClick.bind(this)"
                                            },
                                            "render": {"type": "JSExpression", "value": "infoActionRender.bind(this)"},
                                            "__sid": "item_lhilcm97"
                                        }, {
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"action\" == 'null' ? null :\"action\") : (\"编辑\" == 'null' ? null : \"编辑\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {"type": "JSExpression", "value": "onEditBtnClick.bind(this)"},
                                            "__sid": "item_lhilcm98"
                                        }, {
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"action\" == 'null' ? null :\"action\") : (\"删除\" == 'null' ? null : \"删除\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {
                                                "type": "JSExpression",
                                                "value": "onDelActionClick.bind(this)"
                                            },
                                            "__sid": "item_lhkbsp02"
                                        }],
                                        "showActionBar": true,
                                        "actionBar": [{
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"Action\" == 'null' ? null :\"Action\") : (\"新增\" == 'null' ? null : \"新增\")"
                                            },
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {"type": "JSExpression", "value": "onAddBtnClick.bind(this)"},
                                            "__sid": "item_lhh0z0wx"
                                        }],
                                        "showLinkBar": true,
                                        "linkBar": [],
                                        "showSearch": true,
                                        "searchBarPlaceholder": {
                                            "type": "JSExpression",
                                            "value": "this.utils.getLocale() === 'en_US' ? (\"Please Input\" == 'null' ? null :\"Please Input\") : (\"请搜索\" == 'null' ? null : \"请搜索\")"
                                        },
                                        "theme": "split",
                                        "hasHeader": true,
                                        "rowSelection": {"mode": "multiple", "selectedRowKeys": ""},
                                        "isPagination": true,
                                        "pagination": {
                                            "paginationPosition": "right",
                                            "size": "medium",
                                            "type": "normal",
                                            "shape": "arrow-only",
                                            "pageSizeSelector": false,
                                            "pageSizeList": [5, 10, 20],
                                            "pageSize": 10,
                                            "pageSizePosition": "end",
                                            "pageShowCount": 5,
                                            "hideOnlyOnePage": false,
                                            "showJump": true,
                                            "showMiniPager": false
                                        },
                                        "indent": 16,
                                        "hasExpandedRowCtrl": true,
                                        "expandedRowIndent": "[]",
                                        "getExpandedColProps": "function getExpandedColProps(record, index) {  }",
                                        "mobileMode": "normal",
                                        "mobileExpandViewMode": "normal",
                                        "__router": {
                                            "type": "JSExpression",
                                            "value": "this.utils.router",
                                            "extType": "variable"
                                        },
                                        "fieldId": "tablePc_lhit7rvf",
                                        "loading": false,
                                        "actionWidth": 0,
                                        "actionHidden": false,
                                        "showCustomColumn": false,
                                        "showCustomBarItem": false,
                                        "fixedHeader": false,
                                        "stickyHeader": false,
                                        "useVirtual": false,
                                        "setLoadingComponent": false,
                                        "setEmptyContent": false,
                                        "showRowSelector": false,
                                        "isExpand": false,
                                        "isTree": false,
                                        "expandedRowRender": "",
                                        "openRowKeys": "",
                                        "onRowOpen": "",
                                        "mobileMargin": 0,
                                        "onFetchData": {
                                            "type": "JSExpression",
                                            "value": "this.utils.legaoBuiltin.execEventFlow.bind(this, [this.onListFetchData])",
                                            "events": [{"type": "JSExpression", "value": "onListFetchData.bind(this)"}]
                                        }
                                    },
                                    "title": "表单",
                                    "hidden": false,
                                    "isLocked": false,
                                    "condition": true,
                                    "conditionGroup": ""
                                }]
                            }, {
                                "componentName": "Tab",
                                "id": "node_oclhiiibvs19",
                                "props": {
                                    "title": {
                                        "type": "JSExpression",
                                        "value": "this.utils.getLocale() === 'en_US' ? (\"Tabs Layout\" == 'null' ? null :\"Tabs Layout\") : (\"变量\" == 'null' ? null : \"变量\")"
                                    },
                                    "primaryKey": "tab_lhiiidbq",
                                    "closeable": false,
                                    "disabled": false,
                                    "__sid": "item_lhilcmba"
                                },
                                "hidden": false,
                                "title": "",
                                "isLocked": false,
                                "condition": true,
                                "conditionGroup": "",
                                "children": [{
                                    "componentName": "TablePc",
                                    "id": "node_oclhiiibvs1d",
                                    "props": {
                                        "noPadding": false,
                                        "columns": {
                                            "type": "JSExpression",
                                            "value": "state.vListTitle",
                                            "extType": "variable"
                                        },
                                        "dataSourceType": "data",
                                        "data": {"type": "JSExpression", "value": "state.list", "extType": "variable"},
                                        "primaryKey": "id",
                                        "actionTitle": {
                                            "type": "JSExpression",
                                            "value": "this.utils.getLocale() === 'en_US' ? (\"Action\" == 'null' ? null :\"Action\") : (\"操作\" == 'null' ? null : \"操作\")"
                                        },
                                        "actionType": "link",
                                        "actionFixed": "none",
                                        "maxWebShownActionCount": 3,
                                        "actionColumn": [{
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"Detail\" == 'null' ? null :\"Detail\") : (\"详情\" == 'null' ? null : \"详情\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {
                                                "type": "JSExpression",
                                                "value": "onInfoActionClick.bind(this)"
                                            },
                                            "render": {"type": "JSExpression", "value": "infoActionRender.bind(this)"},
                                            "__sid": "item_lhilcm97"
                                        }, {
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"action\" == 'null' ? null :\"action\") : (\"编辑\" == 'null' ? null : \"编辑\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {"type": "JSExpression", "value": "onEditBtnClick.bind(this)"},
                                            "__sid": "item_lhilcm98"
                                        }, {
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"action\" == 'null' ? null :\"action\") : (\"删除\" == 'null' ? null : \"删除\")"
                                            },
                                            "mode": "VIEW",
                                            "device": "",
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {
                                                "type": "JSExpression",
                                                "value": "onDelActionClick.bind(this)"
                                            },
                                            "__sid": "item_lhkbsp06"
                                        }],
                                        "showActionBar": true,
                                        "actionBar": [{
                                            "title": {
                                                "type": "JSExpression",
                                                "value": "this.utils.getLocale() === 'en_US' ? (\"Action\" == 'null' ? null :\"Action\") : (\"新增\" == 'null' ? null : \"新增\")"
                                            },
                                            "option": "callback",
                                            "pageMode": "",
                                            "callback": {"type": "JSExpression", "value": "onAddBtnClick.bind(this)"},
                                            "__sid": "item_lhh0z0wx"
                                        }],
                                        "showLinkBar": true,
                                        "linkBar": [],
                                        "showSearch": true,
                                        "searchBarPlaceholder": {
                                            "type": "JSExpression",
                                            "value": "this.utils.getLocale() === 'en_US' ? (\"Please Input\" == 'null' ? null :\"Please Input\") : (\"请搜索\" == 'null' ? null : \"请搜索\")"
                                        },
                                        "theme": "split",
                                        "hasHeader": true,
                                        "rowSelection": {"mode": "multiple", "selectedRowKeys": ""},
                                        "isPagination": true,
                                        "pagination": {
                                            "paginationPosition": "right",
                                            "size": "medium",
                                            "type": "normal",
                                            "shape": "arrow-only",
                                            "pageSizeSelector": false,
                                            "pageSizeList": [5, 10, 20],
                                            "pageSize": 10,
                                            "pageSizePosition": "end",
                                            "pageShowCount": 5,
                                            "hideOnlyOnePage": false,
                                            "showJump": true,
                                            "showMiniPager": false
                                        },
                                        "indent": 16,
                                        "hasExpandedRowCtrl": true,
                                        "expandedRowIndent": "[]",
                                        "getExpandedColProps": "function getExpandedColProps(record, index) {  }",
                                        "mobileMode": "normal",
                                        "mobileExpandViewMode": "normal",
                                        "__router": {
                                            "type": "JSExpression",
                                            "value": "this.utils.router",
                                            "extType": "variable"
                                        },
                                        "fieldId": "tablePc_lhit7rvg",
                                        "loading": false,
                                        "actionWidth": 0,
                                        "actionHidden": false,
                                        "showCustomColumn": false,
                                        "showCustomBarItem": false,
                                        "fixedHeader": false,
                                        "stickyHeader": false,
                                        "useVirtual": false,
                                        "setLoadingComponent": false,
                                        "setEmptyContent": false,
                                        "showRowSelector": false,
                                        "isExpand": false,
                                        "isTree": false,
                                        "expandedRowRender": "",
                                        "openRowKeys": "",
                                        "onRowOpen": "",
                                        "mobileMargin": 0,
                                        "onFetchData": {
                                            "type": "JSExpression",
                                            "value": "this.utils.legaoBuiltin.execEventFlow.bind(this, [this.onListFetchData])",
                                            "events": [{"type": "JSExpression", "value": "onListFetchData.bind(this)"}]
                                        }
                                    },
                                    "title": "表单",
                                    "hidden": false,
                                    "isLocked": false,
                                    "condition": true,
                                    "conditionGroup": ""
                                }]
                            }]
                        }]
                    }]
                }, {
                    "componentName": "RootFooter",
                    "id": "node_oclhfyjw0s4",
                    "props": {},
                    "hidden": false,
                    "title": "",
                    "isLocked": false,
                    "condition": true,
                    "conditionGroup": ""
                }],
                "state": {
                    "urlParams": {
                        "type": "JSExpression",
                        "value": "this.utils.legaoBuiltin.getUrlParams()",
                        "extType": "variable"
                    },
                    "listReq": {
                        "type": "JSExpression",
                        "value": "{\r\n    \"pageInfo\": {\r\n        \"pageNum\": 1,\r\n        \"pageSize\": 100,\r\n        \"needTotal\": true,\r\n        \"needList\": true,\r\n        \"fromId\": 0\r\n    },\r\n    \"condition\": {\r\n        \"id\": null\r\n    }\r\n}",
                        "extType": "variable"
                    },
                    "tabs": {
                        "type": "JSExpression",
                        "value": "[\r\n  {\"title\":\"数据源\",\"primaryKey\":\"tab_1\",\"closeable\":false,\"disabled\":false,\"customKey\":{},\"defaultActived\": true},\r\n  {\"title\":\"组件\",\"primaryKey\":\"tab_2\",\"closeable\":false,\"disabled\":false,\"customKey\":{}},\r\n  {\"title\":\"公共配置\",\"primaryKey\":\"tab_3\",\"closeable\":false,\"disabled\":false,\"customKey\":{}},\r\n  {\"title\":\"变量\",\"primaryKey\":\"tab_4\",\"closeable\":false,\"disabled\":false,\"customKey\":{}}\r\n]",
                        "extType": "variable"
                    },
                    "dsListTitle": {
                        "type": "JSExpression",
                        "value": "[\r\n  {\"title\":\"ID\",\"dataKey\":\"id\"},\r\n  {\"title\":\"名称\",\"dataKey\":\"name\"},\r\n  {\"title\":\"类型\",\"dataKey\":\"type\"},\r\n  {\"title\":\"生效\",\"dataKey\":\"isActive\"}\r\n]",
                        "extType": "variable"
                    },
                    "res": {
                        "type": "JSExpression",
                        "value": "{\"success\":true,\"msg\":\"ok\"}",
                        "extType": "variable"
                    },
                    "addFieldInfo": {
                        "type": "JSExpression",
                        "value": "[\r\n  {\"label\":\"name\",\"fieldId\":\"f_name\",\"fieldName\":\"name\"},\r\n  {\"label\":\"type\",\"fieldId\":\"f_type\",\"fieldName\":\"type\"},\r\n  {\"label\":\"isActive\",\"fieldId\":\"f_isActive\",\"fieldName\":\"isActive\"},\r\n  {\"label\":\"config\",\"fieldId\":\"f_config\",\"fieldName\":\"config\"}\r\n]",
                        "extType": "variable"
                    },
                    "domain": {"type": "JSExpression", "value": "\"ds\"", "extType": "variable"},
                    "addJsonObjField": {"type": "JSExpression", "value": "[\"config\"]", "extType": "variable"},
                    "editInfo": {"type": "JSExpression", "value": "{\"id\":0}", "extType": "variable"},
                    "urls": {
                        "type": "JSExpression",
                        "value": "{\r\n        listUrl: \"datasource/list\",\r\n        editUrl: \"datasource/edit\",\r\n        addUrl: \"datasource/add\",\r\n        delUrl: \"datasource/del\",\r\n        infoUrl: \"datasource/text\"\r\n      }",
                        "extType": "variable"
                    },
                    "urlPre": {"type": "JSExpression", "value": "\"../\"", "extType": "variable"},
                    "infoInfo": {"type": "JSExpression", "value": "\"\"", "extType": "variable"},
                    "cListTitle": {
                        "type": "JSExpression",
                        "value": "[\r\n          {\"title\":\"ID\",\"dataKey\":\"id\"},\r\n          {\"title\":\"名称\",\"dataKey\":\"name\"},\r\n          {\"title\":\"数据源类型\",\"dataKey\":\"dataSourceType\"},\r\n          {\"title\":\"数据源ID\",\"dataKey\":\"dataSourceId\"}\r\n        ]",
                        "extType": "variable"
                    },
                    "ccListTitle": {
                        "type": "JSExpression",
                        "value": "[\r\n  {\"title\":\"ID\",\"dataKey\":\"id\"},\r\n  {\"title\":\"分组\",\"dataKey\":\"group\"},\r\n  {\"title\":\"key\",\"dataKey\":\"key\"},\r\n  {\"title\":\"value\",\"dataKey\":\"value\"},\r\n  {\"title\":\"排序\",\"dataKey\":\"sort\"},\r\n]",
                        "extType": "variable"
                    },
                    "vListTitle": {
                        "type": "JSExpression",
                        "value": "[\r\n          {\"title\":\"ID\",\"dataKey\":\"id\"},\r\n          {\"title\":\"名称\",\"dataKey\":\"name\"},\r\n          {\"title\":\"值表达式\",\"dataKey\":\"cresPath\"},\r\n          {\"title\":\"类型\",\"dataKey\":\"type\"},\r\n          {\"title\":\"类型包路径\",\"dataKey\":\"typePath\"},\r\n          {\"title\":\"域\",\"dataKey\":\"domain\"},\r\n          {\"title\":\"渲染器ID\",\"dataKey\":\"rendererId\"},\r\n          {\"title\":\"业务编码\",\"dataKey\":\"busiCode\"},\r\n          {\"title\":\"备注\",\"dataKey\":\"note\"},\r\n        ]",
                        "extType": "variable"
                    },
                    "infoString": {"type": "JSExpression", "value": "\"\"", "extType": "variable"}
                }
            }],
            "id": "FORM-343918AA0F25455D86011C04BE52B7F1",
            "params": {"slug": "config"}
        }],
        "nav": {
            "data": [{
                "config": null,
                "id": "1291",
                "gmtModified": 1683617704047,
                "formUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                "title": {
                    "type": "JSExpression",
                    "value": "this.utils.getLocale() === 'en_US' ? (\"\" == 'null' ? null :\"\") : (\"配置页\" == 'null' ? null : \"配置页\")"
                },
                "navUuid": "FORM-343918AA0F25455D86011C04BE52B7F1",
                "listOrder": 0,
                "slug": "config",
                "navType": "PAGE",
                "parentNavUuid": "NAV-SYSTEM-PARENT-UUID",
                "url": null,
                "hidden": false,
                "targetNew": false,
                "inner": true
            }],
            "appName": {
                "type": "JSExpression",
                "value": "this.utils.getLocale() === 'en_US' ? (\"变量服务\" == 'null' ? null :\"变量服务\") : (\"变量服务\" == 'null' ? null : \"变量服务\")"
            },
            "singletons": {}
        }
    }
});