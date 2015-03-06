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

import java.util.*;

/**
 * Created by tanvimehta on 15-03-03.
 */
@Controller
public class ResourceController {

    private static final String DELIMITER = "->\n";
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

    @RequestMapping(value = "/path/{x}/{y}/{floor}/{id2}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getShortestPath(@PathVariable Long x, @PathVariable Long y, @PathVariable Long floor,
                           @PathVariable String id2) {

        Resource r1 = resourceRepository.getResourceByPoint(x, y, floor);
        if (r1 == null) {
            return "Oops! Could not find you! Please try again later.";
        }
        Resource r2 = resourceRepository.findById(id2);

        String path = "";
        for (EntityPath<Resource, Resource> entityPath : resourceRepository.getPath(r1, r2)) {
            Iterable<Resource> rnodes = entityPath.nodeEntities();
            for (Resource rnode : rnodes) {
                path = path + rnode.toString() + DELIMITER;
            }
        }

        return path;
    }

    @RequestMapping(value = "/findResources/{type}/{x}/{y}/{floor}", method = RequestMethod.GET)
    public
    @ResponseBody
    String findResources(@PathVariable String type, @PathVariable Long x, @PathVariable Long y,
                                @PathVariable Long floor) {
        Resource r = resourceRepository.getResourceByPoint(x, y, floor);
        List<Resource> eligibleResources = resourceRepository.getResourcesByType(type);
        Map<String, Integer> resourceIdToDistCount = new HashMap<>();

        if (eligibleResources.size() == 0)
            return "Oops! There are no " + type + "s near you! Please try again later.";

        for(Resource eligibleResource: eligibleResources) {
            Integer count = 0;
            for (EntityPath<Resource, Resource> entityPath : resourceRepository.getPath(r, eligibleResource)) {
                Iterable<Resource> rnodes = entityPath.nodeEntities();
                for (Resource rnode : rnodes) {
                    count++;
                }
            }
            resourceIdToDistCount.put(eligibleResource.getId(), count);
        }

        LinkedHashMap<String, Integer> sortedResources = sortHashMapByValuesD(resourceIdToDistCount);
        Integer index = 0;
        String closestResources = "";
        Iterator<String> iter = sortedResources.keySet().iterator();
        while (iter.hasNext() && index < 5) {
            closestResources = closestResources + iter.next() + DELIMITER;
            index ++;
        }
        return closestResources;
    }


    public LinkedHashMap sortHashMapByValuesD(Map passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Integer)val);
                    break;
                }

            }

        }
        return sortedMap;
    }
}