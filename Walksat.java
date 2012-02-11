import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;
import org.sat4j.reader.Reader;
import org.sat4j.reader.DimacsReader;
import org.sat4j.specs.IProblem;
import org.sat4j.minisat.core.Solver;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import org.sat4j.core.VecInt;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Walksat {

  public static native boolean runWalkSat(char[] args, int[] partialAssignment, int numPartialLiterals);

  public static native double getMaxPercentageSatisfiedClauses();

  public static native void setNumberOfSolutions(int num);

  public static native void setNumberOfTries(int tries);
  
//  public static native void reset();

  static {
    System.loadLibrary("walksat");
  }

  public static void main(String[] args) {
    if (args.length >= 1) {
      ISolver solver = SolverFactory.newDefault();
      Reader reader = new DimacsReader(solver);
      DecisionLevelTracing tracing = null;
      IProblem problem = null;
      try {
        problem = reader.parseInstance(args[0]);
        int haltLevel = problem.nVars() / 10;
        int numBranches = 1;
        tracing = new DecisionLevelTracing(numBranches, haltLevel);
        solver.setSearchListener(tracing);
        ((Solver) solver).setHaltLevel(haltLevel);
        if (problem.isSatisfiable()) {
          System.out.println("SATISFIABLE");
          System.out.println(reader.decode(problem.model()));
        }
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ParseFormatException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ContradictionException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (TimeoutException e) {
        // TODO Auto-generated catch block
        // we've reached the time out so now we want to run walk sat on our branch
        if (tracing != null && problem != null) {
          int nextLit = Math.abs(((Solver)solver).getNextBranchLiteral());
//         ArrayList<int[]> assignments = tracing.getAssignments();
          System.out.println("We are going to branch on " + nextLit);
//          int[] val = Arrays.copyOf(assignments.get(0), assignments.get(0).length + 1);
          setNumberOfSolutions(1);
           		    int[] temp = new int[4];
           		    for(int i = 1; i <= 4; i++) {
                               temp[i-1] = i % 2 == 0 ? -i : i;
                               System.out.print(temp[i-1] + " -- ");
                             }
	System.out.println();
          boolean satisfied = runWalkSat(args[0].toCharArray(), temp, temp.length);
/*          val[val.length - 1] = -nextLit;
          boolean satisfied = runWalkSat(args[0].toCharArray(), val, val.length);
          if (satisfied) {
            System.out.println("SATISFIABLE");
          }
          double stat0 = getMaxPercentageSatisfiedClauses();
          val[val.length - 1] = nextLit;
          satisfied = runWalkSat(args[0].toCharArray(), val, val.length);
          if (satisfied) {
            System.out.println("SATISFIABLE");
          }
          double stat1 = getMaxPercentageSatisfiedClauses();
          System.out.println(stat0 + " -- " + stat1);
          /*if (stat0 > stat1) {
            val[val.length - 1] = -nextLit;
          }
          VecInt assumps = new VecInt(val);
          try {
            if (problem.isSatisfiable(assumps)) {
              System.out.println("SATISFIABLE");
            } else {
              System.out.println("UNSATISFIABLE");
            }
          } catch (TimeoutException f) {
            f.printStackTrace();
          }*/
        }
      }
      System.out.println("DONE");
    } else {
      System.out.println("A cnf file must be passed to run.");
    }
  }

}
