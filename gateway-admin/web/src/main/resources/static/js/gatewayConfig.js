var Config = (function ($) {
    $.predicate = {
        After: {
            desc: 'After 路由断言',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: after_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - After=2017-01-20T17:42:47.789-07:00[America/Denver]'
        },
        Before: {
            desc: 'Before 路由断言',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: before_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - Before=2017-01-20T17:42:47.789-07:00[America/Denver]'
        },
        Between: {
            desc: 'Between 路由断言',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: between_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - Between=2017-01-20T17:42:47.789-07:00[America/Denver], 2017-01-21T17:42:47.789-07:00[America/Denver]'
        },
        Cookie: {
            desc: 'Cookie 路由断言',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: cookie_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - Cookie=chocolate, ch.p'
        },
        Header: {
            desc: 'Header 路由断言',
            tooltip: 'spring:\n' +
                ' cloud:\n' +
                '   gateway:\n' +
                '     routes:\n' +
                '     - id: header_route\n' +
                '       uri: http://example.org\n' +
                '       predicates:\n' +
                '       - Header=X-Request-Id, \\d+'
        },
        Host: {
            desc: 'Host 路由断言',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: host_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - Host=**.somehost.org,**.anotherhost.org'
        },
        Method: {
            desc: 'Method 路由断言',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: method_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - Method=GET'
        },
        Path: {
            desc: 'Path 路由断言',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: host_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - Path=/foo/{segment},/bar/{segment}'
        },
        Query: {
            desc: 'Query 路由断言',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: query_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - Query=baz'
        },
        RemoteAddr: {
            desc: 'RemoteAddr 路由断言',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: remoteaddr_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - RemoteAddr=192.168.1.1/24'
        }
    };
    $.filter = {
        AddRequestHeader: {
            desc: 'AddRequestHeader 增加Head',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: add_request_header_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - AddRequestHeader=X-Request-Foo, Bar'
        },
        AddRequestParameter: {
            desc: 'AddRequestParameter 向下游请求添加查询字符串',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: add_request_parameter_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - AddRequestParameter=foo, bar'
        },
        AddResponseHeader: {
            desc: 'AddResponseHeader 向下游响应添加head',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: add_request_header_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - AddResponseHeader=X-Response-Foo, Bar'
        },
        Hystrix: {
            desc: 'Hystrix 暂不支持',
            tooltip: ''
        },
        PrefixPath: {
            desc: 'PrefixPath 匹配请求的路径加前缀',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: prefixpath_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - PrefixPath=/mypath\n' +
                '这将给所有匹配请求的路径加前缀/mypath。因此，向/hello发送的请求将发送到/mypath/hello。'
        },
        PreserveHostHeader: {
            desc: 'PreserveHostHeader 发送原始host header',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: preserve_host_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - PreserveHostHeader\n' +
                '该filter没有参数。设置了该Filter后，GatewayFilter将不使用由HTTP客户端确定的host header ，而是发送原始host header 。'
        },
        RequestRateLimiter: {
            desc: 'RequestRateLimiter 暂不支持',
            tooltip: ''
        },
        RedirectTo: {
            desc: 'RedirectTo 重定向',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: prefixpath_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - RedirectTo=302, http://acme.org\n' +
                '发送一个302状态码和一个Location:http://acme.org header来执行重定向。'
        },
        RemoveNonProxyHeaders: {
            desc: 'RemoveNonProxyHeaders 暂不支持',
            tooltip: ''
        },
        RemoveRequestHeader: {
            desc: 'RemoveRequestHeader 向下游删除请求header',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: removerequestheader_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - RemoveRequestHeader=X-Request-Foo\n' +
                '这将在X-Request-Foo header被发送到下游之前删除它。'
        },
        RemoveResponseHeader: {
            desc: 'RemoveResponseHeader 向下游响应删除header',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: removeresponseheader_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - RemoveResponseHeader=X-Response-Foo\n' +
                '这将在返回到网关client之前从响应中删除x-response-foo头。'
        },
        RewritePath: {
            desc: 'RewritePath Java正则表达式重写请求路径',
            tooltip: "spring:\n" +
                "  cloud:\n" +
                "    gateway:\n" +
                "      routes:\n" +
                "      - id: rewritepath_route\n" +
                "        uri: http://example.org\n" +
                "        predicates:\n" +
                "        - Path=/foo/**\n" +
                "        filters:\n" +
                "        - RewritePath=/foo/(?<segment>.*), /$\\{segment}\n" +
                "对于请求路径/foo/bar，将在发出下游请求之前将路径设置为/bar。注意,由于YAML规范，请使用 $\\替换 $。"
        },
        RewriteResponseHeader: {
            desc: 'RewriteResponseHeader Java正则表达式灵活地重写响应头的值',
            tooltip: 'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: rewriteresponseheader_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - RewriteResponseHeader=X-Response-Foo, , password=[^&]+, password=***\n' +
                '对于一个/42?user=ford&password=omg!what&flag=true的header值，在做下游请求时将被设置为/42?user=ford&password=***&flag=true，由于YAML规范，请使用 $\\替换 $。'
        },
        SaveSession: {
            desc: 'SaveSession',
            tooltip: 'SaveSession GatewayFilter Factory将调用转发到下游之前强制执行WebSession::save 操作。这在使用 Spring Session 之类时特别有用，需要确保会话状态在进行转发调用之前已保存。\n' +
                'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: save_session\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - Path=/foo/**\n' +
                '        filters:\n' +
                '        - SaveSession\n' +
                '如果你希望要将[Spring Security]（https://projects.spring.io/Spring Security/）与Spring Session集成,并确保安全详细信息已转发到远程的进程，这一点至关重要。'
        },
        SecureHeaders: {
            desc: 'SecureHeaders 暂不支持',
            tooltip: ''
        },
        SetPath: {
            desc: 'SetPath',
            tooltip: 'SetPath GatewayFilter Factory 采用 template路径参数。它提供了一种通过允许路径的模板化segments来操作请求路径的简单方法。使用Spring Framework中的URI模板，允许多个匹配segments。\n' +
                '\n' +
                'application.yml.\n' +
                '\n' +
                'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: setpath_route\n' +
                '        uri: http://example.org\n' +
                '        predicates:\n' +
                '        - Path=/foo/{segment}\n' +
                '        filters:\n' +
                '        - SetPath=/{segment}\n' +
                '对于一个 /foo/bar请求，在做下游请求前，路径将被设置为/bar'
        },
        SetResponseHeader: {
            desc: 'SetResponseHeader',
            tooltip: 'SetResponseHeader GatewayFilter Factory 包括 name 和 value 参数.\n' +
                '\n' +
                'application.yml.\n' +
                '\n' +
                'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: setresponseheader_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - SetResponseHeader=X-Response-Foo, Bar\n' +
                '此GatewayFilter使用给定的名称替换所有header，而不是添加。因此，如果下游服务器响应为X-Response-Foo:1234，则会将其替换为X-Response-Foo:Bar,这是网关客户端将接收的内容。'
        },
        SetStatus: {
            desc: 'SetStatus',
            tooltip: 'SetStatus GatewayFilter Factory 包括唯一的 status参数.必须是一个可用的Spring HttpStatus。它可以是整数值404或字符串枚举NOT_FOUND。\n' +
                '\n' +
                'application.yml.\n' +
                '\n' +
                'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: setstatusstring_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - SetStatus=BAD_REQUEST\n' +
                '      - id: setstatusint_route\n' +
                '        uri: http://example.org\n' +
                '        filters:\n' +
                '        - SetStatus=401\n' +
                '在这个例子中，HTTP返回码将设置为401.'
        },
        StripPrefix: {
            desc: 'StripPrefix',
            tooltip: 'StripPrefix GatewayFilter Factory 包括一个parts参数。 parts参数指示在将请求发送到下游之前，要从请求中去除的路径中的节数。\n' +
                '\n' +
                'application.yml.\n' +
                '\n' +
                'spring:\n' +
                '  cloud:\n' +
                '    gateway:\n' +
                '      routes:\n' +
                '      - id: nameRoot\n' +
                '        uri: http://nameservice\n' +
                '        predicates:\n' +
                '        - Path=/name/**\n' +
                '        filters:\n' +
                '        - StripPrefix=2\n' +
                '当通过网关发出/name/bar/foo请求时，向nameservice发出的请求将是http://nameservice/foo。'
        },
        Retry: {
            desc: 'Retry 暂不支持',
            tooltip: ''
        },
        RequestSize: {
            desc: 'RequestSize 暂不支持',
            tooltip: ''
        },
        AccessLog: {
            desc: 'AccessLog 请求打印日志',
            tooltip: '自定义过滤器'
        }
    };
    return $;
})({});