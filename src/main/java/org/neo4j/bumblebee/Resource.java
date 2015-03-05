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

    Long min_x;

    Long max_x;

    Long min_y;

    Long max_y;

    Long entrance_x;

    Long entrance_y;

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

    public Resource(String id, String type, Long min_x, Long max_x,
                    Long min_y, Long max_y, Long entrance_x, Long entrance_y, Long floor, String building) {
        this.id = id;
        this.type = type;
        this.min_x = min_x;
        this.max_x = max_x;
        this.min_y = min_y;
        this.max_y = max_y;
        this.entrance_x = entrance_x;
        this.entrance_y = entrance_y;
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
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", min_x=" + String.valueOf(min_x) +
                ", max_x=" + String.valueOf(max_x) +
                ", min_y=" + String.valueOf(min_y) +
                ", max_y=" + String.valueOf(max_y) +
                ", entrance_x=" + String.valueOf(entrance_x) +
                ", entrance_y=" + String.valueOf(entrance_y) +
                ", floor=" + floor +
                ", building='" + building + '\'' +
                '}';
    }
}
