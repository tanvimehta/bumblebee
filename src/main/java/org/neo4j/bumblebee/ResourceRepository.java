package org.neo4j.bumblebee;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.core.EntityPath;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

/**
 * Created by tanvimehta on 15-03-03.
 */
public interface ResourceRepository extends GraphRepository<Resource>,
        NamedIndexRepository<Resource>,
        RelationshipOperationsRepository<Resource>{

    Resource findById(String id);

    @Query("START r1=node({0}), r2=node({1}) MATCH p = shortestPath( r1-[*]-r2 ) RETURN p")
    Iterable<EntityPath<Resource, Resource>> getPath(Resource r1, Resource r2);

    @Query("START r=node(*) WHERE HAS(r.floor) AND HAS(r.min_x) AND HAS(r.max_x) AND HAS(r.min_y) AND HAS(r.max_y) " +
            "AND r.floor = {2} AND r.min_x < {0} AND r.max_x > {0} AND r.min_y < {1} AND r.max_y > {1} RETURN r")
    Resource getResourceByPoint(Long x, Long y, Long floor);
}
