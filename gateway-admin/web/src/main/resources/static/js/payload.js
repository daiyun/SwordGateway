$(document).ready(function () {
    savePayLoad();
    clickPayloadItemAdd();
    $('#payload-table').on('init.dt', function (e, settings) {
        var api = new $.fn.dataTable.Api(settings);
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
                var jsonData = {"pageNum": d.start, "pageSize": d.length};
                return JSON.stringify(jsonData);
            },
            dataSrc: function (data) {
                console.log();
                if (data.code === 0) {
                    toastr.success("请求成功");
                    var json = data.data;
                    thisSettings = $(this).dataTableSettings[0];
                    thisSettings._iRecordsTotal = json.totalCount;
                    thisSettings._iRecordsDisplay = json.totalCount;
                    data.aaData = json.list;
                    data.draw = 1;
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
                return "<button type='button' class='btn btn-info payload-item-modify'><i class='fa fa-edit'></i>修改</button>";
            }
        }, {
            "targets": 8,
            "render": function (data, type, full, meta) {
                return "<button type='button' class='btn btn-info payload-item-del'><i class='fa fa-trash-o'></i>删除</button>";
            }
        }, {
            "targets": 9,
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
            var isEdit = $(this).data("edit");
            var lbType = $("#payloadType").val();
            var lbMark = $("#payloadMark").val();
            var lbName = $("#payloadName").val();
            var lbComment = $("#payloadComment").val();
            var lbRule = $("#payloadRule").val();

            var pingMode = $("#payloadPingMode").val();
            var pingStrategy = $("#payloadPingStrategy").val();
            var pingIntervalTime = $("#payloadPingIntervalTime").val();
            var pingMaxTotalPingTime = $("#payloadMaxTotalPingTime").val();
            var pingUrl = $("#payloadPingUrl").val();

            var payloadServerReload = $("#payloadServerReload").val();
            var initialDelayMs = $("#payloadRefreshInitialDelayMs").val();
            var intervalMs = $("#payloadRefreshIntervalMs").val();

            var dscrEnable = $("#payloadDscrEnable").val();
            var dscrId = $("#payloadDscrId").val();

            var formData = {
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
            if (!payloadServerReload) {
                formData.payloadServerReload = {
                    autoRefresh: false
                }
            } else {
                formData.payloadServerReload = {
                    autoRefresh: true,
                    payloadRefreshInitialDelayMs: initialDelayMs,
                    payloadRefreshIntervalMs: intervalMs
                }
            }
            if (dscrEnable === 1) {
                formData.dscrEnable = 1;
                formData.dscrId = dscrId;
            } else {
                formData.dscrEnable = 0;
            }
            if (isEdit) {
                $.ajaxPostApi({
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
                $.ajaxPostApi({
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
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
            $.ajaxGetApi({
                url: Api.payload.get,
                async: true,
                data: {lbMark: formData.lbMark},
                success: function (data) {
                    if (data.code === 0) {
                        var retData = data.data;
                        // $("#routeTargetMode").val(retData.routeTargetMode);
                        // $("#routeUri").val(retData.routeUri);
                        var payloadMarkInput = $("#payloadMark");
                        payloadMarkInput.val(retData.lbMark);
                        payloadMarkInput.attr("disabled","disabled");
                        $("#routeName").val(retData.routeName);
                        $("#routeComment").val(retData.routeComment);
                        $("#routeSort").val(retData.routeSort);
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
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
            var data = {lbMark: formData.lbMark};
            $.ajaxPostApi({
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
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
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
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
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
});