import edu.umd.cs.findbugs.annotations.ExpectWarning;

class DontCatchUncheckedExceptionTest {

    // Suppress NPE

    @ExpectWarning("DCN_NULLPOINTER_EXCEPTION")
    public int doNotCatchNullpointerException (String s) {
	int len = -1;
	try {
	  len = s.length();
	} catch (NullPointerException e) {}
	return len;
    }

    // Null Object Pattern

    private interface Logger {
      public void print(String msg);
    }

    private class FileLogger implements Logger {
      public void print(String msg) { /*...*/ }
    }

    private class ConsoleLogger implements Logger {
      public void print(String msg) { /*...*/ }
    }

    private class Service {
      private final Logger log;
      public Service() { log = null; }
      public Service(Logger l) { log = l; }
      @ExpectWarning("DCN_NULLPOINTER_EXCEPTION")
      public void serve() {
        try {
	  /*...*/
	  log.print("Serving request...");
	} catch (NullPointerException e) {}
      }
    }

    // Division
    
    public static void calculateAverage(int sum, int count)
      throws ArithmeticException, java.io.IOException  {
      int average  = sum / count;
      // May throw IOException...
      System.out.println("Average: " + average);
    }

    @ExpectWarning("DCN_NULLPOINTER_EXCEPTION")
    public void doNotCatchGenericException() {
      try {
        calculateAverage(321, 5);
        calculateAverage(321, 0); // Divide by zero
      } catch (Exception e) {
        System.out.println("Divide by zero exception : "
                           + e.getMessage());
      }
    }
}
