package com.michaelmeluso.grad_lib;

public class Page {
	private long id;
	private String beginning;
	private String middle;
	private String end;
	private String noun;
	private String verb;

	public Page() {

	}

	public Page(long id, String b, String m, String e, String n, String v) {
		this.id = id;
		this.beginning = b;
		this.middle = m;
		this.end = e;
		this.noun = n;
		this.verb = v;
	}

	public String getNoun() {
		return noun;
	}

	public void setNoun(String noun) {
		this.noun = noun;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public String getBeginning() {
		return beginning;
	}

	public void setBeginning(String beginning) {
		this.beginning = beginning;
	}

	public String getMiddle() {
		return middle;
	}

	public void setMiddle(String middle) {
		this.middle = middle;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
