/*import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;
import org.sat4j.reader.Reader;
import org.sat4j.reader.DimacsReader;*/
import java.util.Arrays;

public class Walksat {

    public static native boolean runWalkSat(char[] args, int[] partialAssignment, int numPartialLiterals);

    public static native double getMaxPercentageSatisfiedClauses();

    public static native void setNumberOfSolutions(int num);

    public static native void setNumberOfTries(int tries);

    static {
        System.loadLibrary("walksat");
    }

    public static void main(String[] args) {
        if (args.length >= 1) {
/*            ISolver solver = SolverFactory.newDefault();
            Reader reader = new DimacsReader(solver);
            DecisionLevelTracing tracing = null;
            try {
                IProblem problem = reader.parseInstance(args[0]);
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
                if (tracing != null) {
                    int nextLit = Math.abs(((Solver)solver).getNextBranchLiteral());
                    ArrayList<int[]> assignments = tracing.getAssignments();
                    System.out.println("We are going to branch on " + nextLit);*/
  		    int[] temp = new int[10];
  		    for(int i = 1; i <= 10; i++) {
                      temp[i-1] = i % 2 == 0 ? -i : i;
                    }
                    setNumberOfSolutions(1);
                    setNumberOfTries(2);
                    boolean satisfied = runWalkSat(args[0].toCharArray(), temp, temp.length);
 		    double stat = getMaxPercentageSatisfiedClauses();
  		    System.out.println(stat);
            /*    }
            }
            System.out.println("DONE");*/
        } else {
            System.out.println("A cnf file must be passed to run.");
        }
    }

}
