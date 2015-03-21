package org.neo4j.bumblebee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.EntityPath;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by tanvimehta on 15-03-03.
 */
@Controller
public class ResourceController {

    private static final String DELIMITER = ",";

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
    String getShortestPath(@PathVariable Float x, @PathVariable Float y, @PathVariable Long floor,
                           @PathVariable String id2) {

        Resource r1 = resourceRepository.getResourceByPoint(x, y, floor);
        if (r1 == null) {
            r1 = getNearestResource(x, y, floor);
        }

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

        if (path == "") {
            return "Oops! Could not find you! Please try again later.";
        }
        
        return path.substring(0, path.length() - 1);
    }

    @RequestMapping(value = "/findResources/{type}/{x}/{y}/{floor}", method = RequestMethod.GET)
    public
    @ResponseBody
    String findResources(@PathVariable String type, @PathVariable Float x, @PathVariable Float y,
                                @PathVariable Long floor) {
        Resource r = resourceRepository.getResourceByPoint(x, y, floor);
        if (r == null) {
            r = getNearestResource(x, y, floor);
        }

        if (r == null) {
            return "Oops! Could not find you! Please try again later.";
        }

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
            resourceIdToDistCount.put(eligibleResource.toString(), count);
        }

        LinkedHashMap<String, Integer> sortedResources = sortHashMapByValuesD(resourceIdToDistCount);
        Integer index = 0;
        String closestResources = "";
        Iterator<String> iter = sortedResources.keySet().iterator();
        while (iter.hasNext() && index < 5) {
            if (index != 0) {
                closestResources += DELIMITER;
            }
            closestResources = closestResources + iter.next() ;
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

    public Resource getNearestResource(Float x, Float y, Long floor) {
        List<Resource> resources = resourceRepository.getAllResourcesOnFloor(floor);
        Map<Resource, Double> rToDist = new HashMap<Resource, Double>();
        Resource result = null;

        for (Resource r: resources) {
            Double dist = Math.sqrt(Math.pow(x-r.getEntrance_lat(), 2) + Math.pow(y-r.getEntrance_long(), 2));
            rToDist.put(r, dist);
        }
        Double min = Double.valueOf(Double.POSITIVE_INFINITY );
        for (Map.Entry<Resource, Double> e:rToDist.entrySet()) {
            if (min.compareTo(e.getValue())>0) {
                result = e.getKey();
                min = e.getValue();
            }
        }

        return result;
    }
}