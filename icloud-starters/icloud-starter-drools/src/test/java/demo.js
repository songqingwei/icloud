export function helloPage() {
    // 你可以这么调用其他函数
    //this.func1();

    // 你可以这么调用组件的函数
    // this.$('textField_xxx').getValue();

    // 你可以这么使用「数据源面板」定义的「变量」
    // this.state.xxx

    // 你可以这么发送一个在「数据源面板」定义的「远程 API」
    // this.dataSourceMap['xxx'].load(data)

    // API 详见：https://lab.lowcode-engine.cn/help/docs/api/js-api
}


/**
 * 选项卡切换时触发
 * @param activeIndex
 * @param key
 */
export function onTabChange(activeIndex, key) {
    console.log(activeIndex, key);
    let sqlDialectConfigTips = "sql样例:{\"sql\":\"SELECT 1 FROM DUAL where 1=${1} and 2=#{2} and 3=#{3} and 4=#{4} and 5=#{5}\"}\n";
    let dubboDialectConfigTips = "dubbo样例:{\"method_type\":\"[\\n    \\\"java.util.map\\\"\\n  ]\",\"interfacename\":\"cn.isqing.icloud.policy.engine.dubbo.api.DemoApi\",\"method_name\":\"getMap\",\"version\":\"1.0.0\",\"group\":\"demo\",\"params\":\" [\\n    {\\n      \\\"1\\\": #{1},\\n      \\\"2\\\": #{2},\\n      \\\"5\\\": #{5}\\n    }\\n  ]\"}";
    switch (activeIndex) {
        case 1:
            this.state.domain = "ds";
            this.state.addFieldInfo = [
                {
                    "label": "name",
                    "fieldId": "f_name",
                    "fieldName": "name",
                    "labelTipsText": "数据源名称",
                    "placeholder": "请输入数据源名称"
                },
                {
                    "label": "type",
                    "fieldId": "f_type",
                    "fieldName": "type",
                    "labelTipsText": "数据源类型:1[sql]、2[dubbo]",
                    "placeholder": "请输入类型数字标识"
                },
                {
                    "label": "isActive",
                    "fieldId": "f_isActive",
                    "fieldName": "isActive",
                    "labelTipsText": "是否生效 0否 1是",
                    "placeholder": "请输入0/1"
                },
                {
                    "label": "config",
                    "fieldId": "f_config",
                    "fieldName": "config",
                    "labelTipsText": "数据源配置",
                    "placeholder": "请输入json字符串"
                }
            ];
            this.state.addJsonObjField = ["config"];
            this.state.listTile = [
                { "title": "ID", "dataKey": "id" },
                { "title": "名称", "dataKey": "name" },
                { "title": "类型", "dataKey": "type" },
                { "title": "生效", "dataKey": "isActive" }
            ];
            this.state.urls = {
                listUrl: "datasource/list",
                editUrl: "datasource/edit",
                addUrl: "datasource/add",
                delUrl: "datasource/del",
                infoUrl: "datasource/text"
            }
            break;
        case 2:
            this.state.domain = "c";
            this.state.addFieldInfo = [
                {
                    "label": "name",
                    "fieldId": "f_name",
                    "fieldName": "name",
                    "labelTipsText": "组件名字",
                    "placeholder": "请输入中文"
                },
                {
                    "label": "dataSourceType",
                    "fieldId": "f_dataSourceType",
                    "fieldName": "dataSourceType",
                    "labelTipsText": "组件的数据源类型:1[sql]、2[dubbo]",
                    "placeholder": "请输入类型数字标识"
                },
                {
                    "label": "dataSourceId",
                    "fieldId": "f_dataSourceId",
                    "fieldName": "dataSourceId",
                    "labelTipsText": "数字数据源主键id",
                    "placeholder": "请输入数字数据源主键id"
                },
                {
                    "label": "dialectConfig",
                    "fieldId": "f_dialectConfig",
                    "fieldName": "dialectConfig",
                    "labelTipsText": sqlDialectConfigTips + dubboDialectConfigTips,
                    "placeholder": "请输入json字符串"
                },
                {
                    "label": "dependCids",
                    "fieldId": "f_dependCids",
                    "fieldName": "dependCids",
                    "labelTipsText": "依赖的组件主键数组",
                    "placeholder": "请输入json数组如[1,2]"
                },
                {
                    "label": "dependInputParams",
                    "fieldId": "f_dependInputParams",
                    "fieldName": "dependInputParams",
                    "labelTipsText": "依赖的入参 map<占位标识,jsonpath>",
                    "placeholder": "请输入json字符串"
                },
                {
                    "label": "dependCRes",
                    "fieldId": "f_dependCRes",
                    "fieldName": "dependCRes",
                    "labelTipsText": "依赖的组件结果 map<占位标识,jsonpath>",
                    "placeholder": "请输入json字符串"
                },
                {
                    "label": "dependConstantParams",
                    "fieldId": "f_dependConstantParams",
                    "fieldName": "dependConstantParams",
                    "labelTipsText": "依赖的系统配置常量 map<占位标识,jsonpath>",
                    "placeholder": "请输入json字符串"
                },
                {
                    "label": "selfConstants",
                    "fieldId": "f_selfConstants",
                    "fieldName": "selfConstants",
                    "labelTipsText": "自身常量属性 map<占位标识,常量值>",
                    "placeholder": "请输入json字符串"
                },
                {
                    "label": "dependSystemVars",
                    "fieldId": "f_dependSystemVars",
                    "fieldName": "dependSystemVars",
                    "labelTipsText": "依赖的系统内置变量 map<占位标识,系统变量标识[如uuid]>",
                    "placeholder": "请输入json字符串"
                },
                {
                    "label": "resJudge",
                    "fieldId": "f_resJudge",
                    "fieldName": "resJudge",
                    "labelTipsText": "执行结果判断 [jsonpath1,jsonpath2]",
                    "placeholder": "请输入json数组"
                }
            ];
            this.state.addJsonObjField = ["dependCids", "dependInputParams", "dependCRes",
                "dependConstantParams", "selfConstants", "dependSystemVars", "resJudge"];
            this.state['cListReq'] = {
                "pageInfo": {
                    "pageNum": 1,
                    "pageSize": 20,
                    "needTotal": true,
                    "needList": true,
                    "fromId": 0
                },
                "condition": {
                    "id": null
                }
            };
            this.state.urls = {
                listUrl: "component/list",
                editUrl: "component/edit",
                addUrl: "component/add",
                delUrl: "component/del",
                infoUrl: "component/text",
            }
            break;
        case 3:
            this.state.domain = "cc";
            this.state.addFieldInfo = [
                {
                    "label": "group",
                    "fieldId": "f_group",
                    "fieldName": "group",
                    "labelTipsText": "配置组:\n12:渲染器",
                    "placeholder": "请输入配置组标识"
                },
                { "label": "key", "fieldId": "f_key", "fieldName": "key", "labelTipsText": "key", "placeholder": "请输入key" },
                {
                    "label": "value",
                    "fieldId": "f_value",
                    "fieldName": "value",
                    "labelTipsText": "value 最长2000 超长应该拆分多条 按顺序添加",
                    "placeholder": "请输入value"
                },
                {
                    "label": "sort",
                    "fieldId": "f_sort",
                    "fieldName": "sort",
                    "labelTipsText": "排序值",
                    "placeholder": "请输入排序数字"
                },
            ];
            this.state.addJsonObjField = [];
            this.state.urls = {
                listUrl: "common_config/list",
                editUrl: "common_config/edit",
                addUrl: "common_config/add",
                delUrl: "common_config/del",
                infoUrl: null
            }
            break;
        case 4:
            this.state.domain = "rt";
            this.state.addFieldInfo = [
                {
                    "label": "domain",
                    "fieldId": "f_domain",
                    "fieldName": "domain",
                    "labelTipsText": "域",
                    "placeholder": "请输入整数数字"
                },
                {
                    "label": "name",
                    "fieldId": "f_name",
                    "fieldName": "name",
                    "labelTipsText": "规则模版名称",
                    "placeholder": "请输入规则模版名称"
                },
                {
                    "label": "comment",
                    "fieldId": "f_comment",
                    "fieldName": "comment",
                    "labelTipsText": "描述",
                    "placeholder": "描述"
                },
                {
                    "label": "actionId",
                    "fieldId": "f_actionId",
                    "fieldName": "actionId",
                    "labelTipsText": "动作",
                    "placeholder": "请输入数字"
                },
                {
                    "label": "cron",
                    "fieldId": "f_cron",
                    "fieldName": "cron",
                    "labelTipsText": "cron时间表达式",
                    "placeholder": "请输入cron时间表达式"
                },
                {
                    "label": "targetRatio",
                    "fieldId": "f_targetRatio",
                    "fieldName": "targetRatio",
                    "labelTipsText": "目标分配比例 json List<Long>",
                    "placeholder": "请输入目标分配比例 json List<Long>"
                },
                {
                    "label": "targetName",
                    "fieldId": "f_targetName",
                    "fieldName": "targetName",
                    "labelTipsText": "目标名称映射 json map<Long,String>",
                    "placeholder": "请输入目标名称映射 json map<Long,String>"
                },
                {
                    "label": "allocationModel",
                    "fieldId": "f_allocationModel",
                    "fieldName": "allocationModel",
                    "labelTipsText": "分配算法:1:固定数量 2:比例 3:高精度比例",
                    "placeholder": "请输入分配算法标识数字,1:固定数量 2:比例 3:高精度比例"
                },
                {
                    "label": "content",
                    "fieldId": "f_content",
                    "fieldName": "content",
                    "labelTipsText": "规则内容",
                    "placeholder": "请输入规则内容"
                },
                {
                    "label": "isActive",
                    "fieldId": "f_isActive",
                    "fieldName": "isActive",
                    "labelTipsText": "是否启用",
                    "placeholder": "0未启用 1启用"
                },
                {
                    "label": "busiMap",
                    "fieldId": "f_busiMap",
                    "fieldName": "busiMap",
                    "labelTipsText": "关联业务Map<busiCode,busiName>",
                    "placeholder": "请输入json：Map<busiCode,busiName>"
                },
                {
                    "label": "ref",
                    "fieldId": "f_ref",
                    "fieldName": "ref",
                    "labelTipsText": "蛇形算法参照物",
                    "placeholder": "蛇形算法参照物"
                },
            ];
            this.state.addJsonObjField = ["targetRatio","targetName","busiMap"];

            this.state.urls = {
                listUrl: "variable/list",
                editUrl: "variable/edit",
                addUrl: "variable/add",
                delUrl: "variable/del",
                infoUrl: null,
            }
            break;
    }
    this.state[this.state.domain + 'ListReq'] = {
        "pageInfo": {
            "pageNum": 1,
            "pageSize": 20,
            "needTotal": true,
            "needList": true,
            "fromId": 0
        },
        "condition": {
            "id": null
        }
    };
    this.dataSourceMap['list'].load();
}


