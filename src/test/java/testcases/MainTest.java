package testcases;

import main.Main;
import org.junit.Test;
import junitx.framework.FileAssert;

import java.io.File;

public class MainTest {

    @Test
    public void oneProcSimpleTest() {

        // Expected output file
        File expected = new File("./src/main/resources/dotfiles/outputfiles/1_processor_simple-output.dot");

        // TODO
        /* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }

    @Test
    public void twoProcSimpleTest() {

        // Expected output file
        File expected = new File("./src/main/resources/dotfiles/outputfiles/2_processor_simple-output.dot");

        // TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcOneSrcOneDestTest() {

        // Expected output file
        File expected = new File("./src/main/resources/dotfiles/outputfiles/4_processor_1_src_1_dest-output.dot");

        // TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcOneSrcThreeDestTest() {
        String workingDir = System.getProperty("user.dir");
        String dir = workingDir.substring(0, workingDir.indexOf("/src"));

        // Expected output file
        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/4_processor_1_src_3_dest-output.dot");

        String[] args = {dir + "/src/main/resources/dotfiles/inputfiles/4_processor_1_src_3_dest.dot", "4", "-o", "4_processor_1_src_3_dest-output"};
        Main.main(args);

		File output = new File(dir + "/src/main/resources/4_processor_1_src_3_dest-output.dot");
        FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcThreeSrcOneDestTest() {
        String workingDir = System.getProperty("user.dir");
        String dir = workingDir.substring(0, workingDir.indexOf("/src"));

        // Expected output file
        File expected = new File("./src/main/resources/dotfiles/outputfiles/4_processor_3_src_1_dest-output.dot");
        String[] args = {dir + "/src/main/resources/dotfiles/inputfiles/4_processor_3_src_1_dest.dot", "4", "-o", "4_processor_1_src_1_dest-output"};
        Main.main(args);

        File output = new File(dir + "/src/main/resources/4_processor_1_src_1_dest-output.dot");
        FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcThreeSrcTwoDestTest() {

        // Expected output file
        Main main = new Main();
        File expected = new File("./src/main/resources/dotfiles/outputfiles/4_processor_3_src_2_dest-output.dot");

        // TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }
}
