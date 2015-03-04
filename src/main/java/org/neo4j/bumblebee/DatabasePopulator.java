package org.neo4j.bumblebee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by tanvimehta on 15-03-03.
 */
@Service
public class DatabasePopulator {

    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    Neo4jOperations template;

    @Transactional
    public void populateDatabase() {
//          resourceRepository.setResource();
        Resource room1 = new Resource("R1", "room", 1L, 2L, 3L, "Bahen");
        Resource room2 = new Resource("R2", "room", 1L, 2L, 3L, "Bahen");
        Resource room3 = new Resource("R3", "room", 1L, 2L, 3L, "Bahen");
        Resource room4 = new Resource("R4", "room", 1L, 2L, 3L, "Bahen");
        Resource room5 = new Resource("R5", "room", 1L, 2L, 3L, "Bahen");
        Resource corridor1 = new Resource("C1", "corridor", 1L, 2L, 3L, "Bahen");
        Resource corridor2 = new Resource("C2", "corridor", 1L, 2L, 3L, "Bahen");
        Resource corridor3 = new Resource("C3", "corridor", 1L, 2L, 3L, "Bahen");

            resourceRepository.save(room1);
            resourceRepository.save(room2);
            resourceRepository.save(room3);
            resourceRepository.save(room4);
            resourceRepository.save(room5);
            resourceRepository.save(corridor1);
            resourceRepository.save(corridor2);
            resourceRepository.save(corridor3);

            room1 = resourceRepository.findById(room1.getId());
            room1.connectsTo(corridor1);
            resourceRepository.save(room1);

            corridor1 = resourceRepository.findById(corridor1.getId());
            corridor1.connectsTo(room2);
            corridor1.connectsTo(room3);
            resourceRepository.save(corridor1);

            room2 = resourceRepository.findById(room2.getId());
            room2.connectsTo(corridor2);
            resourceRepository.save(room2);

            corridor2 = resourceRepository.findById(corridor2.getId());
            corridor2.connectsTo(room5);
            resourceRepository.save(corridor2);

            room5 = resourceRepository.findById(room5.getId());
            room5.connectsTo(room4);
            resourceRepository.save(room5);

            room4 = resourceRepository.findById(room4.getId());
            room4.connectsTo(corridor3);
            resourceRepository.save(room4);

            corridor3 = resourceRepository.findById(corridor3.getId());
            corridor3.connectsTo(room3);
            resourceRepository.save(corridor3);
    }
}
