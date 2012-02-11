/*******************************************************************************
 * SAT4J: a SATisfiability library for Java Copyright (C) 2004-2008 Daniel Le Berre
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU Lesser General Public License Version 2.1 or later (the
 * "LGPL"), in which case the provisions of the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of the LGPL, and not to allow others to use your version of
 * this file under the terms of the EPL, indicate your decision by deleting
 * the provisions above and replace them with the notice and other provisions
 * required by the LGPL. If you do not delete the provisions above, a recipient
 * may use your version of this file under the terms of the EPL or the LGPL.
 * 
 * Based on the original MiniSat specification from:
 * 
 * An extensible SAT solver. Niklas Een and Niklas Sorensson. Proceedings of the
 * Sixth International Conference on Theory and Applications of Satisfiability
 * Testing, LNCS 2919, pp 502-518, 2003.
 *
 * See www.minisat.se for the original solver in C++.
 * 
 *******************************************************************************/

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.sat4j.core.LiteralsUtils;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.ISolverService;
import org.sat4j.specs.Lbool;
import org.sat4j.specs.SearchListener;

/**
 * @since 2.2
 */
public class DecisionLevelTracing implements SearchListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * private final String filename; private PrintStream out;
	 */
	private ISolverService service;
	private ArrayList<int[]> assignments;
	private int numBranches;
	private int branchCount;
	private int haltLevel;
	private boolean tracing;

	public DecisionLevelTracing(/* String filename, */int numBranches,
			int haltLevel) {
		/* this.filename = filename; */
		this.numBranches = numBranches;
		this.haltLevel = haltLevel;
		branchCount = 0;
		assignments = new ArrayList<int[]>();
		tracing = false;
		/* updateWriter(); */
	}
	
	public void setTracing(boolean tracing) {
		this.tracing = tracing;
	}

	/*
	 * private void updateWriter() { try { out = new PrintStream(new
	 * FileOutputStream(filename + ".dat")); } catch (FileNotFoundException e) {
	 * out = System.out; } }
	 */

	public void adding(int p) {
		// TODO Auto-generated method stub

	}

	public void assuming(int p) {

	}

	public void backtracking(int p) {
		// TODO Auto-generated method stub

	}

	public void beginLoop() {
		// TODO Auto-generated method stub

	}

	public void conflictFound(IConstr confl, int dlevel, int trailLevel) {

	}

	public void conflictFound(int p) {
		// TODO Auto-generated method stub

	}

	public void delete(int[] clause) {
		// TODO Auto-generated method stub

	}

	public void end(Lbool result) {
		/* out.close(); */
	}

	public void learn(IConstr c) {
		// TODO Auto-generated method stub

	}

	public void propagating(int p, IConstr reason) {
		// TODO Auto-generated method stub

	}

	public void solutionFound() {
		// TODO Auto-generated method stub

	}

	public void start() {
		/* updateWriter(); */
	}

	public void restarting() {
		if(tracing) {
		int decisionLevel = service.currentDecisionLevel();
		if (decisionLevel == haltLevel) {
			int[] vals = new int[0];
			for(int i = 1; i <= decisionLevel; i++) {
				int[] curVals = service.getLiteralsPropagatedAt(i);
				int index = vals.length;
				vals = Arrays.copyOf(vals, vals.length + curVals.length);
				for(int j = 0; j < curVals.length; j++) {
					vals[index++] = LiteralsUtils.var(curVals[j]);
				}
			}
			int[] boolVals = new int[vals.length];
			for (int i = 0; i < vals.length; i++) {
				int literal = vals[i];
				Lbool bool = service.truthValue(literal);
				if (bool == Lbool.TRUE) {
					boolVals[i] = literal;
				} else if (bool == Lbool.FALSE) {
					boolVals[i] = -literal;
				}
			}
			assignments.add(boolVals);
			branchCount++;
		}
		if (branchCount == numBranches) {
			service.stop();
		}
		}
	}

	public void backjump(int backjumpLevel) {
		/* out.println(backjumpLevel); */
	}

	@Override
	public void init(ISolverService solverService) {
		// TODO Auto-generated method stub
		service = solverService;
	}
	
	public ArrayList<int[]> getAssignments() {
		return assignments;
	}

}
