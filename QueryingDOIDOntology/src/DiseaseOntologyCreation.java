import java.util.List;

public class DiseaseOntologyCreation
{
	//Generate ontology Disease module from DOID ontology
	public static void main(String[] args)
	 {
		//First extract the list of diseases from DOID ontology
		OntologyUtils util = new OntologyUtils();
		List <Disease> listDis = util.getListOfDiseasesFromDOID2();
		//Get the synonyms of each disease and update the list 
		util.getSynonymsOfDisease(listDis);
		//Get the parent classes IRIs of each disease and update the list 
		util.getParentClasses(listDis);
		//Get the data base cross references of each disease and update the list 
		util.getDbXRefs(listDis);
		//Write the disease individuals into the ontology
		util.addDiseaseListToOntology("Ontologies/Disease.owl", listDis);
	 }
}
