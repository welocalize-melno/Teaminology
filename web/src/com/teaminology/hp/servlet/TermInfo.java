package com.teaminology.hp.servlet;

public class TermInfo {

	private String source;
	private String pos;
	private String domain;
	private String category;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null){
			return false;
		}

		if (getClass() != o.getClass()) {
			return false;
		}

		final TermInfo that = (TermInfo)o;
		if(this == o){
			return true;
		}
		
		if(this.getSource() == null || that.getSource()==null){
			return false;
		}
		if(!this.getSource().equalsIgnoreCase(that.getSource())){
			return false;
		}
		
		if(that.getPos()==null && this.getPos()==null){
			return true;
		}
		if(this.getPos().equalsIgnoreCase(that.getPos())){
			return true;
		}

		
		if(this.getCategory()==null || that.getCategory()==null){
			return false;
		}
		if(!this.getCategory().equalsIgnoreCase(that.getCategory())){
			return false;
		}
		
		if(that.getDomain()==null && this.getDomain()==null){
			return true;
		}
		if(this.getDomain().equalsIgnoreCase(that.getDomain())){
			return true;
		}
		
		return false;
	}

	@Override
	public int hashCode(){
		int sourceCode = this.getSource() != null ? this.getSource().hashCode() : System.identityHashCode(this.source);
		int posCode = this.getPos() != null ? this.getPos().hashCode() : System.identityHashCode(this.pos);
		int categoryCode = this.getCategory() != null ? this.getCategory().hashCode() : System.identityHashCode(this.category);
		int domainCode = this.getDomain() != null ? this.getDomain().hashCode() : System.identityHashCode(this.domain);
		 
		 return posCode+sourceCode+categoryCode+domainCode;
	}
}
