package exesoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import sun.font.CreatedFontTracker;

/**
 * 
 * JSerializeReader - Klasa odpowiedzialna za odwzorowanie obiektu na podstawie
 * informacji uzyskanych z obiektu klasy JModel, ktra w procesie dekodowania
 * (metoda decode) wytworzy obiekt klasy String o strukturze jak w przyk³adzie.
 * 
 * String do przetworzenia w JModel :
 * java.lang.Osoba{imie(java.lang.String):x;nazwisko(java.lang.String):y;}
 * 
 * @author Micha³ Krakiewicz
 * 
 */
public class JSerializeReaderImpl implements JSerializeReader {

	/**
	 * Helper class for holding fields data
	 * 
	 * @author Micha³
	 * 
	 */
	static class JSONElement {

		private String name;
		private String type;

		private Object innerTypes;
		private Object innerObject;

		private boolean hasObject = false;

		public JSONElement(String name, String type, Object innerTypes) {

			this.name = name;
			this.type = type;
			this.innerTypes = innerTypes;
		}

		public void setObject(Object o) {
			innerObject = o;
			hasObject = true;
		}

		public boolean hasObject() {
			return hasObject;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public void setType(String newtype) {
			type = newtype;
		}

		public Map<String, Object> getAsMap() {
			return (Map<String, Object>) innerTypes;
		}

		public List getAsList() {
			return (List) innerTypes;
		}

		public String getStringWithHash() {
			return new String(name + '#' + type);
		}

		public Object getInner() {
			return innerTypes;
		}

	}

	/**
	 * class member holding the deserialized object
	 */
	private Object deserializedObject;

	/**
	 * object hash map
	 * 
	 */
	private Map<String, Object> objectHashMap;

	/**
	 * Creates the object from hashmap using java reflection
	 * 
	 * (non-Javadoc)
	 * 
	 * @see exesoft.JSerializeReader#fromMap(java.util.Map)
	 */

	private static boolean debug = true;

	private static void dbg(String msg) {

		if (debug) {
			System.err.println(msg);
		}

	}

	private static final String rootClassKey = "#JSerializeMetaData#RootClassName";

