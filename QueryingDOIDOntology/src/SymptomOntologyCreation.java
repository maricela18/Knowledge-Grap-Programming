import java.util.List;

public class SymptomOntologyCreation {

	public static void main(String[] args) 
	{
		OntologyUtils util = new OntologyUtils();
		List<Symtom> symptomList = util.getSymtomsList();
		
		for(int i = 0; i < symptomList.size(); i++)
		{
			System.out.println("==================================================================");
			System.out.println("Symptom id " + symptomList.get(i).toString());
		}
		
		util.populateSymptomsOntology("Ontologies/Symptom.owl", symptomList);
	}

}
