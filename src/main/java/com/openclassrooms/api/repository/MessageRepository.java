package com.openclassrooms.api.repository;

import com.openclassrooms.api.model.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Integer> {
}
