function initPredicateSelect() {
    for (var i in Config.predicate) {
        $('#predicateEditModal select').append("<option value='" + i + "'>" + Config.predicate[i].desc + "</option>");
    }
}

function initFilterSelect() {
    for (var i in Config.filter) {
        $('#filterEditModal select').append("<option value='" + i + "'>" + Config.filter[i].desc + "</option>");
    }
}

$(document).ready(function () {
    initPredicateSelect();
    initFilterSelect();
    saveRoute();
    savePredicate();
    saveFilter();
    clickRouteItemAdd();
    clickPredicateItemAdd();
    clickFilterItemAdd();
    var predicateInit = false;
    var filterInit = false;
    $('#route-table').on('init.dt', function (e, settings) {
        var api = new $.fn.dataTable.Api(settings);
        api.on('draw.dt', function (e, settings) {
            api.$(".predicate").click(function () {
                var routeMark = $(this).data("route");
                if (predicateInit)
                    updatePredicationTable(routeMark);
                else {
                    initPredicationTable(routeMark);
                    predicateInit = true;
                }
                $("#predicateModal").modal("show");
            });
            api.$(".filter").click(function () {
                var routeMark = $(this).data("route");
                if (filterInit)
                    updateFilterTable(routeMark);
                else {
                    initFilterTable(routeMark);
                    filterInit = true;
                }
                $("#filterModal").modal("show");
            });
            clickRouteItem(api);
        });
        api.$(".predicate").click(function () {
            var routeMark = $(this).data("route");
            if (predicateInit)
                updatePredicationTable(routeMark)
            else {
                initPredicationTable(routeMark);
                predicateInit = true;
            }
            $("#predicateModal").modal("show");
        });

        api.$(".filter").click(function () {
            var routeMark = $(this).data("route");
            if (filterInit)
                updateFilterTable(routeMark)
            else {
                initFilterTable(routeMark);
                filterInit = true;
            }
            $("#filterModal").modal("show");
        });
        clickRouteItem(api);
    }).DataTable({
        ordering: false,
        searching: false,//启用搜索功能
        serverSide: true,//启用服务端分页（这是使用Ajax服务端的必须配置）
        ajax: {
            url: Api.route.list,
            type: "post",
            async: true,
            contentType: "application/json; Charset:UTF-8",
            data: function (d) {
                var jsonData = {"pageNum": d.start, "pageSize": d.length};
                return JSON.stringify(jsonData);
            },
            dataSrc: function (data) {
                if (data.code === 0) {
                    toastr.success("请求成功");
                    var json = data.data;
                    console.log(this)
                    thisSettings = $(this).dataTableSettings[0];
                    thisSettings._iRecordsTotal = json.totalCount;
                    thisSettings._iRecordsDisplay = json.totalCount;
                    return json.list;
                } else {
                    toastr['error'](data.msg, "路由列表响应错误")
                }
            }
        },
        "columns": [
            {"data": "id", visible: false},
            {title: "路由标识", "data": "routeMark"},
            {title: "名称", "data": "routeName"},
            {title: "路由方式", "data": "routeTargetMode"},
            {title: "路由目标", "data": "routeUri"},
            {title: "路由备注", "data": "routeComment"},
            {title: "路由状态", "data": "routeStatus"},
            {title: "匹配规则", "data": null},
            {title: "过滤器规则", "data": null},
            {title: "编辑", "data": null},
            {title: "删除", "data": null},
            {title: "发布配置", "data": null}
        ],
        "columnDefs": [{
            "targets": 3,
            "render": function (data, type, full, meta) {
                if (data === 1)
                    return "直连";
                else if (data === 2)
                    return "负载均衡";
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
                return "<button type='button' class='btn btn-info predicate' data-route='" + full.routeMark + "'><i class='fa fa-bars'></i>查看</button>";
            }
        }, {
            "targets": 8,
            "render": function (data, type, full, meta) {
                return "<button type='button' class='btn btn-info filter' data-route='" + full.routeMark + "'><i class='fa fa-bars'></i>查看</button>";
            }
        }, {
            "targets": 9,
            "render": function (data, type, full, meta) {
                return "<button type='button' class='btn btn-info route-item-modify'><i class='fa fa-edit'></i>修改</button>";
            }
        }, {
            "targets": 10,
            "render": function (data, type, full, meta) {
                return "<button type='button' class='btn btn-info route-item-del'><i class='fa fa-trash-o'></i>删除</button>";
            }
        }, {
            "targets": 11,
            "render": function (data, type, full, meta) {
                if (full.routeStatus === 0 || full.routeStatus === 2) {
                    return "<button type='button' class='btn btn-info route-item-enable'><i class='fa fa-cloud-upload'></i>启用</button>";
                } else if (full.routeStatus === 1) {
                    return "<button type='button' class='btn btn-info route-item-disable'><i class='fa fa-cloud-upload'></i>禁用</button>";
                } else {
                    return "unknown status";
                }
            }
        }]
    });

    function updatePredicationTable(routeMark) {
        var api = $('#route-predicate-table').dataTable().api();
        api.ajax.url(Api.route.predicateList.format({routeMark: routeMark})).load();
        api.draw();
    }

    function updateFilterTable(routeMark) {
        var api = $('#route-filter-table').dataTable().api();
        api.ajax.url(Api.route.filterList.format({routeMark: routeMark})).load();
        api.draw();
    }

    function reloadTable(which) {
        var api = $('#' + which).dataTable().api();
        api.ajax.reload();
        api.draw();
    }

    function clickRouteItemAdd() {
        $('#route-item-add').click(function () {
            $("#routeEditModal .save").data("edit", false);
            $("#routeEditModal").modal("show");
        });
    }

    function clickPredicateItemAdd() {
        $('#predicateModal .predicate-item-add').click(function () {
            var select = $("#predicateEditKeySelect");
            select.val();
            select.removeAttr("disabled");
            $("#predicateEditId").val('');
            $("#predicateEditModal .save").data("edit", false);
            $("#predicateEditModal").modal("show");
        });
    }

    function clickFilterItemAdd() {
        $('#filterModal .filter-item-add').click(function () {
            var select = $("#filterEditKeySelect");
            select.val();
            select.removeAttr("disabled");
            $("#filterEditId").val('');
            $("#filterEditModal .save").data("edit", false);
            $("#filterEditModal").modal("show");
        });
    }

    function clickPredicateItem(api) {
        api.$('.predicate-item-modify').click(function () {
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
            var select = $("#predicateEditKeySelect");
            select.val(formData.routePredicateKey);
            select.attr("disabled", "disabled");
            $("#predicateEditValue").val(formData.routePredicateValue);
            $("#predicateEditId").val(formData.id);
            $("#predicateEditModal").modal("show");
            $("#predicateEditModal .save").data("edit", true);
        });

        api.$('.predicate-item-del').click(function () {
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();

            var delData = {
                id: formData.id,
                routePredicateKey: formData.routePredicateKey
            };
            $.ajaxPostApi({
                url: Api.route.predicateDel,
                async: true,
                data: JSON.stringify(delData),
                success: function (data) {
                    if (data.code === 0) {
                        toastr.success("删除匹配规则成功");
                        reloadTable('route-predicate-table');
                    } else {
                        toastr['error'](data.msg, "删除匹配规则失败")
                    }
                },
                error: function (e) {
                    toastr['error'](e.status, "删除匹配规则失败");
                }
            })
        });
    }

    function clickRouteItem(api) {
        api.$('.route-item-modify').click(function () {
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
            $.ajaxGetApi({
                url: Api.route.get,
                async: true,
                data: {routeMark: formData.routeMark},
                success: function (data) {
                    if (data.code === 0) {
                        var retData = data.data;
                        $("#routeTargetMode").val(retData.routeTargetMode);
                        $("#routeUri").val(retData.routeUri);
                        routeMarkInput = $("#routeMark");
                        routeMarkInput.val(retData.routeMark);
                        routeMarkInput.attr("disabled","disabled");
                        $("#routeName").val(retData.routeName);
                        $("#routeComment").val(retData.routeComment);
                        $("#routeSort").val(retData.routeSort);
                        $("#routeEditModal .save").data("edit", true);
                        $("#routeEditModal").modal("show");
                    } else {
                        toastr['error'](data.msg, "查询路由信息失败")
                    }
                },
                error: function (e) {
                    toastr['error'](e.status, "查询路由信息失败");
                }
            });
        });
        api.$('.route-item-del').click(function () {
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
            var data = {routeMark: formData.routeMark};
            $.ajaxPostApi({
                url: Api.route.del,
                async: true,
                data: JSON.stringify(data),
                success: function (data) {
                    if (data.code === 0) {
                        reloadTable('route-table')
                    } else {
                        toastr['error'](data.msg, "删除路由信息失败")
                    }
                },
                error: function (e) {
                    toastr['error'](e.status, "删除路由信息失败");
                }
            });
        });
        api.$('.route-item-enable').click(function () {
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
            $.ajaxPostApi({
                url: Api.route.enable,
                async: true,
                data: {routeMark: formData.routeMark},
                success: function (data) {
                    if (data.code === 0) {
                        reloadTable('route-table')
                    } else {
                        toastr['error'](data.msg, "启用路由失败")
                    }
                },
                error: function (e) {
                    toastr['error'](e.status, "启用路由失败");
                }
            });
        });
        api.$('.route-item-disable').click(function () {
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
            $.ajaxPostApi({
                url: Api.route.disable,
                async: true,
                data: {routeMark: formData.routeMark},
                success: function (data) {
                    if (data.code === 0) {
                        reloadTable('route-table')
                    } else {
                        toastr['error'](data.msg, "禁用路由失败")
                    }
                },
                error: function (e) {
                    toastr['error'](e.status, "禁用路由失败");
                }
            })
        });
    }

    function clickFilterItem(api) {
        api.$('.filter-item-modify').click(function () {
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();
            var select = $("#filterEditKeySelect");
            select.val(formData.routeFilterKey);
            select.attr("disabled", "disabled");
            $("#filterEditValue").val(formData.routeFilterValue);
            $("#filterEditId").val(formData.id);
            $("#filterEditModal").modal("show");
            $("#filterEditModal .save").data("edit", true);
        });

        api.$('.filter-item-del').click(function () {
            var rowIndex = api.cell(this.parentNode).index().row;
            var formData = api.row(rowIndex).data();

            var delData = {
                id: formData.id,
                routeFilterKey: formData.routeFilterKey
            };
            $.ajaxPostApi({
                url: Api.route.filterDel,
                async: true,
                data: JSON.stringify(delData),
                success: function (data) {
                    if (data.code === 0) {
                        toastr.success("删除过滤器规则成功");
                        reloadTable('route-filter-table');
                    } else {
                        toastr['error'](data.msg, "删除过滤器规则失败")
                    }
                },
                error: function (e) {
                    toastr['error'](e.status, "删除过滤器规则失败");
                }
            })
        });
    }

    function savePredicate() {
        $("#predicateEditModal .save").click(function () {
            var isEdit = $(this).data("edit");
            var predicatekey = $("#predicateEditKeySelect").val();
            var predicateValue = $("#predicateEditValue").val();
            var predicateComment = $("#predicateEditComment").val();
            var data = {
                routePredicateKey: predicatekey,
                routePredicateValue: predicateValue,
                routePredicateComment: predicateComment
            };
            if (isEdit) {
                data.id = $("#predicateEditId").val();
                $.ajaxPostApi({
                    url: Api.route.predicateEdit,
                    async: true,
                    data: JSON.stringify(data),
                    success: function (data) {
                        if (data.code === 0) {
                            toastr.success("修改匹配规则成功");
                            $("#predicateEditModal").modal("hide");
                            reloadTable('route-predicate-table');
                        } else {
                            toastr['error'](data.msg, "修改匹配规则失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "修改匹配规则失败");
                    }
                });
            } else {
                data.routeMark = $("#predicateEditRouteMark").val();
                $.ajaxPostApi({
                    url: Api.route.predicateAdd,
                    async: true,
                    data: JSON.stringify(data),
                    success: function (data) {
                        if (data.code === 0) {
                            toastr.success("新增匹配规则成功");
                            $("#predicateEditModal").modal("hide");
                            reloadTable('route-predicate-table');
                        } else {
                            toastr['error'](data.msg, "新增匹配规则失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "新增匹配规则失败");
                    }
                })
            }
        });
    }

    function saveRoute() {
        $("#routeEditModal .save").click(function () {
            var isEdit = $(this).data("edit");
            var routeTargetMode = $("#routeTargetMode").val();
            var routeUri = $("#routeUri").val();
            var routeMark = $("#routeMark").val();
            var routeName = $("#routeName").val();
            var routeComment = $("#routeComment").val();
            var routeSort = $("#routeSort").val();
            var data = {
                routeTargetMode: routeTargetMode,
                routeUri: routeUri,
                routeMark: routeMark,
                routeName: routeName,
                routeComment: routeComment,
                routeSort: routeSort
            };
            if (isEdit) {
                $.ajaxPostApi({
                    url: Api.route.edit,
                    async: true,
                    data: JSON.stringify(data),
                    success: function (data) {
                        if (data.code === 0) {
                            toastr.success("修改路由成功");
                            $("#routeEditModal").modal("hide");
                            reloadTable('route-table');
                        } else {
                            toastr['error'](data.msg, "修改路由失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "修改路由失败");
                    }
                });
            } else {
                $.ajaxPostApi({
                    url: Api.route.add,
                    async: true,
                    data: JSON.stringify(data),
                    success: function (data) {
                        if (data.code === 0) {
                            toastr.success("新增路由成功");
                            $("#routeEditModal").modal("hide");
                            reloadTable('route-table');
                        } else {
                            toastr['error'](data.msg, "新增路由失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "新增路由失败");
                    }
                })
            }
        });
    }

    function saveFilter() {
        $("#filterEditModal .save").click(function () {
            var isEdit = $(this).data("edit");
            var filterkey = $("#filterEditKeySelect").val();
            var filterValue = $("#filterEditValue").val();
            var filterComment = $("#filterEditComment").val();
            var data = {
                routeFilterKey: filterkey,
                routeFilterValue: filterValue,
                routeFilterComment: filterComment
            };
            if (isEdit) {
                data.id = $("#filterEditId").val();
                $.ajaxPostApi({
                    url: Api.route.filterEdit,
                    async: true,
                    data: JSON.stringify(data),
                    success: function (data) {
                        if (data.code === 0) {
                            toastr.success("修改过滤器规则成功");
                            $("#filterEditModal").modal("hide");
                            reloadTable('route-filter-table');
                        } else {
                            toastr['error'](data.msg, "修改过滤器规则成功")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "修改过滤器规则失败");
                    }
                });
            } else {
                data.routeMark = $("#filterEditRouteMark").val();
                $.ajaxPostApi({
                    url: Api.route.filterAdd,
                    async: true,
                    data: JSON.stringify(data),
                    success: function (data) {
                        if (data.code === 0) {
                            toastr.success("新增过滤器规则成功");
                            $("#filterEditModal").modal("hide");
                            reloadTable('route-filter-table');
                        } else {
                            toastr['error'](data.msg, "新增过滤器规则失败")
                        }
                    },
                    error: function (e) {
                        toastr['error'](e.status, "新增过滤器规则失败");
                    }
                })
            }
        });
    }

    function initPredicationTable(routeMark) {
        $('#predicateEditModal').on('hidden.bs.modal', function () {
            var select = $("#predicateEditModal select");
            select.removeAttr("disabled");
        });
        $('#route-predicate-table').on('init.dt', function (e, settings) {
            var api = new $.fn.dataTable.Api(settings);
            api.on('draw.dt', function (e, settings) {
                clickPredicateItem(api)
                $("#predicateEditRouteMark").val($.getUrlParam(api.ajax.url(), 'routeMark'));
            });
            $("#predicateEditRouteMark").val($.getUrlParam(api.ajax.url(), 'routeMark'));
            clickPredicateItem(api)
        }).DataTable({
            searching: false,
            ordering: false,
            paging: true,
            ajax: {
                url: Api.route.predicateList.format({routeMark: routeMark}),
                type: "get",
                async: true,
                contentType: "application/json; Charset:UTF-8",
                data: function () {
                    return {};
                },
                dataSrc: function (data) {
                    if (data.code === 0) {
                        toastr.success("请求成功");
                        var json = data.data;
                        return json;
                    } else {
                        toastr[error]("请求响应错误" + data.msg);
                    }
                }
            },
            "columns": [
                {data: 'id', visible: false},
                {title: "名称", "data": null},
                {title: "key", "data": "routePredicateKey"},
                {title: "value", "data": "routePredicateValue"},
                {title: "备注", "data": "routePredicateComment"},
                {title: "修改", "data": null},
                {title: "删除", "data": null}
            ],
            "columnDefs": [{
                "targets": 1,
                "render": function (data, type, full, meta) {
                    var prediction = Config.predicate[full.routePredicateKey];
                    if (!prediction)
                        return "未知";
                    else {
                        return '<a href="#" class="tooltip-test" data-toggle="tooltip" title="' + prediction.tooltip + '">' + prediction.desc + '</a>';
                    }
                }
            }, {
                "targets": 5,
                "render": function (data, type, full, meta) {
                    return "<button type='button' class='btn btn-info predicate-item-modify'><i class='fa fa-edit'></i>修改</button>";
                }
            }, {
                "targets": 6,
                "render": function (data, type, full, meta) {
                    return "<button type='button' class='btn btn-info predicate-item-del'><i class='fa fa-trash-o'></i>删除</button>";
                }
            }]
        });
    }

    function initFilterTable(routeMark) {
        $('#filterEditModal').on('hidden.bs.modal', function () {
            var select = $("#filterEditModal select");
            select.removeAttr("disabled");
        });
        $('#route-filter-table').on('init.dt', function (e, settings) {
            var api = new $.fn.dataTable.Api(settings);
            api.on('draw.dt', function (e, settings) {
                clickFilterItem(api)
                $("#filterEditRouteMark").val($.getUrlParam(api.ajax.url(), 'routeMark'));
            });
            $("#filterEditRouteMark").val($.getUrlParam(api.ajax.url(), 'routeMark'));
            clickFilterItem(api)
        }).DataTable({
            searching: false,
            ordering: false,
            paging: true,
            ajax: {
                url: Api.route.filterList.format({routeMark: routeMark}),
                type: "get",
                async: true,
                contentType: "application/json; Charset:UTF-8",
                data: function () {
                    return {};
                },
                dataSrc: function (data) {
                    if (data.code === 0) {
                        toastr.success("请求成功");
                        var json = data.data;
                        return json;
                    } else {
                        toastr[error]("请求响应错误" + data.msg);
                    }
                }
            },
            "columns": [
                {data: 'id', visible: false},
                {title: "名称", "data": null},
                {title: "key", "data": "routeFilterKey"},
                {title: "value", "data": "routeFilterValue"},
                {title: "备注", "data": "routeFilterComment"},
                {title: "修改", "data": null},
                {title: "删除", "data": null}
            ],
            "columnDefs": [{
                "targets": 1,
                "render": function (data, type, full, meta) {
                    var filter = Config.filter[full.routeFilterKey];
                    if (!filter)
                        return "未知";
                    else {
                        return '<a href="#" class="tooltip-test" data-toggle="tooltip" title="' + filter.tooltip + '">' + filter.desc + '</a>';
                    }
                }
            }, {
                "targets": 5,
                "render": function (data, type, full, meta) {
                    return "<button type='button' class='btn btn-info filter-item-modify'><i class='fa fa-edit'></i>修改</button>";
                }
            }, {
                "targets": 6,
                "render": function (data, type, full, meta) {
                    return "<button type='button' class='btn btn-info filter-item-del'><i class='fa fa-trash-o'></i>删除</button>";
                }
            }]
        });
    }
});