export function onAddBtnClick() {
    this.$('dialog_add').show();
}


/**
 * dialog onOk
 */
export function onAddOk() {
    const that = this;
    this.$('form_add').submit((data, error) => {
        if (data) {
            console.log(data);
            this.dataSourceMap['add'].load(data).then(() => {
                if (that.state.res.success) {
                    this.utils.toast({
                        type: 'success',
                        title: '提交成功'
                    });
                } else {
                    this.utils.toast({
                        type: 'error',
                        title: that.state.res.msg
                    });
                }

                this.$('dialog_add').hide();
                this.state['listReq'].pageInfo.pageNum = 1;
                this.dataSourceMap['list'].load();
                this.$('form_add').reset();
            }).catch((reason) => {
                this.utils.toast({
                    type: 'error',
                    title: reason
                });
            });
        }
    });
}

export function onEditOk() {
    const that = this;
    this.$('form_edit').submit((data, error) => {
        if (data) {
            console.log(data);
            this.dataSourceMap['edit'].load(data).then(() => {
                if (that.state.res.success) {
                    this.utils.toast({
                        type: 'success',
                        title: '提交成功'
                    });
                } else {
                    this.utils.toast({
                        type: 'error',
                        title: that.state.res.msg
                    });
                }

                this.$('dialog_edit').hide();
                this.state['listReq'].pageInfo.pageNum = 1;
                this.dataSourceMap['list'].load();
                this.$('form_edit').reset();
            }).catch((reason) => {
                this.utils.toast({
                    type: 'error',
                    title: reason
                });
            });
        }
    });
}


