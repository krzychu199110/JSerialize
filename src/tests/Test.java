package tests;

import java.sql.PreparedStatement;

import exesoft.JModelImpl;
import exesoft.JSerializeWriterImpl;

public class Test {

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String test = new String();
		Osoba tata = new Osoba("Karol", 30, 1);
		Osoba mama = new Osoba("Jola", 30, 2);
		//OsobaWithMap osoba = new OsobaWithMap();

		Rodzina rodzina = new Rodzina();

		rodzina.rodzina.add(tata);
        rodzina.rodzina.add(mama);
        
        JModelImpl jm = new JModelImpl();
        JSerializeWriterImpl js = new JSerializeWriterImpl();
        
        test=jm.encode(js.prepareMap(rodzina));
       // js.printMap(js.prepareMap(osoba));

        System.out.println("Poniżej encode:");
        System.out.println(test);
        

		
	}

}
