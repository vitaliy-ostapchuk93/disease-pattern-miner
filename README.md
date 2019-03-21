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

The dataset for upload has to match the following csv-file format (example full set):
```
GENDER-AGE-GROUP, PATIENT-ID, YYYYMMDD, (min 1, max 3 ) ICD-9-CM 

f0,EW75937189,20010120,0740,4661,
f0,EW75937189,20010107,37311,,
f0,EW75937189,20010120,V202,,
f0,BU45121182,20010103,4659,7806,
f1,KT61521480,20010109,486,94400,
...
```

The application will filter and split the data in gender-age-group files (example f0-group set):
```
<PATIENT-ID>, <YYYYMMDD>, <min_1 max_3 ICD-9-CM codes>

EW75937189,20010120,0740,4661,
EW75937189,20010107,37311,,
BU45121182,20010103,4659,7806,
...
```

Each gender-age-group set will befiltered & converted to a seq-file for the mining using the ICD-9-CM hierarchy.
Positive integers are ordinal values for the ICD-9-CM chapters. -1 represents a TIME_GAP (2 weeks). -2 represents the end of the sequence.
```
<ICD-9-CM CHAPTERS ORDINALS> <ICD-9-CM CHAPTERS ORDINALS> -1 ... <ICD-9-CM CHAPTERS ORDINALS> -1 -2

5 -1 5 -1 5 -1 5 -1 5 -1 7 -1 9 13 15 -1 9 -1 9 -1 -2
7 -1 7 -1 7 -1 7 -1 5 -1 2 7 9 -1 5 7 -1 7 -1 5 -1 5 15 -1 7 -1 -2
7 9 -1 7 9 -1 7 9 -1 7 9 -1 7 9 -1 7 9 -1 7 9 11 -1 9 -1 -2
...
```

Many different sequence mining algorithms can be used. For each mining task a result file is produced:
```
<FREQUENT SEQUENCE PATTERN> #SUP: <ABSOLUTE SUPPORT OF PATTERN>

5 7 -1 7 -1 7 -1 #SUP: 3635
5 7 -1 7 -1 #SUP: 3824
5 7 -1 #SUP: 4000
5 -1 5 -1 7 -1 #SUP: 3551
...
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
