package com.example.william.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.william.entity.Post;
import com.example.william.entity.Relationship;

public interface RelationshipDao extends JpaRepository<Relationship, Integer>, JpaSpecificationExecutor<Relationship> {
	Relationship findBySponsorAndAccepterAndStatus(int sponsorId, int accepterId, String status);
	Relationship findBySponsorAndAccepter(int sponsorId, int accepterId);
	Page<Relationship> findBySponsorAndStatus(int sponsorId, String status, Pageable page);
}
