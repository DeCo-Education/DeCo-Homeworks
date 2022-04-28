### Ergoscript code for futures contract (Box)


```scala
{
//-----------Contract guards Future contract box
// and has the following spending paths:
//  SP1 Trader 1 (opener) sets contract - createContract
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
//val statusCreated: Coll[Byte] = fromBase64("created")
val statusOpened: Coll[Byte] = fromBase64("opened")
val statusFunded: Coll[Byte] = fromBase64("funded")
val statusExpired: Coll[Byte] = fromBase64("expired")

//val jobInfoIni: Coll(long, Coll[Byte]) = (jobID, statusCreated)

//--------------------------------------------
// R5 contains Opening info (OpenInfo)
// Coll(long,long,long, long, Coll[Byte], SigmaProp)
//    (0):int expirydate (in heights?)
//    (1):long exchangeRate
//    (2):long amountProvided of provided token 
//    (3):long amountNeeded of wanted token 
//    (4):Coll[Byte] tokenID of token wanted
//    (5):SigmaProp openerpk
//--------------------------------------------
val ErgInNanoErg: long = 1000000000L
val miningFee: long = 0.001L * ErgInNanoErg

val expirydate: int = 1000000L
val exchangeRate: long = 5 
val amountProvided: long = 20 * ErgInNanoErg
val amountNeeded: long = exchangeRate * amountProvided
val tokenID1: Coll[Byte] = fromBase58("1111111111111111111111111111111111")
val openerpk: SigmaProp = PK(openerpkstring)

val openInfo: Coll(long, long, long, long, Coll[Byte], SigmaProp) = (
  expirydate,exchangeRate, amountProvided, amountNeeded, tokenID1, openerpk)

//--------------------------------------------
// R6 contains Funding info (FundInfo)
// Coll(Boolean, SigmaProp)
//    (0):Boolean funded
//    (1):SigmaProp funderpk
//--------------------------------------------

val funded:Boolean = false
val funderpk: SigmaProp = PK(funderpkstring)
val FundInfo: Coll(Boolean, SigmaProp) = (funded, funderpk)

// --- jobID should be already set, or set it (?)
val jobIDcheck: Boolean = (SELF.R4(0)[long].isdefined) || OUTPUTS(0).R4(0)[long].get == jobID)

//------ requires FCB in OUTPUT(0) - only requires the contract to be the same, but R1-9?
val fcbOutputCheck:Boolean = (OUTPUTS(0).propositionBytes == SELF.propositionBytes) && jobIDcheck

//setting futureContractBox val
val futureContractBox: Box = OUTPUTS(0)

//-------------------------------------------
// ---- conditions for Opening Contract SP
//-------------------------------------------
val createContract: Boolean = allOf(Coll(

// require Output(0) to be FCB
  fcbOutputCheck

// require FCB to have provided amount of ERG + miningFee for expiration tx
  futureContractBox.value == amountProvided + miningFee

// fill in OpenInfo into R5
  futureContractBox.R5[Coll(long, long, long, long, Coll[Byte], SigmaProp)].get == OpenInfo
// can force full Collection? can specify type like this?

// require updated status in R4
  futureContractBox.R4(1)[Coll[Byte]].get == statusOpened
))

//-------------------------------------------
// ---- conditions for Funding contract SP
//-------------------------------------------

// ----- setting userBox value
val userBox1: Box = INPUTS(1)
//what happens in expiry SP, when there's no INPUTS(1)?

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

val fundContract: Boolean = allOf(Coll(

// require Output(0) to be FCB and having the same job ID
  fcbOutputCheck,

// require ERG stay the same in FCB + miningFee for expiration tx
  futureContractBox.value == futureContractBox.value + miningFee,

// require funder to have correct tokens
  funderHasTokensCheck,
// either funder provided less than amount needed or he fully funded it
  partialfund && getAllTokens || fullyfunded,

// require updated status in R4
  futureContractBox.R4(1).get == statusFunded,
// require setting FundInfo (values from off-chain dapp?)
  futureContractBox.R6[Coll[long,SigmaProp]].get == FundInfo
))

//-------------------------------------------
// ---- conditions for expiring contract SP
//-------------------------------------------

// set values from FCB contract
val expiryInFCB: int = SELF.R5(0)[int].get
val xrateInFCB: int = SELF.R5(1)[long].get
val amountProvInFCB: int = SELF.R5(2)[long].get
val amountNeededInFCB: int = SELF.R5(3)[long].get
val tokenID1InFCB: Coll[Byte] = SELF.R5(4)[Coll[Byte]].get
val openerpkInFCB: SigmaProp = SELF.R5(5)[SigmaProp].get

// OUTPUT 1
val OpenerBox: Box = OUTPUT(1)

// checking if FCB was funded
val hasfunds: Boolean = SELF.R6(1)[Boolean].get
if (hasfunds) {
val funderpkInFCB: SigmaProp = SELF.R6(2)[SigmaProp].get
}

// Collection of Booleans for a refund (non-funded contract)
val refund: Boolean = allOf(Coll(
  hasfunds == false,
  OpenerBox.value == amountProvInFCB 
))

// Collection of Booleans when funded
val payout: Boolean = allOf(Coll(
  hasfunds,
  // ERG is sent to funder
  OUTPUT(2).propositionBytes == funderpkInFCB.propBytes,
  OUTPUT(2).value == amountProvInFCB,

  // all tokens in FCB are sent to Openerpk
  OpenerBox.tokens(0)._1.get == tokenID1InFCB,
  OpenerBox.tokens(0)._2.get == SELF.tokens(0)._2.get,

  //leave min ERG in FCB, rest will go to OpenerBox
  futureContractBox.value == miningFee
))

val executeContract: Boolean = allOf(Coll(
// require Output(0) to be FCB and having the same job ID //no need?
  fcbOutputCheck,
// check if height has expiried
  CONTEXT.HEIGHT > expiryInFCB,

// require OUTPUT(1) to be openerpk
  OpenerBox.propositionBytes == openerpkInFCB.propBytes,

  anyOf(Coll(
    refund,
    payout 
  )),

  futureContractBox.R4(1).get == statusExpired
))

sigmaProp(anyOf(Coll(
  createContract,
  fundContract,
  executeContract
)))
}
```