/**
 * tablePc onFetchData
 * @param params.currentPage 当前页码
 * @param params.pageSize 每页显示条数
 * @param params.searchKey 搜索关键字
 * @param params.orderColumn 排序列
 * @param params.orderType 排序方式（desc,asc）
 * @param params.from 触发来源（order,search,pagination）
 */
export function onListFetchData(params) {
    // 如果是搜索的话翻页重置到 1
    if (params.from === 'search') {
        params.currentPage = 1;
    }
    this.state['listReq'].pageInfo.pageNum = params.currentPage;
    this.dataSourceMap['list'].load();
}

export function onEditBtnClick(rowData) {
    let that = this
    if (this.state.urls.infoUrl !== null) {
        this.dataSourceMap["info"].load({ id: rowData.id }).then(() => {
            editShow(rowData, that);
        }).catch((reason) => {
            this.utils.toast({
                type: 'error',
                title: reason
            });
        });
    } else {
        editShow(rowData, that);
    }

}

export function editShow(rowData, that) {
    that.state.editInfo.id = rowData.id;
    let arr = [];
    that.state.addFieldInfo.forEach(one => {
        let clone = Object.assign({}, one);
        let key = clone.fieldName;
        console.log("key:" + key);
        console.log("info:", that.state.infoInfo);
        if (rowData[key] !== null) {
            clone["value"] = rowData[key];
        } else if (that.state.infoInfo.content !== null && that.state.infoInfo.content.hasOwnProperty(key) && that.state.infoInfo.content[key] !== null) {
            clone["value"] = stringify(that.state.infoInfo.content[key]);

        }
        arr.push(clone);
    });
    that.state.editInfo.editFieldInfo = arr;
    that.$('dialog_edit').show();
}

//定义一个函数，接受一个对象作为参数
function stringify(obj) {
    //如果对象是字符串，直接返回
    if (typeof obj === "string") {
        return obj;
    }
    //否则，使用JSON.stringify方法将对象转为json字符串，并返回
    else {
        return JSON.stringify(obj, null, 4);
    }
}

export function infoActionRender(title, rowData) {
    if (this.state.urls.infoUrl == null) {
        return false;
    }
    return title;  // return false 则不显示
}


export function onInfoActionClick(rowData) {
    this.dataSourceMap["info"].load({ id: rowData.id });
    this.$('drawer_info').show();
}


export function onDelActionClick(rowData) {
    this.dataSourceMap["del"].load({ id: rowData.id }).then(() => {
        if (this.state.res.success) {
            this.utils.toast({
                type: 'success',
                title: '提交成功'
            });
            this.state['listReq'].pageInfo.pageNum = 1;
            this.dataSourceMap['list'].load();
        } else {
            this.utils.toast({
                type: 'error',
                title: that.state.res.msg
            });
        }
    }).catch((reason) => {
        this.utils.toast({
            type: 'error',
            title: reason
        });
    });
}
