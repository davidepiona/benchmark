package com.benchmark.registry;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No movie found with the specified ID")
class MovieNotFoundException extends RuntimeException {

}
