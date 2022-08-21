package com.capston.chatting.repository;

import com.capston.chatting.entity.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @EntityGraph(attributePaths = "member")
    @Override
    Optional<Item> findById(Long aLong);
}
