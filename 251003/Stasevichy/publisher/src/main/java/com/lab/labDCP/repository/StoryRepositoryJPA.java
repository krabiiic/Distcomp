package com.lab.labDCP.repository;

import com.lab.labDCP.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepositoryJPA extends JpaRepository<Story, Long> {
}
