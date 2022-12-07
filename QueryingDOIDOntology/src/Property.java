
public class Property 
{
	private String subjectIdentifier;
	private String relationship;
	private String objectIRI;
	private String objectValue;
	
	public Property(String subjectIdentifier, String relationship, String objectIRI, String objectValue) 
	{
		this.subjectIdentifier = subjectIdentifier;
		this.relationship = relationship;
		this.objectIRI = objectIRI;
		this.objectValue = objectValue;
	}

	public String getSubjectIdentifier() {
		return subjectIdentifier;
	}

	public void setSubjectIdentifier(String subjectIdentifier) {
		this.subjectIdentifier = subjectIdentifier;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getObjectIRI() {
		return objectIRI;
	}

	public void setObjectIRI(String objectIRI) {
		this.objectIRI = objectIRI;
	}

	public String getObjectValue() {
		return objectValue;
	}

	public void setObjectValue(String objectValue) {
		this.objectValue = objectValue;
	}

	@Override
	public String toString() 
	{
		return "\n \nSubject " + this.subjectIdentifier + " \n" + 
				"Relation " + this.relationship + " \n" + 
				"Object " + this.objectIRI + " \n" + 
				"Object value " + this.objectValue;
	}
	
	
}
