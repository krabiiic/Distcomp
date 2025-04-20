package com.example.restservice.repository;

import com.example.restservice.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends RepositoryInMemory<User> { }
