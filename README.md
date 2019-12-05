# ExpressionEditor
Java parser and renderer for mathmatical expressions by Ted Clifford and Marie Tessier

<center><p style="font-size: 200%; font-weight: bold">CS2103 2019 B-Term -- Project 5 -- Mathematical Expression Editor
</p>
Prof. Jacob Whitehill
</center>

<h1>Introduction</h1>
<img src="https://web.cs.wpi.edu/~cs2103/b19/Project5/ExpressionEditor.png" width="500"/><br>
In this project you will build an interactive (event-driven) mathematical expression editor with a graphical user interface (GUI). In particular,
the tool you build will allow the user to type in a mathematical expression, which will then be parsed into an "expression tree"
and then displayed graphically. Then, the user will be able to drag-and-drop different subexpressions -- at <b>arbitrary levels of 
granularity</b> -- so as to rearrange the expression <b>while preserving the same mathematical semantics</b>.

<h2>Example</h2>
Suppose the user is editing the expression <tt>2*x + 3*y + 4*z + (7+6*z)</tt>. Because of commutativity of addition, this expression can be
rearranged into <tt>3*y + 2*x + 4*z + (7+6*z)</tt> <em>without changing its meaning</em>. Similarly, because of commutativity of multiplication,
we can also change it into <tt>y*3 + 2*x + z*4 + (7+z*6)</tt>. In contrast, if we were to (nonsensically) reorder the substring <tt>y + 2</tt> (in 
<tt>3*<font color="red">y + 2</font>*x</tt>) to be <tt>2 + y</tt>, then this
would yield <tt>3*<font color="red">2 + y</font>*x + 4*z + (7+6*z)</tt>, which clearly has a different mathematical meaning from the original expression.

<h1>R1: Parsing Mathematical Expressions</h1>
In the first part of the assignment, you will need to build a <b>recursive descent parser</b>, based
on a <b>context-free grammar</b> (CFG), to convert a string -- e.g., <tt>10*x*z + 2*(15+y)</tt> -- into
a <b>parse tree</b> that captures the expression's mathematical  meaning, e.g.:<br>
<img src="https://web.cs.wpi.edu/~cs2103/b19/Project5/ExpressionEditor2.svg" width="400"/><br>
In the figure above, each blue node is a <tt>LiteralExpression</tt>; each yellow node is
a <tt>MultiplicativeExpression</tt>, each red node is an <tt>AdditiveExpression</tt>, and the clear node
is a <tt>ParentheticalExpression</tt>. Obviously,
these nodes are arranged into a <em>tree</em>. Every kind of expression <em>except</em> a <tt>LiteralExpression</tt>
can have children. Moreover,
the different "expression" classes  belong to a <em>class hierarchy</em> to maximize code re-use.

<h2>CFG for Mathematical Expressions</h2>
We will discuss context-free grammars (CFGs) in class. There are many possible grammars you could use to complete
this project. One suggestion (which I used in my own implementation) is the following:
<ul>
<li><tt>E &rarr; A | X</tt></li>
<li><tt>A &rarr; A+M | M</tt></li>
<li><tt>M &rarr; M*M | X</tt></li>
<li><tt>X &rarr; (E) | L</tt></li>
<li><tt>L &rarr; [a-z] | [0-9]+</tt></li>
</ul>
The  <tt>[0-9]+</tt> means <em>one or more characters from the set
[0-9]</em> -- e.g., <tt>1</tt>, <tt>51</tt>,  <tt>5132762351</tt>,  etc.
<br>
(Note: the grammar above does not lend itself to a particularly efficient parser, but it is arguably easier to understand
than other grammars that could also work.)
<p>

Based on this CFG, you can implement a recursive descent parser, in a 
similar manner as described in class. However, in contrast to the example
in class in which each "parse" method returned a <tt>boolean</tt>, your
parse methods  should return an object of type <tt>Expression</tt>
(an interface type described below). Each parse method should either return an
<tt>Expression</tt> object representing the sub-tree for the string
that you are parsing, or <tt>null</tt> if the string passed to the parse
method cannot be parsed.<p>

In this assignment, you should create a class called <tt>SimpleExpressionParser</tt>
that implements the <tt>ExpressionParser</tt> interface.

