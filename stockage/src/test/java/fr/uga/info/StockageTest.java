package fr.uga.info;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StockageTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ajoutTest() {
		Stockage s = new Stockage();
		s.ajouterObjet("test", "une chaine de caractère quelconque");
		
		assertEquals(1, s.nombreObjet());
		
		s.ajouterObjet("test2", new Date());
		
		assertEquals(2, s.nombreObjet());
	}
	
	@Test
	public void recupererTest() {
		Stockage s = new Stockage();
		String save = "une chaine de caractère quelconque";
		s.ajouterObjet("test", save);
		
		assertEquals(save, (String)s.recupererObjet("test"));
	}
}