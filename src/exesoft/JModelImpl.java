package exesoft;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * 
 * @author Karol Buler, Wojciech Mucha
 * 
 */
public class JModelImpl implements JModel {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String encode(final Map<String, Object> toJson)
			throws ClassCastException {

		StringBuffer bufferJson = new StringBuffer();
		Iterator<Entry<String, Object>> it = toJson.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if (pairs.getKey().equals("#JSerializeMetaData#RootClassName")) {
				bufferJson.insert(0, pairs.getValue() + " {");

			} else if (pairs.getValue() instanceof List) {
				bufferJson.append(getName((String) pairs.getKey()));
				bufferJson.append(handleList((List) pairs.getValue()));
				if (it.hasNext()){
					bufferJson.append(", ");
				}

			} else if (pairs.getValue() instanceof Map) {
				bufferJson.append(getName((String) pairs.getKey()));
				bufferJson.append(": {");
				bufferJson.append(handleMap((Map) pairs.getValue()));

			} else {
				if (!it.hasNext()) {
					if (pairs.getKey().toString().contains("value")){
						bufferJson.append(getName((String) pairs.getKey())
								+ ": { " + pairs.getValue() + " ");
					}
					else{
						bufferJson.append(getName((String) pairs.getKey())
								+ ":" + pairs.getValue() + " ");
					}
				} else {
					if (pairs.getKey().toString().contains("value")){
						bufferJson.append(getName((String) pairs.getKey())
								+ ": { " + pairs.getValue() + ", ");
					}
					else{
						bufferJson.append(getName((String) pairs.getKey())
								+ ":" + pairs.getValue() + ", ");
					}
				}
			}
			it.remove();
		}
		bufferJson.append("}");

		return bufferJson.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String handleList(List list) {

		StringBuffer bufferJson = new StringBuffer();
		
		bufferJson.append(" ");
		if (list.get(0) instanceof List) {
			for (int i = 0; i < list.size(); i++) {
				bufferJson.append("{");
				bufferJson.append(handleList((List) list.get(i)));
				bufferJson.append("}");
				if (i < list.size() - 1){
					bufferJson.append(", ");
				}
			}
		} else if (list.get(0) instanceof Map) {
			for (int i = 0; i < list.size(); i++) {
				bufferJson.append("{");
				bufferJson.append(handleMap((Map) list.get(i)));
				if (i < list.size() - 1){
					bufferJson.append(", ");
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				bufferJson.append(list.get(i));
				if (i < list.size() - 1){
					bufferJson.append(", ");
				}
			}
		}
		bufferJson.append(" ]");

		return bufferJson.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String handleMap(Map<String, Object> mapaIn) {

		StringBuffer bufferJson = new StringBuffer();
		Iterator<Entry<String, Object>> it = mapaIn.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if (pairs.getValue() instanceof List) {
				bufferJson.append(getName(pairs.getKey().toString()));
				bufferJson.append(handleList((List) pairs.getValue()));
				if (it.hasNext()){
					bufferJson.append(", ");
				}

			} else if (pairs.getValue() instanceof Map) {
				bufferJson.append(getName(pairs.getKey().toString()));
				bufferJson.append(": {");
				bufferJson.append(handleMap((Map) pairs.getValue()));

			} else {
				if (!it.hasNext()) {
					if (pairs.getKey().toString().contains("value")){
						bufferJson.append(getName(pairs.getKey().toString())
								+ ": { " + pairs.getValue() + " ");
					}
					else{
						bufferJson.append(pairs.getKey() + ":"
								+ pairs.getValue() + " ");
					}
				} else {
					if (pairs.getKey().toString().contains("value")){
						bufferJson.append(getName(pairs.getKey().toString())
								+ ": { " + pairs.getValue() + ", ");
					}
					else{
						bufferJson.append(getName(pairs.getKey().toString())
								+ ":" + pairs.getValue() + ", ");
					}
				}
			}
			it.remove();
		}
		bufferJson.append("}");

		return bufferJson.toString();
	}

	@Override
	public Map<String, Object> decode(String fromJson) {
		Map<String, Object> outJson = new HashMap<String, Object>();

		return outJson;
	}

	private String getName(String str) {
		StringBuffer out = new StringBuffer();

		boolean hash = true;
		int i = 0;
		while (hash) {
			if (str.charAt(i) == '#') {
				hash = false;
			} else{
				out.append(str.charAt(i));
			
			i++; }
		}

		return "\"" + out.toString() + "\"";
	}
}