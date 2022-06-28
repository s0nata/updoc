
[![StandWithUkraine](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/badges/StandWithUkraine.svg)](https://github.com/vshymanskyy/StandWithUkraine/blob/main/docs/README.md)

[![SWUbanner](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/banner2-direct.svg)](https://github.com/vshymanskyy/StandWithUkraine/blob/main/docs/README.md)

[![Java Maven CI](https://github.com/s0nata/updoc/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/s0nata/updoc/actions/workflows/maven.yml)
[![CodeQL](https://github.com/s0nata/updoc/actions/workflows/codeql.yml/badge.svg?branch=main)](https://github.com/s0nata/updoc/actions/workflows/codeql.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/1c882d09db991fc3ce25/maintainability)](https://codeclimate.com/github/s0nata/updoc/maintainability)
[![codecov](https://codecov.io/gh/s0nata/updoc/branch/main/graph/badge.svg?token=D8QZL6JJUB)](https://codecov.io/gh/s0nata/updoc)
![Known Vulnerabilities](https://snyk.io/test/github/s0nata/updoc/badge.svg)



# upDoc

`upDoc` is an experimental comment inconsistency detection tool for Java source code. Given a file under change it checks whether the javadoc comments have been updated for the methods that were changed.

This repository contains:
- the source code of the tool prototype _(currently under development)_
- replication instructions for the experiments from the [original publication at SCAM'20](http://scg.unibe.ch/archive/papers/Stul20b-InconsistentComments.pdf)


## Build and Test

`upDoc` is written in Java 11 and developed with `java-11-openjdk-amd64`. We use `maven` as a build system:

| build phase(s) | result |
|-|-|
| `$ mvn clean compile` | compiles the source code |
| `$ mvn test `| runs unit tests |
| `$ mvn clean package` | produces a standalone executable JAR |
| `$ mvn clean package -DskipTests` | same as above, but not running the unit tests |

### GumTreeDiff

We rely on [`GumTreeDiff`](https://github.com/GumTreeDiff/gumtree#gumtree) in the `ChangeExtractor` component.

This dependency is handled by `maven` but additional configuration of your local working environment might be needed:


**Step 1**

Go to your account and generate a _personal access token_:

  1. Folow the instructions [here](https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token)

  2. In step 6 of the above instructions set the token name to `upDoc-GTD` or any other of your choice

  3. In step 7 of the above instructions set at least the following permissions: `repo`, `read:packages`

**Step 2**

Configure `maven` to authenticate GithubPackages as a valid package source:

  1. Full instructions are available [here](https://docs.github.com/en/packages/guides/configuring-apache-maven-for-use-with-github-packages#authenticating-with-a-personal-access-token), section _Authenticating to GitHub Packages/Authenticating with a personal access token_

  2. Create or edit the `~/.m2/settings.xml` from the template provided in the above instructions

  3. Update `repository` and `server` sections of the `settings.xml` to the following values:

```xml
<repository>
  <id>github</id>
  <name>GitHub GumTreeDiff Apache Maven Packages</name>
  <url>https://maven.pkg.github.com/gumtreediff/gumtree</url>
  <releases><enabled>true</enabled></releases>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
```

```xml
<server>
  <id>github</id>
  <username>HERE_YOUR_GITHUB_USERNAME</username>
  <password>HERE_YOUR_GITHUB_PERSONAL_ACCESS_TOKEN</password>
</server>
```

**Step 4**

The `pom.xml` of `upDoc` already has the dependencies from GithubPackages, you can extend them by checking out more `GumTreeDiff`'s packages from its full [package list](https://github.com/orgs/GumTreeDiff/packages).

After succesfully configuring your local `maven` instance you should be able to build `upDoc` following one of the flows described above.


### IntelliJ IDEA and Maven builds

Currently there is some kind of incompatibility between IntelliJ and `maven` third-party library indices. In case an import error for any library occurs, such as `java: package org.apache.commons.collections4.bag does not exist`, follow this [StackOverflow advice](https://stackoverflow.com/a/37601043/7819059):
- close the project
- delete the `.idea` directory, and
- reopen the project


## Demo

Currently there are four demos available, each demonstrating how one individual component of `upDoc` works. You can either run the demos from the command line or use one of the run configurations for IntelliJ IDEA.


| Component | Demo description| IDE run configuration 
|-|-|-|
| _Parser_ | prints out the coarse AST nodes extracted from the original source code and respective bag-of-words representations of identifiers | `demo:parsing` |
| _Mapper_ | prints out similarities between the method AST nodes (both signature and body) and individual sentences of the respective javadoc comment |`demo:mapping`
| _Change Extractor_| prints out a simple change description with LOCs and source code excerpts |`demo:change:extraction` |
| _Change Analyzer_ | prints out the complete report of an inconsistent code change, giving the details about sentences that should be updated| `demo:change:analysis` |

### CLI interface

`upDoc` takes the following strings as input arguments: `DEMO-NAME DIR FILE[S] [MAPPING-SENSITIVITY]`. Sample Java source files for the demos are stored in `src/main/resources/demo/` folder.


Running the demos after building the JAR file (e.g., from the `target` directory):

- `$ java -jar upDoc-1.1.0-alpha-jar-with-dependencies.jar demo:parsing ../src/main/resources/demo/ 0_AdaptiveIsomorphismInspectorFactory.java`
- `$ java -jar upDoc-1.1.0-alpha-jar-with-dependencies.jar demo:mapping ../src/main/resources/demo/ FromAdaptiveIsomorphismInspectorFactory.java 0.2`
- `$ java -jar upDoc-1.1.0-alpha-jar-with-dependencies.jar demo:change:extraction ../src/main/resources/demo/ 0_AdaptiveIsomorphismInspectorFactory.java 1_AdaptiveIsomorphismInspectorFactory.java`
- `$ java -jar upDoc-1.1.0-alpha-jar-with-dependencies.jar demo:change:analysis ../src/main/resources/demo/ 0_AdaptiveIsomorphismInspectorFactory.java 1_AdaptiveIsomorphismInspectorFactory.java`






Additionally, there are also IDE run configurations for building and tesing `upDoc`:

- `maven:[clean,compile]` compiles the sources
- `maven:[test]` runs all unit tests

IDE run configurations may be picked up automatically by the IDE or not (depending by the IntelliJ version you are using):
- if your IntelliJ version is using the `.run/` folder, great: configurations are exactly there, hence they should be visible in your IDE!
- otherwise, your IntelliJ version is using the `.idea/runConfigurations` folder to pick up configurations: copy the files in `.run/` to this other folder to see them in your IDE.

#### Tweaks/Troubleshooting

When switching to a newer version of one library (GumTree here in particular), IntelliJ _may_ still be trying to use the older cached `.jar` file. You may realize this is the issue if `mvn compile` is successfull but the code is not compiling in IntelliJ. 

**Fix** 
If you follow the error message with cmd+click on the line that does not compile, you should find where is this problematic `.jar`. You can safely delete it. Then, in the Project column (on the left of the IDE) do right click on `pom.xml` and `Maven -> Reimport`.



# Publications

## SCAM'20

`upDoc v1.0` was presented at the 20th IEEE International Working Conference on Source Code Analysis and Manipulation ([SCAM'20](https://www.ieee-scam.org/2020/)). Cite this publication as:

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

### Running the tool and replicating the experimental results from the publication 

#### Source code

[![DOI](https://zenodo.org/badge/493304392.svg)](https://zenodo.org/badge/latestdoi/493304392)

The source code of `upDoc v1.0` is available either directly from this repository as a [dedicated release](https://github.com/s0nata/updoc/releases/tag/v1.0) or from [Zenodo](https://zenodo.org/record/6566994).

#### Build instructions

Use maven to

- compile the sources execute: `$ mvn clean compile`

- build a jar execute: `$ mvn package`

- run the unit tests: `$ mvn test`

You can also use several IntelliJ IDEA run configurations to perform various tasks:

- `upDoc [clean,compile]` compiles the sources
- `upDoc [test]` runs unit tests
- `upDoc analysis:mapping` runs the code-comment mapping demo (see below)

These configurations may be picked up automatically by the IDE or not (depending on the IntelliJ version you are using):
- if your IntelliJ version is using the `.run/` folder, great: configurations are exactly there, hence they should be visible in your IDE!
- otherwise, your IntelliJ version is using the `.idea/runConfigurations` folder to pick up configurations: copy the files in `.run/` to this other folder to see them in your IDE.



#### Code-comment mapping demo


1) execution from the sources

`mvn exec:java -Dexec.mainClass="upDoc" -Dexec.args="analysis:mapping src/test/resources/paper-example/ FromAdaptiveIsomorphismInspectorFactory.java 0.2"`

2) executing the jar

`java -jar upDoc-1.0.jar analysis:mapping ../src/test/resources/paper-example/ FromAdaptiveIsomorphismInspectorFactory.java 0.2
`

The demo will run `upDoc` on a single method and report the similarities of the sentences of the javadoc comment to the method signature.

#### Running example

Execute `$ mvn -Dtest=PaperExampleStatTest test`

#### Preliminary evaluation

Execute `$ mvn -Dtest=ICPC19StatsTest test -DuseWMD="true"`

Please note that while in the paper we talk about 67 changes, in the outputs you will read a total of 40. It is because some changes were essentially equivalent, and it was not worth it to write a test for each duplicate (check the [google doc](https://docs.google.com/spreadsheets/d/1maRH6YY0OVuKSB2ACDhrz1-CQCS7sRHHoaZJDvLWi2Y/edit?usp=sharing) for the manual ICPC19 dataset analysis, where we list al lthe 67 changes in question)
