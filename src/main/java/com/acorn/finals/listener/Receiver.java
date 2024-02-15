package com.acorn.finals.listener;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

public class Receiver {
	
	// 기본 리스너 
  public void receiveMessage(String message) {
	  System.out.println("receive `" + message + "` from default listener");
  }

  @RabbitListener(queues = "#{queue1.name}")
  public void Queue1Listener(String message) {
	  System.out.println("receive `" + message + "` from q1 listener");
  }

  @RabbitListener(queues = "#{queue2.name}")
  public void Queue2Listener(String message) {
	  System.out.println("receive `" + message + "` from q2 listener");
  }

  @RabbitListener(queues = "#{queue3.name}")
  public void Queue3Listener(String message) {
	  System.out.println("receive `" + message + "` from q3 listener");
  }  
}
