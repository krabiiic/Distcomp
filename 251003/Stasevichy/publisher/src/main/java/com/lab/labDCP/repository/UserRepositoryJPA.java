package com.lab.labDCP.repository;

import com.lab.labDCP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepositoryJPA extends JpaRepository<User, Long> {
}
