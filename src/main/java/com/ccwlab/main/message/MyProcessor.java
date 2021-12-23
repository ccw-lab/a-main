package com.ccwlab.main.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MyProcessor {
    String out = "m-to-c";
    String in = "c-to-m";
    @Input(MyProcessor.in)
    SubscribableChannel input();

    @Output(MyProcessor.out)
    MessageChannel output();
}
