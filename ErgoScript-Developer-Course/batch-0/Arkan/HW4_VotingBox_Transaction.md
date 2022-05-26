## Voting box spending transaction

Example transaction of adding a new user using a voting box (without a LETS box for the new user).

Transaction: [05773b4add7fe8ff3db7b35dfdb1bb00a75dd792d4352342f4a9a7693597320b](https://testnet.ergoplatform.com/en/transactions/05773b4add7fe8ff3db7b35dfdb1bb00a75dd792d4352342f4a9a7693597320b)

Script address: [BrCm9RahNN9SfASE1n6MNSfVuoWeGwojSawoMVytBGpzeAVUu1Ko4x6TSfCZhYo2jqUxpgzir8JroRPPJRawFWf8MjTbmFSQNM1caAn4yTf9AG2F9bhb6c5Vhzx5gLyzQ5mE5yRPXRY9rmwsTo8erZ8tQjQNxJJRKs33VD3VovNb7Zs11JZc4EMPTXAweqW8dUdMg338LHwTT3kc4TMUTb2ncTFJkXPxYqRi1Cixap3WdV3tRxUmtHRyHoZeKnngvZzexKipmgj9eeAXtnwJrnvGaQ7GMGJgRHGnfJRTzgh77nz8XjY1pdFjSTqeNK6tJiqHNo1Qc8mqEQ8k9xCxpkzcBKobJQ78qBVQXYoeqkRCRfXFPbaWeMeWXU19i2iRjJNsaFazDQjma5SmmcdU7fWhDhGJ7vwkFMLkQQNMkq84jqtCVpAdd8EyBK7ZWxKBKM9Ga8YfCzuT](https://testnet.ergoplatform.com/en/addresses/BrCm9RahNN9SfASE1n6MNSfVuoWeGwojSawoMVytBGpzeAVUu1Ko4x6TSfCZhYo2jqUxpgzir8JroRPPJRawFWf8MjTbmFSQNM1caAn4yTf9AG2F9bhb6c5Vhzx5gLyzQ5mE5yRPXRY9rmwsTo8erZ8tQjQNxJJRKs33VD3VovNb7Zs11JZc4EMPTXAweqW8dUdMg338LHwTT3kc4TMUTb2ncTFJkXPxYqRi1Cixap3WdV3tRxUmtHRyHoZeKnngvZzexKipmgj9eeAXtnwJrnvGaQ7GMGJgRHGnfJRTzgh77nz8XjY1pdFjSTqeNK6tJiqHNo1Qc8mqEQ8k9xCxpkzcBKobJQ78qBVQXYoeqkRCRfXFPbaWeMeWXU19i2iRjJNsaFazDQjma5SmmcdU7fWhDhGJ7vwkFMLkQQNMkq84jqtCVpAdd8EyBK7ZWxKBKM9Ga8YfCzuT)
### Script
```scala
{
    //Hard-coded constants
    //val managementBoxNftId: Coll[Byte]
    //
    //Registers
    //R4: SigmaProp - Owner's sigmaProp
    //R5: Coll[Coll[Byte]] - PropBytes of PKs that the owner votes for to be added to LETS
    //
    //Transactions
    //
    //Changing votes
    //INPUTS(0) - this box
    //INPUTS(1) - tx fee if needed
    //OUTPUTS(0) - this box with changed R5
    //
    //Adding new user
    //INPUTS(0) - management box
    //INPUTS(1 - n) - voting boxes
    //OUTPUTS(0) - management box
    //OUTPUTS(1) - new user LETS box
    //OUTPUTS(2) - new user voting box
    //OUTPUTS(3 - n + 2) - voting boxes

    val sameVoteBox: Boolean = {(outVoteBox: Box)=>
    allOf(Coll(
        SELF.propositionBytes == outVoteBox.propositionBytes,
        SELF.R4[SigmaProp].get == outVoteBox.R4[SigmaProp].get,
        SELF.tokens(0)._1 == outVoteBox.tokens(0)._1
        ))
    }

    val changeVotes: Boolean = OUTPUTS(0).R4[SigmaProp].isDefined && OUTPUTS(0).R4[SigmaProp].get == SELF.R4[SigmaProp].get
    if (changeVotes)
    {
        //User changes casted votes
        val outVoteBox: Box = OUTPUTS(0)
        val ownerVotes: SigmaProp = SELF.R4[SigmaProp].get && sigmaProp(sameVoteBox(outVoteBox))
        ownerVotes
    }
    else
    {
       //New user is being added to LETS
       val newUserLetsBox = OUTPUTS(1)
       val newUserPkBytes: Coll[Byte] = newUserLetsBox.R4[SigmaProp].get.propBytes
       val usersVotedFor: Coll[Coll[Byte]] = SELF.R5[Coll[Coll[Byte]]].get
       val votesAfterAddingUser: Coll[Coll[Byte]] = usersVotedFor.filter{(user: Coll[Byte]) => {user != newUserPkBytes}}
       val userBeingAddedInVotes: Boolean = votesAfterAddingUser.size == usersVotedFor.size - 1
       val voteBoxOutputExists: Boolean = OUTPUTS.exists{(out: Box) =>
          {
          if (out.R4[SigmaProp].isDefined && out.tokens.size > 0){
          allOf(Coll(
            sameVoteBox(out),
            if (SELF.R5[Coll[Coll[Byte]]].get.size > 1)
                {
                    out.R5[Coll[Coll[Byte]]].get == votesAfterAddingUser
                }
                else {
                    !(out.R5[Coll[Coll[Byte]]].isDefined)
                }
          ))
          }
          else false
          }
       }
       val managementBoxIsBeingSpent: Boolean = OUTPUTS(0).tokens(0)._1 == managementBoxNftId
       val voteTakesPlace: SigmaProp = sigmaProp(userBeingAddedInVotes && voteBoxOutputExists && managementBoxIsBeingSpent)
       voteTakesPlace
    }
}
```
