package org.neo4j.bumblebee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

        // Reads csv resources file and creates resource nodes for each line in the file
        Path path = Paths.get("src/main/resources/resources.csv");
        List<String> lines = new ArrayList<String>();
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line: lines) {
            String[] resourceData = line.split(",");
            Resource resource = new Resource(resourceData[0], resourceData[1], Float.parseFloat(resourceData[2]),
                    Float.parseFloat(resourceData[3]), Float.parseFloat(resourceData[4]), Float.parseFloat(resourceData[5]),
                    Float.parseFloat(resourceData[6]), Float.parseFloat(resourceData[7]),
                    resourceData[8], Long.parseLong(resourceData[9]));
            resourceRepository.save(resource);
        }


        // Reads csv file for relationships between resources. Creates relationship for each existing resource node
        Path relpath = Paths.get("src/main/resources/relationships.csv");
        List<String> relLines = new ArrayList<String>();
        try {
            relLines = Files.readAllLines(relpath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line: relLines) {
            String[] relData = line.split(",");
            Resource resource1 = resourceRepository.findById(relData[0]);
            resource1.connectsTo(resourceRepository.findById(relData[1]));
            resourceRepository.save(resource1);
        }
    }
}
