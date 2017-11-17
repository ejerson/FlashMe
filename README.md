## Flash Me
_Flash me_ is a a flash card generation and studying app 
that allows users to save time by optimizing the card creation process and
utilizing the power of our visual memories. It is an implementation of Leitner
System, which is a way of enhancing retention using the concept of 
Spaced Repetition. 

For more information:
https://en.wikipedia.org/wiki/Leitner_system

### Functionality
The time saving aspect of _Flash me_ lies in its built in functionality 
of automatically searching for images based on the user specified 
keyword/s. In addition, the deck and card creation process follows a specific order, which
add to the time saving capacity of _Flash me_. Currently, the scaled down implementation 
of the Leitner System is at its initial stages, but should be online by November 25th. In
the future, the Leitner System will be fully implemented.

### Action Items
* Implement a daily limit on how many cards are being reviewed (30)
    * Each day a card pool will be populated with a specific amount of cards 
    from each card level 
      * 15 - Level 1
      * 10 - Level 2
      * 5 - Level 3
    * If a user fails to remember a card, demote card to level 1
    * If a user succeeds to remember a card, promote card to a higher level
 
* In the card review fragment, make sure that the review button, promote button,
and demote button only appears after the check button is clicked.

* Be able to search for images based on the value of the backside of a card.
    * Query Google Custom Search API

### Stretch Goals
* Users can select multiple cards to edit sequentially, instead of the current 
functionality of users only able to select one card to edit at a time.
* Users can promote cards up to Level 7.
* App will have a way for users to see how many cards are in each level.
* Users will be able to specify a specific number of cards to review daily.
* User will be able to get a check mark for a given day if all card within
the daily limit pool are reviewed.
* Auto demote a card if a user fails to remember them at a given set of time.
* Users will be able to swipe instead of having to click the review button
to review a new card

  
  






