package com.topbaby.cloud.demoresourceserver.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by Qing on 08/11/2017.
 */
@RestController
public class Hello {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String getActor() {
        return "Hello, resource server";
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Principal getUser(Principal user) {
        return user;
    }
}
