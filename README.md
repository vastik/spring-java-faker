# spring-data-faker
This library allows you to generate fake data and customize it by using annotations. It uses [java-faker](https://github.com/DiUS/java-faker) as main fake data source and supports enumerations, collections and user-defined types.

* [Basic usage](#usage)  
* [Defaults](#default)
* [Annotations](#annotations)
* [Enumerations](#enumerations)
* [Collections](#collections)
* [Objects](#objects)
* [Custom fakers](#custom-faker)
* [Ignoring](#ignoring)

<a name="usage"><h3>Basic usage</h3></a>
1. Add [jitpack.io](https://jitpack.io) maven repository to your project:
    ```groovy
    repositories {
        maven { url "https://jitpack.io" }
    }
    ```

2. Add spring-data-faker to your dependencies list:
    ```groovy
    compile 'com.github.vastik:spring-data-faker:1.0.0'
    ```

3. Annotate your class fields with @Fake-annotations, for example:
    ```java
    public class SimpleClass {
    
        @FakeRandom(15)
        private Integer count;
    
        @FakeFaker("gameOfThrones.dragon")
        private String name;
    
        @FakeValue({"RED", "BLACK"})
        private Colors colors;
    
        @FakeRandom(25)
        @FakeCollection(min = 5, max = 15)
        private Set<Integer> integers;
    }
    ```
4. Enable DataFaker in your spring boot application by adding annotation @EnableDataFaker in your main class.
    ```java
    @SpringBootApplication
    @EnableDataFaker
    public class AppBoot {
        public static void main(String[] args) {
            SpringApplication.run(AppBoot.class, args);
        }
    }
    ```
5. Use autowired DataFaker instance to fake data
    ```java
    @Autowired
    private DataFaker dataFaker;
    
    public void createSimpleClass() throws Exception {
        SimpleClass simpleClass = dataFaker.fake(SimpleClass.class);
    } 
    ```
<a name="default"><h3>Defaults</h3></a>
By default DataFaker will try to fake all fields that public or has public setters. By default DataFaker can fake next classes:
- Boolean (using faker's [bool.bool](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Bool.html#bool()))
- Integer (using [random.nextInt](https://docs.oracle.com/javase/6/docs/api/java/util/Random.html#nextInt()))
- Date (only if @Fake-annotation present)
- LocalDateTime (only if @Fake-annotation present)
- Long (using [random.nextLong](https://docs.oracle.com/javase/6/docs/api/java/util/Random.html#nextLong()))
- String (using faker's [bothify](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Faker.html#bothify(java.lang.String)))
- Enumerations (by picking random value)
- List
- Set

<a name="annotations"><h3>Annotations</h3></a>
* ##### @FakeFuture
    Faker method: [date.future](http://dius.github.io/java-faker/apidocs/com/github/javafaker/DateAndTime.html#between(java.util.Date,%20java.util.Date))
    
    Supported class: **java.lang.Date**, **java.lang.LocalDateTime**
    ```java
    @FakeFuture(value = 10, unit = TimeUnit.DAYS)
    private LocalDateTime dateOpen;
    ```
* ##### @FakePast
    Faker method: [date.past](http://dius.github.io/java-faker/apidocs/com/github/javafaker/DateAndTime.html#past(int,%20java.util.concurrent.TimeUnit))
    
    Supported class: **java.lang.Date**, **java.lang.LocalDateTime**
    ```java
    @FakePast(value = 5, unit = TimeUnit.DAYS)
    private LocalDateTime dateOpen;
    ```
* ##### @FakeBetween
    Faker method: [date.between](http://dius.github.io/java-faker/apidocs/com/github/javafaker/DateAndTime.html#between(java.util.Date,%20java.util.Date))
    
    Supported class: **java.lang.Date**, **java.lang.LocalDateTime**
    
    Notes: Use **@FakePast** and **@FakeFuture** annotations to define time interval.
    ```java
    @FakeBetween(
                past = @FakePast(value = 5, unit = TimeUnit.DAYS), 
                future = @FakeFuture(value = 10, unit = TimeUnit.DAYS)
    )
    private LocalDateTime dateOpen;
    ```
    
* ##### @FakeNow
    Will call **LocalDateTime.now()** on this field.
    
    Supported class: **java.lang.Date**, **java.lang.LocalDateTime**
    ```java
    @FakeNow
    private LocalDateTime dateOpen;
    ```    
    
* ##### @FakeLetterify
    Faker method: [letterfiy](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Faker.html#letterify(java.lang.String))
    
    Supported class: **java.lang.String**
    ```java
    @FakeLetterify("12??34")
    private String callerId;
    ```
    
* ##### @FakeBothify
    Faker method: [bothify](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Faker.html#bothify(java.lang.String))
    
    Supported class: **java.lang.String**
    ```java
    @FakeBothify("###???")
    private String callerId;
    ```

* ##### @FakeNumberify
    Faker method: [numberify](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Faker.html#numerify(java.lang.String))
    
    Supported class: **java.lang.String**
    ```java
    @FakeBothify("ABC##EFG")
    private String callerId;
    ```
* ##### @FakeRandom
    Faker method: [Number::randomNumber](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Number.html#randomNumber())
    
    Supported class: **java.lang.Interger**, **java.lang.Long**
    ```java
    @FakeRandom(15)
    private Integer updateInterval;
    ```
    
* ##### @FakeRandomNumber
    Faker method: [Number::randomNumber(digits)](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Number.html#randomNumber(int,%20boolean))
    
    Supported class: **java.lang.Interger**, **java.lang.Long**
    ```java
    @FakeRandomNumber(digits = 7)
    private Integer phone;
    ```   
 * ##### @FakeNumberBetween
    Faker method: [Number::numberBetween](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Number.html#numberBetween(long,%20long))
    
    Supported class: **java.lang.Integer**, **java.lang.Long**
    ```java
    @FakeNumberBetween(min = 15, max = 20)
    private Integer phone;
    ```      
  * ##### @FakeFaker
    You can call custom faker method by using **@FakeFaker** annotation that takes faker method chain. For example:
    ```
    @FakeFaker("gameOfThrones.dragon")
    private String name;
    ```
    The only limitation here is that the calling method **should not take any arguments**.
    
<a name="enumerations"><h3>Enumerations</h3></a>
If field is enum then **DataFaker** will try to apply random value from target enum class. If you want to use specific values from your enumeration, you can place **@FakeValue** annotation that provide a list of possible enum values.
```java
public enum Colors {
    RED,
    WHITE,
    BLACK
}

@FakeValue({"RED", "BLACK"})
private Colors colors;
```

<a name="objects"><h3>Objects</h3></a>
If you want to fake custom classes in your class you can use **@FakeObject** annotation. **Be careful! Applying this annotation on self-referenced or bi-directional classes will result in endless recursive process!**

<a name="collections"><h3>Fake collections</h3></a>
You can fake **java.util.List**, **java.util.Set** by placing **@Fake**-annotation that match generic type of your collection. **@FakeCollection** allows you to set size of elements in result collection. For example:
```java
@FakeFaker("gameOfThrones.dragon")
@FakeCollection(min = 5, max = 20)
private List<String> dragonNames;
```

<a name="custom-faker"><h3>Custom fakers</h3></a>
If you want to fake custom types in your classes you should:
 - Creating class that implements **DataTypeFaker** interface
    ```java
    public class BooleanTypeFaker implements DataTypeFaker<Boolean> {
        @Override
        public Boolean getValue(DataFakeContext dataFakeContext) {
            return dataFakeContext.getFaker().bool().bool();
        }
    }
    ```
 - Register your class in DataFakerTypeFactory
    ```java
     public class SimpleClassGenerator {
        @Autowired
        private DataFaker faker;
        
        public void register() {
            faker.getFactory().setTypeFaker(Boolean.class, BooleanTypeFaker.class);
        }
    }
    ```
    
Also you can specify custom DataTypeFaker for field by using annotation **@FakeCustom**
```java
@FakeCustom(BooleanTypeFaker.class)
Boolean hasValue;
```
    
<a name="ignoring"><h3>Ignoring</h3></a>
If you want to ignore specific field just apply **@FakeIgnore** on it.
```java
@FakeIgnore
private String password;
```