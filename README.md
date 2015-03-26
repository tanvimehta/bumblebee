-# bumblebee
Neo4J Database Web Application for Resources

To run the application:(Runs server on port 9090)
mvn clean 
mvn install -Djetty.port=9090 jetty:run

To populate the db:(Assumes that resources.csv and relationships.csv are populated)
/populate
Get resource by ID:
/resources/id

Get shortest path between 2 resources:
(Finds shortest path from resource 1(with given coordinates to resource 2(with given id))
/resources/x/y/floor/id2

Find 5 nearest locations of particular type (sorted by increasing order of distance)
/findResources/{type}/{x}/{y}/{floor}