package tests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;

import org.junit.Before;
import org.junit.Test;

import exesoft.JModel;

public class JModelImplTestE extends TestCase {


	/*
	private IMocksControl jmodelMockControl, encodeMControl;
	private JModel jmodelMock;
*/
	public String encodeM;

	@Before
	public void setUp() throws Exception {

	/*	// tworzenie mocka
		jmodelMockControl = EasyMock.createStrictControl();
		jmodelMock = jmodelMockControl.createMock(JModel.class); // przyjmuje
																	// interfejs
																	// nie klasê
																	// !
*/
		
		encodeM = "{<Krzysztof : Kamikaze>}";

	}

	@Test
	// nawias otwarcia
	public void testencodeOpenBracket() throws Exception {
		assertEquals('{', encodeM.charAt(0));
	}

	@Test
	// nawias zamkniecia
	public void testencodeCloseBracket() throws Exception {
		assertEquals('}', encodeM.charAt(encodeM.length() - 1));
	}

	@Test
	// parzystosc nawiasow/ klamr i nawiasów
	public void testencodePairOfBracket() throws Exception {

		int a = 0, b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0;

		for (int i = 0; i < encodeM.length(); i++) {

			if (encodeM.charAt(i) == '{')
				a++;
			if (encodeM.charAt(i) == '}')
				b++;

			if (encodeM.charAt(i) == '[')
				c++;
			if (encodeM.charAt(i) == ']')
				d++;

			if (encodeM.charAt(i) == '(')
				e++;
			if (encodeM.charAt(i) == ')')
				f++;

			if (encodeM.charAt(i) == '<')
				g++;
			if (encodeM.charAt(i) == '>')
				h++;
		}

		assertEquals(a, b);
		assertEquals(c, d);
		assertEquals(e, f);
		assertEquals(g, h);

	}

}
