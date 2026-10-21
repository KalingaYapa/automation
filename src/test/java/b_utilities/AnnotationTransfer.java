package b_utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Attaches {@link RetryAnalyzer} to every @Test method without each test having
 * to declare it.
 */
public class AnnotationTransfer implements IAnnotationTransformer {

	@Override
	public synchronized void transform(ITestAnnotation testAnnotation, @SuppressWarnings("rawtypes") Class testClass,
			@SuppressWarnings("rawtypes") Constructor testConstructor, Method testMethod) {
		testAnnotation.setRetryAnalyzer(RetryAnalyzer.class);
	}
}
