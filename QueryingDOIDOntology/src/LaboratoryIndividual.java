
public class LaboratoryIndividual 
{
	private String id;
	private String iri;
	private String desc;
	
	public LaboratoryIndividual(String id, String iri, String desc) {
		this.id = id;
		this.iri = iri;
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}
	
	
}
