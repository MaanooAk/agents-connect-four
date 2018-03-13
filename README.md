# Agents Connect Four

JADE agents competing in the connect four game.

## Agreement on first player

In order to avoid a trusted third agent to determine the first player, there is a protocol in place for the two agents to fairly decide on a first player.

#### Algorithm

- Both agents send a random seed integer to the other agent.
- From the xor of the two seeds a boolean is generated deteministicly.
- If the boolean is true then the player with the smaller seed is the first player, else the other player.

## Agents Protocol

*TODO fill out*

| Description | Performative | Content |
|---|---|---|
| Request game | Propose | game? |
| Accept game | Accept Proposal | accept |
| Accept Proposal | Reject Proposal | reject |
| Seed | Inform | *seed* |
| Move | Inform | *move* |

## Agents Strategies

*TODO fill out*

## Build and run

To build and run maven with a makefile wrapper is used:

```bash
# clean, compile and package
make clean
# start the platform
make main
# start the agents
make agents
```



