# HW1 eUTXO Explanation
## Recap
- Read up on **eUTXO, boxes & addresses**
- Post a **short summary** of each

## Overview
### Introduction
In order to understand how blockchain works and is designed, we have to learn what is a blockchain paradigm. Blockchain paradigm is sort of like a design principle that the application layer built on top of it is designed according to it, in order to solve problems. Each paradigm gives you different ways in approaching the problems and they each have their pros & cons. In current scene, blockchain has been mainly dominated by two paradigms, ***Account-based Model*** in ETH and ***UTXO*** in BTC

#### Account-Based
To understand account-based model, we can imagine going to McDonald's for lunc. We queue up at the big counter to wait for our turn. When it's our turn, we order what we need and pay with credit card. Then, we proceed to the next line to wait for the order completion

#### UTXO (Unspent Transtion Output)
UTXO is pretty much a big pool of boxes that each of them are:
- Immutable
- Can only be spent once
- Burn once they're spent
- Create a new box when they're spent
- Output of the current UTXO becomes input of this new box

### eUTXO
So what is eUTXO? We can summarize ***eUTXO*** as a more sophisticated version of ***UTXO***. What it has compared to ***UTXO*** is registers that can help to store key informations within the same box (***UTXO***). It then allows these boxes to be spent in transaction of higher complexity, usually smart contract with the these specific informations stored.

### Box
Box is a keyword that we discuss frequently here. So box can be seen as a block of information available in a pool. With registers, box is capable of storing informations such as:
- Asset (token)
- Guard scripts (Smart contract)
- Data (Oracle?)
- Others, like metadata

A box is protected by guard scripts (smart contract). Only when smart contract is evaluated to `True`, the box is accessible. 

In ERGO, we also have `read-only inputs` box. This allows any other ***UTXOs*** to refer to this box for information, and the box **is not destroyed** after the read process because **no transaction occurs**. This gives benefits like
- Greater parallelisation due to data being generated in parallel to ***UTXOs*** transactions and allowing parallel reading from ***UTXOs***
- Reduced gas fees since the box is repetitively used and not destroyed

### Addresses
Address is an **unique hashes** and it defines a smart contract whether it **locates where is the smart contract** or it is the **serialised form of smart contract**. It serves as an identifier to tell us how to find or execute dedicated smart contract.

In blockchain, one of the core functionalities it has is aggregating the total balance of a wallet. A wallet can have multiple addresses as long as their smart contract executes using inputs from UTXOs and return `True` , then we know these ***UTXOs*** are owned by the same wallet.