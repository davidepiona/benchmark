package com.example.benchmark.wsTest;



import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix ="test")
public class TestProperties {

    private String ws;
}