<h2><tt>Expression</tt> and <tt>CompoundExpression</tt> interfaces</h2>
This assignment will require multiple classes that represent different
kinds of mathematical expressions. Every expression has some methods
that must be supported, however. Accordingly, we have defined an
<tt>Expression</tt> interface. For non-terminal expression nodes, we have also created
a <tt>CompoundExpression</tt> interface, which extends <tt>Expression</tt> and
includes one extra method <tt>addSubexpression(subexpression)</tt>. 
See the comments in the <tt>Expression.java</tt> and <tt>CompoundExpression.java</tt> files
for more details.
<br>
<b>Important note</b>: these interfaces will be expanded upon during R2 of Project 5.

<h2>Equivalent Parse Trees</h2>
Multiple parse trees can be generated that have the same mathematical meaning. Consider the following example:<br>
<img src="https://web.cs.wpi.edu/~cs2103/b19/Project5/ExpressionEditor1.svg" width="400"/><br>
This is a different parse tree than the one shown above, but it encodes the same sequence of mathematical
operations. The only differences are  that (1) the <tt>MultiplicationExpression</tt> on the left now has
three children, whereas before it had only two; and (2) the children and grandchildren have been "merged"
into one layer. This equivalency
is reminiscent of the fact that <tt>10*x*z</tt> is completely equivalent to <tt>10*(x*z)</tt>: In the
first parse tree, the multiplication of <tt>x</tt> by <tt>z</tt> is computed first, and then its product
is multiplied by <tt>10</tt>. Due to the commutativity of multiplication, however, both parse trees are equivalent.<p>

<b>Important note</b>: While you could apply the same logic to claim (correctly) that the expression
<tt>10*x*z</tt> can be rearranged into <tt>z*x*10</tt>, you should not do so in this project. In particular,
<b>your parser should preserve the left-to-right ordering
of the sub-expressions in the parse tree</b>. The reason is that, in the GUI of R2, when the user types in
<tt>10*x*z</tt>, we want them to <em>see</em> <tt>10*x*z</tt> on the screen -- not some arbitrary rearrangement
thereof (which would be counterintuitive for them).

<h2><tt>withJavaFXControls</tt></h2>
The <tt>parse</tt> method of every <tt>ExpressionParser</tt> class takes a parameter called <tt>withJavaFXControls</tt>.
For R1, the value of this parameter should always be <tt>false</tt>, and you can thus ignore it altogether. This
parameter will become more important for R2, whose GUI requires that every <tt>Expression</tt> object also be
associated with a JavaFX "node". We'll talk more about this in class.

<h2>Converting an expression to a string</h2>
In order to verify that your parser is working correctly -- and to enable grading of your R1 submission -- you need
to implement a <tt>convertToString</tt> method (see the <tt>Expression</tt> interface). This method should
print out the contents of the entire expression tree, using one line per node of the tree, such that
each child node is indented (using <tt>\t</tt>) one more time than its parent. For example, if we parse the
string "10*x*z + 2*(15+y)":
<pre>
final ExpressionParser parser = new SimpleExpressionParser();
final Expression expression = parser.parse("10*x*z + 2*(15+y)", false);
</pre>
and then we call <tt>expression.convertToString(0)</tt>, then the result should be:
<pre>
+
	*
		10
		x
		z
	*
		2
		()
			+
				15
				y