	@Override
	public Object fromMap(Map<String, Object> map) {

		dbg("Started creating object");

		Class deserialized = null;

		JSONElement rootClass = processClassRoot(map);

		dbg("Class name: " + rootClass.getType());

		deserializedObject = null;

		try {
			deserializedObject = createObject(rootClass);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deserializedObject;

	}

	public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
		try {
			return clazz.cast(o);
		} catch (ClassCastException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected List decodeList(JSONElement encoded, Field field) {

		ArrayList<JSONElement> elementDataArr = decodeHashMapKeys((Map<String, Object>) encoded
				.getAsMap());

		JSONElement elementData = elementDataArr.get(0);

		Type type = field.getGenericType();

		ParameterizedType pt = (ParameterizedType) type;

		Type t = pt.getActualTypeArguments()[0];
		String tmp = t.toString();

		int space = tmp.indexOf(' ');

		String className = tmp.substring(space + 1, tmp.length());

		if (debug) {
			dbg("Name: " + className);
			System.out.println("type: " + type);
			System.out.println("raw type: " + pt.getRawType());

		}

		ArrayList<JSONElement> elems = new ArrayList<>();

		List<HashMap<String, Object>> hashmaps = elementData.getAsList();
		List returnList = new ArrayList<>();
		try {
			for (HashMap map : hashmaps) {

				if (map.get("0") == null) {
					JSONElement object = new JSONElement("elementData",
							className, decodeHashMapKeys(map));

					if (object != null)
						returnList.add(createObject(object));
					// dbg("Decode list item: " + elem.getName());
				}

			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// for (JSONElement elem : elems) {
		// try {
		// Object obj = createObject(elem);
		// returnList.add(obj);
		// } catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// dbg("Decode list item: " + elem.getName());
		// }

		return returnList;

	}

	private int[] toIntArray(List<Integer> list) {
		int[] ret = new int[list.size()];
		int i = 0;
		for (Integer e : list)
			ret[i++] = e.intValue();
		return ret;
	}

	protected Object createObject(JSONElement elem)
			throws ClassNotFoundException {

		try {

			switch (elem.getType()) {
			case "intArray":
				// return elem.getInner();
				int[] tmp = toIntArray((List<Integer>) elem.getInner());
				return Arrays.copyOf(tmp, tmp.length);
			case "charArray":
				return elem.getInner();
			case "0":
				return "0";
				// // case "java.lang.String":
				// // return
				//

			}

			Class deserialized = Class.forName(elem.getType());

			Object deserializedInstance = deserialized.newInstance();

			Object innerTypes = elem.getInner();

			if (innerTypes != null) {

				ArrayList<JSONElement> members = null;

				if (innerTypes instanceof Map<?, ?>)
					members = decodeHashMapKeys((Map<String, Object>) innerTypes);
				else if (innerTypes instanceof List) {
					members = (ArrayList<JSONElement>) innerTypes;
				}

				for (JSONElement jsonElement : members) {
					dbg("Deserializing member: " + jsonElement.getType() + " "
							+ jsonElement.getName());
					// dbg("Member contents: " )
					Field field = deserialized.getDeclaredField(jsonElement
							.getName());
					field.setAccessible(true);

					Class<?> c = field.getType();

					// Object deser = createObject(jsonElement);

					if (jsonElement.getType().equals(List.class.getName())) {

						List l = decodeList(jsonElement, field);
						field.set(deserializedInstance, l);
					} else if (jsonElement.getType().equals(
							String.class.getName())) {

						ArrayList<JSONElement> tmp = decodeHashMapKeys(jsonElement
								.getAsMap());
						List inner = (List) createObject(tmp.get(0));

						StringBuilder sb = new StringBuilder(inner.size());
						for (Object c_ : inner)
							sb.append(c_);
						String result = sb.toString();

						String value = result;

						field.set(deserializedInstance, value);
					} else {

						Object tmp = createObject(jsonElement);

						field.set(deserializedInstance, tmp);
						dbg("else");
						// ArrayList<JSONElement> inner =
						// decodeHashMapKeys((Map<String, Object>) jsonElement
						// .getInner());
					}
					// tmp.set(deserializedObject, );

					dbg(" ");

				}

			} else {
				throw new InvalidParameterException(elem.toString()
						+ " has no innerTypes!\n");
			}

			return deserializedInstance;

		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException("Class " + elem.getType()
					+ "not found!\n");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Reads the object from input stream.
	 * 
	 * @see exesoft.JSerializeReader#readObject(java.io.InputStream)
	 */
	@Override
	public Boolean readObject(InputStream input) {
		JModel parser = new JModelImpl();

		/**
		 * Convert InputStream to string:
		 */

		// Create BufferedReader object
		BufferedReader bReader = new BufferedReader(
				new InputStreamReader(input));

		StringBuffer sbf = new StringBuffer();
		String line = null;

		// read line by line
		try {
			while ((line = bReader.readLine()) != null) {
				sbf.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		objectHashMap = parser.decode(sbf.toString());

		if (objectHashMap == null)
			return false;

		return true;
	}

	protected static ArrayList<JSONElement> decodeHashMapKeys(
			Map<String, Object> map) {

		ArrayList<JSONElement> tmp = new ArrayList<>();

		Set keys = map.keySet();

		if (keys.size() == 0) {
			// empty hashmap?
			return null;
		}

		Iterator<String> i = keys.iterator();

		while (i.hasNext()) {

			String full_string = i.next();

			int hash_index = full_string.indexOf('#');

			if (hash_index == -1) {

				if (full_string.equals("0")) {
					Object inner = map.get(full_string);
					if (((String) inner).equals("0")) {
						tmp.add(new JSONElement("0", "0", null));
						continue;
					}
				}

				throw new java.security.InvalidParameterException('"'
						+ full_string + '"'
						+ " doesn't contain a hash (#) in hashmap name: \n"
						+ map.toString());
			}
			String name = full_string.substring(0, hash_index);
			String type = full_string.substring(hash_index + 1,
					full_string.length());

			Object inner = map.get(full_string);

			// @SuppressWarnings("unchecked")
			// Map<String, Object> innerTypes = (Map<String, Object>) inner;

			tmp.add(new JSONElement(name, type, inner));

		}

		return tmp;

	}

	/**
	 * @param map
	 * @return JSONElement with name of the class, and innerTypes contatinng the
	 *         map withou class name
	 */
	protected static JSONElement processClassRoot(Map<String, Object> map) {
		Object tmp = null;
		tmp = map.get(rootClassKey);

		String type = (String) tmp;

		if ((type == null) || type.isEmpty()) {
			throw new InvalidParameterException("Hashmap: \n" + map.toString()
					+ "\ndoesn't contain class name (" + rootClassKey + ") !");
		}

		map.remove(rootClassKey);

		return new JSONElement(rootClassKey, type, map);
	}

}
