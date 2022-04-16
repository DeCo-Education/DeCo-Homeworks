
# Homework 2: Recap basic ErgoScript and programming

```
{
  val state: Boolean = true //Boolean data type
  val oneErg: Long = 1000L* 1000L * 1000L // Long data type
  val ergColl: Coll[Long] = Coll(oneErg, 2*oneErg, 3*oneErg) // Collection data type
  val moreErg: Coll[Long] = ergColl.append(10*oneErg)
  val getColl : Int = ergColl(1)

  if(state && (getColl == 2*oneErg)) { // If else
    val byte: Byte = 1 // Byte data type
    val pairs: (Byte, Long) = (byte, oneErg) // Pairs data type
    val mapByteToErg: Coll[(Byte, Long)] = Coll((byte, oneErg), (byte, 3*oneErg))
    // Arbitrary data type
    sigmaProp(1==1) // SigmaProp
  }
  val sum: Long = moreErg.fold(0, { // val with lambda.
  // This iterates through the moreErg Coll and sums the total value in the variable sum
    (accumulator: Long, element: Long) =>
      accumulator + element
  })
  def equalsSum(number: Long) = number == sum // def function
  sigmaProp(equalsTwo(15 * oneErg))
}
```
This contract does not acheive anything. It is meant to demonstrate the various things ErgoScript can do. 

