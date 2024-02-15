package com.acorn.finals.listener;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.acorn.finals.config.AmqpConfig;

@Component
public class Runner implements CommandLineRunner {


  private final RabbitTemplate rabbitTemplate;
  private final Receiver receiver;

  public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
    this.receiver = receiver;
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("Sending message...");

    // topic exchange 
    rabbitTemplate.convertAndSend("MyExchange", "foo.bar.baz.force", "Hello from RabbitMQ!");    
    rabbitTemplate.convertAndSend("MyExchange", "foo.bar.array","hi hi am i in 1");
    rabbitTemplate.convertAndSend("MyExchange", "xxx.baz.array","hi hi am i in 2");
    rabbitTemplate.convertAndSend("MyExchange", "foo.yyyy.force","hi hi am i in 3");
  }

}