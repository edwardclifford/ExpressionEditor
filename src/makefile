FLAGS = -g
JC = javac
JVM = java

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  CompoundExpression.java \
		  Expression.java \
		  ExpressionParseException.java \
		  ExpressionParser.java \
		  ExpressionParserPartialTester.java \
		  SimpleExpressionParser.java \

MAIN = ExpressionParser 

TEST = ExpressionParserPartialTester

default: classes

classes: $(CLASSES:.java=.class)

run: classes 
	$(JVM) $(MAIN) 

test: classes 
	$(JVM) org.junit.runner.JUnitCore $(TEST) 

clean:
	$(RM) *.class
