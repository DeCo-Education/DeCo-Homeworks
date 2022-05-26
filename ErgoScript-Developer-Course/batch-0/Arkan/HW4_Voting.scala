package appkitTest

import org.ergoplatform.appkit.{BlockchainContext, ConstantsBuilder, Eip4Token, ErgoProver, ErgoToken, ErgoType, ErgoValue, OutBox, Parameters, RestApiErgoClient, SecretString}
import org.ergoplatform.appkit.config.ErgoToolConfig
import special.collection.Builder.DefaultCollBuilder.fromArray
import special.collection.Coll

import scala.io.Source

object Voting extends App {
  val conf = ErgoToolConfig.load("ergotool.json")
  val node = conf.getNode
  val ergoClient = RestApiErgoClient.create(node, RestApiErgoClient.defaultTestnetExplorerUrl)

  ergoClient.execute((ctx: BlockchainContext) => {

    val prover: ErgoProver = ctx.newProverBuilder()
      .withMnemonic(
        SecretString.create(node.getWallet.getMnemonic),
        SecretString.empty()
      )
      .withEip3Secret(0)
      .build()

    val prover1 = ctx.newProverBuilder().withMnemonic(
      SecretString.create("slow silly start wash bundle suffer bulb ancient height spin express remind today effort helmet"), SecretString.empty() //very secret
    ).build()

    val inputBoxes = ctx.getWallet.getUnspentBoxes(Parameters.OneErg)

    val managementBox: OutBox = ctx.newTxBuilder().outBoxBuilder()
      .contract(ctx.compileContract(ConstantsBuilder.empty(),
        "{ sigmaProp(SELF.tokens(0)._1 == OUTPUTS(0).tokens(0)._1 && SELF.propositionBytes == OUTPUTS(0).propositionBytes)}")) //dummy script
      .value(Parameters.OneErg - Parameters.MinFee)
      .mintToken(new Eip4Token(inputBoxes.get().get(0).getId.toString, 1, "Management Box Test NFT", "MNG", 0))
      .build()


    val unsignedManagementBoxTx = ctx.newTxBuilder().boxesToSpend(inputBoxes.get())
      .outputs(managementBox)
      .fee(Parameters.MinFee)
      .sendChangeTo(prover.getP2PKAddress)
      .build()

    val signedManagementBoxTx = prover.sign(unsignedManagementBoxTx)
    println(ctx.sendTransaction(signedManagementBoxTx))

    val managementBoxOut = signedManagementBoxTx.getOutputsToSpend.get(0)

    val script = Source.fromFile("./votingBox.ergo").mkString
    val voteBoxContract = ctx.compileContract(ConstantsBuilder.create()
      .item("managementBoxNftId", managementBoxOut.getTokens.get(0).getId.getBytes)
      .build(), script)

    val managementBox2: OutBox = ctx.newTxBuilder().outBoxBuilder()
      .contract(ctx.compileContract(ConstantsBuilder.empty(),
        "{ sigmaProp(SELF.tokens(0)._1 == OUTPUTS(0).tokens(0)._1 && SELF.propositionBytes == OUTPUTS(0).propositionBytes)}"))
      .value(managementBoxOut.getValue - Parameters.MinFee)
      .tokens(managementBox.getTokens.get(0))
      .mintToken(new Eip4Token(managementBoxOut.getId.toString, 100, "Voting Test Token", "VOT", 0))
      .build()

    val votingTokenMintingTx = ctx.newTxBuilder().boxesToSpend(java.util.Arrays.asList(managementBoxOut))
      .outputs(managementBox2)
      .fee(Parameters.MinFee)
      .sendChangeTo(prover.getP2PKAddress)
      .build()

    val signedVotingTokenMintingTx = prover.sign(votingTokenMintingTx)
    println(ctx.sendTransaction(signedVotingTokenMintingTx))

    val managementOut2 = signedVotingTokenMintingTx.getOutputsToSpend.get(0)

    val tk = managementOut2.getTokens.get(1)
    val votingToken = new ErgoToken(tk.getId, 1L)

    val managementBox3: OutBox = ctx.newTxBuilder().outBoxBuilder()
      .contract(ctx.compileContract(ConstantsBuilder.empty(),
        "{ sigmaProp(SELF.tokens(0)._1 == OUTPUTS(0).tokens(0)._1 && SELF.propositionBytes == OUTPUTS(0).propositionBytes)}"))
      .value(managementBox2.getValue - Parameters.MinFee - Parameters.MinChangeValue)
      .tokens(managementOut2.getTokens.get(0), new ErgoToken(tk.getId, managementOut2.getTokens.get(1).getValue - 1))
      .build()


    val oneProverPk: Coll[Coll[Byte]] = fromArray(Array(ErgoValue.of(prover1.getAddress.toErgoContract.getErgoTree.bytes).getValue))
    val r5: ErgoValue[Coll[Coll[Byte]]] =
      ErgoValue.of(oneProverPk, ErgoType.collType(ErgoType.byteType()))

    val voteBox = ctx.newTxBuilder().outBoxBuilder()
      .contract(voteBoxContract)
      .tokens(votingToken)
      .registers(
        ErgoValue.of(prover.getAddress.getPublicKey),
        r5
      )
      .value(Parameters.MinChangeValue)
      .build()

    val votingBoxCreateTx = ctx.newTxBuilder().boxesToSpend(java.util.Arrays.asList(managementOut2))
      .outputs(managementBox3, voteBox)
      .fee(Parameters.MinFee)
      .sendChangeTo(prover.getP2PKAddress)
      .build()
    val votingBoxCreateSignedTx = prover.sign(votingBoxCreateTx)
    println(ctx.sendTransaction(votingBoxCreateSignedTx))
    val managementOut3 = votingBoxCreateSignedTx.getOutputsToSpend.get(0)
    val votingOut = votingBoxCreateSignedTx.getOutputsToSpend.get(1)

    val voteBox2 = ctx.newTxBuilder().outBoxBuilder()
      .contract(voteBoxContract)
      .tokens(votingToken)
      .registers(
        ErgoValue.of(prover.getAddress.getPublicKey),
      )
      .value(Parameters.MinChangeValue)
      .build()

    val newVoteBox = ctx.newTxBuilder().outBoxBuilder()
      .contract(voteBoxContract)
      .tokens(votingToken)
      .registers(
        ErgoValue.of(prover1.getAddress.getPublicKey),
      )
      .value(Parameters.MinChangeValue)
      .build()

    val managementBox4: OutBox = ctx.newTxBuilder().outBoxBuilder()
      .contract(ctx.compileContract(ConstantsBuilder.empty(),
        "{ sigmaProp(SELF.tokens(0)._1 == OUTPUTS(0).tokens(0)._1 && SELF.propositionBytes == OUTPUTS(0).propositionBytes)}"))
      .value(managementBox3.getValue - Parameters.MinFee - Parameters.MinChangeValue)
      .tokens(managementOut3.getTokens.get(0), new ErgoToken(tk.getId, managementOut3.getTokens.get(1).getValue - 1))
      .build()

    val unsignedVotingTx = ctx.newTxBuilder().boxesToSpend(java.util.Arrays.asList(managementOut3, votingOut))
      .outputs(managementBox4, newVoteBox, voteBox2)
      .fee(Parameters.MinFee)
      .sendChangeTo(prover.getP2PKAddress)
      .build()

    val signedVotingTx = prover.sign(unsignedVotingTx)
    println(ctx.sendTransaction(signedVotingTx))


  })
}
