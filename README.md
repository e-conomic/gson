# Persist Gson
Gson is a Java library that can be used to convert a Java object into its 
JSON representation. It can also be used to convert a JSON string into an 
equivalent Java object. Gson can work with arbitrary Java objects including 
pre-existing objects that you do not have source-code of.

Complete Gson documentation is available at its project page 
https://github.com/google/gson

## Important information

This repo is a fork of [Google's Gson](https://github.com/google/gson) library. The library has 
been modified to provide persist functionality for our models. The library is being used in the 
[econ-android-foundation](https://github.com/e-conomic/econ-android-foundation). Because we have
added Kotlin files in the library, the `kotlin` compiler is needed to build the current version of 
the library. 
