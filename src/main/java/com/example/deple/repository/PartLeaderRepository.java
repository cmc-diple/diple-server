package com.example.deple.repository;

import com.example.deple.entity.PartLeader;
import com.example.deple.entity.enums.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartLeaderRepository extends JpaRepository<PartLeader, Long> {
    List<PartLeader> findAllByPart(Part part);
    List<PartLeader> findAllByPartOrderByCountDesc(Part part);
}
