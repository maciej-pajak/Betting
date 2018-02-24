# Betting

Final project for CodersLab (Amelco) coding bootcamp. This project implements few core functionalities for online betting industry.
Technologies used: Spring Boot, Spring Data, Hibernate, Spring Security (with oauth2), MySQL, Multithreading, JUnit.

## Implemented functionality

* Registering new user.
* Login via oauth2 - resource owner credentials grant.
* Placing both single and combined bets.
* Placing group bets with invited users.
* Defining win conditions for bets in flexible form, ex. *{game.gameFinalScore.partyOneScore} >  {game.gameFinalScore.partyTwoScore}*
* Resolving bets and paying out prizes.
* Transaction support.



## Demonstrational operation

To simulate real-world sport events special API end point (POST /demo/feed-event) has been implement which can be feed with simulated live events. 

```
{
  "eventType": "GAME_START",
  "gameId": 0,
  "message": "string",
  "time": "2018-02-24T15:34:10.434Z",
  "value": 0
}
```

Available event types are: GAME_START, GAME_END, GAME_PART_END, GAME_PART_START, PARTY_ONE_SCORED, PARTY_TWO_SCORED
The system will process those events and perform appropriate actions. (for instance feeding GAME_START event will result in changed status of game with specified id, and GAME_END event will cause system to resolve all bets placed for this game).


