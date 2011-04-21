package pt.c02foundations.frango.appTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.c01interfaces.s01chaveid.s01base.impl.BaseConhecimento;
import pt.c01interfaces.s01chaveid.s01base.inter.IBaseConhecimento;
import pt.c01interfaces.s01chaveid.s01base.inter.IDeclaracao;
import pt.c01interfaces.s01chaveid.s01base.inter.IEnquirer;
import pt.c01interfaces.s01chaveid.s01base.inter.IObjetoConhecimento;
import pt.c01interfaces.s01chaveid.s01base.inter.IResponder;
import pt.c02foundations.frango.components.IQuestionsHash;
import pt.c02foundations.frango.components.QuestionsHash;

public class EnquirerAdvanced implements IEnquirer {

    private IBaseConhecimento baseConhecimento;
    private static String[] namesList;
    private static IQuestionsHash negativeAnswers;
    private static IQuestionsHash positiveAnswers;
    private static IQuestionsHash dontKnowAnswers;
    private static List<String> questions = new ArrayList<String>();
    private static HashMap<String, IResponder> responders = new HashMap<String, IResponder>();
    private static HashMap<String, IObjetoConhecimento> objs = new HashMap<String, IObjetoConhecimento>();
    private static Boolean alreadyCached = false;

    public EnquirerAdvanced() {
        baseConhecimento = new BaseConhecimento();
        namesList = baseConhecimento.listaNomes();

        if (alreadyCached == false) {
            negativeAnswers = new QuestionsHash();
            positiveAnswers = new QuestionsHash();
            dontKnowAnswers = new QuestionsHash();
            questions = new ArrayList<String>();

            for (int i = 0; (i < namesList.length); i++) {
                IObjetoConhecimento obj;
                obj = baseConhecimento.recuperaObjeto(namesList[i]);
                objs.put(namesList[i], obj);

                IDeclaracao decl = obj.primeira();

                while (decl != null) {
                    if (!isQuestion(decl.getPropriedade())) {
                        insertIntoHash(decl.getPropriedade());
                        addQuestion(decl.getPropriedade());
                    }
                    decl = obj.proxima();
                }
            }
        }

        alreadyCached = true;
    }

    public void connect(IResponder responder) {
        boolean acertei, found = false;
        String animalName = null;
        List<String> intersectedAnswers = new ArrayList<String>();

        for (int i = 0; ((i < questions.size()) && (!found)); i++) {
            String resposta = responder.ask((String) questions.get(i));
            if (resposta.equalsIgnoreCase("nao")) {
                intersectedAnswers = intersectList((List<String>) negativeAnswers.getAnimals((String) questions.get(i)), intersectedAnswers);
            } else if (resposta.equalsIgnoreCase("sim")) {
                intersectedAnswers = intersectList((List<String>) positiveAnswers.getAnimals((String) questions.get(i)), intersectedAnswers);
            } else if (resposta.equalsIgnoreCase("nao sei")) {
                intersectedAnswers = intersectList((List<String>) dontKnowAnswers.getAnimals((String) questions.get(i)), intersectedAnswers);
            }

            if (intersectedAnswers.size() == 1) {
                animalName = intersectedAnswers.get(0);
                found = true;
            }
        }

        if (animalName != null) {
            acertei = responder.finalAnswer(animalName);
        } else {
            acertei = responder.finalAnswer("nao sei");
        }

        if (acertei) {
            System.out.println("Oba! Acertei! - " + animalName);
        } else {
            System.out.println("fuem! fuem! fuem!");
        }
    }

    private List<String> intersectList(List<String> l1, List<String> l2) {
        if (l1.isEmpty()) {
            return l2;
        } else if (l2.isEmpty()) {
            return l1;
        }

        int size = (l1.size() > l2.size()) ? l1.size() : l2.size();
        List<String> listIntersected = new ArrayList<String>();

        for (int i = 0; i < size; i++) {
            if (l1.size() > l2.size()) {
                if (l2.contains(l1.get(i))) {
                    listIntersected.add(l1.get(i));
                }
            } else {
                if (l1.contains(l2.get(i))) {
                    listIntersected.add(l2.get(i));
                }
            }

        }

        return listIntersected;
    }

    private void insertIntoHash(String question) {
        for (int i = 0; i < namesList.length; i++) {
            IResponder responder = responders.get(namesList[i]);
            if (responder == null) {
                responder = new Responder(namesList[i]);
                responders.put(namesList[i], responder);
            }

            String resposta = responder.ask(question);

            if (resposta.equalsIgnoreCase("nao")) {
                negativeAnswers.putAnimal(question, namesList[i]);
            } else if (resposta.equalsIgnoreCase("sim")) {
                positiveAnswers.putAnimal(question, namesList[i]);
            } else if (resposta.equalsIgnoreCase("nao sei")) {
                dontKnowAnswers.putAnimal(question, namesList[i]);
            }
        }
    }

    private void addQuestion(String question) {
        questions.add(question);
    }

    private boolean isQuestion(String question) {
        return (questions.contains(question));
    }
}
