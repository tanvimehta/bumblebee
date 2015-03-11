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

    Float min_lat;

    Float max_lat;

    Float min_long;

    Float max_long;

    Float entrance_lat;

    Float entrance_long;

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

    public Resource(String id, String type, Float max_lat, Float min_long,
                    Float min_lat, Float max_long, Float entrance_lat, Float entrance_long, String building, Long floor) {
        this.id = id;
        this.type = type;
        this.min_lat = min_lat;
        this.max_lat = max_lat;
        this.min_long = min_long;
        this.max_long = max_long;
        this.entrance_lat = entrance_lat;
        this.entrance_long = entrance_long;
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
                ", min_lat=" + String.valueOf(min_lat) +
                ", max_lat=" + String.valueOf(max_lat) +
                ", min_long=" + String.valueOf(min_long) +
                ", max_long=" + String.valueOf(max_long) +
                ", entrance_lat=" + String.valueOf(entrance_lat) +
                ", entrance_long=" + String.valueOf(entrance_long) +
                ", floor=" + floor +
                ", building='" + building + '\'' +
                '}';
    }
}
