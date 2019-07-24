var predicateConfig = (function ($) {
    $.After = {
        desc: 'After 路由断言',
        tooltip: 'spring:\n' +
        '  cloud:\n' +
        '    gateway:\n' +
        '      routes:\n' +
        '      - id: after_route\n' +
        '        uri: http://example.org\n' +
        '        predicates:\n' +
        '        - After=2017-01-20T17:42:47.789-07:00[America/Denver]'
    };
    $.Before = {
        desc: 'Before 路由断言',
        tooltip: 'spring:\n' +
        '  cloud:\n' +
        '    gateway:\n' +
        '      routes:\n' +
        '      - id: before_route\n' +
        '        uri: http://example.org\n' +
        '        predicates:\n' +
        '        - Before=2017-01-20T17:42:47.789-07:00[America/Denver]'
    };
    $.Between = {
        desc: 'Between 路由断言',
        tooltip: 'spring:\n' +
        '  cloud:\n' +
        '    gateway:\n' +
        '      routes:\n' +
        '      - id: between_route\n' +
        '        uri: http://example.org\n' +
        '        predicates:\n' +
        '        - Between=2017-01-20T17:42:47.789-07:00[America/Denver], 2017-01-21T17:42:47.789-07:00[America/Denver]'
    };
    $.Cookie = {
        desc: 'Cookie 路由断言',
        tooltip: 'spring:\n' +
        '  cloud:\n' +
        '    gateway:\n' +
        '      routes:\n' +
        '      - id: cookie_route\n' +
        '        uri: http://example.org\n' +
        '        predicates:\n' +
        '        - Cookie=chocolate, ch.p'
    };
    $.Header = {
        desc: 'Header 路由断言',
        tooltip: 'spring:\n' +
        ' cloud:\n' +
        '   gateway:\n' +
        '     routes:\n' +
        '     - id: header_route\n' +
        '       uri: http://example.org\n' +
        '       predicates:\n' +
        '       - Header=X-Request-Id, \\d+'
    };
    $.Host = {
        desc: 'Host 路由断言',
        tooltip: 'spring:\n' +
        '  cloud:\n' +
        '    gateway:\n' +
        '      routes:\n' +
        '      - id: host_route\n' +
        '        uri: http://example.org\n' +
        '        predicates:\n' +
        '        - Host=**.somehost.org,**.anotherhost.org'
    };
    $.Method = {
        desc: 'Method 路由断言',
        tooltip: 'spring:\n' +
        '  cloud:\n' +
        '    gateway:\n' +
        '      routes:\n' +
        '      - id: method_route\n' +
        '        uri: http://example.org\n' +
        '        predicates:\n' +
        '        - Method=GET'
    };
    $.Path = {
        desc: 'Path 路由断言',
        tooltip: 'spring:\n' +
        '  cloud:\n' +
        '    gateway:\n' +
        '      routes:\n' +
        '      - id: host_route\n' +
        '        uri: http://example.org\n' +
        '        predicates:\n' +
        '        - Path=/foo/{segment},/bar/{segment}'
    };
    $.Query = {
        desc: 'Query 路由断言',
        tooltip: 'spring:\n' +
        '  cloud:\n' +
        '    gateway:\n' +
        '      routes:\n' +
        '      - id: query_route\n' +
        '        uri: http://example.org\n' +
        '        predicates:\n' +
        '        - Query=baz'
    };
    $.RemoteAddr = {
        desc: 'RemoteAddr 路由断言',
        tooltip: 'spring:\n' +
        '  cloud:\n' +
        '    gateway:\n' +
        '      routes:\n' +
        '      - id: remoteaddr_route\n' +
        '        uri: http://example.org\n' +
        '        predicates:\n' +
        '        - RemoteAddr=192.168.1.1/24'
    };
    return $;
})({});
var util = {
    serializeObject: function (form) {
        var formEL = $(form);
        var o = {};
        var a = formEL.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    },

    fillFormData: function (form, obj, isStatus) {
        var formEL = $(form);
        $.each(obj, function (index, item) {
            formEL.find("[name=" + index + "]").val(item);
        });
    },
    empty: function (data) {
        if (null == data || "" == data) return true;
        else return false;
    }
};

function initPredicateSelect() {
    for (var i in predicateConfig) {
        $('#predicateEditModal select').append("<option value='" + i + "'>" + predicateConfig[i].desc + "</option>");
    }
}

