package org.neo4j.bumblebee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.EntityPath;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by tanvimehta on 15-03-03.
 */
@Controller
public class ResourceController {

    private static final String DELIMITER = ";";
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    Neo4jOperations template;
    @Autowired
    private DatabasePopulator populator;

    @RequestMapping(value = "/populate", method = RequestMethod.GET)
    public
    @ResponseBody
    String populateDatabase() {
        populator.populateDatabase();
        return "success";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public
    @ResponseBody
    String hello() {
        return "Hello World!";
    }

    @RequestMapping(value = "/resources/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getResource(@PathVariable String id) {
        return resourceRepository.findById(id).toString();
    }

    @RequestMapping(value = "/path/{id1}/{id2}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getShortestPath(@PathVariable String id1, @PathVariable String id2) {
        Resource r1 = resourceRepository.findById(id1);
        Resource r2 = resourceRepository.findById(id2);

        String path = r1.getId();
        for (EntityPath<Resource, Resource> entityPath : resourceRepository.getPath(r1, r2)) {
            Iterable<Resource> rnodes = entityPath.nodeEntities();
            for (Resource rnode : rnodes) {
                path = path + DELIMITER + rnode.getId();
            }
        }

        path = path + DELIMITER + r2.getId();
        return path;    }
}
