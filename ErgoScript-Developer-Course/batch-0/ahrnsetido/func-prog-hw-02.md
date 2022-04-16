# Homework 2

## Recap basic ErgoScript and programming

### Example 1

This example contains simple Data Type definitions
- [x] Long
- [x] Byte

```scala 
  val ErgToNanoErg: Long = 1000000000L
  val tokenPriceErg: Long = 20 * ErgToNanoErg
  val thisYear: Byte = 2022
  val thisMonth: Byte = 4
```
### Example 2

This example checks if the value in the input box INPUTS(0) is larger than tokenPriceErg (20 Erg), and contains:
- [x] If else
- [x] Boolean

```scala 
  val userBox: Box = INPUTS(0)

  if(userBox.value > tokenPriceErg ){
    val enoughErg: Boolean = true
  }else{
    val enoughErg: Boolean = false
  }

```

### Example 3

This examples defines two collections and maps them into a collection of pairs. It contains:
- [x] Collections
- [x] Pairs (inside a collection)
- [x] Arbitrary data types using map function
- [x] Lambda expression (=>)

```scala 
  val weekdaysShort: Coll[string] = Coll("Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun")
  val weekdaysNum: Coll[Int] = Coll(1,2,3,4,5,6,7)

  val weekdaysMap: Coll[(Int,string)] = {
    weekdaysNum.map{
      (myDay: Int) =>
        (myDay, weekdaysShort(myDay-1))     //Coll starts at 0
    }
  }
  //expected: Coll[(Int, string)] = [(1,"Mon"),(2,"Tue"),(3,"Wed")...
```

### Example 4

This example compares the implementation a *def* and a *val statement* for a function that checks if it's a leap year. It contains:
- [x] val statement
- [x] def statement

```scala 
  def isLeapYearDef(myYear: Int): Boolean = {
    val isLeapyear: Boolean = (myYear.toLong % 4) == 0
  }
```

```scala 
  val isLeapYearVal: Boolean = {
    (myYear:Int) => (myYear.toLong % 4) == 0
  }
```

### Example 5

This example illustrates how SigmaProps are used to define spending conditions for a Smart Contract.  It contains:
- [x] SigmaProps


```scala 
{
  val ownerPK: SigmaProp   = PK(ownerPKString) //Public key of owner 1
  val owner2PK: SigmaProp  = PK(ownerPKString2) //Public key of owner 2
  val thisYear: Int        = INPUTS(0).R4[Int].get //current year is in R4 of INPUT(0)

  // define val statement for leap year check 
  val isLeapYearVal: Boolean = {
    (myYear:Int) => (myYear.toLong % 4) == 0
  }

  // boolean to show if it's a leap year
  val isRegLeapYear: Boolean = isLeapYearVal(thisYear)

  //ownerPK can always spend this box, owner 2 can only spend it during a leap year
  (SigmaProp(isRegLeapYear) && owner2PK) || ownerPK
}
```
