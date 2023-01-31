package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Asset;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Asset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetRepository extends Neo4jRepository<Asset, String> {}
