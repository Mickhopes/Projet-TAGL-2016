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
		
		int old = s.nombreObjetListe("test");
		
		s.ajoutListeObject("test", "coucou", false);
		s.ajoutListeObject("test", "hello", true);
		s.ajoutListeObject("test", "je suis premier", false);
		
		assertEquals(1, s.nombreObjet());
		assertEquals(old+3, s.nombreObjetListe("test"));
		
		old = s.nombreObjet();
		
		s.ajoutListeObject("des dates", new Date(), false);
		s.ajouterObjet("intrus", "Je suis un intrus !");
		
		assertEquals(old+2, s.nombreObjet());
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
		
		assertNull(s.recupererListeObject("test", 15, 17));
	}
	
	@Test
	public void supprimerListeTest() {
		Stockage s = new Stockage();
		
		int old = s.nombreObjetListe("test");
		s.ajoutListeObject("test", "coucou", false);
		s.ajoutListeObject("test", "hello", true);
		s.ajoutListeObject("test", "je suis premier", false);
		
		assertEquals(old+3, s.nombreObjetListe("test"));
		
		s.supprimerListeObject("test", true);
		s.supprimerListeObject("test", false);
		
		assertEquals(old+1, s.nombreObjetListe("test"));
		
		s.supprimerObjet("test");
		
		assertNull(s.supprimerListeObject("test", true));
		assertEquals(0, s.nombreObjet());
	}
	
	@Test
	public void recupererObjetEnListeTest() {
		Stockage s = new Stockage();
		
		s.ajouterObjet("test", "ceci est un test");
		
		assertNull(s.recupererListeObject("test", 0, -1));
		assertEquals(-1, s.ajoutListeObject("test", "coucou", true));
		assertEquals(-2, s.nombreObjetListe("test"));
	}
	
	@Test
	public void cacheLRUTest() {
		Stockage s = new Stockage(4);
		
		s.ajouterObjet("toujours un test", "Il faut bien tout tester !");
		s.ajouterObjet("encore...", "et oui !");
		s.ajouterObjet("date_inscription", new Date());
		s.ajouterObjet("ami", "flash");
		
		assertEquals(4, s.nombreObjet());
		
		s.ajouterObjet("ennemi", "Zoom");
		
		assertEquals(4, s.nombreObjet());
		
		assertNotNull(s.recupererObjet("toujours un test"));
	}
	
	@Test
	public void tailleLimiteListeTest() {
		Stockage s = new Stockage(Stockage.TAILLE_LIMITE, 3);
		
		s.ajoutListeObject("test", "Adrien", true);
		s.ajoutListeObject("test", "Adrienne", true);
		s.ajoutListeObject("test", "Gérard", false);
		
		assertEquals(-2, s.ajoutListeObject("test", "en trop", false));
	}
	
	@Test
	public void cacheLRUListeTest() {
		Stockage s = new Stockage(4);
		
		s.ajoutListeObject("test", new Date(), true);
		s.ajoutListeObject("test", "aujourd'hui", true);
		s.ajouterObjet("identifiant", "darkkikoolol42");
		s.ajouterObjet("mdp", "ctro1mdpfeaur");
		
		assertEquals(3, s.nombreObjet());
		
		s.ajouterObjet("video", "youtube");
		
		assertEquals(3, s.nombreObjet());
		
		assertEquals(s.recupererListeObject("test", 1, 1).get(0), "aujourd'hui");
	}
}