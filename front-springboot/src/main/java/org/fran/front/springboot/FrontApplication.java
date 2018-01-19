package org.fran.front.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by fran on 2018/1/17.
 */
@EnableZuulProxy
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackageClasses = {
        FrontApplication.class
})
public class FrontApplication {
    public static void main(String[] args) {

        SpringApplication.run(FrontApplication.class, args);
    }
}
