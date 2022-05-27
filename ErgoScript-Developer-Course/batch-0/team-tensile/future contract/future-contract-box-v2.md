# Tensile - Future Trade Contract (ERG to tokenID)
## Registers
|R4: |funded| |
|---|---|---|
|Coll(|Boolean|)|

|R5:|expiryHeight| |
|--|--|--|
|Coll(|int|)|

|R6:|jobID|exRate|amountProv|amountNeed| |
|--|--|--|--|--|--|
|Coll(|long,|long,|long,|long|)|

|R7:|tokenID1|openerPK|funderPK| |
|--|--|--|--|--|
|Coll(|Coll[Byte],|Coll[Byte],|Coll[Byte]|)|

# Transactions
#### Opening a future trade contract (created by off-chain code)

|INPUTS:|T1|OUTPUTS:|FCB|T1|Fee|
|--|--|--|--|--|--|

Trigger: T1 (Opener) sends ERG and sets:
- jobID 
- expiryHeight
- exRate
- amountProv
- amountNeed
- tokenID1 
- openerPK

Conditions: all set variables valid

#### Funding existing future contract
|INPUTS:|FCB|T2|OUTPUTS:|FCB|T2|Fee|
|--|--|--|--|--|--|--|

Trigger: T2 (Funder) sends tokens with tokenID to an already opened contract and sets:
- R2: token ID and value
- funderPK

Spending Conditions:
- OUT(0):FCB keeps all ERG, 
- INPUTS=1, 
- token ID of T2 matches tokenID1, 
- amount of provided tokens by T2 > 0,
- if amount of tokens in T2 > amountNeed,
-   send excess tokens back to T2.
- set OUT(0):FCB as funded

#### Expiration of Contract
|INPUTS:|FCB|OUTPUTS:|T1|T2|Fee|
|--|--|--|--|--|--|
Trigger: 
- expiry date reached

Conditions if funded:
- calculate ERGs to send to funder.
- all tokens send to opener
- any remaining ERG send to opener

Conditions if not funded:
- ERG sent back to opener





