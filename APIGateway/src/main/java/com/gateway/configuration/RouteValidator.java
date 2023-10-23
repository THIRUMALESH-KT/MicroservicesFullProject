package com.gateway.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
public class RouteValidator {

    public static final List<String> OpenApiEndpoints = List.of("/auth/hello", "/auth/login","/auth/authenticate","/auth/welcome","/employee/welcome","/leave/welcome","/employee/password/reset/request","/employee/password/reset/confirm");

    public Predicate<ServerWebExchange> isSecured = exchange -> {
        log.info("******inside isSecured RouteValidator");
        String requestPath = exchange.getRequest().getURI().getPath();
        return OpenApiEndpoints.stream().noneMatch(requestPath::contains);
    };
}
