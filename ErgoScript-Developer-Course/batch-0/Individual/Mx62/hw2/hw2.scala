val testBool = true && false
val testLong = if (testBool) 1 else 0
println("testLong: " + testLong)
val testColl = Coll(1, 2, 3)
def testPair = (0, 2)
def mapper( i:Long ) : Long = {i + 1}
val testMap = testColl.map(mapper)
val testLambda = ( x:Int ) => x + 1
val testProp = sigmaProp(false)
