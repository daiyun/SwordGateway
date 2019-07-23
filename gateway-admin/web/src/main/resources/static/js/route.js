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

$(document).ready(function () {
    var operateTarget;
    var predicateTable;
    $('#predicateModal').on('hidden.bs.modal', function () {
        operateTarget = null;
        predicateTable.destroy();
    });
    $('#route-table').on('init.dt', function () {
        $(".preficate").click(function () {
            var routeMark = $(this).data("route");
            predicateTable = $('#route-predicate-table').on('init.dt',function () {
                predicateTable.$('td').editable('/route', {
                    "callback": function (sValue, y) {
                        console.log(this)
                        console.log(sValue)
                        var aPos = predicateTable.fnGetPosition(this);
                        predicateTable.fnUpdate(sValue, aPos[0], aPos[1]);
                    },
                    "submitdata": function (value, settings) {
                        console.log(predicateTable.fnGetPosition(predicateTable))
                        console.log(value)
                        return {
                            "row_id": this.parentNode.getAttribute('id'),
                            "column": predicateTable.fnGetPosition(this)[2]
                        };
                    }
                })
            }).DataTable({
                searching: false,
                ordering: false,
                paging: true,
                ajax: {
                    url: "/route/predication/list",
                    type: "get",
                    contentType: "application/json; Charset:UTF-8",
                    data: function () {
                        return {"routeMark": routeMark};
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
                    {title: "value", "data": "routePredicateValue"},
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
                },{
                    "targets": 4,
                    "render": function (data, type, full, meta) {
                        return "<button type='button' class='btn btn-outline btn-info preficate-item-modify' data-route='" + full.routePredicateKey + "'>修改</button>";
                    }
                },{
                    "targets": 5,
                    "render": function (data, type, full, meta) {
                        return "<button type='button' class='btn btn-outline btn-info preficate-item-del' data-route='" + full.routePredicateKey + "'>删除</button>";
                    }
                }]
            });
            $("#predicateModal").modal("show");
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
});