package edu.umd.cs.findbugs.detect;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.bcel.classfile.CodeException;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.visitclass.PreorderVisitor;

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
    public void visitClassContext(ClassContext classContext) {
        this.classContext = classContext;
        this.classContext.getJavaClass().accept(this);
    }

    @Override
    public void report() {
    }
}
