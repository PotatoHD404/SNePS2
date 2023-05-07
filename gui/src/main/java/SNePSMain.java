/**
 *
 * @author Michael Kandefer
 */
public class SNePSMain {

    /** Creates a new instance of SNePSMain */
    public SNePSMain() {
    }

    public void testPrint() {
        System.out.println("Invocation succeeded");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        com.franz.jlinker.JavaLinkDist.advertise(42559,-1);
        System.out.println("I'm here!");
        // TODO code application logic here
    }

}
