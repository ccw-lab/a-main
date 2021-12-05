package com.ccwlab.main.work;

import com.ccwlab.main.message.MyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkService {

    @Autowired
    private MyProcessor processor;

    void startNewWork(){
        throw new UnsupportedOperationException();
    }

    void stopWork(int id){
        throw new UnsupportedOperationException();
    }
}
