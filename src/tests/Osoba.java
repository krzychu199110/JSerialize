package tests;

public class Osoba {
	public Osoba(String name, int age, int id) {
		this.idOsoby = id;
		this.name = name;
		this.isDorosly = true;
		this.age[0] = age;
	}

	int idOsoby;
	boolean isDorosly;
	String name;
	int[] age = { 1, 2 };

}
