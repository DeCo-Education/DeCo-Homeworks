# Pooled NFT purchase

A group of friends decides to purchase an NFT but only together they have enough ERGs to do that.
They decide to pool their funds and store the NFT in a box protected with a threshold signature.


## Transactions
<table>
<th></th>
<th> Inputs</th>
<th>Outputs</th>
<tr>
   <td>1</td>
   <td>
      <table>
         <th>Single friend's wallet boxes</th>
      </table>
   </td>
   <td>
      <table>
         <th colspan="2">Pooling box</th>
         <tr>
            <td colspan="2">A single box that can be used in combination with </br>other friends' pooling boxes to purchase an NFT</br> after the sum of pooling boxes values </br>will reach at least NFT price + txFee + NFT box min value</td>
         </tr>
         <tr>
            <td>R4</td>
            <td>PK(sending_friend)</td>
         </tr>
         <tr>
            <td>Script </td>
            <td> Pooling box script</td>
         </tr>
      </table>
      </br>
      <table>
         <th>Transaction fee</th>
      </table>
      <table>
         <th>Change</th>
      </table>
</tr>
<tr>
   <td>2</td>
   <td>
      <table>
         <th>Pooling boxes</th>
      </table>
      <table>
         <th colspan="2">NFT seller's box</th>
         <tr>
            <td>Tokens</td>
            <td>NFT to sell</td>
         </tr>
         <tr>
            <td>Script</td>
            <td>NFT seller's box script</td>
         </tr>
      </table>
   </td>
   <td>
      <table>
         <th colspan="2">NFT box</th>
         <tr>
            <td>Tokens</td>
            <td>NFT to sell</td>
         </tr>
         <tr>
            <td>Script</td>
            <td>Threshold signature</td>
         </tr>
      </table>
      <table>
         <th colspan="2">Seller's income box</th>
         <tr>
            <td>Value</td>
            <td>NFT price</td>
         </tr>
         <tr>
            <td>Script</td>
            <td>PK(NFT_seller)</td>
         </tr>
      </table>
      <table>
         <th>Transaction fee</th>
      </table>
	  </tr>
</table>



## Scripts
### Pooling box script
```
{
	val refund: SigmaProp = SELF.R4[SigmaProp].get //creator's PK

	val friendsNeededToSpendNFTbox: Int = getVar[Int](0).get
	val NFTid: Coll[Byte] = getVar[Coll[Byte]](1).get
	val friendsPKs: Coll[SigmaProp] = getVar[Coll[SigmaProp]](2).get

	val thresholdSignature: SigmaProp = atLeast(friendsNeededToSpendNFTbox, friendsPKs)

	val NFTbought: SigmaProp = OUTPUTS.exists { (out: Box) =>
		out.tokens(0)._1 == NFTid &&
		out.tokens(0)._2 == 1 &&
		out.propositionBytes == thresholdSignature.propBytes
	}

	NFTbought || refund
}
```

### NFT seller's box script
```
{
	val NFTprice: Long = getVar[Long](0).get
	val sellerPK: SigmaProp = getVar[SigmaProp](1).get

	val NFTsold: SigmaProp = OUTPUTS.exists { (out: Box) =>
		out.value >= NFTprice &&
		out.propositionBytes == sellerPK.propBytes
	}
	NFTsold || sellerPK
}
```
