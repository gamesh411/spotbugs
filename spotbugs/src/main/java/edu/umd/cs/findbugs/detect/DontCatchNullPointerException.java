package edu.umd.cs.findbugs.detect;

import java.util.BitSet;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.umd.cs.findbugs.ba.*;
import org.apache.bcel.classfile.CodeException;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.visitclass.PreorderVisitor;
import org.apache.bcel.classfile.Method;

public class DontCatchNullPointerException
        extends PreorderVisitor implements Detector {

    private final BugReporter reporter;
    ClassContext classContext;

    public DontCatchNullPointerException(BugReporter reporter) {
        this.reporter = reporter;
    }

    private static final String NullPtrExceptionID =
            "java.lang.NullPointerException";

    private static final Set<String> NullPtrExceptionAncestors = Stream.of(
            "java.lang.Throwable",
            "java.lang.Exception",
            "java.lang.RuntimeException")
            .collect(Collectors.toCollection(HashSet::new));

    private boolean handlerContainsAddSuppressed(CodeException exc) {
        System.out.println("2");
        return false;
        /*
        Collection<BasicBlock> bbs = null;
        try {
            bbs = classContext.getCFG(getMethod()).getBlocksContainingInstructionWithOffset(exc.getHandlerPC());
        } catch (CFGBuilderException e) {
            e.printStackTrace();
        }
        for (BasicBlock bb : bbs) {
            // bb.instructionIterator()
            System.out.println("---");
            //PostDominatorsAnalysis domanal = classContext.getNonImplicitExceptionDominatorsAnalysis(getMethod());
            DominatorsAnalysis domdom = null;
            try {
                domdom = classContext.getNonImplicitExceptionDominatorsAnalysis(getMethod());
            } catch (CFGBuilderException e) {
                e.printStackTrace();
            } catch (DataflowAnalysisException e) {
                e.printStackTrace();
            }
            BitSet bs = domdom.getAllDominatedBy(bb);
            System.out.println("dominated by bb " + String.valueOf(exc.getHandlerPC()));
            bs.stream().forEach(System.out::println);
            System.out.println("domination ends");
        
            try {
                for (BasicBlock dominatedBlock : classContext.getCFG(getMethod()).getBlocks(bs)) {
                    System.out.println("###");
                    for (BasicBlock.InstructionIterator it = dominatedBlock.instructionIterator(); it.hasNext();) {
                        System.out.println(it.next().toString());
                    }
                }
            } catch (CFGBuilderException e) {
                e.printStackTrace();
            }
        
        }
        
        System.out.println("3");
        boolean addSuppressedFound = false;
        Collection<BasicBlock> blockList = cfg.getBlocksContainingInstructionWithOffset(exc.getHandlerPC());
        for (BasicBlock block : blockList) {
            System.out.println("4");
            for (BasicBlock.InstructionIterator it = block.instructionIterator(); it.hasNext();) {
                System.out.println("5");
                Instruction inst = it.next().getInstruction();
                System.out.println(inst.toString());
                if (inst instanceof INVOKEVIRTUAL) {
                    System.out.println("6");
                    INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;
                    String invocationName = invoke.getName(classContext.getConstantPoolGen());
                    System.out.println("7: " + invocationName);
                    // throw new RuntimeException("invocation name: " + invocationName);
                    //if (invocationName.equals("java/lang/Throwable.addSuppressed")) {
                    //    addSuppressedFound = true;
                    //}
                }
            }
        }
        
        return false;
                 */
    }

    @Override
    public void visit(CodeException exc) {
        int type = exc.getCatchType();

        if (type == 0)
            return;

        String name = getConstantPool()
                .constantToString(getConstantPool()
                        .getConstant(type));

        if (name.equals(NullPtrExceptionID)) {
            BugInstance bug = new BugInstance(this,
                    "DCN_NULLPOINTER_EXCEPTION",
                    NORMAL_PRIORITY);
            bug.addClassAndMethod(this);
            bug.addSourceLine(this.classContext, this, exc.getHandlerPC());

            reporter.reportBug(bug);
        }

        if (NullPtrExceptionAncestors.contains(name)) {

            System.out.println("1");
            // Dont report try-with-resources usecases.
            if (name.equals("java.lang.Throwable") && handlerContainsAddSuppressed(exc))
                return;

            BugInstance bug = new BugInstance(this,
                    "DCN_NULLPOINTER_EXCEPTION_ANCESTORS",
                    LOW_PRIORITY);
            bug.addString(name.split("\\.")[2]);
            bug.addClassAndMethod(this);
            bug.addSourceLine(this.classContext, this, exc.getHandlerPC());

            reporter.reportBug(bug);
        }
    }

    @Override
    public void visit(Method obj) {
        // super.visit(obj);
        CFG cfg = null;
        try {
            cfg = classContext.getCFG(getMethod());
        } catch (CFGBuilderException e) {
            e.printStackTrace();
        }

        if (cfg == null)
            return;

        for (BasicBlock bb : cfg.blocks()) {
            System.out.println();
            System.out.println(String.valueOf(bb.toString()));
            for (BasicBlock.InstructionIterator it = bb.instructionIterator(); it.hasNext();) {
                System.out.println(it.next().toString());
            }
            DominatorsAnalysis postDominators = null;
            try {
                postDominators = classContext.getNonImplicitExceptionDominatorsAnalysis(getMethod());
            } catch (CFGBuilderException e) {
                e.printStackTrace();
            } catch (DataflowAnalysisException e) {
                e.printStackTrace();
            }
            assert postDominators != null;
            BitSet dominatorsOf = postDominators.getAllDominatorsOf(bb);
            System.out.println("dominators of " + bb.toString());
            dominatorsOf.stream().forEach(System.out::println);

            BitSet dominatedBy = postDominators.getAllDominatedBy(bb);
            System.out.println("dominated by " + bb.toString());
            dominatedBy.stream().forEach(System.out::println);
        }

    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        this.classContext = classContext;
        this.classContext.getJavaClass().accept(this);
    }

    @Override
    public void report() {
    }
}
