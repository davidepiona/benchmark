package com.example.benchmark.wsTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponse {
    private int number;
    private long time;
    private int status;
    private Boolean success;
}
