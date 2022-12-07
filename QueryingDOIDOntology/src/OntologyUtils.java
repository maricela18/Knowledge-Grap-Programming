import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.WordUtils;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class OntologyUtils {

	public List<Symtom> getSymtomsList() {
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/SymptomsRDF.owl");
		String baseURI = "http://purl.obolibrary.org/obo/symp.owl";
		List<Symtom> listOfSymptoms = new ArrayList<Symtom>();

		try {

			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			String queryString = "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
			queryString += "PREFIX oboInOwl: <http://www.geneontology.org/formats/oboInOwl#> \n";
			queryString += "SELECT ?s ?label ?id \n";
			queryString += "WHERE { \n";
			queryString += "    ?s rdf:type owl:Class . \n";
			queryString += "    ?s rdfs:subClassOf* obo:SYMP_0000462 . \n";
			queryString += "    ?s rdfs:label ?label . \n";
			queryString += "    ?s oboInOwl:id ?id . \n";
			queryString += "}";

			System.out.println(queryString);
			TupleQuery query = conn.prepareTupleQuery(queryString);
			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) {
				BindingSet solution = result.next();

				String iri = solution.getValue("s").toString();
				String label = solution.getValue("label").toString();
				label = covertStringToCammel(label);

				String id = solution.getValue("id").toString();
				id = id.replaceAll("\"", "");
				String id2 = id.replaceAll(":", "_");

				System.out.println(
						"=======Superclass of individual=========================================================");
				System.out.println("SUPERCLASS Iri " + iri);
				System.out.println("Label " + label);
				System.out.println("Id " + id2);

				Symtom sym = new Symtom(id2, label, iri);
				listOfSymptoms.add(sym);
			}

		} catch (RDF4JException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listOfSymptoms;
	}

	// Método para leer los sintomas de la ontologia symp.owl
	public void getSymptoms() {

		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/SymptomsRDF.owl");
		String baseURI = "http://purl.obolibrary.org/obo/symp.owl";
		List<String> SymptomsMainClasses = new ArrayList<String>();
		List<String> listOfSubClasses = new ArrayList<String>();
		List<String> listOfSubSubClasses = new ArrayList<String>();

		// Primero se genera la lista de clases
		try {
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			String queryString = "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
			queryString += "PREFIX oboInOwl: <http://www.geneontology.org/formats/oboInOwl#> \n";
			queryString += "SELECT ?s ?label ?id \n";
			queryString += "WHERE { \n";
			queryString += "    ?s rdf:type owl:Class . \n";
			queryString += "    ?s rdfs:subClassOf obo:SYMP_0000462 . \n";
			queryString += "    ?s rdfs:label ?label . \n";
			queryString += "    ?s oboInOwl:id ?id . \n";
			queryString += "}";

			System.out.println(queryString);
			TupleQuery query = conn.prepareTupleQuery(queryString);
			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) {
				BindingSet solution = result.next();

				String iri = solution.getValue("s").toString();
				String label = solution.getValue("label").toString();
				label = covertStringToCammel(label);

				String id = solution.getValue("id").toString();
				id = id.replaceAll("\"", "");
				String id2 = id.replaceAll(":", "_");

				System.out.println("================================================================");
				System.out.println("Iri " + iri);
				System.out.println("Label " + label);
				System.out.println("Id " + id2);
				SymptomsMainClasses.add(id2);
			}

			// PRIMER NIVEL DE SUBCLASES
			for (int i = 0; i < SymptomsMainClasses.size(); i++) {
				// Recorrer las classes para obtener las subclases

				String superClassId = SymptomsMainClasses.get(i);
				System.out.println("PRIMER NIVEL " + superClassId);

				String queryString2 = "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
				queryString2 += "PREFIX oboInOwl: <http://www.geneontology.org/formats/oboInOwl#> \n";
				queryString2 += "SELECT ?s ?label ?id \n";
				queryString2 += "WHERE { \n";
				queryString2 += "    ?s a owl:Class . \n";
				queryString2 += "    ?s rdfs:subClassOf obo:" + superClassId + " . \n";
				queryString2 += "    ?s rdfs:label ?label . \n";
				queryString2 += "    ?s oboInOwl:id ?id . \n";
				queryString2 += "}";

				TupleQuery query2 = conn.prepareTupleQuery(queryString2);
				TupleQueryResult result2 = query2.evaluate();

				while (result2.hasNext()) {
					BindingSet solution = result2.next();

					String iri = solution.getValue("s").toString();
					String label = solution.getValue("label").toString();
					String label2 = label.replaceAll("\"", "");
					String id = solution.getValue("id").toString();
					id = id.replaceAll("\"", "");
					String id2 = id.replaceAll(":", "_");

					System.out.println("=====SubClass===========================================================");
					System.out.println("Iri " + iri);
					System.out.println("Label " + label);
					System.out.println("Id " + id2);

					listOfSubClasses.add(id2);
				}

			} // end for

			// SEGUNDO NIVEL DE SUBCLASES
			for (int i = 0; i < listOfSubClasses.size(); i++) {
				// Recorrer las subclasses para obtener las subsubclases

				String subClassId = listOfSubClasses.get(i);
				System.out.println("SEGUNDO NIVEL " + subClassId);

				String queryString3 = "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
				queryString3 += "PREFIX oboInOwl: <http://www.geneontology.org/formats/oboInOwl#> \n";
				queryString3 += "SELECT ?s ?label ?id \n";
				queryString3 += "WHERE { \n";
				queryString3 += "    ?s a owl:Class . \n";
				queryString3 += "    ?s rdfs:subClassOf obo:" + subClassId + " . \n";
				queryString3 += "    ?s rdfs:label ?label . \n";
				queryString3 += "    ?s oboInOwl:id ?id . \n";
				queryString3 += "}";

				TupleQuery query2 = conn.prepareTupleQuery(queryString3);
				TupleQueryResult result3 = query2.evaluate();

				if (result3.hasNext()) {
					System.out.println("Tiene subclases por lo tanto ES UNA CLASE");
					while (result3.hasNext()) {
						BindingSet solution = result3.next();

						String iri = solution.getValue("s").toString();
						String label = solution.getValue("label").toString();
						String label2 = label.replaceAll("\"", "");
						String id = solution.getValue("id").toString();
						id = id.replaceAll("\"", "");
						String id2 = id.replaceAll(":", "_");

						System.out.println(
								"=====Sub Sub Class===========================================================");
						System.out.println("Iri " + iri);
						System.out.println("Label " + label);
						System.out.println("Id " + id2);

						listOfSubSubClasses.add(id2);
					}
				} else {
					System.out.println("No tiene subclases por lo tanto ES UN INDIVIDUO");
				}
			} // end for

		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}

	}

	// Método que obtiene la lista de pruebas de laboratorio de la ontologia
	// LOINC.owl
	public List<LaboratoryClass> getLaboratoryTests() {
		List<LaboratoryClass> listLabClasses = new ArrayList<LaboratoryClass>();
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/LOINCRDF.owl");
		String baseURI = "http://purl.bioontology.org/ontology/LNC/";

		// Primero se genera la lista de clases
		try {
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			String queryString = "PREFIX loinc: <http://purl.bioontology.org/ontology/LNC/> \n";
			queryString += "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n";
			queryString += "SELECT ?s ?label ?not \n";
			queryString += "WHERE { \n";
			queryString += "    ?s a owl:Class . \n";
			queryString += "    ?s rdfs:subClassOf loinc:MTHU000001 . \n";
			queryString += "    ?s skos:prefLabel ?label . \n";
			queryString += "    ?s skos:notation ?not . \n";
			queryString += "}";

			System.out.println(queryString);
			TupleQuery query = conn.prepareTupleQuery(queryString);
			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) {
				BindingSet solution = result.next();

				String iri = solution.getValue("s").toString();
				String label = solution.getValue("label").toString();
				label = covertStringToCammel(label);

				String id = solution.getValue("not").toString();
				String id2 = id.replaceAll("\"", "");

				LaboratoryClass lab = new LaboratoryClass(id2, iri, label);
				listLabClasses.add(lab);
			}

			for (int i = 0; i < listLabClasses.size(); i++) {
				// Recorrer las classes para obtener las subclases
				List<LaboratoryIndividual> listOfSubClasses = new ArrayList<LaboratoryIndividual>();

				String superClassId = listLabClasses.get(i).getId();
				System.out.println("Search for subclasses of " + superClassId);

				String queryString2 = "PREFIX loinc: <http://purl.bioontology.org/ontology/LNC/> \n";
				queryString2 += "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n";
				queryString2 += "SELECT ?s ?label ?not \n";
				queryString2 += "WHERE { \n";
				queryString2 += "    ?s a owl:Class . \n";
				queryString2 += "    ?s rdfs:subClassOf loinc:" + superClassId + " . \n";
				queryString2 += "    ?s skos:prefLabel ?label . \n";
				queryString2 += "    ?s skos:notation ?not . \n";
				queryString2 += "}";

				TupleQuery query2 = conn.prepareTupleQuery(queryString2);
				TupleQueryResult result2 = query2.evaluate();

				while (result2.hasNext()) {
					BindingSet solution = result2.next();

					String iri = solution.getValue("s").toString();
					String label = solution.getValue("label").toString();
					String label2 = label.replaceAll("\"", "");
					String id = solution.getValue("not").toString();
					String id2 = id.replaceAll("\"", "");

					LaboratoryIndividual labInd = new LaboratoryIndividual(id2, iri, label2);
					listOfSubClasses.add(labInd);
				}
				listLabClasses.get(i).setIndividuals(listOfSubClasses);
			} // end for

		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}

		return listLabClasses;
	}

	// Método para leer los SYNONYMS de los medicamentos de la ontología NDFRT
	public List<Synonym> getSynonymsOfMedicaments() {
		List<Synonym> listSynonyms = new ArrayList<Synonym>();
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/NDF-RT.owl");
		String baseURI = "http://evs.nci.nih.gov/ftp1/NDF-RT/NDF-RT.owl#";

		try {
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			String queryString = "PREFIX ndf: <http://evs.nci.nih.gov/ftp1/NDF-RT/NDF-RT.owl#> \n";
			queryString += "SELECT ?medicament ?nui ?synonym \n";
			queryString += "WHERE { \n";
			queryString += "    ?medicament a owl:Class . \n";
			queryString += "    ?medicament rdfs:subClassOf ndf:N0000182631 . \n";
			queryString += "    ?medicament ndf:NUI ?nui . \n";
			queryString += "    ?medicament ndf:Synonym ?synonym . \n";
			queryString += "}";

			System.out.println(queryString);
			TupleQuery query = conn.prepareTupleQuery(queryString);

			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) {
				BindingSet solution = result.next();

				String medicament = solution.getValue("medicament").toString();
				String nui = solution.getValue("nui").toString();
				String nui2 = nui.replaceAll("\"", "");
				String synonym = solution.getValue("synonym").toString();
				String synonym2 = synonym.replaceAll("\"", "");

				System.out.println("================================================");
				System.out.println("Medicament " + medicament);
				System.out.println("Nui " + nui);
				System.out.println("Synonym " + synonym);

				Synonym syn = new Synonym(nui2, synonym2);
				listSynonyms.add(syn);

			}
			System.out.println(" //////////////////////////////////////////////////////////////////////////////  ");
			System.out.println("Numero de synonyms " + listSynonyms.size());

		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}

		return listSynonyms;
	}

	// Método para obtener todas las PROPIEDADES de los medicamentos
	public List<Property> getPropertiesOfMedicaments(List<Medicament> listMeds) 
	{
		List<Property> listOfProperties = new ArrayList<Property>();
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/NDFRT2_RDF.owl");
		String baseURI = "http://evs.nci.nih.gov/ftp1/NDF-RT/NDF-RT.owl#";

		try 
		{
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			for(int i=0; i<listMeds.size(); i++)
			{
				Medicament eachMed = listMeds.get(i);
				String medId = eachMed.getNuiId();

				String queryString = "PREFIX NDF-RT: <http://evs.nci.nih.gov/ftp1/NDF-RT/NDF-RT.owl#> \n";
				queryString += "SELECT ?property ?value ?valueLabel \n";
				queryString += "WHERE { \n";
				queryString += "    NDF-RT:" + medId + " rdf:type owl:Class . \n";
				queryString += "    NDF-RT:" + medId + " rdfs:subClassOf ?s1 . \n";
				queryString += "    ?s1 owl:onProperty ?property; owl:someValuesFrom ?value .\n";
				queryString += "    ?value rdfs:label ?valueLabel . \n";
				queryString += "}";

				//System.out.println(queryString);
				TupleQuery query = conn.prepareTupleQuery(queryString);

				TupleQueryResult result = query.evaluate();

				while (result.hasNext()) 
				{
					BindingSet solution = result.next();

					String property = solution.getValue("property").toString();
					String value = solution.getValue("value").toString();
					String valueLabel = solution.getValue("valueLabel").toString();
					valueLabel = valueLabel.replaceAll("\"", "");

					/*System.out.println("  Property " + property);
					System.out.println("  Value " + value);
					System.out.println("  Value label " + valueLabel);*/
					
					Property prop = new Property(medId, property, value, valueLabel);
					listOfProperties.add(prop);
				}
			}
			
			System.out.println(" //////////////////////////////////////////////////////////////////////////////  ");
			System.out.println("Number of properties " + listOfProperties.size());

		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}

		return listOfProperties;
	}

	// Método para leer las clases de medicamentos de la ontología NDFRT
	public List<Medicament> getListOfMedicamentsNDFRT() 
	{
		List<Medicament> listMedicaments = new ArrayList<Medicament>();
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/NDFRT2_RDF.owl");
		String baseURI = "http://evs.nci.nih.gov/ftp1/NDF-RT/NDF-RT.owl#";

		try {
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			String queryString = "PREFIX NDF-RT: <http://evs.nci.nih.gov/ftp1/NDF-RT/NDF-RT.owl#> \n";
			queryString += "SELECT distinct ?medicament ?label ?units ?strength ?nui ?cui ?name \n";
			queryString += "WHERE { \n";
			queryString += "    ?medicament rdf:type owl:NamedIndividual . \n";
			queryString += "    ?medicament rdfs:label ?label . \n";
			queryString += "    ?medicament NDF-RT:Level \"VA Product\" .";
			queryString += "    ?medicament NDF-RT:Units ?units . \n";
			queryString += "    ?medicament NDF-RT:Strength ?strength . \n";
			queryString += "    ?medicament NDF-RT:NUI ?nui . ";
			queryString += "    ?medicament NDF-RT:RxNorm_CUI ?cui . ";
			queryString += "    ?medicament NDF-RT:RxNorm_Name ?name . ";
			queryString += "}";

			System.out.println(queryString);
			TupleQuery query = conn.prepareTupleQuery(queryString);

			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) {
				BindingSet solution = result.next();

				String medicamentIRI = solution.getValue("medicament").toString();
				String units = solution.getValue("units").toString();
				String strength = solution.getValue("strength").toString();
				String label = solution.getValue("label").toString();
				label = label.replaceAll("\"", "");
				String nui = solution.getValue("nui").toString();
				nui = nui.replaceAll("\"", "");
				String cui = solution.getValue("cui").toString();
				cui = cui.replaceAll("\"", "");
				String name = solution.getValue("name").toString();

				System.out.println("================================================");
				System.out.println("Medicament IRI " + medicamentIRI);
				System.out.println("Units " + units);
				System.out.println("Strength " + strength);
				System.out.println("Label " + label);
				System.out.println("NUI " + nui);
				System.out.println("RxNorm CUI " + cui);
				System.out.println("RxNorm Name " + name);

				Medicament drug = new Medicament(nui, medicamentIRI, label, units, strength, cui, name);

				listMedicaments.add(drug);

			}
			System.out.println("Number of classes identified: " + listMedicaments.size());
			System.out.println(" //////////////////////////////////////////////////////////////////////////////  ");

			/*for (int i = 0; i < listMedicaments.size(); i++)
				System.out.println(listMedicaments.get(i).toString());*/
			
		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}

		return listMedicaments;
	}
	
	// Method to retrieve ingredients of each medicament from NDFRT ontology
	public List<Medicament> getIngredientsOfMedicamentsNDFRT(List<Medicament> listMedicaments) 
	{
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/NDFRT2_RDF.owl");
		String baseURI = "http://evs.nci.nih.gov/ftp1/NDF-RT/NDF-RT.owl#";

		try 
		{
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);
			
			for (int i = 0; i < listMedicaments.size(); i++) 
			{
				List<String> ingredients = new ArrayList<String>();
				String id = listMedicaments.get(i).getNuiId();

			String queryString = "PREFIX NDF-RT: <http://evs.nci.nih.gov/ftp1/NDF-RT/NDF-RT.owl#> \n";
			queryString += "SELECT ?ingredient \n";
			queryString += "WHERE { \n";
			queryString += "    NDF-RT:" + id + " NDF-RT:Product_Component ?ingredient . \n";
			queryString += "}";

			//System.out.println(queryString);
			TupleQuery query = conn.prepareTupleQuery(queryString);

			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) 
			{
				BindingSet solution = result.next();

				String ingredient = solution.getValue("ingredient").toString();

				System.out.println("================================================");
				System.out.println("Medicament NUI " + id);
				System.out.println("Ingredient " + ingredient);
				
			}
			System.out.println(" //////////////////////////////////////////////////////////////////////////////  ");

			}//end for each medicament NUI
		} 
			catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}

		return listMedicaments;
	}

	// Método para leer las clases de medicamentos de la ontología dron-rxnorm.owl
	public void getListOfDrugsDronRxNorm() {
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/dron-rxnorm.owl");
		String baseURI = "http://purl.obolibrary.org/obo/dron/dron-rxnorm.owl#";

		try {
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			String queryString = "PREFIX rxnorm: <http://purl.obolibrary.org/obo/dron/dron-rxnorm.owl> \n";
			queryString += "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
			queryString += "SELECT  ?s ?label ?o ?id ";
			queryString += "WHERE { \n";
			queryString += "    ?s a owl:Class . ";
			queryString += "    ?s rdfs:label ?label . ";
			queryString += "    ?s rdfs:subClassOf ?o .";
			queryString += "    ?s obo:DRON_00010000 ?id . ";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);

			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) {
				BindingSet solution = result.next();
				String iri = solution.getValue("s").toString();
				String name = solution.getValue("label").toString();
				String superC = solution.getValue("o").toString();
				String dronId = solution.getValue("id").toString();
				System.out.println("================================================");
				System.out.println("Clase " + iri);
				System.out.println("Nombre " + name);
				System.out.println("Clase superior " + superC);
				System.out.println("DRON Id " + dronId);
			}
			// System.out.println("Number of classes identified: " + disList.size());
		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}

	}

	// Obtener datos de enfermedades de la ontología DOID
	public List<Disease> getListOfDiseasesFromDOID() 
	{
		List<Disease> disList = new ArrayList<Disease>();
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/doid.owl");
		String baseURI = "http://purl.obolibrary.org/obo/doid.owl#";

		try {
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			String queryString = "PREFIX oboIn: <http://www.geneontology.org/formats/oboInOwl#> \n";
			queryString += "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
			queryString += "SELECT ?s ?label ?def ?id ";
			queryString += "WHERE { \n";
			queryString += "    ?s a owl:Class . ";
			queryString += "    ?s rdfs:label ?label . ";
			queryString += "    ?s obo:IAO_0000115 ?def . ";
			queryString += "    ?s oboIn:id ?id . ";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) {
				BindingSet solution = result.next();
				String iri = solution.getValue("s").toString();
				String name = solution.getValue("label").toString();
				String name2 = name.replaceAll("\"", "");
				String def = solution.getValue("def").toString();
				String def2 = def.replaceAll("\"", "");
				String id = solution.getValue("id").toString();
				String disId = id.replace(":", "_");
				String disId2 = disId.replaceAll("\"", "");
				disList.add(new Disease(disId2, iri, name2, def2));
			}
			System.out.println("Number of classes identified: " + disList.size());
		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}

		return disList;
	}

	// Obtener datos de enfermedades de la ontología DOID
	public List<Disease> getListOfDiseasesFromDOID2() 
	{
		List<Disease> disList = new ArrayList<Disease>();
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/doid2RDF.owl");
		String baseURI = "http://purl.obolibrary.org/obo/doid.owl#";

		try {
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			String queryString = "PREFIX oboIn: <http://www.geneontology.org/formats/oboInOwl#> \n";
			queryString += "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
			queryString += "SELECT DISTINCT ?s ?label ?def ?synonym ?id ";
			queryString += "WHERE { \n";
			queryString += "    ?s rdf:type owl:Class . ";
			queryString += "    ?s rdfs:label ?label . ";
			queryString += "    ?s obo:IAO_0000115 ?def . ";
			queryString += "    ?s oboIn:hasExactSynonym ?synonym . ";
			queryString += "    ?s oboIn:id ?id . ";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) 
			{
				BindingSet solution = result.next();
				String iri = solution.getValue("s").toString();
				String name = solution.getValue("label").toString();
				String name2 = name.replaceAll("\"", "");
				String def = solution.getValue("def").toString();
				String def2 = def.replaceAll("\"", "");
				String id = solution.getValue("id").toString();
				String disId = id.replace(":", "_");
				String disId2 = disId.replaceAll("\"", "");
				String disId3 = disId2.replaceAll("@en", "");

				// Filtering only DOID diseases
				if (disId3.contains("DOID_")) 
				{
					disList.add(new Disease(disId3, iri, name2, def2));
				}
			}
		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}
		return disList;
	}
	
	// Method to recover the list of synonyms for each disease
		public void getSynonymsOfDisease(List<Disease> listDiseases) 
		{
			File dataDir = new File("C:/Ontos/");
			Repository repo = new SailRepository(new MemoryStore());
			repo.init();

			ValueFactory factory = repo.getValueFactory();

			File file = new File("C:/Ontos/doid2RDF.owl");
			String baseURI = "http://purl.obolibrary.org/obo/doid.owl#";

			try 
			{
				RepositoryConnection conn = repo.getConnection();
				conn.add(file, baseURI, RDFFormat.RDFXML);

				for (int i = 0; i < listDiseases.size(); i++) {
					List<String> synonyms = new ArrayList<String>();
					String id = listDiseases.get(i).getId();

					String queryString = "PREFIX oboIn: <http://www.geneontology.org/formats/oboInOwl#> \n";
					queryString += "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
					queryString += "SELECT ?syn ";
					queryString += "WHERE { \n";
					queryString += "    obo:" + id + " oboIn:hasExactSynonym ?syn . ";
					queryString += "}";

					TupleQuery query = conn.prepareTupleQuery(queryString);
					TupleQueryResult result = query.evaluate();

					while (result.hasNext()) 
					{
						BindingSet solution = result.next();
						String syn = solution.getValue("syn").toString();
						syn = eliminateChars(syn);
						synonyms.add(syn);
					}

					listDiseases.get(i).setSynonym(synonyms);
				} // End for

			} catch (RDF4JException | IOException e) {
				e.printStackTrace();
			}
		}

	// Method to recover the list of parent classes for each disease
	public void getParentClasses(List<Disease> listDiseases) 
	{
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/doid2RDF.owl");
		String baseURI = "http://purl.obolibrary.org/obo/doid.owl#";

		try 
		{
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			for (int i = 0; i < listDiseases.size(); i++) {
				List<String> parentClasses = new ArrayList<String>();
				String id = listDiseases.get(i).getId();

				String queryString = "PREFIX oboIn: <http://www.geneontology.org/formats/oboInOwl#> \n";
				queryString += "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
				queryString += "SELECT ?parent ";
				queryString += "WHERE { \n";
				queryString += "    obo:" + id + " rdfs:subClassOf ?parent . ";
				queryString += "}";

				TupleQuery query = conn.prepareTupleQuery(queryString);
				TupleQueryResult result = query.evaluate();

				while (result.hasNext()) 
				{
					BindingSet solution = result.next();
					String parent = solution.getValue("parent").toString();
					if(parent.startsWith("http:"))
					{
						parentClasses.add(parent);
					}
					
				}

				listDiseases.get(i).setParent(parentClasses);
			} // End for

		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}
	}

	// Method to recover data base cross references
	public void getDbXRefs(List<Disease> listDiseases) {
		File dataDir = new File("C:/Ontos/");
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		ValueFactory factory = repo.getValueFactory();

		File file = new File("C:/Ontos/doid2RDF.owl");
		String baseURI = "http://purl.obolibrary.org/obo/doid.owl#";

		try {
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			for (int i = 0; i < listDiseases.size(); i++) {
				List<String> refs = new ArrayList<String>();
				String id = listDiseases.get(i).getId();

				String queryString = "PREFIX oboIn: <http://www.geneontology.org/formats/oboInOwl#> \n";
				queryString += "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
				queryString += "SELECT ?ref ";
				queryString += "WHERE { \n";
				queryString += "    obo:" + id + " oboIn:hasDbXref ?ref . ";
				queryString += "}";

				TupleQuery query = conn.prepareTupleQuery(queryString);
				TupleQueryResult result = query.evaluate();

				while (result.hasNext()) {
					BindingSet solution = result.next();
					String ref = solution.getValue("ref").toString();
					ref = eliminateChars(ref);
					refs.add(ref);
				}
				listDiseases.get(i).setRefs(refs);
			} // End for

		} catch (RDF4JException | IOException e) {
			e.printStackTrace();
		}
	}

	public void addDiseaseListToOntology(String ontoFile, List<Disease> listDiseases) 
	{
		try 
		{
			File file = new File(ontoFile);
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
			IRI iri = ontology.getOntologyID().getOntologyIRI();
			OWLOntologyFormat format = man.getOntologyFormat(ontology);
			System.out.println("Format detected " + format.toString());

			System.out.println("Loaded ontology: " + ontology);
			System.out.println("Ontology IRI: " + iri);
			System.out.println("Number of axioms: " + ontology.getAxiomCount());

			OWLDataFactory factory = man.getOWLDataFactory();

			for (int i = 0; i < listDiseases.size(); i++) 
			{
				Disease oneDis = listDiseases.get(i);
				// System.out.println("Adding " + oneDis.toString());

				OWLClass disease = factory.getOWLClass(IRI.create(iri + "#Disease"));
				OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(iri + "#" + oneDis.getId()));
				OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(disease, ind);
				man.addAxiom(ontology, classAssertion);

				OWLDataProperty diseaseId = factory.getOWLDataProperty(IRI.create(iri + "#hasIri"));
				OWLLiteral litDisId = factory.getOWLTypedLiteral(oneDis.getIri());
				OWLDataPropertyAssertionAxiom propDisId = factory.getOWLDataPropertyAssertionAxiom(diseaseId, ind,
						litDisId);
				man.addAxiom(ontology, propDisId);

				OWLDataProperty diseaseName = factory.getOWLDataProperty(IRI.create(iri + "#hasName"));
				OWLLiteral litDisName = factory.getOWLTypedLiteral(oneDis.getName());
				OWLDataPropertyAssertionAxiom propDisName = factory.getOWLDataPropertyAssertionAxiom(diseaseName, ind,
						litDisName);
				man.addAxiom(ontology, propDisName);

				OWLDataProperty diseaseDefinition = factory.getOWLDataProperty(IRI.create(iri + "#hasDefinition"));
				OWLLiteral litDisDef = factory.getOWLTypedLiteral(oneDis.getDefinition());
				OWLDataPropertyAssertionAxiom propDisDef = factory.getOWLDataPropertyAssertionAxiom(diseaseDefinition,
						ind, litDisDef);
				man.addAxiom(ontology, propDisDef);
				
				//Adding synonyms
				for(String synonym : listDiseases.get(i).getSynonym())
				{
					OWLDataProperty diseaseSynonym = factory.getOWLDataProperty(IRI.create(iri + "#hasSynonym"));
					OWLLiteral synonymLiteral = factory.getOWLTypedLiteral(synonym);
					OWLDataPropertyAssertionAxiom propDisSyn = factory.getOWLDataPropertyAssertionAxiom(diseaseSynonym, ind, synonymLiteral);
					man.addAxiom(ontology, propDisSyn);
				}
				
				//Adding parent classes
				for(String parentClass : listDiseases.get(i).getParent())
				{
					OWLDataProperty diseaseParentClass = factory.getOWLDataProperty(IRI.create(iri + "#hasParentClass"));
					OWLLiteral parentLiteral = factory.getOWLTypedLiteral(parentClass);
					OWLDataPropertyAssertionAxiom propDisParent = factory.getOWLDataPropertyAssertionAxiom(diseaseParentClass, ind, parentLiteral);
					man.addAxiom(ontology, propDisParent);
				}
				
				//Adding DbXrefs
				for(String dbxRef : listDiseases.get(i).getRefs())
				{
					OWLDataProperty diseaseDbXRef = factory.getOWLDataProperty(IRI.create(iri + "#hasDbXref"));
					OWLLiteral dbXRefLiteral = factory.getOWLTypedLiteral(dbxRef);
					OWLDataPropertyAssertionAxiom propDiseDbXref = factory.getOWLDataPropertyAssertionAxiom(diseaseDbXRef, ind, dbXRefLiteral);
					man.addAxiom(ontology, propDiseDbXref);
				}
			}

			String fileOnt = new File(ontoFile).toURI().toString();

			man.saveOntology(ontology, new RDFXMLOntologyFormat(), IRI.create(fileOnt));

		} catch (OWLOntologyCreationException e) {
			System.out.println("Ontology could not be loaded: " + e.getMessage());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	//Method to populate the initial Medicament ontology
	public void populateMedicamentOntology(String ontoFile, List<Medicament> listOfMedicaments) 
	{
		try 
		{
			File file = new File(ontoFile);
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
			IRI iri = ontology.getOntologyID().getOntologyIRI();
			OWLOntologyFormat format = man.getOntologyFormat(ontology);
			System.out.println("Format detected " + format.toString());

			System.out.println("Loaded ontology: " + ontology);
			System.out.println("Ontology IRI: " + iri);
			System.out.println("Number of axioms: " + ontology.getAxiomCount());

			OWLDataFactory factory = man.getOWLDataFactory();

			for (int i = 0; i < listOfMedicaments.size(); i++) 
			{
				Medicament oneMedicament = listOfMedicaments.get(i);
				System.out.println("Recording " + oneMedicament.toString());

				// Create individual
				OWLClass medIndividual = factory.getOWLClass(IRI.create(iri + "#Medicament"));
				OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(iri + "#" + oneMedicament.getNuiId()));
				OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(medIndividual, ind);
				man.addAxiom(ontology, classAssertion);

				//String data properties to register for each medicament
				//nuiId, medicamentIRI, label, units, strength, rxNormCUI, rxNormName
				
				//Create data property assertions
				OWLDataProperty medicamentIRI = factory.getOWLDataProperty(IRI.create(iri + "#hasIRI"));
				OWLLiteral litMedIRI = factory.getOWLTypedLiteral(oneMedicament.getMedicamentIRI());
				OWLDataPropertyAssertionAxiom propMedIRI = factory.getOWLDataPropertyAssertionAxiom(medicamentIRI, ind,litMedIRI);
				man.addAxiom(ontology, propMedIRI);

				OWLDataProperty medicamentLabel = factory.getOWLDataProperty(IRI.create(iri + "#hasLabel"));
				OWLLiteral litMedName = factory.getOWLTypedLiteral(oneMedicament.getLabel());
				OWLDataPropertyAssertionAxiom propMedName = factory.getOWLDataPropertyAssertionAxiom(medicamentLabel, ind, litMedName);
				man.addAxiom(ontology, propMedName);

				OWLDataProperty medicamentUnits = factory.getOWLDataProperty(IRI.create(iri + "#hasUnits"));
				OWLLiteral litMedUnits = factory.getOWLTypedLiteral(oneMedicament.getUnits());
				OWLDataPropertyAssertionAxiom propMedUnits = factory.getOWLDataPropertyAssertionAxiom(medicamentUnits,ind, litMedUnits);
				man.addAxiom(ontology, propMedUnits);
				
				OWLDataProperty medicamentStrength = factory.getOWLDataProperty(IRI.create(iri + "#hasStrength"));
				OWLLiteral litMedStrength = factory.getOWLTypedLiteral(oneMedicament.getStrength());
				OWLDataPropertyAssertionAxiom propMedStrength = factory.getOWLDataPropertyAssertionAxiom(medicamentStrength,ind, litMedStrength);
				man.addAxiom(ontology, propMedStrength);
				
				OWLDataProperty medicamentRxNorm = factory.getOWLDataProperty(IRI.create(iri + "#hasRxNormCUI"));
				OWLLiteral litMedRxNorm = factory.getOWLTypedLiteral(oneMedicament.getRxNormCUI());
				OWLDataPropertyAssertionAxiom propMedRxNorm = factory.getOWLDataPropertyAssertionAxiom(medicamentRxNorm,ind, litMedRxNorm);
				man.addAxiom(ontology, propMedRxNorm);
				
				OWLDataProperty medicamentRxNormName = factory.getOWLDataProperty(IRI.create(iri + "#hasRxNormName"));
				OWLLiteral litMedRxNormName = factory.getOWLTypedLiteral(oneMedicament.getRxNormCUI());
				OWLDataPropertyAssertionAxiom propMedRxNormName = factory.getOWLDataPropertyAssertionAxiom(medicamentRxNormName,ind, litMedRxNormName);
				man.addAxiom(ontology, propMedRxNormName);
				
			}

			String fileOnt = new File(ontoFile).toURI().toString();

			man.saveOntology(ontology, new RDFXMLOntologyFormat(), IRI.create(fileOnt));

		} catch (OWLOntologyCreationException e) {
			System.out.println("Ontology could not be loaded: " + e.getMessage());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	public void addSynonymstoMedicaments(String ontoFile, List<Synonym> listOfSynonyms) {
		try {
			File file = new File(ontoFile);
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
			IRI iri = ontology.getOntologyID().getOntologyIRI();
			OWLOntologyFormat format = man.getOntologyFormat(ontology);
			System.out.println("Format detected " + format.toString());

			System.out.println("Loaded ontology: " + ontology);
			System.out.println("Ontology IRI: " + iri);
			System.out.println("Number of axioms: " + ontology.getAxiomCount());

			OWLDataFactory factory = man.getOWLDataFactory();

			for (int i = 0; i < listOfSynonyms.size(); i++) {
				Synonym synonymForMedicament = listOfSynonyms.get(i);

				// Select individual
				OWLClass medIndividual = factory.getOWLClass(IRI.create(iri + "#Medicament"));
				OWLNamedIndividual ind = factory
						.getOWLNamedIndividual(IRI.create(iri + "#" + synonymForMedicament.getIdentifier()));
				OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(medIndividual, ind);
				man.addAxiom(ontology, classAssertion);

				// Create data property assertions
				OWLDataProperty medSynonym = factory.getOWLDataProperty(IRI.create(iri + "#hasSynonym"));
				OWLLiteral literalSynonym = factory.getOWLTypedLiteral(synonymForMedicament.getMeaning());
				OWLDataPropertyAssertionAxiom propHasSynonym = factory.getOWLDataPropertyAssertionAxiom(medSynonym, ind,
						literalSynonym);
				man.addAxiom(ontology, propHasSynonym);

			}

			String fileOnt = new File(ontoFile).toURI().toString();

			man.saveOntology(ontology, new RDFXMLOntologyFormat(), IRI.create(fileOnt));

		} catch (OWLOntologyCreationException e) {
			System.out.println("Ontology could not be loaded: " + e.getMessage());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	public void populatePropertiesOfMedicaments(String ontoFile, List<Property> listOfProperties) 
	{
		try 
		{
			File file = new File(ontoFile);
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
			IRI iri = ontology.getOntologyID().getOntologyIRI();
			OWLOntologyFormat format = man.getOntologyFormat(ontology);
			System.out.println("Format detected " + format.toString());

			System.out.println("Loaded ontology: " + ontology);
			System.out.println("Ontology IRI: " + iri);
			System.out.println("Number of axioms: " + ontology.getAxiomCount());

			OWLDataFactory factory = man.getOWLDataFactory();

			for (int i = 0; i < listOfProperties.size(); i++) 
			{
				Property propertyForMedicament = listOfProperties.get(i);

				// Select individual
				OWLClass medIndividual = factory.getOWLClass(IRI.create(iri + "#Medicament"));
				OWLNamedIndividual medInd = factory
						.getOWLNamedIndividual(IRI.create(iri + "#" + propertyForMedicament.getSubjectIdentifier()));
				OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(medIndividual, medInd);
				man.addAxiom(ontology, classAssertion);

				// Create object property assertions
				System.out.println(
						"=== CREATING OBJECT PROPERTY ASSERTIONS =============================================");

				// Add specific properties
				if (propertyForMedicament.getRelationship().endsWith("CI_ChemClass")) 
				{
					System.out.println("Creating has CI Chemical Class relationship");
					// Create ChemicalIngredient individual
					OWLClass chemClass = factory.getOWLClass(IRI.create(iri + "#ChemicalIngredient"));
					OWLNamedIndividual chemInd = factory.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
					OWLClassAssertionAxiom chemClassAssertion = factory.getOWLClassAssertionAxiom(chemClass, chemInd);
					man.addAxiom(ontology, chemClassAssertion);

					// Create data type property hasChemicalName
					OWLDataProperty chemName = factory.getOWLDataProperty(IRI.create(iri + "#hasChemicalName"));
					OWLLiteral literalChemName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
					OWLDataPropertyAssertionAxiom propHasChemName = factory.getOWLDataPropertyAssertionAxiom(chemName,chemInd, literalChemName);
					man.addAxiom(ontology, propHasChemName);

					// Create object property relation between medicament and new individual
					OWLObjectProperty medHasChemStr = factory.getOWLObjectProperty(IRI.create(iri + "#hasCIChemicalClass"));
					OWLObjectPropertyAssertionAxiom medHasChemAssertion = factory.getOWLObjectPropertyAssertionAxiom(medHasChemStr, medInd, chemInd);
					man.addAxiom(ontology, medHasChemAssertion);

				} 
				else if (propertyForMedicament.getRelationship().endsWith("has_Ingredient")) 
				{
					System.out.println("Creating has ingredient relationship");
					// Create TerapeuticCategory individual
					OWLClass tcClass = factory.getOWLClass(IRI.create(iri + "#ChemicalIngredient"));
					OWLNamedIndividual tcInd = factory.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
					OWLClassAssertionAxiom tcClassAssertion = factory.getOWLClassAssertionAxiom(tcClass, tcInd);
					man.addAxiom(ontology, tcClassAssertion);

					// Create data type property hasChemicalName
					OWLDataProperty tcName = factory.getOWLDataProperty(IRI.create(iri + "#hasChemicalName"));
					OWLLiteral literalTCName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
					OWLDataPropertyAssertionAxiom propHasTCName = factory.getOWLDataPropertyAssertionAxiom(tcName,tcInd, literalTCName);
					man.addAxiom(ontology, propHasTCName);

					// Create object property relation between medicament and new individual
					OWLObjectProperty medHasTC = factory.getOWLObjectProperty(IRI.create(iri + "#hasIngredient"));
					OWLObjectPropertyAssertionAxiom medHasTCAssertion = factory.getOWLObjectPropertyAssertionAxiom(medHasTC, medInd, tcInd);
					man.addAxiom(ontology, medHasTCAssertion);
					
				} 
				else if (propertyForMedicament.getRelationship().endsWith("has_active_metabolites")) 
				{
					System.out.println("Creating has active metabolites relationship");
					// Create Chemical Ingredient individual
					OWLClass tcClass = factory.getOWLClass(IRI.create(iri + "#ChemicalIngredient"));
					OWLNamedIndividual tcInd = factory.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
					OWLClassAssertionAxiom tcClassAssertion = factory.getOWLClassAssertionAxiom(tcClass, tcInd);
					man.addAxiom(ontology, tcClassAssertion);

					// Create data type property hasChemicalName
					OWLDataProperty tcName = factory.getOWLDataProperty(IRI.create(iri + "#hasChemicalName"));
					OWLLiteral literalTCName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
					OWLDataPropertyAssertionAxiom propHasTCName = factory.getOWLDataPropertyAssertionAxiom(tcName,tcInd, literalTCName);
					man.addAxiom(ontology, propHasTCName);

					// Create object property relation between medicament and new individual
					OWLObjectProperty medHasTC = factory.getOWLObjectProperty(IRI.create(iri + "#hasActiveMetabolites"));
					OWLObjectPropertyAssertionAxiom medHasTCAssertion = factory.getOWLObjectPropertyAssertionAxiom(medHasTC, medInd, tcInd);
					man.addAxiom(ontology, medHasTCAssertion);
					
				} 
				else if (propertyForMedicament.getRelationship().endsWith("has_MoA")) 
				{
					System.out.println("Creating has mechanism of action relationship");
					// Create MechanismOfAction individual
					OWLClass moaClass = factory.getOWLClass(IRI.create(iri + "#MechanismOfAction"));
					OWLNamedIndividual moaInd = factory.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
					OWLClassAssertionAxiom moaClassAssertion = factory.getOWLClassAssertionAxiom(moaClass, moaInd);
					man.addAxiom(ontology, moaClassAssertion);

					// Create data type property hasMechanismName
					OWLDataProperty moaName = factory.getOWLDataProperty(IRI.create(iri + "#hasMechanismName"));
					OWLLiteral literalMoAName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
					OWLDataPropertyAssertionAxiom propHasMoAName = factory.getOWLDataPropertyAssertionAxiom(moaName,moaInd, literalMoAName);
					man.addAxiom(ontology, propHasMoAName);

					// Create object property relation between medicament and new individual
					OWLObjectProperty medHasMoA = factory.getOWLObjectProperty(IRI.create(iri + "#hasMechanismOfAction"));
					OWLObjectPropertyAssertionAxiom medHasMoAAssertion = factory.getOWLObjectPropertyAssertionAxiom(medHasMoA, medInd, moaInd);
					man.addAxiom(ontology, medHasMoAAssertion);
				}
					
				else if (propertyForMedicament.getRelationship().endsWith("CI_MoA")) 
				{
						System.out.println("Creating has CI mechanism of action relationship");
						// Create MechanismOfAction individual
						OWLClass moaClass = factory.getOWLClass(IRI.create(iri + "#MechanismOfAction"));
						OWLNamedIndividual moaInd = factory.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
						OWLClassAssertionAxiom moaClassAssertion = factory.getOWLClassAssertionAxiom(moaClass, moaInd);
						man.addAxiom(ontology, moaClassAssertion);

						// Create data type property hasMechanismName
						OWLDataProperty moaName = factory.getOWLDataProperty(IRI.create(iri + "#hasMechanismName"));
						OWLLiteral literalMoAName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
						OWLDataPropertyAssertionAxiom propHasMoAName = factory.getOWLDataPropertyAssertionAxiom(moaName, moaInd, literalMoAName);
						man.addAxiom(ontology, propHasMoAName);

						// Create object property relation between medicament and new individual
						OWLObjectProperty medHasMoA = factory.getOWLObjectProperty(IRI.create(iri + "#hasCIMoA"));
						OWLObjectPropertyAssertionAxiom medHasMoAAssertion = factory.getOWLObjectPropertyAssertionAxiom(medHasMoA, medInd, moaInd);
						man.addAxiom(ontology, medHasMoAAssertion);

				} 
					else if (propertyForMedicament.getRelationship().endsWith("has_PE")) {
					System.out.println("Creating has physiological effect relationship");
					OWLClass peClass = factory.getOWLClass(IRI.create(iri + "#PhysiologicalEffect"));
					OWLNamedIndividual peInd = factory
							.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
					OWLClassAssertionAxiom peClassAssertion = factory.getOWLClassAssertionAxiom(peClass, peInd);
					man.addAxiom(ontology, peClassAssertion);

					// Create data type property hasEffectName
					OWLDataProperty peName = factory.getOWLDataProperty(IRI.create(iri + "#hasPhysiologicalEffectName"));
					OWLLiteral literalPEName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
					OWLDataPropertyAssertionAxiom propHasPEName = factory.getOWLDataPropertyAssertionAxiom(peName,
							peInd, literalPEName);
					man.addAxiom(ontology, propHasPEName);

					// Create object property relation between medicament and new individual
					OWLObjectProperty medHasPE = factory
							.getOWLObjectProperty(IRI.create(iri + "#hasPhysiologicalEffect"));
					OWLObjectPropertyAssertionAxiom medHasPEAssertion = factory
							.getOWLObjectPropertyAssertionAxiom(medHasPE, medInd, peInd);
					man.addAxiom(ontology, medHasPEAssertion);
					}
					else if (propertyForMedicament.getRelationship().endsWith("CI_PE")) {
						System.out.println("Creating has CI physiological effect relationship");
						OWLClass peClass = factory.getOWLClass(IRI.create(iri + "#PhysiologicalEffect"));
						OWLNamedIndividual peInd = factory
								.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
						OWLClassAssertionAxiom peClassAssertion = factory.getOWLClassAssertionAxiom(peClass, peInd);
						man.addAxiom(ontology, peClassAssertion);

						// Create data type property hasEffectName
						OWLDataProperty peName = factory.getOWLDataProperty(IRI.create(iri + "#hasPhysiologicalEffectName"));
						OWLLiteral literalPEName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
						OWLDataPropertyAssertionAxiom propHasPEName = factory.getOWLDataPropertyAssertionAxiom(peName,
								peInd, literalPEName);
						man.addAxiom(ontology, propHasPEName);

						// Create object property relation between medicament and new individual
						OWLObjectProperty medHasPE = factory
								.getOWLObjectProperty(IRI.create(iri + "#hasCIPE"));
						OWLObjectPropertyAssertionAxiom medHasPEAssertion = factory
								.getOWLObjectPropertyAssertionAxiom(medHasPE, medInd, peInd);
						man.addAxiom(ontology, medHasPEAssertion);

				} 
					else if (propertyForMedicament.getRelationship().endsWith("has_DoseForm")) {
						System.out.println("Creating may prevent relationship");
						OWLClass diseaseClass = factory.getOWLClass(IRI.create(iri + "#DoseForm"));
						OWLNamedIndividual disInd = factory
								.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
						OWLClassAssertionAxiom diseaseClassAssertion = factory.getOWLClassAssertionAxiom(diseaseClass,
								disInd);
						man.addAxiom(ontology, diseaseClassAssertion);

						// Create data type property hasDiseaseName
						OWLDataProperty disName = factory.getOWLDataProperty(IRI.create(iri + "#hasDoseDescription"));
						OWLLiteral literalDisName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
						OWLDataPropertyAssertionAxiom propHasDisName = factory.getOWLDataPropertyAssertionAxiom(disName,
								disInd, literalDisName);
						man.addAxiom(ontology, propHasDisName);

						// Create object property relation between medicament and new individual
						OWLObjectProperty medMayPrevent = factory.getOWLObjectProperty(IRI.create(iri + "#hasDoseForm"));
						OWLObjectPropertyAssertionAxiom medMayPreventAssertion = factory
								.getOWLObjectPropertyAssertionAxiom(medMayPrevent, medInd, disInd);
						man.addAxiom(ontology, medMayPreventAssertion);
						
					}
					
					else if (propertyForMedicament.getRelationship().endsWith("may_prevent")) {
					System.out.println("Creating may prevent relationship");
					OWLClass diseaseClass = factory.getOWLClass(IRI.create(iri + "#Disease"));
					OWLNamedIndividual disInd = factory
							.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
					OWLClassAssertionAxiom diseaseClassAssertion = factory.getOWLClassAssertionAxiom(diseaseClass,
							disInd);
					man.addAxiom(ontology, diseaseClassAssertion);

					// Create data type property hasDiseaseName
					OWLDataProperty disName = factory.getOWLDataProperty(IRI.create(iri + "#hasDiseaseName"));
					OWLLiteral literalDisName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
					OWLDataPropertyAssertionAxiom propHasDisName = factory.getOWLDataPropertyAssertionAxiom(disName,
							disInd, literalDisName);
					man.addAxiom(ontology, propHasDisName);

					// Create object property relation between medicament and new individual
					OWLObjectProperty medMayPrevent = factory.getOWLObjectProperty(IRI.create(iri + "#mayPrevent"));
					OWLObjectPropertyAssertionAxiom medMayPreventAssertion = factory
							.getOWLObjectPropertyAssertionAxiom(medMayPrevent, medInd, disInd);
					man.addAxiom(ontology, medMayPreventAssertion);

				} else if (propertyForMedicament.getRelationship().endsWith("may_treat")) {
					System.out.println("Creating may treat relationship");
					OWLClass diseaseClass = factory.getOWLClass(IRI.create(iri + "#Disease"));
					OWLNamedIndividual disInd = factory
							.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
					OWLClassAssertionAxiom diseaseClassAssertion = factory.getOWLClassAssertionAxiom(diseaseClass,
							disInd);
					man.addAxiom(ontology, diseaseClassAssertion);

					// Create data type property hasDiseaseName
					OWLDataProperty disName = factory.getOWLDataProperty(IRI.create(iri + "#hasDiseaseName"));
					OWLLiteral literalDisName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
					OWLDataPropertyAssertionAxiom propHasDisName = factory.getOWLDataPropertyAssertionAxiom(disName,
							disInd, literalDisName);
					man.addAxiom(ontology, propHasDisName);

					// Create object property relation between medicament and new individual
					OWLObjectProperty medMayTreat = factory.getOWLObjectProperty(IRI.create(iri + "#mayTreat"));
					OWLObjectPropertyAssertionAxiom medMayPreventAssertion = factory
							.getOWLObjectPropertyAssertionAxiom(medMayTreat, medInd, disInd);
					man.addAxiom(ontology, medMayPreventAssertion);

				} else if (propertyForMedicament.getRelationship().endsWith("may_diagnose")) {
					System.out.println("Creating may diagnose relationship");
					OWLClass diseaseClass = factory.getOWLClass(IRI.create(iri + "#Disease"));
					OWLNamedIndividual disInd = factory
							.getOWLNamedIndividual(IRI.create(propertyForMedicament.getObjectIRI()));
					OWLClassAssertionAxiom diseaseClassAssertion = factory.getOWLClassAssertionAxiom(diseaseClass,
							disInd);
					man.addAxiom(ontology, diseaseClassAssertion);

					// Create data type property hasDiseaseName
					OWLDataProperty disName = factory.getOWLDataProperty(IRI.create(iri + "#hasDiseaseName"));
					OWLLiteral literalDisName = factory.getOWLTypedLiteral(propertyForMedicament.getObjectValue());
					OWLDataPropertyAssertionAxiom propHasDisName = factory.getOWLDataPropertyAssertionAxiom(disName,
							disInd, literalDisName);
					man.addAxiom(ontology, propHasDisName);

					// Create object property relation between medicament and new individual
					OWLObjectProperty medMayDiagnose = factory.getOWLObjectProperty(IRI.create(iri + "#mayDiagnose"));
					OWLObjectPropertyAssertionAxiom medMayDiagnoseAssertion = factory
							.getOWLObjectPropertyAssertionAxiom(medMayDiagnose, medInd, disInd);
					man.addAxiom(ontology, medMayDiagnoseAssertion);

				}

			}

			String fileOnt = new File(ontoFile).toURI().toString();

			man.saveOntology(ontology, new RDFXMLOntologyFormat(), IRI.create(fileOnt));

		} catch (OWLOntologyCreationException e) {
			System.out.println("Ontology could not be loaded: " + e.getMessage());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	public void populateLaboratoryOntology(String ontoFile, List<LaboratoryClass> listOfLabTests) {
		try {
			File file = new File(ontoFile);
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
			IRI iri = ontology.getOntologyID().getOntologyIRI();
			OWLOntologyFormat format = man.getOntologyFormat(ontology);
			System.out.println("Format detected " + format.toString());

			System.out.println("Loaded ontology: " + ontology);
			System.out.println("Ontology IRI: " + iri);
			System.out.println("Number of axioms: " + ontology.getAxiomCount());

			OWLDataFactory factory = man.getOWLDataFactory();

			for (int i = 0; i < listOfLabTests.size(); i++) {
				// Create sub class of LaboratoryTest
				String subClassName = listOfLabTests.get(i).getName();
				OWLClass superClass = factory.getOWLClass(IRI.create(iri + "#LaboratoryTest"));
				OWLClass subClass = factory.getOWLClass(IRI.create(iri + "#" + subClassName));
				OWLSubClassOfAxiom subClassAssertion = factory.getOWLSubClassOfAxiom(subClass, superClass);
				man.addAxiom(ontology, subClassAssertion);

				// Add individuals for each class
				List<LaboratoryIndividual> listOfIndividuals = listOfLabTests.get(i).getIndividuals();
				for (int j = 0; j < listOfIndividuals.size(); j++) {
					LaboratoryIndividual indToCreate = listOfIndividuals.get(j);
					System.out.println("Creating a new lab test individual");
					// Reusing the subClass iri
					OWLNamedIndividual labTestInd = factory.getOWLNamedIndividual(IRI.create(indToCreate.getId()));
					OWLClassAssertionAxiom labTestIndAssertion = factory.getOWLClassAssertionAxiom(subClass,
							labTestInd);
					man.addAxiom(ontology, labTestIndAssertion);

					// Create data type property hasDescription
					OWLDataProperty labTestDesc = factory.getOWLDataProperty(IRI.create(iri + "#hasDescription"));
					OWLLiteral literalTestDesc = factory.getOWLTypedLiteral(indToCreate.getDesc());
					OWLDataPropertyAssertionAxiom propTestHasDesc = factory
							.getOWLDataPropertyAssertionAxiom(labTestDesc, labTestInd, literalTestDesc);
					man.addAxiom(ontology, propTestHasDesc);

					// Create data type property hasLOINCIRI
					OWLDataProperty loincIRI = factory.getOWLDataProperty(IRI.create(iri + "#hasLOINCIRI"));
					OWLLiteral literalLoincIRI = factory.getOWLTypedLiteral(indToCreate.getIri());
					OWLDataPropertyAssertionAxiom propHasLoincIRI = factory.getOWLDataPropertyAssertionAxiom(loincIRI,
							labTestInd, literalLoincIRI);
					man.addAxiom(ontology, propHasLoincIRI);

				} // end iterating over indivuduals

			} // end iterating over classes

			String fileOnt = new File(ontoFile).toURI().toString();
			man.saveOntology(ontology, new RDFXMLOntologyFormat(), IRI.create(fileOnt));

		} catch (OWLOntologyCreationException e) {
			System.out.println("Ontology could not be loaded: " + e.getMessage());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	//Method to populate the Sympton ontology
	public void populateSymptomsOntology(String ontoFile, List<Symtom> listOfSymptoms) 
	{
		try {
			File file = new File(ontoFile);
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
			IRI iri = ontology.getOntologyID().getOntologyIRI();
			OWLOntologyFormat format = man.getOntologyFormat(ontology);
			System.out.println("Format detected " + format.toString());

			System.out.println("Loaded ontology: " + ontology);
			System.out.println("Ontology IRI: " + iri);
			System.out.println("Number of axioms: " + ontology.getAxiomCount());

			OWLDataFactory factory = man.getOWLDataFactory();

			for (int i = 0; i < listOfSymptoms.size(); i++) {
				Symtom oneSymptom = listOfSymptoms.get(i);

				// Create individual
				OWLClass medIndividual = factory.getOWLClass(IRI.create(iri + "#Symptom"));
				OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(iri + "#" + oneSymptom.getSymId()));
				OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(medIndividual, ind);
				man.addAxiom(ontology, classAssertion);

				// Create data property assertions
				OWLDataProperty symptomIRI = factory.getOWLDataProperty(IRI.create(iri + "#hasSymptomIRI"));
				OWLLiteral literalSymIRI = factory.getOWLTypedLiteral(oneSymptom.getSymIRI());
				OWLDataPropertyAssertionAxiom propSymIRI = factory.getOWLDataPropertyAssertionAxiom(symptomIRI, ind,
						literalSymIRI);
				man.addAxiom(ontology, propSymIRI);

				OWLDataProperty symptomName = factory.getOWLDataProperty(IRI.create(iri + "#hasSymptomName"));
				OWLLiteral literalSymName = factory.getOWLTypedLiteral(oneSymptom.getSymName());
				OWLDataPropertyAssertionAxiom propSymName = factory.getOWLDataPropertyAssertionAxiom(symptomName, ind,
						literalSymName);
				man.addAxiom(ontology, propSymName);

			}

			String fileOnt = new File(ontoFile).toURI().toString();

			man.saveOntology(ontology, new RDFXMLOntologyFormat(), IRI.create(fileOnt));

		} catch (OWLOntologyCreationException e) {
			System.out.println("Ontology could not be loaded: " + e.getMessage());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	public String covertStringToCammel(String cad) 
	{
		String res = cad.replaceAll("\"", "");
		res = res.replaceAll("@en", "");
		res = res.replaceAll("- ", "");
		final char[] delimiters = { ' ', '_' };
		res = WordUtils.capitalizeFully(res, delimiters);
		res = res.replaceAll(" ", "_");
		return res;
	}
	
	public String eliminateChars(String cad) 
	{
		String res = cad.replaceAll("\"", "");
		res = res.replaceAll("@en", "");
		//res = res.replaceAll("- ", "");
		//res = res.replaceAll(" ", "_");
		return res;
	}
}
