package com.cjvaldi.libs.msvc.commons;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration (exclude = {DataSourceAutoConfiguration.class})
public class LibsMsvcCommonsApplication {


}
