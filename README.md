# Amuyaña
![Amuyaña](http://i64.tinypic.com/24g3loz.png)

[Amuyaña](http://amuyaña.com) is a prototype software that creates visual representations of Systems with the formal logic axioms of the Dynamic Logic of the Contradictory. The word *amuyaña* is a term in Aymara that means "thinking", "reasoning" or "understanding".

It has two main purposes: Generate visual representations of the main concepts of the Logic of the Contradictory and maintain the statistical data and its analysis.

The development of this software parallels the progression of my understanding of the Logic of the Contradictory.

The first version released in 2016 was called Chuyma, but it would recreate the *transfinite* development of new branches of the Table of Deductions without considering the statistical aspect.

After testing that first release and trying to create logic systems, I realized that the code (written in Python) was not good enough to do what I was expecting (to print in the screen the same figures I draw by hand). That is why I started using Java (first Java Swing and later JavaFX) and renamed the software Amuyaña. One of my main concerns was the relation between the statistics (from the theory to its empirical implementation) and the definition of new elements of the CLS. Later I would get to the conclusion that if one wants to create a visual representation -as detailed as possible but also as simple as it can be-, there are actually three concepts which need to be defined:

- Table of Deductions
- Tridialectic Apparatus
- Space-Time Configuration

From a formal logic point of view, these three concepts are direct implications of the Fundamental Postulate of the Dynamic Logic of the Contradictory. In the current version of Amuyaña only the first is implemented, that is we can only create Table of Deductions.

It is worth emphazising the fact that these three concepts, which I claim are sufficient to create a detailed representation of a CLS, are deduced, implied, from one axiom only, called by Stéphane Lupasco (1951) the Fundamental Postulate of the Dynamic Logic of the Contradictory. This fundamental Postulate describes how and why the energy orientates to the three polarities, or *devenir*, the eventualities (the thee Contradictional conjunctions) can happen and actually do happen.

Amuyaña has the final objective of serving as a tool for organizing and analysing information in scientific investigations, specifically when the Logic of the Contradictory is required, for example in a methodology of visualisatio of the (social) Reciprocity in a comunity or society.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* JDK11
* JavaFX11
* Maven 3.5.2 (maybe older versions work, I don't know)
* Only dependency of the project is MySQL connector

### Installing

Clone the project.

### Compilation and execution

```
mvn compile exec:java
```

### Package

```
mvn package
```

## Deployment

The pom.xml maven file will package Amuyaña for Linux, Windows and Macintosh computers. The users must have installed JDK11 though.

## Contributing

Any contribution is welcome, please write info@amuyaña.com to know how.

## Author

**Ayar Waman Portugal Michaux** - *Initial work* - [Academia.edu website](https://independent.academia.edu/AyarPortugal)

## License

Amuyaña is distributed under the General Public Licence version 3.

## Acknowledgments

* Thanks to my mother
* To all the contributors to the StackOverflow network, for their free codes (too many to count)
