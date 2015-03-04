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

//    @Query("LOAD CSV WITH HEADERS FROM \"file:/Users/tanvimehta/Desktop/optimus-graph/src/main/resources\" IMPORT resources.csv AS csvLine\n" +
//            "CREATE (r:Resource { id: csvLine.id, type: csvLine.type, geo_x: csvLine.geo_x, geo_y: csvLine.geo_y, " +
//            "floor: csvLine.floor, building: csvLine.building})")
//    void setResource();
}
