#!/bin/bash

javac Main.java
java Main < example/sample2.in > output/result.out
python3 tester.py example/sample2.in output/result.out
