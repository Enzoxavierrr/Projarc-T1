package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Mensageria;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper.TypePrecedence;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@SuppressWarnings("null")
public class RabbitMQConfig {
    public static final String PEDIDOS_EXCHANGE = "pedidos.entrega";
    public static final String PEDIDOS_QUEUE = "pedidos.entrega";
    public static final String PEDIDOS_ROUTING_KEY = "pedido.pronto";
    public static final String STATUS_EXCHANGE = "entregas.status";
    public static final String STATUS_QUEUE = "entregas.status";
    public static final String STATUS_ROUTING_KEY = "pedido.status";

    @Bean
    public DirectExchange pedidosEntregaExchange() {
        return new DirectExchange(PEDIDOS_EXCHANGE, true, false);
    }

    @Bean
    public Queue pedidosEntregaQueue() {
        return QueueBuilder.durable(PEDIDOS_QUEUE).build();
    }

    @Bean
    public Binding pedidosEntregaBinding() {
        return BindingBuilder.bind(pedidosEntregaQueue())
            .to(pedidosEntregaExchange())
            .with(PEDIDOS_ROUTING_KEY);
    }

    @Bean
    public DirectExchange entregasStatusExchange() {
        return new DirectExchange(STATUS_EXCHANGE, true, false);
    }

    @Bean
    public Queue entregasStatusQueue() {
        return QueueBuilder.durable(STATUS_QUEUE).build();
    }

    @Bean
    public Binding entregasStatusBinding() {
        return BindingBuilder.bind(entregasStatusQueue())
            .to(entregasStatusExchange())
            .with(STATUS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
