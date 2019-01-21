# special-octo-garbanzo -- easumé
The process of creating a good looking resume or CV might be tiring if using \LaTeX. This app will focus on how to automate that activity.

# General Todo List:
- [x] Plan the initial structure
- [x] Come up with a better name: easumé (as in easy + résumé)
- [ ] Text version
- [x] Check user input
- [x] Delete generated files (aux and log)
- [x] Delete tex file and check for notNull
- [ ] Make templates customizable by the user
- [ ] GUI version
- [ ] Move to Android

# Important decisions along the way:
##### About not having tests:
There are no tests for the functionality of the application. This is because I find it really hard to write unit tests for an application that should take some input from the user. Also, I would rather not play around with the creation/deletion of files.
When created, all parts worked together in a nice way and, because I do not offer a too complex problem and the space of what might go wrong is not that big, I will limit myself to not creating any tests until further notice.

##### About the creation of templates
The customisable details can have one of the following shapes:
* !(NAME)
* !(NAME,R)
Really, the only difference is that the second one means the detail can be repeated as many time as the user wants. Everything until the closing matching !() will be repeated.