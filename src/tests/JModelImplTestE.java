package tests;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import exesoft.JModelImpl;
import exesoft.JSerializeWriterImpl;

public class JModelImplTestE extends TestCase {

	public String encodeM;

	JModelImpl jModelImpl;

	public String testString;

	@Before
	public void setUp() throws Exception {

		testString = new String();

		Osoba tata = new Osoba("Karol", 30, 1);
		Osoba mama = new Osoba("Jola", 59, 2);

		Rodzina rodzina = new Rodzina();

		rodzina.rodzina.add(tata);
		rodzina.rodzina.add(mama);

		JModelImpl jm = new JModelImpl();
		JSerializeWriterImpl js = new JSerializeWriterImpl();

		testString = jm.encode(js.prepareMap(rodzina));

	}

	@Test
	public void testJModelImplNotNull() throws Exception {

		jModelImpl = new JModelImpl();

		assertNotNull(jModelImpl);

	}

	@Test
	// nawias otwarcia
	public void testencodeOpenBracket() throws Exception {
		int x = 0;
		for (int i = 0; i < testString.length(); i++) {

			if (testString.charAt(i) == (char) 32) {
				x = i;
				break;
			}
		}

		assertEquals('{', testString.charAt(x + 1));

	}

	@Test
	// nawias zamkniecia
	public void testencodeCloseBracket() throws Exception {
		assertEquals('}', testString.charAt(testString.length() - 1));
	}

	@Test
	// parzystosc nawiasow/ klamr i nawiasów
	public void testencodePairOfBracket1() throws Exception {

		int a = 0, b = 0;

		for (int i = 0; i < testString.length(); i++) {

			if (testString.charAt(i) == '{')
				a++;
			if (testString.charAt(i) == '}')
				b++;
		}
		assertEquals(a, b);

	}

	public void testencodePairOfBracket2() throws Exception {
		int c = 0, d = 0;
		for (int i = 0; i < testString.length(); i++) {
			if (testString.charAt(i) == '[')
				c++;
			if (testString.charAt(i) == ']')
				d++;
		}
		assertEquals(c, d);
	}

	public void testencodePairOfBracket3() throws Exception {
		int e = 0, f = 0;
		for (int i = 0; i < testString.length(); i++) {
			if (testString.charAt(i) == '(')
				e++;
			if (testString.charAt(i) == ')')
				f++;
		}
		assertEquals(e, f);
	}

	public void testencodePairOfBracket4() throws Exception {
		int g = 0, h = 0;
		for (int i = 0; i < testString.length(); i++) {
			if (testString.charAt(i) == '<')
				g++;
			if (testString.charAt(i) == '>')
				h++;

		}

		assertEquals(g, h);
	}

	@Test
	// parzystosc cudzyslowów
	public void testencodePairOfQuotationMarks() throws Exception {

		int x = 0, y = 0;

		for (int i = 0; i < testString.length(); i++) {
			if (testString.charAt(i) == (char) 34)
				x++;
		}

		for (int i = 0; i < testString.length(); i++) {
			if (testString.charAt(i) == (char) 39)
				y++;
		}

		assertEquals("Poprawnie utworzone cudzyslowy", 0, x % 2);
		assertEquals("Poprawnie utworzone apostrofy", 0, y % 2);

	}

	@Test
	// nawias otwarcia
	public void testencodeCommaSpace() throws Exception {
		int x = 0;
		for (int i = 0; i < testString.length(); i++) {

			if ((testString.charAt(i) == (char) 44)
					&& (testString.charAt(i + 1) != (char) 32)) {
				x = 1;
				break;
			}
		}
		assertEquals("Poprawna skadnia dot 'przecinek-spacja'", 0, x);
	}

	@Test
	public void testencodeOpenBracketSpace() throws Exception {
		int x = 0;
		for (int i = 0; i < testString.length(); i++) {

			if ((testString.charAt(i) == (char) 91)
					&& (testString.charAt(i + 1) != (char) 32)) {
				x = 1;
				break;
			}
		}
		assertEquals("Poprawna skadnia dot 'nawias kwadratowy-spacja'", 0, x);

	}

	@Test
	public void testencodeQuotationMarksColon() throws Exception {
		int x = 0;

		for (int j = 0; j < testString.length(); j++) {

			if ((testString.charAt(j) == (char) 58)
					&& (testString.charAt(j - 1) != (char) 34)) {

				if (testString.charAt(j - 1) != 'g') {
					x = 1;
					break;
				}
			}
		}

		System.out.println(testString);
		assertEquals("Poprawnosc skadni dot String:value \": ", 0, x);

	}
}
