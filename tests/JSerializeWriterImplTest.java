package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import exesoft.*;

public class JSerializeWriterImplTest {

	JSerializeWriter writer;
	ObjectWithPrimitives os;
	ObjectWithTreeMap os5;
	ObjectWithTransientField os6;
	ObjectWithHashSet os7;
	ObjectWithTreeSet os8;
	ObjectWithLinkedList os9;

	boolean equalMaps(Map<String, Object> m1, Map<String, Object> m2) {

		if (m1.size() != m2.size()) {
			return false;
		}
		if (!m1.keySet().equals(m2.keySet())) {
			return false;
		}
		return true;
	}

	@Before
	public void setUp() throws Exception {
		writer = new JSerializeWriterImpl();
		os = new ObjectWithPrimitives();
		os5 = new ObjectWithTreeMap();
		os6 = new ObjectWithTransientField();
		os7 = new ObjectWithHashSet();
		os8 = new ObjectWithTreeSet();
		os9 = new ObjectWithLinkedList();
	}

	@Test
	public void testObjectWithPrimitives() {

		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("a#int", 10);
		expected.put("b#int", 10);
		expected.put("#JSerializeMetaData#RootClassName", os.getClass()
				.getName());

		Map<String, Object> generated = writer.prepareMap(os);

		assertTrue(equalMaps(expected, generated));
	}

	@Test
	public void testObjectWithTransientFields() {
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("a#int", 10);
		expected.put("b#int", 10);
		expected.put("#JSerializeMetaData#RootClassName", os6.getClass()
				.getName());

		Map<String, Object> generated = writer.prepareMap(os6);

		assertTrue(equalMaps(expected, generated));
	}

	@Test
	public void testObjectWithHashSet() {
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("set#java.util.Set", new HashMap<String, Object>());
		expected.put("#JSerializeMetaData#RootClassName", os7.getClass()
				.getName());

		Map<String, Object> generated = writer.prepareMap(os7);

		assertTrue(equalMaps(expected, generated));
	}

	@Test
	public void testObjectWithTreeSet() {
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("ts#java.util.TreeSet", new HashMap<String, Object>());
		expected.put("#JSerializeMetaData#RootClassName", os8.getClass()
				.getName());

		Map<String, Object> generated = writer.prepareMap(os8);

		assertTrue(equalMaps(expected, generated));
	}

	@Test
	public void testObjectWithLinkedList() {
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("link#java.util.LinkedList", new HashMap<String, Object>());
		expected.put("#JSerializeMetaData#RootClassName", os9.getClass()
				.getName());

		Map<String, Object> generated = writer.prepareMap(os9);

		assertTrue(equalMaps(expected, generated));
	}

	@Test
	public void testObjectWithTreeMap() {
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("map#java.util.TreeMap", new Object());
		expected.put("#JSerializeMetaData#RootClassName", os5.getClass()
				.getName());

		Map<String, Object> generated = writer.prepareMap(os5);
		writer.printMap(expected);
		writer.printMap(generated);
		assertTrue(equalMaps(expected, generated));
	}

	@Test
	public void isRootNamePresent() {
		Map<String, Object> generated = writer.prepareMap(os);
		assertTrue(generated.containsKey("#JSerializeMetaData#RootClassName"));
	}
}
