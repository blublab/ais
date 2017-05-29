package main;

import java.io.File;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import scala.Console;

public class Demo {

	enum Relationship implements RelationshipType {
		NACHBAR, VOR
	}

	public static void main(String[] args) {


		// Pfad zur Datenbank
		File db_path = new File("./graph.db");

		// Instanziiere DB Service
		GraphDatabaseService graphDB = new GraphDatabaseFactory().
				newEmbeddedDatabase(db_path);

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
			node2.setProperty("name", "Tom");

			// Erstelle Beziehungen
			node1.createRelationshipTo(node2, Relationship.NACHBAR);
			node3.createRelationshipTo(node1, Relationship.VOR);
			
			// Transaktion erfolgreich
			tx.success();
		}
		
		Console.readLine();
		// am Ende der Anwendung
		graphDB.shutdown();
	}

}
