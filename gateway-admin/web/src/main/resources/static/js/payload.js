$(document).ready(function () {
        let serverTableInit = false;
        let serverDisoveryTableInit = false;
        savePayLoad();
        saveServer();
        clickPayloadItemAdd();
        clickServerItemAdd();
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

        function saveServer() {
            $('#serverEditModal').on('hidden.bs.modal', function () {
                $("#payloadSrvIp").removeAttr("disabled");
                $("#payloadSrvId").val('')
                $("#payloadSrvPort").removeAttr("disabled");
            });
            $("#serverEditModal .save").click(function () {
                let isEdit = $(this).data("edit");
                let lbMark = $("#payloadLbMark").val();
                let srvName = $("#payloadSrvName").val();
                let srvIp = $("#payloadSrvIp").val();
                let srvPort = $("#payloadSrvPort").val();
                let srvWeight = $("#payloadSrvWeight").val();
                let srvComment = $("#payloadSrvComment").val();
                let id = $("#payloadSrvId").val();

                let formData = {
                    lbMark: lbMark,
                    srvName: srvName,
                    srvComment: srvComment,
                    srvIp: srvIp,
                    srvPort: srvPort,
                    srvWeight: srvWeight
                };
                if (isEdit) {
                    $.ajaxPostJsonApi({
                        url: Api.payload.serverEdit,
                        async: true,
                        data: JSON.stringify(formData),
                        success: function (data) {
                            if (data.code === 0) {
                                toastr.success("修改路由成功");
                                $("#serverEditModal").modal("hide");
                                reloadTable('server-table');
                            } else {
                                toastr['error'](data.msg, "修改服务失败")
                            }
                        },
                        error: function (e) {
                            toastr['error'](e.status, "修改服务失败");
                        }
                    })
                } else {
                    formData.id = id;
                    $.ajaxPostJsonApi({
                        url: Api.payload.serverAdd,
                        async: true,
                        data: JSON.stringify(formData),
                        success: function (data) {
                            if (data.code === 0) {
                                toastr.success("修改路由成功");
                                $("#serverEditModal").modal("hide");
                                reloadTable('server-table');
                            } else {
                                toastr['error'](data.msg, "新增服务失败")
                            }
                        },
                        error: function (e) {
                            toastr['error'](e.status, "新增服务失败");
                        }
                    })
                }
            })
        }

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
                serverDiscveryTable(formData.lbMark);
                $("#serverTableModal").modal('show');
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

        function clickServerItemAdd() {
            $("#server-item-add").click(function () {
                $("#serverEditModal .save").data("edit", false);
                $("#serverEditModal").modal('show');
            });
        }

        function updateServerTable(lbMark) {
            var api = $('#server-table').dataTable().api();
            api.ajax.url(Api.payload.serverList.format({lbMark: lbMark})).load();
            api.draw();
        }

        function updateServerDiscoverTable(lbMark) {
            var api = $('#server-discover-table').dataTable().api();
            api.ajax.url(Api.payload.serverDiscoverList.format({lbMark: lbMark})).load();
            api.draw();
        }

        function serverTable(lbMark) {
            $("#payloadLbMark").val(lbMark);
            if (serverTableInit) {
                updateServerTable(lbMark);
                return;
            }
            serverTableInit = true;
            $('#server-table').on('init.dt', function (e, settings) {
                let api = new $.fn.dataTable.Api(settings);
                api.on('draw.dt', function (e, settings) {
                    clickServerItem(api);
                });
                clickServerItem(api);
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
                            toastr['error'](data.msg, "服务列表响应错误")
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
                    {title: "服务状态", "data": "srvStatus"},
                    {title: "服务可用", "data": "srvEnable"},
                    {title: "编辑", "data": null},
                    {title: "删除", "data": null},
                    {title: "服务状态控制", "data": null},
                    {title: "可用状态", "data": null},
                    {title: "发布", "data": null}
                ],
                "columnDefs": [{
                    "targets": 7,
                    "render": function (data, type, full, meta) {
                        if (data === 0)
                            return "已下线";
                        else if (data === 1)
                            return "已上线";
                        return "未知";
                    }
                }, {
                    "targets": 8,
                    "render": function (data, type, full, meta) {
                        if (data === 0)
                            return "未启用";
                        else if (data === 1)
                            return "已启用";
                        else if (data === 2)
                            return "已禁用";
                        return "未知";
                    }
                }, {
                    "targets": 9,
                    "render": function (data, type, full, meta) {
                        return "<button type='button' class='btn btn-info server-item-modify'><i class='fa fa-edit'></i>修改</button>";
                    }
                }, {
                    "targets": 10,
                    "render": function (data, type, full, meta) {
                        return "<button type='button' class='btn btn-info server-item-del'><i class='fa fa-remove'></i>删除</button>";
                    }
                }, {
                    "targets": 11,
                    "render": function (data, type, full, meta) {
                        if (full.srvStatus === 0)
                            return "<button type='button' class='btn btn-info server-item-on'><i class='fa fa-edit'></i>上线</button>";
                        else if (full.srvStatus === 1)
                            return "<button type='button' class='btn btn-info server-item-off'><i class='fa fa-edit'></i>下线</button>";
                        return "未知";
                    }
                }, {
                    "targets": 12,
                    "render": function (data, type, full, meta) {
                        if (full.srvEnable === 1)
                            return "<button type='button' class='btn btn-info server-item-disable'><i class='fa fa-edit'></i>禁用</button>";
                        else if (full.srvEnable === 0 || full.srvEnable === 2)
                            return "<button type='button' class='btn btn-info server-item-enable'><i class='fa fa-edit'></i>启用</button>";
                        return "未知";
                    }
                }, {
                    "targets": 13,
                    "render": function (data, type, full, meta) {
                        return "<button type='button' class='btn btn-info server-item-publish'><i class='fa fa-edit'></i>发布</button>";
                    }
                }]
            });
        }

        function serverDiscveryTable(lbMark) {
            if (serverDisoveryTableInit) {
                updateServerDiscoverTable(lbMark);
                return;
            }
            serverDisoveryTableInit = true;
            $('#server-discover-table').on('init.dt', function (e, settings) {

            }).DataTable({
                ordering: false,
                searching: false,//启用搜索功能
                serverSide: true,//启用服务端分页（这是使用Ajax服务端的必须配置）
                ajax: {
                    url: Api.payload.serverDiscoverList.format({lbMark: lbMark}),
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
                            let thisSettings = $(this).dataTableSettings[2];
                            thisSettings._iRecordsTotal = json.totalCount;
                            thisSettings._iRecordsDisplay = json.totalCount;
                            return json.list;
                        } else {
                            toastr['error'](data.msg, "服务列表响应错误")
                        }
                    }
                },
                "columns": [
                    {"data": "id", visible: false},
                    {title: "所属负载器", "data": "lbMark"},
                    {title: "地址", "data": "srvIp"},
                    {title: "端口", "data": "srvPort"},
                    {title: "服务状态", "data": "metaData"},
                    {title: "控制", "data": null}
                ],
                "columnDefs": [{
                    "targets": 4,
                    "render": function (data, type, full, meta) {
                        if (data.app_status === "ON") {
                            return "已上线";
                        } else if (data.app_status === "OFF") {
                            return "已下线";
                        }
                        return "未知";
                    }
                }, {
                    "targets": 5,
                    "render": function (data, type, full, meta) {
                        if (full.metaData.app_status === "ON") {
                            return "<button type='button' class='btn btn-info server-discover-item-off'><i class='fa fa-edit'></i>下线</button>";
                        } else if (full.metaData.app_status === "OFF") {
                            return "<button type='button' class='btn btn-info server-discover-item-on'><i class='fa fa-edit'></i>上线</button>";
                        }
                        return "<button type='button' class='btn btn-info server-discover-item-on'><i class='fa fa-edit'></i>上线</button>";
                    }
                }]
            });
        }

        function clickServerItem(api) {
            api.$('.server-item-modify').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                $("#payloadSrvId").val(formData.id)
                $("#payloadSrvName").val(formData.srvName);
                $("#payloadSrvIp").val(formData.srvIp);
                $("#payloadSrvIp").attr("disabled", "disabled");
                $("#payloadSrvPort").val(formData.srvPort);
                $("#payloadSrvPort").attr("disabled", "disabled");
                $("#payloadSrvWeight").val(formData.srvWeight);
                $("#payloadSrvComment").val(formData.comment);
                $("#serverEditModal .save").data("edit", true);
                $("#serverEditModal").modal('show');
            });
            api.$('.server-item-del').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                let data = {id: formData.id};
                $.ajaxPostApi({
                    url: Api.payload.serverDel,
                    async: true,
                    data: data,
                    success: function (data) {
                        if (data.code === 0) {
                            reloadTable('server-table')
                        } else {
                            toastr['error'](data.msg, "删除服务信息失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "删除服务信息失败");
                    }
                });
            });
            api.$('.server-item-on').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                let data = {id: formData.id};
                $.ajaxPostApi({
                    url: Api.payload.serverOn,
                    async: true,
                    data: data,
                    success: function (data) {
                        if (data.code === 0) {
                            reloadTable('server-table')
                        } else {
                            toastr['error'](data.msg, "上线服务信息失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "上线服务信息失败");
                    }
                });
            });
            api.$('.server-item-off').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                let data = {id: formData.id};
                $.ajaxPostApi({
                    url: Api.payload.serverOff,
                    async: true,
                    data: data,
                    success: function (data) {
                        if (data.code === 0) {
                            reloadTable('server-table')
                        } else {
                            toastr['error'](data.msg, "下线服务信息失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "下线服务信息失败");
                    }
                });
            });
            api.$('.server-item-enable').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                let data = {id: formData.id};
                $.ajaxPostApi({
                    url: Api.payload.serverEnable,
                    async: true,
                    data: data,
                    success: function (data) {
                        if (data.code === 0) {
                            reloadTable('server-table')
                        } else {
                            toastr['error'](data.msg, "启用服务信息失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "启用服务信息失败");
                    }
                });
            });
            api.$('.server-item-disable').click(function () {
                let rowIndex = api.cell(this.parentNode).index().row;
                let formData = api.row(rowIndex).data();
                let data = {id: formData.id};
                $.ajaxPostApi({
                    url: Api.payload.serverDisable,
                    async: true,
                    data: data,
                    success: function (data) {
                        if (data.code === 0) {
                            reloadTable('server-table')
                        } else {
                            toastr['error'](data.msg, "禁用服务信息失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "禁用服务信息失败");
                    }
                });
            });
        }
    }
);