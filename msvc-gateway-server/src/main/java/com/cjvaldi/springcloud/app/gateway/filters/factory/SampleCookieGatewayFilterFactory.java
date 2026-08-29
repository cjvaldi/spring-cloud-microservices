package com.cjvaldi.springcloud.app.gateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class SampleCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.ConfigurationCookie> {

    private final Logger logger = LoggerFactory.getLogger(SampleCookieGatewayFilterFactory.class);

    public SampleCookieGatewayFilterFactory() {
        super(ConfigurationCookie.class);
    }
    
    @Override
    public GatewayFilter apply(ConfigurationCookie config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            logger.info("Ejecutando el PRE gateway filter factory: " + config.getMessage());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Optional.ofNullable(config.getValue()).ifPresent(cookie -> {
                    exchange.getResponse().getCookies()
                            .add(config.getName(), ResponseCookie.from(config.getName(), cookie).build());
                    logger.info("Ejecutando el POST gateway filter factory: " + config.getMessage());
                });
            }));
        },100);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("message", "name", "value");
    }

    
    // Solo si se quiere cambiar el nombre del filtro, sino se puede omitir este método y se usará el nombre de la clase
    // si se implementa usar este nombre obligatoriamente en el application.yml
    // @Override
    // public String name() {
    //     return "EjemploCookie";
    // }



    public static class ConfigurationCookie {
        private String name;
        private String value;
        private String message;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        

    }


}