</pre>
In the output above, the <tt>+</tt> in the first line signifies that the root expression is an <tt>AdditiveExpression</tt>
(that's what I call an expression that performs addition in my own implementation). Its two children are both
<tt>MultiplicativeExpression</tt> objects. The first such <tt>MultiplicativeExpression</tt> itself has <em>three</em>
children, namely <tt>10</tt>, <tt>x</tt>, and <tt>z</tt>, and so on.<p>


<h2>Flattening the Parse Tree</h2>
It turns out that, for the purpose of implementing a GUI-based mathematical expression editor,
the second parse tree shown above is more useful. The reason is that it will facilitate intuitive
drag-and-drop behavior whereby the user can "move" (using drag-and-drop) each child expression  to be
anywhere among the list of its siblings. (We will discuss this in more detail in class.) Therefore, we require
that every class that implements the <tt>Expression</tt> interface must have a method called <tt>flatten</tt>
that modifies the target <tt>Expression</tt> in the following way: whenever a child <em>c</em> has a type
(<tt>AdditiveExpression</tt>, <tt>MultiplicativeExpression</tt>, etc.) that is the same as the type of its
parent  <em>p</em>, you should replace <em>c</em> with its <em>own</em> children, which thereby become children
of <em>p</em>. For example, by flattening the first parse tree shown above, we obtain the second parse tree.
The <tt>flatten</tt> method should recursively flatten the entire <tt>Expression</tt> tree as much as possible.
Note that you are only required to flatten the <tt>AdditiveExpression</tt> and <tt>MultiplicativeExpression</tt>
objects.

<h1>Requirements</h1>
<ol>
<li>R1 (50 points): Build a parser to convert a <tt>String</tt> into an <tt>Expression</tt>. Your parser must be
able to handle the operations of <b>addition</b> (<tt>+</tt>) and <b>multiplication</b> (<tt>*</tt>). It
must also be able to handle <b>arbitrarily deeply nested balanced parentheses</b> (e.g., <tt>((2+(((z)))+3))</tt>).
Note, however, that you do <b>not</b> have to handle subtraction or division. <b>Note</b>: you <b>must</b>
complete this assignment using a CFG. (While it might be possible to hack together something that works for this project
without one, the only strategy that scales to more complex languages is based on CFGs.)</li>
</ol>

<h1>Design and Style</h1>
Your code must adhere to reasonable Java style. In particular, please adhere to the following guidelines:
<ul>
<li><b>Factor out</b> the logic that is common to the various <tt>Expression</tt>  classes.</li>
<li>Each class name should be a singular noun that can be easily pluralized.</li>
<li>Class names should be in <tt>CamelCase</tt>; variables should be in <tt>mixedCase</tt>.</li>
<li>Avoid "magic numbers" in your code (e.g., <tt>for (int i = 0; i < 999 /*magic number*/; i++)</tt>). Instead,
use <b>constants</b>, e.g., <tt>private static final int NUM_ELEPHANTS_IN_THE_ROOM = 999;</tt>, defined at the top of your class file.</li>

<li>Use whitespace consistently.</li>
<li>No method should exceed 50 lines of code (for a "reasonable" maximum line length, e.g., 100 characters). If your method is larger than
that, it's probably a sign it should be decomposed into a few helper methods.</li>
<li>Use comments to explain non-trivial aspects of code.</li>
<li>Use a <a href="http://www.oracle.com/technetwork/articles/java/index-137868.html">Javadoc</a>
comment to explain what each method does, what parameters it takes, and what it returns. Use
the <tt>/**...*/</tt> syntax along with <tt>@param</tt> and <tt>@return</tt> tags, as appropriate.</li>
<li>Use the <tt>final</tt> keyword whenever possible.</li>
<li>Use the <b>most restrictive</b> access modifiers (e.g., <tt>private</tt>, default, <tt>protected</tt>>, <tt>public</tt>),
for both variables and methods, that you can. Note that this does not mean you can never use non-<tt>private</tt> access; it
just means you should have a good reason for doing so.</li>
<li>Declare variables using the <b>weakest type</b> (e.g., an interface rather than a specific class implementation) you can;
ithen instantiate new objects according to the actual class you need. This will help to ensure <b>maximum flexibility</b> of your code.
For example, instead of<br>
<tt>final ArrayList&lt;String&gt; list = new ArrayList<String>();</tt><br>use<br>
<tt>final List&lt;String&gt; list = new ArrayList&lt;String&gt;();</tt><br>If, on the other hand, you have a good reason
for using the actual type of the object you instantiate (e.g., you need to access specific methods of
<tt>ArrayList</tt> that are not part  of the <tt>List</tt> interface), then it's fine to declare the variable with a stronger type.</li>
</ul>


<h1>Teamwork</h1>
You may work as a team on this project; the maximum team size is 2.

<h1>Getting started</h1>
<h2>R1</h2>
<ol>
<li>Please download the <a href="https://web.cs.wpi.edu/~cs2103/b19/Project5/R1.zip">R1 starter file</a>.</li>
<li>Have a look at the <tt>ExpressionParserPartialTester.java</tt> file, which includes some -- but not all -- of the test cases with which we will test your expression parser.</li>
<li>Write the parse methods necessary to implement the <tt>SimpleExpressionParser</tt>  that  we will
test as part of R1.</li>
</ol>


<h1>How,  What, and When to Submit</h1>
<h2>R1</h2>
<ul>
<li>Create a Zip file containing only and all those files necessary to test your parser using <tt>ExpressionParserPartialTester.java</tt>.</li>
<li><b>Submission deadline for R1</b>: Saturday, Dec 7, at 11:59pm EDT.</li>
</ul> 

