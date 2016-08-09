package testcases;

import main.Main;
import org.junit.Before;
import org.junit.Test;

import java.io.File;


public class MainTest {

    String dir;

    @Before
    public void setUp() {
        String workingDir = System.getProperty("user.dir");
        dir = ".";
        if (workingDir.length() > 0 && workingDir.contains("/src")) {
            dir = workingDir.substring(0, workingDir.indexOf("/src"));
        }
    }

    @Test
    public void oneProcSimpleTest() {

        // Expected output file
        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/1_processor_simple-output.dot");

        // TODO
        /* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput(dir + "/src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }

    @Test
    public void twoProcSimpleTest() {

        // Expected output file
        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/2_processor_simple-output.dot");

        // TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput(dir + "/src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcOneSrcOneDestTest() {

        // Expected output file
        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/4_processor_1_src_1_dest-output.dot");

        // TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput(dir + "/src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcOneSrcThreeDestTest() {

        // Expected output file
        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/4_processor_1_src_3_dest-output.dot");

        String[] args = {dir + "/src/main/resources/dotfiles/inputfiles/4_processor_1_src_3_dest.dot", "4", "-o", "4_processor_1_src_3_dest-output"};
        Main.main(args);

		File output = new File(dir + "/src/main/resources/4_processor_1_src_3_dest-output.dot");
        //FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcThreeSrcOneDestTest() {

        // Expected output file
        File expected = new File("/src/main/resources/dotfiles/outputfiles/4_processor_3_src_1_dest-output.dot");
        String[] args = {dir + "/src/main/resources/dotfiles/inputfiles/4_processor_3_src_1_dest.dot", "4", "-o", "4_processor_1_src_1_dest-output"};
        Main.main(args);

        File output = new File(dir + "/src/main/resources/4_processor_1_src_1_dest-output.dot");
        //FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcThreeSrcTwoDestTest() {

        // Expected output file

        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/4_processor_3_src_2_dest-output.dot");

        // TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput(dir + "/src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }
}
