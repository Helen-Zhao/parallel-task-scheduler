package testcases;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import main.Main;
import models.Node;

import org.junit.Before;
import org.junit.Test;

public class Visualisation_Test {

	String dir;
	
	 @Before
	    public void setUp() {
	        String workingDir = System.getProperty("user.dir");
	        dir = ".";

	        if (workingDir.length() > 0 && workingDir.contains("src")) {
	            dir = workingDir.substring(0, workingDir.indexOf(File.separator + "src"));
	        }
	    }
	
	@Test
	public void visualisationTest() {
        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_8_Random.dot", "2", "-v"};

        Main.main(args);
        
        String str = "hfhfh";
        System.out.println(str);
        
	}

}
