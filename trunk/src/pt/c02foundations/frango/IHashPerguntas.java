package pt.c02foundations.frango;

import java.util.List;

import anima.annotation.ComponentInterface;
import anima.component.ISupports;

/**
 * 
 * 
 * @author mbertanha
 *
 */
@ComponentInterface("<http://purl.org/dcc/[package].IHashPerguntas>")
public interface IHashPerguntas extends ISupports{

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public List<String> getAnimals(String question);
	
	public void putAnimal(String question, String animal);

	public boolean removeAnimals(String question, String animal);
	
	public List<String> getQuestions();
	
	public int getNumberOfQuestions();
}
