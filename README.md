JSerialize
==========

An University of Zielona Góra student's project about serialization to JSON format in Java language.

For contributors: follow to [WIKI](https://github.com/exesoft/JSerialize/wiki/)


The main idea is to have 3 classes: JSerializeWriter which is going to be a class where we load the object, go through all the fields, store the information about its type and value (with knowledge of possible circual references or references to same objects) and using JModel class provide a JSON representation of the data we receive in the process aswell as storing this information in given stream. JSerializeReader is going to reverse the process so it will instantiate an object with fields initialized with data based on stored JSON information translated by JModel class.


DEADLINE 14 JUNE 2013


_This github repository is public just because we are students and its free so dont bother to contribute._





