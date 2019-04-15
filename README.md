# spring-data-faker
Annotation based object faker which using [java-faker](https://github.com/DiUS/java-faker) to generate data in spring context.

### Basic usage
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
### Default values
By default DataFaker will try to fake all fields that public or has public setters. By default DataFaker can fake next types:
- Boolean
- Integer
- Date
- LocalDateTime
- Long
- String
- Enum (any enumeration)
- List
- Set

### Basic Annotations
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
    
    Supported class: **java.lang.Interger**, **java.lang.Long**
    ```java
    @FakeNumberBetween(min = 15, max = 20)
    private Integer phone;
    ```      

### Custom faker method
You can call custom faker method by using **@FakeFaker** annotation that takes faker method chain. For example:
```java
@FakeFaker("gameOfThrones.dragon")
private String name;
```
The only limitation here is that the calling method **should not take any arguments**.

### Fake enumerations
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

### Fake nested objects
If you want to fake custom classes in your class you can use **@FakeObject** annotation. **Be careful! Applying this annotation on self-referenced or bi-directional classes will result in endless recursive process!**

### Fake collections
You can fake **java.util.List**, **java.util.Set** by placing **@Fake**-annotation that match generic type of your collection. **@FakeCollection** allows you to set size of elements in result collection. For example:
```java
@FakeFaker("gameOfThrones.dragon")
@FakeCollection(min = 5, max = 20)
private List<String> dragonNames;
```

### Ignoring fields
If you don't want to ignore field apply **@FakeIgnore** on it.
```java
@FakeIgnore
private String password;
```

### Custom type faker
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

### Custom DataTypeFaker for specific field
You can specify custom DataTypeFaker for single field by using annotation **@FakeCustom**
```java
@FakeCustom(BooleanTypeFaker.class)
Boolean hasValue;
```
    
