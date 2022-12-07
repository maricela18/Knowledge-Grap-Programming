import java.util.List;

public class LaboratoryClass 
{
	private String id;
	private String iri;
	private String name;
	private List<LaboratoryIndividual> individuals;
	
	public LaboratoryClass(String id, String iri, String name) {
		this.id = id;
		this.iri = iri;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<LaboratoryIndividual> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(List<LaboratoryIndividual> individuals) {
		this.individuals = individuals;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}
	
	
	
}
