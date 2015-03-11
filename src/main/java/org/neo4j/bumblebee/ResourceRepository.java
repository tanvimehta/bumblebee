package org.neo4j.bumblebee;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.core.EntityPath;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

import java.util.List;

/**
 * Created by tanvimehta on 15-03-03.
 */
public interface ResourceRepository extends GraphRepository<Resource>,
        NamedIndexRepository<Resource>,
        RelationshipOperationsRepository<Resource>{

    Resource findById(String id);

    @Query("START r1=node({0}), r2=node({1}) MATCH p = shortestPath( r1-[*]-r2 ) RETURN p")
    Iterable<EntityPath<Resource, Resource>> getPath(Resource r1, Resource r2);

    @Query("START r=node(*) WHERE HAS(r.floor) AND HAS(r.min_lat) AND HAS(r.max_lat) AND HAS(r.min_long) AND HAS(r.max_long) " +
            "AND r.floor = {2} AND r.min_lat <= {0} AND r.max_lat >= {0} AND r.min_long <= {1} AND r.max_long >= {1} RETURN r")
    Resource getResourceByPoint(Float x, Float y, Long floor);

    @Query("START r=node(*) WHERE HAS(r.type) AND r.type = {0} RETURN r")
    List<Resource> getResourcesByType(String type);
}
