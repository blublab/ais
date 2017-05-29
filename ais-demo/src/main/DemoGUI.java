package main;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.Pair;
import org.neo4j.server.CommunityBootstrapper;
import org.neo4j.server.NeoServer;
import org.neo4j.server.ServerBootstrapper;

public class DemoGUI {

	enum Relationship implements RelationshipType {
		NACHBAR, VOR
	}

	public static void main(String[] args) throws IOException {

        ServerBootstrapper serverBootstrapper = new CommunityBootstrapper();

		// Pfad zur Datenbank
		File db_path = new File("./graph.db");

		serverBootstrapper.start(
				db_path,
	            Optional.empty(), // omit configfile, properties follow
	            Pair.of("dbms.connector.http.address","127.0.0.1:7575"),
	            Pair.of("dbms.connector.http.enabled", "true"),
	            Pair.of("dbms.connector.bolt.enabled", "true"),

	            // allow the shell connections via port 1337 (default)
	            Pair.of("dbms.shell.enabled", "true"),
	            Pair.of("dbms.shell.host", "127.0.0.1"),
	            Pair.of("dbms.shell.port", "1337")
	        );
		// Instanziiere DB Service
        NeoServer neoServer = serverBootstrapper.getServer();
        GraphDatabaseService graphDB = neoServer.getDatabase().getGraph();

		// Starte Transaktion
		try (Transaction tx = graphDB.beginTx()) {
			
			// Erstelle Label "Student"
			Label lStudent = Label.label("Student");
			
			// Erstelle Knoten 
			Node node1 = graphDB.createNode(lStudent);
			Node node2 = graphDB.createNode(lStudent);
			Node node3 = graphDB.createNode(lStudent);

			// Setzte Namen
			node1.setProperty("name", "Peter");
			node2.setProperty("name", "Andreas");
			node3.setProperty("name", "Tom");

			// Erstelle Beziehungen
			node1.createRelationshipTo(node2, Relationship.NACHBAR);
			node3.createRelationshipTo(node1, Relationship.VOR);
			
			// Transaktion erfolgreich
			tx.success();
		}
		

        System.out.println("Press ENTER to quit.");
        System.in.read();
        System.exit(0);
	}

}
