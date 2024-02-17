package com.acorn.finals.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acorn.finals.listener.Receiver;

//@Configuration
public class AmqpConfig {
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange("MyExchange");
	}

	private static class QueuePool {
		@Bean
		public Receiver reciever() {
			return new Receiver();
		}

		@Bean
		@Qualifier("queue1")
		public Queue queue1() {
			return new Queue("queue1", true);
		}

		@Bean
		@Qualifier("binding1")
		public Binding binding1(@Qualifier("queue1") Queue queue1, TopicExchange exchange) {
			return BindingBuilder.bind(queue1).to(exchange).with("foo.bar.#");
		}

		@Bean
		@Qualifier("queue2")
		public Queue queue2() {
			return new Queue("queue2", true);
		}

		@Bean
		@Qualifier("binding2")
		public Binding binding2(@Qualifier("queue2") Queue queue2, TopicExchange exchange) {
			return BindingBuilder.bind(queue2).to(exchange).with("#.baz.#");
		}

		@Bean
		@Qualifier("queue3")
		public Queue queue3() {
			return new Queue("queue3", true);
		}

		@Bean
		@Qualifier("binding3")
		public Binding binding3(@Qualifier("queue3") Queue queue3, TopicExchange exchange) {
			return BindingBuilder.bind(queue3).to(exchange).with("#.#.force");
		}
	}

}
