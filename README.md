# in-store-api-java-sdk

## Requirements

Java version ≥ 1.8

## Description

This project provides an example JavaFX application to interact with the Satispay in store API.
More information about the API can be retrived in this documentation: 

https://s3-eu-west-1.amazonaws.com/docs.satispay.com/index.html#instore-api

## More info

To run the example project, clone the repository, sync the gradle project and run InStoreApiClientApplication.

## Environments (Production vs Sandbox)

This sample use as default the Sandbox Environment (called "staging").

### Setup

Add this in your root `build.gradle` file:

```gradle
allprojects {
    repositories {
	    // ...
	    maven { url "https://jitpack.io" }
    }
}
```

Then, add the library to your module `build.gradle`
```gradle
dependencies {
    // ...
    compile 'com.github.satispay:in-store-api-java-sdk:0.0.9'
}
```


## License

    Copyright 2016 Satispay SpA.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    All trademarks and registered trademarks are the property of their respective owners.
