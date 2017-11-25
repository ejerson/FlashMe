## Flash Me
_Flash me_ is a a flash card generation and studying app 
that allows users to save time by optimizing the card creation process and
utilizing the power of our visual memories. It includes an implementation of Leitner
System, which is a way of enhancing retention using the concept of 
Spaced Repetition. 

For more information:
https://en.wikipedia.org/wiki/Leitner_system

### Why build this app?
I built this app for the sole purpose of saving time while creating flashcards.
I love using Anki as a way to create and study flash cards, but I disliked
the fact that I could not automatically search for images and use
them as memory aid from within Anki. 

I believe that any app that allows one to save time is a useful tool.
The Flash Me app falls into this category. At the same time, Flash Me
does not diminish or neglect to preserve a user's ability to interact
with the topic he or she is trying to master. A user is still
able to manually search for images and create the content, which
is essential in helping a user retain information.

### Current State
The time saving aspect of _Flash me_ lies in its built in functionality 
of automatically searching for images based on the user specified 
keyword/s, which is now fully functional. In addition, the deck and card creation process follows a specific order, which
add to the time saving capacity of _Flash me_. Currently, the scaled down implementation 
of the Leitner System is at its initial stages, and is functioning according to specifications. In
the future, the Leitner System will be fully implemented and be more robust. 
Aside from having to deal with the UX flow, the app itself is minimally viable. 

### Hardware and Software Specifications and Testing
Emulators 
* Nexus 5X API 22

Device 
* Samsung S7 Edge API 24
  
Orientation Restrictions
* The ImageActivity is restricted to Portrait orientation to solve
an issue with the Google Custom Search API disappearing on layout change. 
In addition, this decision is made to preserve the aesthetic feel of 
the search functionality. 

### Necessary Aesthetic Improvements
* Check buttons should have a white background with a line on top.
* The Review button and Level up button should only appear when 
the user checks each cards.

### Action Items
* Implement a daily limit on how many cards are being reviewed (30)
    * Each day a card pool will be populated with a specific amount of cards 
    from each card level 
      * 15 - Level 1
      * 10 - Level 2
      * 5 - Level 3
    * If a user fails to remember a card, demote card to level 1
    * If a user succeeds to remember a card, promote card to a higher level
 
### Stretch Goals
* Users can promote cards up to Level 7.
* Users can select multiple cards to edit sequentially, instead of the current 
functionality of users only able to select one card to edit at a time.
* App will have a way for users to see how many cards are in each level.
* Users will be able to specify a specific number of cards to review daily.
* User will be able to get a check mark for a given day if all card within
the daily limit pool are reviewed.
* Auto demote a card if a user fails to remember them at a given set of time.
* Users will be able to swipe instead of having to click the review button
to review a new card

  
  
## TODOS
* Add a link to generated Javadoc HTML pages in your repository.
* The project must include Javadoc documentation. 





