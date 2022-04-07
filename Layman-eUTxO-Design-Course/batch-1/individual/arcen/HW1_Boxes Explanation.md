# Boxes Understanding

Each Box in Ergo is broken down to the following: Assets, Data, and Script. The Script part is the Smart Contract. Each Script consists of logic that will evaluate to a boolean, true or false.

Each Box has a script that corresponds to a wallets public key. In order to use these boxes for a transaction you must provide a signed message proving you are the owner of these boxes. Only way to sign is if you have the private key.

A Box is made of 10 registers. The first 4 registers are reserved for the following mandatory fields:

    1) Value - in NanoErgs
    2) Guard Script - Smart Contract
    3) Assets - tokens and I am assuming NFTs and such..
    4) Creation Info - txId(identifier that created the box) and output index.

The remaining 6 registers are optional. They are used for optional custom data used in smart contracts.

Note - Registers must be densely packed...meaning you can't have set register 5, 7, and 9...which means you have registers 6 and 8 empty.