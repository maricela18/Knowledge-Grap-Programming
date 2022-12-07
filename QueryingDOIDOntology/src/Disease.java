import java.util.List;

public class Disease 
{
	private String id;
	private String iri;
	private String name;
	private String definition;
	private List<String> synonym;
	private List<String> parent;
	private List<String> refs;

	public Disease(String id, String iri, String name, String definition) 
	{
		this.id = id;
		this.iri = iri;
		this.name = name;
		this.definition = definition;
	}

	public void setSynonym(List<String> synonym) {
		this.synonym = synonym;
	}

	public List<String> getSynonym() {
		return synonym;
	}

	public List<String> getRefs() {
		return refs;
	}

	public void setRefs(List<String> refs) {
		this.refs = refs;
	}

	public List<String> getParent() {
		return parent;
	}

	public void setParent(List<String> parent) {
		this.parent = parent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Id " + id + " iri " + iri + " name " + name + " def " + definition;
	}
	
	
	
}
