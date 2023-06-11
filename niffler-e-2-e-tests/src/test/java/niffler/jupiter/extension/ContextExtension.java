package niffler.jupiter.extension;

import org.junit.jupiter.api.extension.*;

public class ContextExtension implements
        BeforeAllCallback,
        AfterAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        TestExecutionExceptionHandler {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.out.println("#### BeforeAllCallback!");
        System.out.println("#### " + context.getRequiredTestClass());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        System.out.println("#### AfterAllCallback!");
        System.out.println("#### " + context.getRequiredTestClass());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.println("  #### BeforeEachCallback!");
        System.out.println("  #### " + context.getRequiredTestMethod());
        System.out.println("  #### " + context.getRequiredTestClass());
        System.out.println("  #### " + context.getRequiredTestInstance());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        System.out.println("  #### AftereEachCallback!");
        System.out.println("  #### " + context.getRequiredTestMethod());
        System.out.println("  #### " + context.getRequiredTestClass());
        System.out.println("  #### " + context.getRequiredTestInstance());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        System.out.println("          ####  AfterTestExecutionCallback!");
        System.out.println("          #### " + context.getRequiredTestMethod());
        System.out.println("          #### " + context.getRequiredTestClass());
        System.out.println("          #### " + context.getRequiredTestInstance());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.out.println("          #### BeforeTestExecutionCallback!");
        System.out.println("          #### " + context.getRequiredTestMethod());
        System.out.println("          #### " + context.getRequiredTestClass());
        System.out.println("          #### " + context.getRequiredTestInstance());
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
            throws Throwable {
        System.out.println("--------------------------------------------------");
        System.out.println("          #### TestExecutionExceptionHandler!");
        System.out.println("          #### " + context.getRequiredTestMethod());
        System.out.println("          #### " + context.getRequiredTestClass());
        System.out.println("          #### " + context.getRequiredTestInstance());
        System.out.println("--------------------------------------------------");
        throw throwable;
    }
}
