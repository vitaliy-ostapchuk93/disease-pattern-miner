[![GitHub license](https://img.shields.io/github/license/Naereen/StrapDown.js.svg)](LICENCE.md)
[![Demo http://pwas.tmu.edu.tw/icdminer/](https://img.shields.io/website-up-down-green-red/http/shields.io.svg)](http://pwas.tmu.edu.tw/icdminer/)
[![Open Source Love svg2](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)


# Disease Pattern Miner <img src="https://github.com/vitaliy-ostapchuk93/disease-pattern-miner/blob/master/PatternMiner/app/src/main/webapp/resources/static/img/logo.png" width="48">

**Disease Pattern Miner** is a free, open-source mining framework for interactively discovering sequential disease patterns in medical health record datasets.

******

## Features

- Many of the state-of-the-art sequence mining algorithms.
- Modular design, but single monolithic web application.
- Modern, responsive UI.
- Single results table with many different filtering options to explore patterns.
- Interactive sequence pattern model to provide insights to disease trajectories.
- Tested on Windows 10 & Ubuntu 18.04.

******

## Documentation

The web aplication is designed to perform sequential mining tasks on EHR datasets. 
The results can be viewed in a table and explored in an interactive Sankey chart.

The dataset for upload has to match the following csv-file format:
```
GENDER-AGE-GROUP, PATIENT-ID, YYYYMMDD, ICD-9-CM (1-3)

f0,EW75937189,20010120,0740,4661,
f0,FS54767684,20010107,37311,,
f0,CT58401081,20010120,V202,,
f0,BU45121182,20010103,4659,7806,
f0,KT61521480,20010109,486,94400,
```


For more detailed examples and project insights please look into the publications or contact author.


******

## System Requirements & Recommendations

A machine with:
- 4 GB of RAM, although at least 16 GB is recommended. Make sure the server container can access it!
- 10 GB of drive space, although at least 40 GB is recommended. This might depend on the dataset.

The following software installed:
- Java 11 or 12, Java Development Kit (JDK)
- Apache Tomcat as servlet container.

******

## Quick Start

1) Make sure you got all system and software requirements!
2) Clone the repository.
3) Build a .war-file of the project.
4) Deploy the .war-file to the server

******

## Authors
* [Vitaliy Ostapchuk](mailto:vostapch@stud.hs-heilbronn.de)
******

## Publication


******
