package fr.uga.info;

import java.util.Date;

import junit.framework.TestCase;

public class StockageTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void ajoutTest() {
		Stockage s = new Stockage();
		s.ajouterObjet("test", "une chaine de caractère quelconque");
		
		assertEquals(1, s.nombreObjet());
		
		s.ajouterObjet("test", new Date());
		
		assertEquals(2, s.nombreObjet());
	}
	
	public void recupererTest() {
		Stockage s = new Stockage();
		String save = "une chaine de caractère quelconque";
		s.ajouterObjet("test", s);
		
		assertEquals(save, s.recupererObjet("test"));
	}
}
