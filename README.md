# Photon [![Build Status](https://travis-ci.org/amitness/photon.svg?branch=master)](https://travis-ci.org/amitness/photon)

![Imgur](http://i.imgur.com/6W9Fftu.png)

Code base for our minor project.

## Local Development
### Installation
- Install Java Development Kit
  ```
  $ sudo apt-get remove --purge openjdk*
  $ sudo add-apt-repository -y ppa:webupd8team/java
  $ sudo apt-get update
  $ sudo apt-get -y install oracle-java8-installer
  ```
- To verify that java is installed, run
  ```
  $ java -version
  
  java version "1.8.0_31"
  Java(TM) SE Runtime Environment (build 1.8.0_31-b13)
  Java HotSpot(TM) 64-Bit Server VM (build 25.31-b07, mixed mode)
  ```

- Download [Android Studio](https://developer.android.com/studio/index.html).

- Extract the zip.

- Inside the folder, navigate to `bin/` and open a terminal there.
- Run this command
  ```
  ./studio.sh
  ```
- A setup window should appear. Follow the instructions.

### Development
- Clone the repo
  ```
  git clone https://github.com/amitness/photon
  ```
- In Android studio, select `Open Existing Project` and point to the location where you cloned the repo.

- The build should start. 

### Testing
- Builds are automatically tested through continuous integration using Travis. 
- Unit tests and instrumentation tests are automatically run on each build.

### Authors
- [Amit Chaudhary](https://github.com/studenton) | [Ashish Ghimire](https://github.com/deashish) | [Ashwin Neupane](https://github.com/ashwin101) | [Kiran Koirala](https://github.com/koiralakiran1)
