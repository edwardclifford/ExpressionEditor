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
To get an immediate sense of what this will look like,
check out <a href="https://web.cs.wpi.edu/~cs2103/b19/Project5/ExpressionEditor.mp4">this demo video</a>.

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

<h1>R2: Editing Mathematical Expressions</h1>
The overall goal of R2 is to replicate all the functionality in the demo video above as closely as possible. In the
sections below, you will see specific functionality marked with "F1", "B1", etc.; these represent the specific
functionality that will be graded.<p>

There are several key features of the GUI-based editor:
<ul>
<li>The user can enter an expression in the textbox, click the Parse button, and have it displayed within the window.</li>
<li>The user can click on an expression and thereby change the <b>focus</b>.</li>
<li>The user can drag+drop the focused expression into a different location <b>among its siblings</b>.</li>
<li>Drag+drop affects not only the visual representation of the expression, but also the underlying tree-based representation.
In particular, you should be able to observe the change in the order of the siblings when you call
<tt>parent.convertToString()</tt>.</li>
</ul>

<h2>Parsing and displaying the Expression</h2>
When the expression string typed by the user is parsed, the <tt>SimpleExpressionParser</tt> should return an object of type
<tt>Expression</tt>. The <tt>ExpresssionEditor</tt> class will then call the <tt>getNode()</tt> method of this <tt>Expression</tt>
object and display it within the window (<b>B1</b>).<p>

Note that, since you actually need to display a JavaFX Node for every (sub-)expression, you should pass a value of
<tt>true</tt> for <tt>withJavaFXControls</tt>.

<h2>Focus</h2>
The user can select a subexpression and thereby give it the "focus", denoted by a red box around the focused node.
Focus is important because it denotes the subexpression that can be dragged+dropped among its siblings.
The focus behavior you implement must adhere to the following
rules:
<ul>
  <li>Initially (after parsing a string), there is no focus.</li>
  <li>If there is no current focus, and if the user clicks on location (<em>x</em>,<em>y</em>) that is contained within the
  rectangular bounds <em>b</em> of some child expression <em>c</em> whose parent is the root of the entire expression tree, 
  then <em>c</em> receives the focus (<b>F1</b>). (<b>Note</b>: the reason why the root itself never receives the focus is that, since the
  root has no parent, then it cannot be moved anywhere.)</li>
  <li>Suppose the currently focused subexpression <em>p</em> has rectangular bounds <em>b</em>:
    <ul>
    <li>If the user clicks on an (<em>x</em>,<em>y</em>) location that <b>is not</b> contained within <em>b</em>,
