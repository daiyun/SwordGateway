$(document).ready(function () {
        let serverTableInit = false;
        savePayLoad();
        clickPayloadItemAdd();
        $('#payload-table').on('init.dt', function (e, settings) {
            let api = new $.fn.dataTable.Api(settings);
            api.on('draw.dt', function (e, settings) {
                clickPayloadItem(api);
            });
            clickPayloadItem(api);
        }).DataTable({
            ordering: false,
            searching: false,//启用搜索功能
            serverSide: true,//启用服务端分页（这是使用Ajax服务端的必须配置）
            ajax: {
                url: Api.payload.list,
                type: "post",
                async: true,
                processing: true,
                contentType: "application/json; charset=UTF-8",
                data: function (d) {
                    let jsonData = {"pageNum": d.start, "pageSize": d.length};
                    return JSON.stringify(jsonData);
                },
                dataSrc: function (data) {
                    if (data.code === 0) {
                        toastr.success("请求成功");
                        let json = data.data;
                        let thisSettings = $(this).dataTableSettings[0];
                        thisSettings._iRecordsTotal = json.totalCount;
                        thisSettings._iRecordsDisplay = json.totalCount;
                        return json.list;
                    } else {
                        toastr['error'](data.msg, "路由列表响应错误")
                    }
                }
            },
            "columns": [
                {title: "负载器标识", "data": "lbMark"},
                {title: "负载器名称", "data": "lbName"},
                {title: "负载器类型", "data": "lbType"},
                {title: "负载器备注", "data": "lbComment"},
                {title: "服务发现", "data": "dscrEnable"},
                {title: "服务发现标识", "data": "dscrId"},
                {title: "状态", "data": "lbStatus"},
                {title: "服务列表", "data": null},
                {title: "编辑", "data": null},
                {title: "删除", "data": null},
                {title: "发布配置", "data": null}
            ],
            "columnDefs": [{
                "targets": 4,
                "render": function (data, type, full, meta) {
                    if (data === 0)
                        return "未启用";
                    else if (data === 1)
                        return "启用";
                    else
                        return "未知";
                }
            }, {
                "targets": 6,
                "render": function (data, type, full, meta) {
                    if (data === 0)
                        return "尚未启用";
                    else if (data === 1)
                        return "已启用";
                    else if (data === 2)
                        return "已禁用";
                    else
                        return "未知";
                }
            }, {
                "targets": 7,
                "render": function (data, type, full, meta) {
                    return "<button type='button' class='btn btn-info payload-item-server  data-payloadMark='" + full.lbMark + "'><i class='fa fa-server'></i>服务列表</button>";
                }
            }, {
                "targets": 8,
                "render": function (data, type, full, meta) {
                    return "<button type='button' class='btn btn-info payload-item-modify'><i class='fa fa-edit'></i>修改</button>";
                }
            }, {
                "targets": 9,
                "render": function (data, type, full, meta) {
                    return "<button type='button' class='btn btn-info payload-item-del'><i class='fa fa-trash-o'></i>删除</button>";
                }
            }, {
                "targets": 10,
                "render": function (data, type, full, meta) {
                    if (full.lbStatus === 0 || full.lbStatus === 2) {
                        return "<button type='button' class='btn btn-info payload-item-enable'><i class='fa fa-cloud-upload'></i>启用</button>";
                    } else if (full.lbStatus === 1) {
                        return "<button type='button' class='btn btn-info payload-item-disable'><i class='fa fa-cloud-upload'></i>禁用</button>";
                    } else {
                        return "unknown status";
                    }
                }
            }]
        });

        function savePayLoad() {
            $("#payloadEditModal .save").click(function () {
                let isEdit = $(this).data("edit");
                let lbType = $("#payloadType").val();
                let lbMark = $("#payloadMark").val();
                let lbName = $("#payloadName").val();
                let lbComment = $("#payloadComment").val();
                let lbRule = $("#payloadRule").val();

                let pingMode = $("#payloadPingMode").val();
                let pingStrategy = $("#payloadPingStrategy").val();
                let pingIntervalTime = $("#payloadPingIntervalTime").val();
                let pingMaxTotalPingTime = $("#payloadMaxTotalPingTime").val();
                let pingUrl = $("#payloadPingUrl").val();

                let payloadServerReload = $("#payloadServerReload").val();
                let initialDelayMs = $("#payloadRefreshInitialDelayMs").val();
                let intervalMs = $("#payloadRefreshIntervalMs").val();

                let dscrEnable = $("#payloadDscrEnable").val();
                let dscrId = $("#payloadDscrId").val();

                let formData = {
                    lbMark: lbMark,
                    lbType: lbType,
                    lbName: lbName,
                    lbComment: lbComment,
                    lbRule: lbRule
                };
                if (pingMode) {
                    formData.payloadPing = {
                        pingStrategy: pingStrategy,
                        pingIntervalTime: pingIntervalTime,
                        maxTotalPingTime: pingMaxTotalPingTime
                    };
                    if (pingStrategy === 'url') {
                        formData.payloadPing.pingUrl = pingUrl;
                    }
                }
                if (payloadServerReload === 1) {
                    formData.payloadServerReload = {
                        autoRefresh: true,
                        payloadRefreshInitialDelayMs: initialDelayMs,
                        payloadRefreshIntervalMs: intervalMs
                    }
                } else if (payloadServerReload === 0) {
                    formData.payloadServerReload = {
                        autoRefresh: false
                    }
                }
                if (dscrEnable == 1) {
                    formData.dscrEnable = 1;
                    formData.dscrId = dscrId;
                } else {
                    formData.dscrEnable = 0;
                }
                if (isEdit) {
                    $.ajaxPostJsonApi({
                        url: Api.payload.edit,
                        async: true,
                        data: JSON.stringify(formData),
                        success: function (data) {
                            if (data.code === 0) {
                                toastr.success("修改路由成功");
                                $("#payloadEditModal").modal("hide");
                                reloadTable('payload-table');
                            } else {
                                toastr['error'](data.msg, "修改负载器失败")
                            }
                        },
                        error: function (e) {
                            toastr['error'](e.status, "修改负载器失败");
                        }
                    })
                } else {
                    $.ajaxPostJsonApi({
                        url: Api.payload.add,
                        async: true,
                        data: JSON.stringify(formData),
                        success: function (data) {
                            if (data.code === 0) {
                                toastr.success("修改路由成功");
                                $("#payloadEditModal").modal("hide");
                                reloadTable('payload-table');
                            } else {
                                toastr['error'](data.msg, "新增负载器失败")
                            }
                        },
                        error: function (e) {
                            toastr['error'](e.status, "新增负载器失败");
                        }
                    })
                }
            })
        }

        function clickPayloadItem(api) {
            api.$('.payload-item-modify').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                $.ajaxGetApi({
                    url: Api.payload.get,
                    async: true,
                    data: {lbMark: formData.lbMark},
                    success: function (data) {
                        if (data.code === 0) {
                            let retData = data.data;
                            let payloadMarkInput = $("#payloadMark");
                            payloadMarkInput.val(retData.lbMark);
                            payloadMarkInput.attr("disabled", "disabled");
                            $("#payloadName").val(retData.lbName);
                            $("#payloadComment").val(retData.lbComment);
                            $("#payloadType").val(retData.lbType);
                            $("#payloadDscrEnable").val(retData.dscrEnable);
                            $("#payloadDscrId").val(retData.dscrId);
                            let payloadRule = retData.payloadRule;
                            if (payloadRule) {
                                $("#payloadRule").val(payloadRule.lbRule);
                            }
                            let payloadPing = retData.payloadPing;
                            if (payloadPing) {
                                $("#payloadPingMode").val(payloadPing.pingMode);
                                $("#payloadPingStrategy").val(payloadPing.pingStrategy);
                                $("#payloadPingIntervalTime").val(payloadPing.pingIntervalTime);
                                $("#payloadMaxTotalPingTime").val(payloadPing.maxTotalPingTime);
                                $("#payloadPingUrl").val(payloadPing.pingUrl);
                            }
                            let severReload = retData.serverReload;
                            if (severReload) {
                                let autoRefresh = severReload.autoRefresh;
                                let payloadRefreshInitialDelayMs = severReload.payloadRefreshInitialDelayMs;
                                let payloadRefreshIntervalMs = severReload.payloadRefreshIntervalMs;
                                if (autoRefresh === false) {
                                    $("#payloadServerReload").val(0);
                                } else if (autoRefresh === true) {
                                    $("#payloadServerReload").val(1);
                                } else {
                                    $("#payloadServerReload").val(-1);
                                }

                                if (payloadRefreshInitialDelayMs) {
                                    $("#payloadRefreshInitialDelayMs").val(payloadRefreshInitialDelayMs);
                                }
                                if (payloadRefreshIntervalMs) {
                                    $("#payloadRefreshIntervalMs").val(payloadRefreshIntervalMs);
                                }
                            }
                            $("#payloadEditModal .save").data("edit", true);
                            $("#payloadEditModal").modal("show");
                        } else {
                            toastr['error'](data.msg, "查询负载器信息失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "查询负载器信息失败");
                    }
                });
            });
            api.$('.payload-item-del').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                let data = {lbMark: formData.lbMark};
                $.ajaxPostJsonApi({
                    url: Api.payload.del,
                    async: true,
                    data: JSON.stringify(data),
                    success: function (data) {
                        if (data.code === 0) {
                            reloadTable('payload-table')
                        } else {
                            toastr['error'](data.msg, "删除负载器信息失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "删除负载器信息失败");
                    }
                });
            });
            api.$('.payload-item-enable').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                $.ajaxPostApi({
                    url: Api.payload.enable,
                    async: true,
                    data: {lbMark: formData.lbMark},
                    success: function (data) {
                        if (data.code === 0) {
                            reloadTable('payload-table')
                        } else {
                            toastr['error'](data.msg, "启用负载器失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "启用负载器失败");
                    }
                });
            });
            api.$('.payload-item-disable').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                $.ajaxPostApi({
                    url: Api.payload.disable,
                    async: true,
                    data: {lbMark: formData.lbMark},
                    success: function (data) {
                        if (data.code === 0) {
                            reloadTable('payload-table')
                        } else {
                            toastr['error'](data.msg, "禁用负载器失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "禁用负载器失败");
                    }
                })
            });
            api.$('.payload-item-server').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                serverTable(formData.lbMark);
                $("#serverEditModal").modal('show');
            });
        }

        function reloadTable(which) {
            var api = $('#' + which).dataTable().api();
            api.ajax.reload();
            api.draw();
        }

        function clickPayloadItemAdd() {
            $("#payload-item-add").click(function () {
                $("#payloadEditModal .save").data("edit", false);
                $("#payloadEditModal").modal("show");
            })
        }

        function updateServerTable(lbMark) {
            var api = $('#server-table').dataTable().api();
            api.ajax.url(Api.payload.serverList.format({lbMark: lbMark})).load();
            api.draw();
        }

        function serverTable(lbMark) {
            if (serverTableInit) {
                updateServerTable(lbMark);
                return;
            }
            serverTableInit = true;
            $('#server-table').on('init.dt', function (e, settings) {
            }).DataTable({
                ordering: false,
                searching: false,//启用搜索功能
                serverSide: true,//启用服务端分页（这是使用Ajax服务端的必须配置）
                ajax: {
                    url: Api.payload.serverList.format({lbMark: lbMark}),
                    type: "post",
                    async: true,
                    processing: true,
                    contentType: "application/json; charset=UTF-8",
                    data: function (d) {
                        let jsonData = {"pageNum": d.start, "pageSize": d.length};
                        return JSON.stringify(jsonData);
                    },
                    dataSrc: function (data) {
                        if (data.code === 0) {
                            toastr.success("请求成功");
                            let json = data.data;
                            let thisSettings = $(this).dataTableSettings[1];
                            thisSettings._iRecordsTotal = json.totalCount;
                            thisSettings._iRecordsDisplay = json.totalCount;
                            return json.list;
                        } else {
                            toastr['error'](data.msg, "路由服务列表响应错误")
                        }
                    }
                },
                "columns": [
                    {"data": "id", visible: false},
                    {title: "名称", "data": "srvName"},
                    {title: "所属负载器", "data": "lbMark"},
                    {title: "地址", "data": "srvIp"},
                    {title: "端口", "data": "srvPort"},
                    {title: "权重", "data": "srvWeight"},
                    {title: "备注", "data": "comment"},
                    {title: "编辑", "data": null},
                    {title: "删除", "data": null}
                ]
            });
        }
    }
);