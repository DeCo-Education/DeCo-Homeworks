
# Recap Of Multi-Stage Transactions And Boxes

## Goal ##

Send funds to an address controlled by multiple parties.  Then widthdraw those funds from the multisig wallet. 

### Purpose ###

Multsig wallets are often used when an entity recives crypto. A good example would be for crowd funding. \
Rather than sending ERG to an address, it is sent to a script controlled by several addresses. 

### Parties involved ###

- Sender address
- Script address
- Controller A address
- Controller B address
- Withdrawal address

### Transaction overview ###

- Sender address will send ERG to the script address
- The box controlled by the script will be spent by withdrawing ERG to the withdrawal address 

- Sender transaction
     - Sender wants to sent a certain amount of ERG
     - An unspent box, box 1, will be used as input for the transaction as long as it has the specified amount of ERG
     - Box 2 is generated as an output with the specificed script 


- Withdrawal transaction
     - ERG from the script will be withdrawn 
     - Box 2 will be used as an input 
     - Box 3 is generated as an output


### Box information ###

- Box 1
   - Registers
       - R0
          - (amount ERG sent)(1000^3)
       - R1
          - Default guard script which only allows private key of sender to spend the box

- Box 2
   - Registers
       - R0
          - (amount ERG sent)(1000^3) - (minFee)
       - R1
          - Ergo script contract which requires the private key of both controller a and b to spend the box

- Box 3
   - Registers
       - R0
          - (amount ERG sent)(1000^3) - (2*minFee)
       - R1
          - Default guard script which only allows private key of sender to spend the box



### Box 2 Contract ###

```
{
  val signerIsA: SigmaProp = PK("9gvdGk47es9tpA5jjwZT4FaKMFFFh75usf4h4KSK3ehYP2ZjkFh")
  val signerIsB: SigmaProp = PK("9i1hJHAcR1aWqsc6aYWwNb3UABjvPCPQt4qKkuZ7ukbPedJPUCa")

  signerIsA && signerIsB

}
```
    




