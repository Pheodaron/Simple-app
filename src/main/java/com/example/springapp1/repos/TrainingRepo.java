package com.example.springapp1.repos;

import com.example.springapp1.domain.Training;
import org.springframework.data.repository.CrudRepository;

public interface TrainingRepo extends CrudRepository<Training, Long> {
}
