import inputoutput.InputReader;
import models.Edge;
import models.Node;
import scheduler.*;

import java.io.File;
import java.util.List;

/**
 * @author helen zhao
 * Created 08/08/16
 */

public class Main {

	public static void main(String[] args) {
		/*
			Input params take the form of: input.dot, p, algorithm
			where input.dot is the input file, p is the number of processors and algorithm specifes
			what algorithm the scheduler should use
		 */


		String inputFileName = args[0];
		int numProcessors = Integer.parseInt(args[1]);
		String algorithm = args[2];

		InputReader inputReader = new InputReader(new File(inputFileName));
		List<Node> nodeList = inputReader.nodeList;
		List<Edge> edgeList = inputReader.edgeList;

		SchedulerInterface scheduler;

		switch (algorithm.toLowerCase()){
			case "bnb":
				//dostuff
				break;
			case "as":

		}


	}

}
