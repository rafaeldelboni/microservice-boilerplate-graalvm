# Performance Comparison
You can download the test file I used here [assertion.jmx](docs/assertion.jmx)

This was the jmeter settings:  
![settings-1](docs/assertion-settings-1.jpg)
![settings-2](docs/assertion-settings-2.jpg)

The only change I did in the base code, was replace the http out for the crypto API to use a mocked http server in my machine.
I used [moclojer](https://github.com/moclojer/moclojer) for the moc, with a basic default json response body.

## Throughput
### Native
![native-throughput](docs/jmeter-native.jpg)
### Jar
![java-throughput](docs/jmeter-java.jpg)

## Threads
### Native
![threads-native](docs/threads-native.jpg)
### Jar
![threads-java](docs/threads-java.jpg)

You can download the resulting test flights files here: [java](docs/jar-flight.jfr) [native](docs/native-flight.jfr)
