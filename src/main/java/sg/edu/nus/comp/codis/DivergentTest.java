package sg.edu.nus.comp.codis;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import sg.edu.nus.comp.codis.ast.*;
import sg.edu.nus.comp.codis.ast.theory.Equal;
import sg.edu.nus.comp.codis.ast.theory.Not;

import java.util.*;


/**
 * Created by Sergey Mechtaev on 14/4/2016.
 */
public class DivergentTest {

    private Solver solver;

    public DivergentTest(Solver solver) {
        this.solver = solver;
    }

    public Optional<Triple<TestCase, Node, Node>> generate(Map<Node, Integer> componentMultiset,
                                                           List<TestCase> testSuite,
                                                           List<ProgramVariable> inputVariables) {
        assert !testSuite.isEmpty();

        CBS cbs = new CBS(Z3.getInstance(), false, Optional.empty()); // integer only for now

        Type outputType = testSuite.get(0).getOutputType();

        Map<ProgramVariable, Parameter> parametricAssignment = new HashMap<>();
        for (ProgramVariable variable : inputVariables) {
            parametricAssignment.put(variable, new Parameter("<generatedInput>" + variable.getName(), variable.getType()));
        }

        ArrayList<Component> components1 = CBS.flattenComponentMultiset(componentMultiset);
        Component result1 = new Component(new Hole("result", outputType, Node.class));
        Parameter output1 = new Parameter("<generatedOutput1>", outputType);
        TestCase newTest1 = TestCase.ofAssignment(parametricAssignment, output1);
        ArrayList<TestCase> testSuite1 = new ArrayList<>(testSuite);
        testSuite1.add(newTest1);
        ArrayList<Node> clauses1 = cbs.encode(testSuite1, components1, result1);

        ArrayList<Component> components2 = CBS.flattenComponentMultiset(componentMultiset);
        Component result2 = new Component(new Hole("result", outputType, Node.class));
        Parameter output2 = new Parameter("<generatedOutput2>", outputType);
        TestCase newTest2 = TestCase.ofAssignment(parametricAssignment, output2);
        ArrayList<TestCase> testSuite2 = new ArrayList<>(testSuite);
        testSuite2.add(newTest2);
        ArrayList<Node> clauses2 = cbs.encode(testSuite2, components2, result2);

        ArrayList<Node> clauses = new ArrayList<>();

        clauses.addAll(clauses1);
        clauses.addAll(clauses2);

        clauses.add(new Not(new Equal(output1, output2)));

        Optional<Map<Variable, Constant>> model = solver.getModel(clauses);
        if (!model.isPresent()) {
            return Optional.empty();
        }

        Node program1 = cbs.decode(model.get(), components1, result1);
        Node program2 = cbs.decode(model.get(), components2, result2);

        Map<ProgramVariable, Node> newAssignment = new HashMap<>();
        for (ProgramVariable variable : parametricAssignment.keySet()) {
            newAssignment.put(variable, cbs.substituteParameters(model.get(), parametricAssignment.get(variable)));
        }

        TestCase newTest = TestCase.ofAssignment(newAssignment, cbs.substituteParameters(model.get(), output1));
        return Optional.of(new ImmutableTriple<>(newTest, program1, program2));
    }
}
