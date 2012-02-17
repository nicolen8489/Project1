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

  public static native boolean runWalkSat(char[] args, int[] partialAssignment, int numPartialLiterals, DataInfo dataInfo);

  public static native void setNumberOfSolutions(int num);

  public static native void setNumberOfTries(int tries);
  
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
        tracing.setTracing(true);
        solver.setSearchListener(tracing);
        ((Solver) solver).setHaltLevel(haltLevel);
        if (problem.isSatisfiable()) {
          System.out.println("SATISFIABLE - original minisat");
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
          DataInfo dataInfo = new DataInfo();
          ArrayList<int[]> assignments = tracing.getAssignments();
          int nextLit = tracing.getNextBranches().get(0); 
          System.out.println("We are going to branch on " + nextLit);
          for(int i = 0; i < assignments.get(0).length; i++) {
            System.out.print(assignments.get(0)[i] + " ");
          }
          System.out.println();
          int[] val = Arrays.copyOf(assignments.get(0), assignments.get(0).length + 1);
          setNumberOfSolutions(1);
          val[val.length - 1] = -nextLit;
          boolean satisfied = runWalkSat(args[0].toCharArray(), val, val.length, dataInfo);
          if (satisfied) {
            System.out.println("SATISFIABLE - first walk sat");
            System.exit(0);
          }
          double stat0 = dataInfo.getMaxSatClausePercent();
          val[val.length - 1] = nextLit;
          satisfied = runWalkSat(args[0].toCharArray(), val, val.length, dataInfo);
          if (satisfied) {
            System.out.println("SATISFIABLE - second walk sat");
            System.exit(0);
          }
          double stat1 = dataInfo.getMaxSatClausePercent();
          System.out.println(stat0 + " -- " + stat1);
          if (stat0 > stat1) {
            val[val.length - 1] = -nextLit;
          }
          try {
            ((Solver)solver).setOrder(new PartialVarOrderHeap(val));
            ((Solver)solver).setHaltLevel(Integer.MAX_VALUE);
            tracing.setTracing(false);
            if (problem.isSatisfiable()) {
              System.out.println("SATISFIABLE - minisat");
              System.out.println(reader.decode(problem.model()));
            } else {
              System.out.println("UNSATISFIABLE");
            }
          } catch (TimeoutException f) {
            f.printStackTrace();
          }
        }
      }
      System.out.println("DONE");
    } else {
      System.out.println("A cnf file must be passed to run.");
    }
  }
}