$(document).ready(function () {
    initPredicateSelect();
    var predicateTable;
    $('#route-table').on('draw.dt', function (e, settings) {
        var api = new $.fn.dataTable.Api(settings);
        api.$(".preficate").click(function () {
            console.log(323)
            var routeMark = $(this).data("route");
            $("#predicateModal").modal("show");
            if (!predicateTable) {
                initPredicationTable(routeMark);
            } else {
                updatePredicationTable(routeMark)
            }
        })
    }).DataTable({
        ordering: false,
        searching: false,//启用搜索功能
        serverSide: true,//启用服务端分页（这是使用Ajax服务端的必须配置）
        ajax: {
            url: "/route/list",
            type: "post",
            contentType: "application/json; Charset:UTF-8",
            data: function (d) {
                var jsonData = {"pageNum": d.start, "pageSize": d.length};
                return JSON.stringify(jsonData);
            },
            dataSrc: function (data) {
                if (data.code === 0) {
                    toastr.success("请求成功");
                    var json = data.data;
                    data.recordsTotal = json.totalCount;
                    data.recordsFiltered = json.totalCount;
                    return json.list;
                } else {
                    toastr[error]("请求响应错误" + data.msg)
                    return {};
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
            {
                title: "匹配规则",
                "data": null
            },
            {
                title: "过滤器规则",
                "data": null,
            }
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
                return "<button type='button' class='btn btn-outline btn-info preficate' data-route='" + full.routeMark + "'>查看</button>";
            }
        }, {
            "targets": 8,
            "render": function (data, type, full, meta) {
                return "<button type='button' class='btn btn-outline btn-info filter' data-route='" + full.routeMark + "'>查看</button>";
            }
        }]
    });
    // $("#route-table").click(function (event) {
    //     // console.log(event.target.id)
    //
    // });
    function updatePredicationTable(routeMark) {
        predicateTable.ajax.url("/route/predication/list?routeMark=" + routeMark).load();
    }

    function initPredicationTable(routeMark) {
        $('#predicateEditModal').on('hidden.bs.modal', function () {
            var select = $("#predicateEditModal select");
            select.removeAttr("disabled");
        });
        $('#route-predicate-table').on('draw.dt', function (e, settings) {
            predicateTable = new $.fn.dataTable.Api(settings);
            predicateTable.$('.preficate-item-modify').click(function () {
                var rowIndex = predicateTable.cell(this.parentNode).index().row;
                var formData = predicateTable.row(rowIndex).data();
                var select = $("#predicateEditModal select");
                select.val(formData.routePredicateKey);
                select.attr("disabled", "disabled");
                $("#predicateEditValue").val(formData.routePredicateValue);
                $("#predicateEditComment").val(formData.routePredicateComment);
                $("#predicateEditModal").modal("show");
            });
        }).DataTable({
            searching: false,
            ordering: false,
            paging: true,
            ajax: {
                url: "/route/predication/list?routeMark=" + routeMark,
                type: "get",
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
                        toastr[error]("请求响应错误" + data.msg)
                        return {};
                    }
                }
            },
            "columns": [
                {title: "名称", "data": null},
                {title: "key", "data": "routePredicateKey"},
                {title: "value(单击修改)", "data": "routePredicateValue"},
                {title: "备注", "data": "routePredicateComment"},
                {title: "修改", "data": null},
                {title: "删除", "data": null}
            ],
            "columnDefs": [{
                "targets": 0,
                "render": function (data, type, full, meta) {
                    var prediction = predicateConfig[full.routePredicateKey];
                    if (!prediction)
                        return "未知";
                    else {
                        return '<a href="#" class="tooltip-test" data-toggle="tooltip" title="' + prediction.tooltip + '">' + prediction.desc + '</a>';
                    }
                }
            }, {
                "targets": 4,
                "render": function (data, type, full, meta) {
                    return "<button type='button' class='btn btn-outline btn-info preficate-item-modify' data-route='" + full.routePredicateKey + "'>修改</button>";
                }
            }, {
                "targets": 5,
                "render": function (data, type, full, meta) {
                    return "<button type='button' class='btn btn-outline btn-info preficate-item-del' data-route='" + full.routePredicateKey + "'>删除</button>";
                }
            }]
        });
    }
});