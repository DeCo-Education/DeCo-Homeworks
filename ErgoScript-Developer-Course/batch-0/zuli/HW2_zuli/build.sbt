import org.ergoplatform.compiler.ErgoScalaCompiler._
import org.ergoplatform.playgroundenv.utils.ErgoScriptCompiler
import org.ergoplatform.playground._

//1. Booleans
val thisbool = true
val y = !false

//2. if else statements
val a=1
val b=2
val smallervalue = if (a < b ) a else b
println(smallervalue)

//Datatypes

//long
val x = 123L
println(x)

//Boolean
val alreadytrue = thisbool
println(alreadytrue)
println(y)

//Byte
val something = Array[Byte](10, -20, 30)
println(something)

//complex data types

//lists or collections
val numbers = List(1,2,3,4)
println(numbers)
val fruit: List[String] = List("apples", "oranges", "pears")
println(fruit)

//pairs
val ingredientamount = ("water" , 1000)
println(ingredientamount._1)
println(ingredientamount._2)

//arbitrary data type
var A:Map[Char,Int] = Map()
val colors = Map("red" -> "#FF0000", "azure" -> "#F0FFFF")
println(colors)

//val vs def statements

//val
val add = (a: Int, b: Int) => a + b
//add is created as a variable, in this case as a val field
//val functions can use "case" expressions without a beginning "match"

//def
def add(a: Int, b: Int) = a + b
//It is a method that needs to be defined within a "class" or "object"
//As a method in a class, meaning it has access to the other members in the same class

//Syntax
//Not sure what we are supposed to write here...
//A Scala program can be defined as a collection of objects that communicate via invoking each other’s methods.
//Method: A method is like some specific behavior. A class can contain many methods.
//It is in methods where the logics are written, data is manipulated and all the actions are executed.

//Lambdas
val ex1 = (x:Int) => x + 2

// with multiple parameters
val ex2 = (x:Int, y:Int) => x * y

println(ex1(7))
println(ex2(2, 3))

//map function???
// list of numbers
val l = List(1, 1, 2, 3, 5, 8)
// squaring each element of the list
val res = l.map( x=> x * x )
println(res)

//sigmaprops

//I have no idea what this is, I'm sorry.
