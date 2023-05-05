import edu.buffalo.sneps.JavaSnepsAPI;
import edu.buffalo.sneps.Substitution;

import java.util.HashSet;


// Small example on how to use the SNePSAPI

// Copy to your own directory if you want to recompile the code.
// Replace "/projects/snwiz/Install/Sneps-2.7.0" with the location of the
// SNePS directory


// Compile: javac -classpath ".:/projects/snwiz/Install/Sneps-2.7.0/Jlinker/jlinker.jar:/projects/snwiz/Install/Sneps-2.7.0/JavaSnepsAPI/JavaSnepsAPI.jar" TestAPI.java

// My version compiled under Java 1.5
// Run: java -classpath ".:/projects/snwiz/Install/Sneps-2.7.0/Jlinker/jlinker.jar:/projects/snwiz/Install/Sneps-2.7.0/JavaSnepsAPI/JavaSnepsAPI.jar" TestAPI

public class TestAPI {

    public static String[] commands
            = {"set-mode-1",
            "man(socrates).",
            "all(x)(man(x) => mortal(x)).",
            "a(r)."};

//    public static String [] commands
//	= {"set-mode-3",
//	   "define-frame Isa (nil member class)",
//	   "define-frame Ako (nil class1 class2)",
//	   "define-frame Believes (nil agent prop)",
//	   "Isa(Clyde, Elephant).",
//	   "Isa(Dumbo, Elephant).",
//	   "Isa(Tweety, Canary).",
//	   "Ako(Elephant, Mammal).",
//	   "Ako(Mammal, Animal).",
//	   "Ako(Bird, Animal)."};


    public static void main(String[] args) {

        // Create an instance of the API
        JavaSnepsAPI api =
                new JavaSnepsAPI("C:\\Users\\PotatoHD\\Documents\\GitHub\\sneps-test\\api\\java_sneps_config.config",
                        5668);

        // Testing the tell method:
//        System.out.println("Telling: \"sneps:demo\" 2");
//        api.myTell("sneps:demo", "2");

//        api.tell("sneps:show");
        for (String command : commands) {
            System.out.println("Telling: " + command);
            api.tell(command);
        }


//	// Testing the ask method:
//	System.out.println("Asking Isa(?x, Elephant)...");
        HashSet<String> answers = api.ask("mortal(socrates)?");
//
        for (String answer : answers) {
            System.out.println("Received:" + answer);
        }
        api.show();

//        answers = api.ask("(?x)");

//        for (String answer : answers) {
//            System.out.println("Received:" + answer);
//        }
//
//
//	// Testing the askwh method:
//	System.out.println("Asking Ako(?x,?y)...");
//	HashSet<Substitution> substset = api.askwh("Ako(?x,?y)");
//	for(Substitution subst: substset) {
//	    System.out.println("Received: " + subst.getValFromVar("x")
//			       + "s are " + subst.getValFromVar("y") + "s.");
//	}


        api.endLispConnection();

    }
}
