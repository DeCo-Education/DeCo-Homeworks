### Ergoscript code for futures contract (Box)


```scala
{
//-----------Contract guards Future contract box
// and has the following spending paths:
//  SP1 Trader 1 (opener) sets contract - openContract
//  SP2 Trader 2 (funder) funds contract - fundContract
//  SP3 Contract expires - executeContract
//----------------------------------------------------------


//--------------------------------------------
// R4 contains future contract data: (JobInfo)
// Coll(long, Coll[Byte]) with:
//    (0):long JobID
//    (1):Coll[Byte] status
//--------------------------------------------

// assume variables coming from off-chain dapp?  
val jobID: long = 1234567L

//some can be hardcoded (?)
val statusEmpty: Coll[Byte] = fromBase64("empty")
val statusOpened: Coll[Byte] = fromBase64("opened")
val statusFunded: Coll[Byte] = fromBase64("funded")

val statusEmptyCheck: Boolean = SELF.R4(1)[Cool[Byte]].get == statusEmpty
val statusOpenedCheck: Boolean = SELF.R4(1)[Cool[Byte]].get == statusOpened
val statusFundedCheck: Boolean = SELF.R4(1)[Cool[Byte]].get == statusFunded
//val jobInfoIni: Coll(long, Coll[Byte]) = (jobID, status)

//--------------------------------------------
// R5 contains Opening info (OpenInfo)
// Coll(long,long,long, long, Coll[Byte], SigmaProp)
//    (0):int expirydate (in heights?)
//    (1):long exchangeRate
//    (2):long amountProvided of provided token in nanoErg
//    (3):long amountNeeded of wanted token in sigUSD
//    (4):Coll[Byte] tokenID of token wanted
//    (5):SigmaProp of openerpk (propositionBytes)
//--------------------------------------------
val ErgInNanoErg: long = 1000000000L
val miningFee: long = 0.001L * ErgInNanoErg

// ----- setting userBox pk
if(INPUTS(1).exists) {
  val userBox1: Box = INPUTS(1)
  val userBox1pk: SigmaProp = PK(userBox1.propBytes)
}
else
{
  val noUserBox1 == true
}

val expirydate: int = 1000000L
val exchangeRate: long = 5 
val amountProvided: long = 20 * ErgInNanoErg
val amountNeeded: long = exchangeRate * amountProvided / ErgInNanoErg // i.e. 5*20 nE / nE = 100sigUSD
val tokenID1: Coll[Byte] = fromBase58("1111111111111111111111111111111111")
val openerpk: SigmaProp = userBox1pk

val openInfo: Coll(int, long, long, long, Coll[Byte], SigmaProp) = (
  expirydate,exchangeRate, amountProvided, amountNeeded, tokenID1, openerpk)

//--------------------------------------------
// R6 contains Funding info (FundInfo)
// Coll(Boolean, SigmaProp)
//    (0):Boolean funded
//    (1):SigmaProp funderpk
//--------------------------------------------

val funded:Boolean = false 
val funderpk: SigmaProp = userBox1pk
val FundInfo: Coll(Boolean, SigmaProp) = (funded, funderpk)

//------ requires FCB in OUTPUT(0) - only requires the contract to be the same, but not R1-9!
val fcbOutputCheck:Boolean = (OUTPUTS(0).propositionBytes == SELF.propositionBytes)

// checks validity in openInfo Collection
val openInfoCheck: Boolean = allOf(Coll(
  openInfo(0) > CONTEXT.HEIGHT, 
  openInfo(3) <= openInfo(1)*openInfo(2)/ErgInNanoErg,
  openInfo(5).propBytes == userBox1.propBytes
))

//setting futureContractBox val
val futureContractBox: Box = OUTPUTS(0)

//-------------------------------------------
// ---- conditions for Opening Contract SP
//-------------------------------------------
val openContract: Boolean = allOf(Coll(

// require Output(0) to be FCB
  fcbOutputCheck,
// require updated jobID and status in R4, check status was empty
  futureContractBox.R4(0)[long].get == jobID,
  futureContractBox.R4(1)[Coll[Byte]].get == statusOpened,
  statusEmptyCheck,

// require FCB to have provided amount of ERG + miningFee for expiration tx
  futureContractBox.value == amountProvided + miningFee,

// fill in OpenInfo into R5
  openInfoCheck,
  futureContractBox.R5[Coll(int, long, long, long, Coll[Byte], SigmaProp)].get == OpenInfo
// can force full Collection? can specify type like this?

))

//-------------------------------------------
// ---- Funding contract SP
//-------------------------------------------

// --- jobID should be the same
val jobIDcheck: Boolean = (SELF.R4(0)[long].get == OUTPUTS(0).R4(0)[long].get)

// get OpenInfo from current box
val requestedTokenID = SELF.R5(4)[Coll[Byte]].get
val tokenAmountNeeded = SELF.R5(3)[long].get

//check if funder has correct tokens
val funderHasTokensCheck: Boolean = userBox1.tokens(0)._1.get == requestedTokenID
 //how to make sure data type is correct?

// check if only partial fund 
val partialfund: Boolean = (userBox1.tokens(0)._2.get < tokenAmountNeeded)

if(partialfund) {
  val getAllTokens: Boolean = (futureContractBox.tokens(0)._2.get == userBox1.tokens(0)._2.get)
}
else
{
  val fullyfunded: Boolean = futureContractBox.tokens(0)._2.get == tokenAmountNeeded
} // assuming change excess tokens are sent back to funder

//-------------------------------------------
// ---- conditions for Funding contract SP
//-------------------------------------------
val fundContract: Boolean = allOf(Coll(

// require Output(0) to be FCB and having the same job ID
  fcbOutputCheck,
  jobIDcheck,
  statusOpenedCheck,
// require updated status in R4
  futureContractBox.R4(1).get == statusFunded,

// require ERG stay the same in FCB + miningFee for expiration tx
  futureContractBox.value == futureContractBox.value + miningFee,

// require funder to have correct tokens
  funderHasTokensCheck,
// either funder provided less than amount needed or he fully funded it
  partialfund && getAllTokens || fullyfunded,

// require setting FundInfo (values from off-chain dapp?)
  futureContractBox.R6[Coll[long,SigmaProp]].get == FundInfo
))

//-------------------------------------------
// ---- conditions for expiring contract SP
//-------------------------------------------

// set values from FCB contract
val expiryInFCB: int = SELF.R5(0)[int].get
val xrateInFCB: long = SELF.R5(1)[long].get
val amountProvInFCB: long = SELF.R5(2)[long].get //in nanoErg
val amountNeededInFCB: long = SELF.R5(3)[long].get // in SigUSD
val tokenID1InFCB: Coll[Byte] = SELF.R5(4)[Coll[Byte]].get
val openerpkInFCB: SigmaProp = SELF.R5(5)[SigmaProp].get

// OUTPUT 1
val OpenerBox: Box = OUTPUT(1)

// checking if FCB was funded
val hasfunds: Boolean = SELF.R6(1)[Boolean].get
if (hasfunds) {
  val funderpkInFCB: SigmaProp = SELF.R6(2)[SigmaProp].get
}

//-------------------------------------------
// ----- Collection of Booleans for a refund (non-funded contract)
//-------------------------------------------
val refund: Boolean = allOf(Coll(
  hasfunds == false,
  OpenerBox.value == amountProvInFCB 
))

//-------------------------------------------
// ----- Collection of Booleans when funded
//-------------------------------------------
val payout: Boolean = allOf(Coll(
  hasfunds,
  // Erg is sent to funder, but calculated from sigUSD amount Needed value and exchangerate
  OUTPUT(2).propositionBytes == funderpkInFCB.propBytes,
  OUTPUT(2).value == amountNeededInFCB / xrateInFCB * ErgInNanoErg, //in nanoErgs

  // all tokens in FCB are sent to Openerpk
  OpenerBox.tokens(0)._1.get == tokenID1InFCB, // in sigUSD
  OpenerBox.tokens(0)._2.get == SELF.tokens(0)._2.get,

  //leave min ERG in FCB, rest will go to OpenerBox
  futureContractBox.value == miningFee 
))

//-------------------------------------------
// ---- conditions for expiring contract SP
//-------------------------------------------
val executeContract: Boolean = allOf(Coll(
// require Output(0) to be FCB and having the same job ID //no need?
  fcbOutputCheck,
  jobIDcheck,
// check if height has expiried
  CONTEXT.HEIGHT > expiryInFCB,

// require OUTPUT(1) to be openerpk
  OpenerBox.propositionBytes == openerpkInFCB.propBytes,

  anyOf(Coll(
    refund,
    payout 
  )),

  futureContractBox.R4(1).get == statusEmpty
))

// should have one more SP when creating empty FCB?
sigmaProp(anyOf(Coll(
  openContract,
  fundContract,
  executeContract
)))
}
```
