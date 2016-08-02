package testcases;

import java.io.File;
import junitx.framework.FileAssert;
import org.junit.Test;

public class MainTest {
	
	@Test
	public void OneProcSimpleTest() {
		
		// Expected output file
		File expected = new File("./src/main/resources/dotfiles/outputfiles/1_processor_simple-output.dot");
		
		// TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = Main.writeOutput();
		*/

		// FileAssert.assertEquals(expected, output);
	}

	@Test
	public void TwoProcSimpleTest() {
		
		// Expected output file
		File expected = new File("./src/main/resources/dotfiles/outputfiles/2_processor_simple-output.dot");
		
		// TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = Main.writeOutput();
		*/

		// FileAssert.assertEquals(expected, output);
	}
	
	@Test
	public void FourProcOneSrcOneDestTest() {
		
		// Expected output file
		File expected = new File("./src/main/resources/dotfiles/outputfiles/4_processor_1_src_1_dest-output.dot");
		
		// TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = Main.writeOutput();
		*/

		// FileAssert.assertEquals(expected, output);	
	}
	
	@Test
	public void FourProcOneSrcThreeDestTest() {
		
		// Expected output file
		File expected = new File("./src/main/resources/dotfiles/outputfiles/4_processor_1_src_3_dest-output.dot");
		
		// TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = Main.writeOutput();
		*/

		// FileAssert.assertEquals(expected, output);
	}
	
	@Test
	public void FourProcThreeSrcOneDestTest() {
		
		// Expected output file
		File expected = new File("./src/main/resources/dotfiles/outputfiles/4_processor_3_src_1_dest-output.dot");
		
		// TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = Main.writeOutput();
		*/

		// FileAssert.assertEquals(expected, output);
	}
	
	@Test
	public void FourProcThreeSrcTwoDestTest() {
		
		// Expected output file
		File expected = new File("./src/main/resources/dotfiles/outputfiles/4_processor_3_src_2_dest-output.dot");
		
		// TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new Main.readInput("./src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = Main.writeOutput();
		*/

		// FileAssert.assertEquals(expected, output);
	}
}
