package pt.c03ensaios.frango.appTest;

import pt.c01interfaces.s01chaveid.s01base.impl.*;
import pt.c01interfaces.s01chaveid.s01base.inter.IEnquirer;
import pt.c01interfaces.s01chaveid.s01base.inter.IResponder;

public class AppBase02 {

	public static void main(String[] args) {
		String animalName;

		BaseConhecimento bc = new BaseConhecimento();
		String namesList[] = bc.listaNomes();

		// runs all the animals for the enquirer basic and advanced
		for (int i = 0; i < namesList.length; i++) {
			animalName = namesList[i];
			System.out.print("Basic:    ");
			enquirerBasicTest(animalName);
			System.out.print("Advanced: ");
			enquirerAdvancedTest(animalName);
		}
	}

	public static void enquirerBasicTest(String animalName) {
		IEnquirer eb = new EnquirerBasic();
		IResponder rb = new Responder(animalName);

		eb.connect(rb);
	}

	public static void enquirerAdvancedTest(String animalName) {
		IEnquirer ea = new EnquirerAdvanced();
		IResponder ra = new Responder(animalName);

		ea.connect(ra);
	}
}
