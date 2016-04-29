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
	
	@Test
	public void supprimerTest() {
		Stockage s = new Stockage();
		s.ajouterObjet("test", new Date());
		
		assertEquals(1, s.nombreObjet());
		
		s.supprimerObjet("Super");
		
		assertEquals(1, s.nombreObjet());
		
		s.supprimerObjet("test");
		
		assertEquals(0, s.nombreObjet());
	}
	
	@Test
	public void ajoutListeTest() {
		Stockage s = new Stockage();
		
		s.ajoutListeObject("test", "coucou", false);
		s.ajoutListeObject("test", "hello", true);
		s.ajoutListeObject("test", "je suis premier", false);
		
		assertEquals(1, s.nombreObjet());
		assertEquals(3, s.nombreObjetListe("test"));
		
		s.ajoutListeObject("des dates", new Date(), false);
		s.ajouterObjet("intru", "Je suis un intru !");
		
		assertEquals(3, s.nombreObjet());
	}
	
	@Test
	public void recupererListeTest() {
		Stockage s = new Stockage();
		
		s.ajoutListeObject("test", "coucou", false);
		s.ajoutListeObject("test", "hello", true);
		s.ajoutListeObject("test", "je suis premier", false);
		s.ajoutListeObject("test", "je suis le dernier", true);
		
		assertEquals(s.recupererListeObject("test", 0, -1).size(), s.nombreObjetListe("test"));
		
		assertEquals(s.recupererListeObject("test", 1, 1).get(0), "coucou");
		
		assertEquals(s.recupererListeObject("test", 2, 3).size(), 2);
		
		assertNull(s.recupererListeObject("test", 5, 7));
	}
	
	@Test
	public void supprimerListeTest() {
		Stockage s = new Stockage();
		
		s.ajoutListeObject("test", "coucou", false);
		s.ajoutListeObject("test", "hello", true);
		s.ajoutListeObject("test", "je suis premier", false);
		
		assertEquals(3, s.nombreObjetListe("test"));
		
		s.supprimerListeObject("test", true);
		
		assertEquals(2, s.nombreObjetListe("test"));
		
		s.supprimerObjet("test");
		
		assertNull(s.supprimerListeObject("test", true));
		assertEquals(0, s.nombreObjet());
	}
}