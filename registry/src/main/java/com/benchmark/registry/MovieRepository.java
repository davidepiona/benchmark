package com.benchmark.registry;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MovieRepository extends MongoRepository<Movie, String> {
    List<Movie> findByTitleLike(String user);
}