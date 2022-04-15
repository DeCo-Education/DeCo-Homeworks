### Booleans, if/else, pair
```scala
{
    val whichOutputHasMoreErgs = SELF.R4[Int].get
    val outValues: (Long, Long) = (OUTPUTS(0).value, OUTPUTS(1).value)
    val firstHasMore: Boolean = outValues._1 > outValues._2
    if (whichOutputHasMoreErgs == 0)
        sigmaProp(firstHasMore)
    else if (whichOutputHasMoreErgs == 1)
        sigmaProp(!firstHasMore)
    else
        sigmaProp(1==1)
}
```
### Coll, lambda, Long
```scala
{
    val twoInputs: Coll[Box] = Coll(INPUTS(0), INPUTS(1))
    val values: Coll[Long] = twoInputs.map{
        (input: Box) =>
            (input.value)
	}

    val doubled: Coll[Long] = values.map{
        (value: Long) =>
            (value * 2L)
    }
    val sum: Long = doubled.fold(0L, {
    (first: Long, second: Long) =>
        (first + second)
  })
   sigmaProp(OUTPUTS(0).value == sum)
}
```
### def
```scala
{
    def equalsTwo(number: Int) = number == 2
    sigmaProp(equalsTwo(getVar[Int](0).get))
}
```
### val
```scala
{
    val halve: Long ={
        (longToHalve: Long) =>
          longToHalve/2
    }
    val firstOutHalved: Boolean = OUTPUTS(0).value == halve(INPUTS(0).value)
    sigmaProp(firstOutHalved)
}
```
### Data type using coll + pair
```scala
{
    val tokensOut: Coll[(SigmaProp, (Coll[Byte], Long))] = Coll(
            (PK("9etXmP7D3ZkWssDopWcWkCPpjn22RVuEyXoFSbVPWAvvzDbcDXE"), (SELF.R4[Coll[Byte]].get, 1000L)),
            (PK("9etXmP7D3ZkWssDopWcWkCPpjn22RVuEyXoFSbVPWAvvzDbcDXE"), (SELF.R5[Coll[Byte]].get, 12L)))

    val out: Box = OUTPUTS(0)
    sigmaProp(
    allOf(Coll(
         out.propositionBytes == tokensOut(0)._1.propBytes,
         out.tokens(0)._1 == tokensOut(0)._2._1,
         out.tokens(0)._2 == tokensOut(0)._2._2
	 ))
    )
}
```
### SigmaProp, Byte
```scala
{
 val inputPropBytes: Coll[Byte] = INPUTS(100).propositionBytes
 sigmaProp(OUTPUTS(24).propositionBytes == inputPropBytes)
}
```
