package org.neo4j.bumblebee;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tanvimehta on 15-03-03.
 */
@NodeEntity
public class Resource {

    @GraphId Long resourceId;

    @Indexed(unique = true)
    String id;

    String type;

    Long geo_x;

    Long geo_y;

    Long floor;

    String building;

    @RelatedTo(type = "CONNECTS_TO", direction = Direction.BOTH)
    @Fetch
    Set<Resource> neighbours;

    public void connectsTo(Resource resource) {
        if (neighbours == null) {
            neighbours = new HashSet<Resource>();
        }
        neighbours.add(resource);
    }

    public Resource () {

    }

    public Resource(String id, String type, Long geo_x,
                    Long geo_y, Long floor, String building) {
        this.id = id;
        this.type = type;
        this.geo_x = geo_x;
        this.geo_y = geo_y;
        this.floor = floor;
        this.building = building;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", building='" + building + '\'' +
                '}';
    }
}
