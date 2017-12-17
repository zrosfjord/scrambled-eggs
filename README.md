# scrambled-eggs
A java program that utilizes Last FM's artist similarity feature in order to find the similar musicians between two user inputted ones.

When you run the program, a pleasant GUI appears which will allow you to pick your two artists and check to ensure they are valid. Your next options are for the minimum number of steps you want the program to take, and the final one is the number of similar arts it looks at, starting at the most similar. Once you hit the 'Enter' button, it will begin running my recursive function. 

<b>The check buttons must have been pressed, and the check boxes checked in order for the enter button to function</b>

The recursive function is designed so that it will look through the most similar until it hits the step minimum. If at that point it hasn't reached the top one, it will then backtrack through all the combinations of similar artists. If by the time it has exhausted every route, it will up the minimum number of steps and restart. This whole process can be quite lengthy at times, but luckily there is loading animation to keep you entertained.

This program is dependent on the Java bindings for the Last.fm Web Services which are available here https://github.com/jkovacs/lastfm-java

Anyone reading this, feel free to use my code in anyway. The only thing you would need to run this that I don't supply is a Last FM API Key.


Enjoy.
