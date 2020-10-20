# RxSON
Reactive JSON: Reactive approach to JSON.

 RxSON speeds up you code by providing reactive objects before the response complete.
 
 This library provides an Asynchronous Reactive REST Client to stream any REST resource.
 RxSON makes it easy for the application to use JSON streamed chunks from the response as soon as they arrive, and rendering you code fastr.

 RxSON treats the HTTP response as a series of small, useful chunks and map them to Java Objects
 
## Why RxSON

- Fast:
makes it easy for the application to use JSON streamed chunks
from the response as soon as they arrive, and rendering you code faster.
- [Reactive Programming](http://www.reactive-streams.org/) with [RxJava](https://github.com/ReactiveX/RxJava):
    * An Asynchronous Reactive JSON REST Client to stream any REST resource
- Non-Blocking JSON parsing
- Fetching huge JSON payload larger than the available memory:
   * RxSON can load json trees larger than the available memory.
   * Fetch response in chunks.
   * No need to deserialize entire json into memory.
    
- [JsonPath](https://github.com/json-path/JsonPath) to selectively extract desired json data 

### Simple to use
Given the [JSON](https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json)
```json
[
  {
    "Airport": {
      "Code": "LA",
      "Name": "LA: LA International"
    },
    //Other objects
  },
  {
    "Airport": {
      "Code": "ATL",
      "Name": "Atlanta: Atlanta International"
    }
  }
]
```

RxSON example
```
   String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";
   HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
   RxSON rxson = new RxSON.Builder().build();

   String jsonPath = "$[*].Airport.Name";
   Flowable<String> airportStream = rxson.create(String.class, req, jsonPath);
   airportStream
       .doOnNext(it -> System.out.println("Received new item: " + it))
       //Just for test
       .toList()
       .blockingGet();
```

Class example:

```java
package io.rxson.examples.model.airline;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Airport {
    @JsonProperty("Code")
    public String code;
    @JsonProperty("Name")
    public String name;
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

```
  RxSON rxson = new RxSON.Builder().build();
  String jsonPath = "$[*].Airport";
  Flowable<Airport> airportStream = rxson.create(Airport.class, req, jsonPath);
  airportStream
      .observeOn(Schedulers.io())
      .subscribeOn(Schedulers.io())
      .doOnNext(it -> System.out.println("Received a flow item: " + it.getName()))
      //Just for test
      .toList()
      .blockingGet();
```

Model example:

```java
//Create your own models
public class JsonNodeModel {
    @Reactive(path = "$[*].Airport")
    private Flowable<JsonNode> result;
    public Flowable<JsonNode> getResult() { return result; }
    public void setResult(final Flowable<JsonNode> result) { this.result = result; }
}
```
```
  final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
  final HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
  final RxSON rxson = new RxSON.Builder().client(client).build();
  final var airlinesStream = rxson.create(JsonNodeModel.class, req);
  airlinesStream.getResult()
       .doOnNext(it -> System.out.println("Received a flow item: " + it.get("Name")))
       .toList().blockingGet();
```

More examples are available in tests.

###Useful helpers
[JsonPath helper](http://jsonpath.herokuapp.com/?path=$.store.book[*].author)

[Http Client](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html)

 **Prerequisites:** [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)