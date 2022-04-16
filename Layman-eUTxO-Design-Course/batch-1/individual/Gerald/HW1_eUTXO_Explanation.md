# HW1 eUTXO Explanation
## Recap
- Read up on eUTXO, boxes & addresses
- Post a short summary of each

## Overview
### Introduction
In order to understand how blockchain works and is designed, we have to learn what is a blockchain paradigm. Blockchain paradigm is sort of like a design principle that the application layer built on top of it is designed according to it, in order to solve problems. Each paradigm gives you different ways in approaching the problems and they each have their pros & cons. In current scene, blockchain has been mainly dominated by two paradigms, ***Account-based Model*** in ETH and ***UTXO*** in BTC

### Account-Based
To understand account-based model, it can be viewed as a state machine that tracks every transactions of an account globally, is always **turing-complete** and computation is done externally. 

A good analogy I come across is that savings account is only capable of spending any amount of money up until how much it actually owns and this is actively monitored by bank services. For example, `account A` has $100 and it is only eligible to spend up to $100, anything more will require more deposits. Transaction that goes in and out, and deducing account balance is what smart contract does in account-based model blockchain.


### UTXO (Unspent Transition Output)
![Bitcoin](https://miro.medium.com/max/1400/0*1v3M5u7SFdpCB3Bk)

***UTXO*** is pretty much a big pool of boxes that each of them is:
- Immutable
- Can only be spent once
- Burn once they're spent
- Create a new box when they're spent
- Output of the current UTXO becomes input of this new box

To imagine this, think of a cash payment. When we only have ***$50*** note and looking to buy something that's ***$10***, we have to first pay with our ***$50*** then only receive ***$40*** worth of change.

## eUTXO (Extended Unspent Transition Output)
So what is eUTXO? We can summarize ***eUTXO*** as a more sophisticated version of ***UTXO***. What it has, compared to ***UTXO*** is registers that can help to store key infos within the same box (***UTXO***). It then allows these boxes to be spent in transaction of higher complexity, usually smart contract coupled with these specific infos.

## Box

<figure>
  <img
  src="https://raw.githubusercontent.com/Emurgo/Emurgo-Research/master/smart-contracts/images/UTXO.jpg"
  alt="UTXO">
  <figcaption>UTXO box content, credits to Robert Kornacki</figcaption>
</figure>


Box is a keyword that we discuss frequently here. So box can be seen as a block of infos available in a pool. With registers, box is capable of storing infos such as:
- Asset (token)
- Guard scripts (Smart contract)
- Data (Oracle?)
- Others, like metadata

A box is protected by guard scripts (smart contract). Only when smart contract is evaluated to `True`, the box is accessible. 

In ERGO, we also have `read-only inputs` box. This allows any other ***UTXOs*** to refer to this box for infos, and the box **is not destroyed** after the read process because **no transaction occurs**. This gives benefits like
- Greater parallelisation due to data being generated in parallel to ***UTXOs*** transactions and allowing parallel reading from ***UTXOs***
- Reduced gas fees since the box is repetitively used and not destroyed

## Addresses
Address is an **unique hashes** and it defines a smart contract whether it **locates where is the smart contract** or it is the **serialised form of smart contract**. It serves as an identifier to tell us how to find or execute dedicated smart contract.

In blockchain, one of the core functionalities it has is aggregating the total balance of a wallet. A wallet can have multiple addresses as long as their smart contract executes using inputs from UTXOs and return `True` , then we know these ***UTXOs*** are owned by the same wallet.