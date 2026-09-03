package com.rookies6.myspringbootlab.runner;

import com.rookies6.myspringbootlab.config.MyEnvironment;
import com.rookies6.myspringbootlab.property.MyPropProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyPropRunner {
//    @Value("${myprop.username}")
//    private String username;
//    @Value("${myprop.port}")
//    private String portNumber;

//    public void printUserName() { System.out.println(username);}
//    public void printPortNumber() { System.out.println(portNumber); }
    private final MyPropProperties myPropProperties;
    private final MyEnvironment myEnvironment;

    public MyPropRunner(MyPropProperties myPropProperties, MyEnvironment myEnvironment) {
        this.myPropProperties = myPropProperties;
        this.myEnvironment = myEnvironment;

        log.info("Application Name = {}", myPropProperties.getApplicationName());
        log.info("Username = {}", myPropProperties.getUsername());
        log.info("Port = {}", myPropProperties.getPort());

        log.info("Current Environment Mode = {}", myEnvironment.getMode());
    }
}
