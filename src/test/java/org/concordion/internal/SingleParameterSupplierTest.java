package org.concordion.internal;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.concordion.api.BeforeExample;
import org.concordion.api.ExampleName;
import org.junit.Test;

public class SingleParameterSupplierTest {
    SingleParameterSupplier singleParameterSupplier = new SingleParameterSupplier(BeforeExample.class, ExampleName.class, "myExample");

    @Test
    public void returnsParameterWhenAnnotatedCorrectly() throws Exception {
        Method method = this.getClass().getMethod("validMethod", String.class);
        Parameter[] parameters = method.getParameters();
        assertThat((String)singleParameterSupplier.getValueForParameter(method, parameters[0]), is("myExample"));
    }
    
    public void validMethod(@ExampleName String param1) {
    }

    
    @Test
    public void throwsExceptionWhenParameterTypeIsIncorrect() throws Exception {
        Method method = this.getClass().getMethod("incorrectParamType", int.class);
        Parameter[] parameters = method.getParameters();
        try {
            singleParameterSupplier.getValueForParameter(method, parameters[0]);
            fail("Expected exception");
        } catch (AnnotationFormatError e) {
            assertThat(e.getMessage(), containsString("is expected to be of type 'java.lang.String'"));
        }
    }
    
    public void incorrectParamType(@ExampleName int param1) {
    }

    @Test
    public void throwsExceptionWhenParameterHasNoAnnotations() throws Exception {
        Method method = this.getClass().getMethod("parameterWithNoAnnotations", String.class);
        Parameter[] parameters = method.getParameters();
        try {
            singleParameterSupplier.getValueForParameter(method, parameters[0]);
            fail("Expected exception");
        } catch (AnnotationFormatError e) {
            assertThat(e.getMessage(), containsString("parameters annotated with '" + ExampleName.class.getName()));
        }
    }
    
    public void parameterWithNoAnnotations(String param1) {
    }
    

    @Test
    public void throwsExceptionWhenParameterHasMultipleAnnotations() throws Exception {
        Method method = this.getClass().getMethod("parameterWithMultipleAnnotations", String.class);
        Parameter[] parameters = method.getParameters();
        try {
            singleParameterSupplier.getValueForParameter(method, parameters[0]);
            fail("Expected exception");
        } catch (AnnotationFormatError e) {
            assertThat(e.getMessage(), containsString("parameters annotated with '" + ExampleName.class.getName()));
        }
    }
    
    public void parameterWithMultipleAnnotations(@ExampleName @Deprecated String param1) {
    }
}