then the focus is cleared -- i.e., nothing is focused anymore (<b>F2</b>).</li>
    <li>If the user clicks on an (<em>x</em>,<em>y</em>) location that <b>is</b> contained within <em>b</em>; if there exists a direct child expression <em>c</em>  of <em>p</em>; and if the rectangular bounds of <em>c</em> contains (<em>x</em>,<em>y</em>), then the focus is set to <em>c</em> (<b>F3</b>). ("Direct child" means that <em>c</em>'s parent is <em>p</em>.)</li>
    <li>If the user clicks on an (<em>x</em>,<em>y</em>) location that <b>is</b> contained within <em>b</em>, but there does not exist any child expression of <em>p</em> whose bounds also contains (<em>x</em>,<em>y</em>), then the focus is cleared (<b>F4</b>).</li>
    </ul>
  </li>
</ul>

<b>Important note</b>: In an additive or multiplicative expression, the operator symbol itself (<tt>+</tt>,<tt>*</tt>, or <tt>*</tt>)
should <b>not</b> count as part of the child expression (<b>F5</b>). For example, the expression <tt>1+2</tt> is an additive expression with
two child nodes, <tt>1</tt> and <tt>2</tt>. Clicking on the <tt>+</tt> symbol should <b>clear</b> the focus since an (<em>x</em>,
<em>y</em>) positioned directly on  top of the <tt>+</tt>  is not contained within the rectangular bounds of any child expression
of <tt>1+2</tt>.


<h2>Drag+drop</h2>
<h3>Drag</h3>
As soon as the user presses the mouse button on a subexpression that already has the focus, then a <b>deep copy</b> of that
focused expression should be made. One copy should remain at the same location where the subexpression already was and
should become <b>ghosted</b>, i.e., displayed in a light-grey color (<b>G1</b>). The other copy should be moved according to where
the user moves the mouse, i.e., is dragged across the GUI (<b>D1</b>).

<h3>Drop</h3>
A child expression <em>c</em> that is currently being dragged can be relocated (through "dropping" it) to any index
among its siblings. Note, however, that dragging+dropping <em>c</em>  should <b>not</b> affect the relative ordering of its siblings (<b>D2</b>).
As an example, suppose the child expression <tt>2</tt>  in the expression <tt>1+2+3</tt> is being dragged. Then, depending
on the (<em>x</em>,<em>y</em>) location of where <tt>2</tt> is dropped, the expression could be rearranged into one of three
possible "configurations":
<ul>
<li><tt>1+2+3</tt></li>
<li><tt>2+1+3</tt></li>
<li><tt>1+3+2</tt></li>
</ul>
Note that dragging+dropping the <tt>2</tt> should <b>not</b> result in any of the following expressions (since they would require
a change in the relative ordering of <tt>2</tt>'s siblings):
<ul>
<li><tt>3+2+1</tt></li>
<li><tt>2+3+1</tt></li>
<li><tt>3+1+2</tt></li>
</ul>
To decide when, based on the current (<em>x</em>,<em>y</em>) position of the dragged child expression <em>c</em>, to update the
index of <em>c</em> among its siblings (<b>D3</b>), you should implement the  
following  strategy (<b>D3</b>):
<ol>
<li>When the user first begins dragging <em>c</em>, compute the set of all valid configurations of <em>c</em>'s parent
that could result by dragging and dropping <em>c</em>. (In the example above, these would be <tt>1+2+3</tt>,
<tt>2+1+3</tt>, and <tt>1+3+2</tt>.)</li>
<li>Compute the (<em>x</em>,<em>y</em>) location of where <em>c</em> would appear within each of the possible configurations
computed during the previous step. You will actually only need the <em>x</em> values; let <em>x<sub>i</sub></em> denote the <em>x</em> coordinate
where the <em>c</em> appears in configuration <em>i</em>.</li>
<li>Whenever <em>c</em> is dragged to position (<em>x</em>,<em>y</em>), find the configuration <em>i</em> (among all the configurations)
whose <em>x<sub>i</sub></em> value is closest to <em>x</em>.</li>
</ol>
Note that getting feature <b>D3</b> exactly right is tricky.
Below is an example of a configuration that should <em>not</em> occur if the mouse is at rest (i.e., not moving)
and your program is implemented correctly.<br>
<img src="https://web.cs.wpi.edu/~cs2103/b19/Project5/shouldNeverHappen.png" width="500"/><br>
Notice
how the ghosted expression of <tt>3*y</tt> is <em>not</em> where it should be. This situation was triggered (in a buggy implementation)
by dragging the <tt>3*y</tt> expression <em>very fast</em>.
(It took me several tries to generate this.) Also, in a fully correct implementation, there should be a "critical point" (x coordinate)
when the expression shifts back and forth between positions in its parent. If you don't quite implement this correctly,
then you may have to move the expression back and forth a <em>considerable distance</em> before the swap occurs.<p>

Finally, at all times, the "ghost" expression should be moved to reflect where <em>c</em> <em>would</em> be moved <em>if</em> the user released
the mouse (i.e., "dropped" <em>c</em>) (<b>G2</b>).<p>


<h3>Modifying the underlying expression tree</h3>
After the user has dragged+dropped a child expression to a different location among its siblings, the expression tree
should reflect this change. In particular, the order of the lines of output  of <tt>convertToString()</tt> should reflect
the new ordering (<b>E1</b>). As an example, suppose we drag+drop the <tt>2</tt> in the expression <tt>1+2</tt> to be to the left of the
<tt>1</tt>, so that the new expression becomes <tt>2+1</tt>. Then calling <tt>convertToString()</tt> on the
modified expression tree should produce:
<pre>
+
	2
	1
</pre>
<b>In order to enable us to test whether you implemented this correctly, you are required to call
<tt>convertToString(0)</tt> and print the results (using <tt>System.out.println()</tt>) whenever the user "drops" an expression.</b>

<h1>Requirements</h1>
<ol>
<li>R2 (50 points): Build a GUI-based interactive drag-and-drop mathematical expression editor, based on the
parser you coded in R1. In particular, we will manually verify that you implement the following aspects of the
project correctly:
	<ul>
	<li><b>B1</b>: 7 points</li>
	<li><b>F1</b>: 2 points</li>
	<li><b>F2</b>: 2 points</li>
	<li><b>F3</b>: 2 points</li>
	<li><b>F4</b>: 2 points</li>
	<li><b>F5</b>: 1 points</li>
	<li><b>G1</b>: 2 points</li>
	<li><b>G2</b>: 2 points</li>
	<li><b>D1</b>: 4 points</li>
	<li><b>D2</b>: 2 points</li>
	<li><b>D3</b>: 6 points</li>
	<li><b>E1</b>: 8 points</li>
	</ul>
	We will also assign 10 points for design &amp; style.
</li>
<li>Extra credit (4 points): Implement <b>animations</b> so that, while the user is dragging-and-dropping a sub-expression,
the ghost moves smoothly instead of "jumping" from one location to another. (You might use the JavaFX <tt>TranslateTransition</tt>
class.)</li>
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
<h2>R2</h2>
<ol>
<li>Please download the <a href="https://web.cs.wpi.edu/~cs2103/b19/Project5/R2.zip">R2 starter file</a>.</li>
<li>Have a look at the <tt>ExpressionEditor.java</tt> file, which includes some starter code for the GUI.</li>
</ol>


<h1>How,  What, and When to Submit</h1>
<h2>R2</h2>
<ul>
<li>Create a Zip file containing only and all those files necessary to finish implementing the <tt>ExpressionEditor.java</tt>.</li>
<li><b>Submission deadline for R2</b>: Friday, Dec 13, at 11:59pm EDT.</li>
</ul> 
