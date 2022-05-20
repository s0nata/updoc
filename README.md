# upDoc

`upDoc` is an experimental comment inconsistency detection tool for Java source code. Given a file under change it checks whether the javadoc comments have been updated for the methods that were changed.

This repository contains:
- the source code of the tool
- replication instructions for the experiments from the [original publication at SCAM'20](http://scg.unibe.ch/archive/papers/Stul20b-InconsistentComments.pdf)

Cite the SCAM'20 publication as:

```
@inproceedings{upDoc2020,
  author    = {Nataliia Stulova and
               Arianna Blasi and
               Alessandra Gorla and
               Oscar Nierstrasz},
  title     = {Towards Detecting Inconsistent Comments in Java Source Code Automatically},
  booktitle = {20th {IEEE} International Working Conference on Source Code Analysis
               and Manipulation, {SCAM} 2020, Adelaide, Australia,
               September 28 - October 2, 2020},
  pages     = {65--69},
  publisher = {{IEEE}},
  year      = {2020},
  url       = {https://doi.org/10.1109/SCAM51674.2020.00012},
  doi       = {10.1109/SCAM51674.2020.00012},
}
```

Please see below for the experiment replication instructions.

## Build

### Requisites

#### Java

`upDoc` is written in Java 11 and developed with `java-11-openjdk-amd64`.


### Compiling and building the sources

To compile the sources execute: `$ mvn clean compile`

To build a jar execute: `$ mvn package`

## Test and Run

### IDE configurations

This repository stores several IntelliJ IDEA run configurations to perform
various tasks:

- `upDoc analysis:mapping` runs `upDoc` Mapper component on an example input
- `upDoc [clean,compile]` compiles the sources
- `upDoc [test]` runs unit tests

These configurations may be picked up automatically by the IDE or not (depending
by the IntelliJ version you are using):
- if your IntelliJ version is using the `.run/` folder, great: configurations are exactly there, hence they should be visible in your IDE!
- otherwise, your IntelliJ version is using the `.idea/runConfigurations` folder to pick up configurations: copy the files in `.run/` to this other folder to see them in your IDE.

###  Unit testing

Use maven to run the unit tests: `$ mvn test`


### Code-comment mapping demo

To see how `upDoc` maps code to comments, you can run these commands:

1) execution from the sources

`mvn exec:java -Dexec.mainClass="upDoc" -Dexec.args="analysis:mapping src/test/resources/paper-example/ FromAdaptiveIsomorphismInspectorFactory.java 0.2"`

2) executing the jar

`java -jar upDoc-1.0.jar analysis:mapping ../src/test/resources/paper-example/ FromAdaptiveIsomorphismInspectorFactory.java 0.2
`

The demo will run `upDoc` on a single method and report the similarities of the sentences of the javadoc comment to the method signature.

### Replicate the experimental results from the publication

#### Running example

Execute `$ mvn -Dtest=PaperExampleStatTest test`

#### Preliminary evaluation

Execute `$ mvn -Dtest=ICPC19StatsTest test -DuseWMD="true"`

Please note that while in the paper we talk about 67 changes, in the outputs you will read a total of 40. It is because some changes were essentially equivalent, and it was not worth it to write a test for each duplicate (check the [google doc](https://docs.google.com/spreadsheets/d/1maRH6YY0OVuKSB2ACDhrz1-CQCS7sRHHoaZJDvLWi2Y/edit?usp=sharing) for the manual ICPC19 dataset analysis, where we list al lthe 67 changes in question)
