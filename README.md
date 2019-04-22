# spring-data-faker
This library allows you to generate fake data and customize it by using annotations. It uses [java-faker](https://github.com/DiUS/java-faker) as main fake data source and supports enumerations, collections and user-defined types.

* [Basic usage](#usage)  
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

2. Add library to your dependencies list (make sure you using last version):
    ```groovy
    compile 'com.github.vastik:spring-boot-starter-data-faker:1.0.+'
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
4. Use autowired DataFaker instance to fake data
    ```java
    @Autowired
    private DataFaker dataFaker;
    
    public void createSimpleClass() throws Exception {
        SimpleClass simpleClass = dataFaker.fake(SimpleClass.class);
    } 
    ```

<a name="annotations"><h3>Annotations</h3></a>
* ##### @FakeDateFuture
    Faker method: [date.future](http://dius.github.io/java-faker/apidocs/com/github/javafaker/DateAndTime.html#between(java.util.Date,%20java.util.Date))
    
    Supported class: **java.lang.Date**, **java.lang.LocalDateTime**
    ```java
    @FakeDateFuture(value = 10, unit = TimeUnit.DAYS)
    private LocalDateTime dateOpen;
    ```
* ##### @FakeDatePast
    Faker method: [date.past](http://dius.github.io/java-faker/apidocs/com/github/javafaker/DateAndTime.html#past(int,%20java.util.concurrent.TimeUnit))
    
    Supported class: **java.lang.Date**, **java.lang.LocalDateTime**
    ```java
    @FakeDatePast(value = 5, unit = TimeUnit.DAYS)
    private LocalDateTime dateOpen;
    ```
* ##### @FakeDateBetween
    Faker method: [date.between](http://dius.github.io/java-faker/apidocs/com/github/javafaker/DateAndTime.html#between(java.util.Date,%20java.util.Date))
    
    Supported class: **java.lang.Date**, **java.lang.LocalDateTime**
    
    Notes: Use **@FakePast** and **@FakeFuture** annotations to define time interval.
    ```java
    @FakeDateBetween(
                past = @FakeDatePast(value = 5, unit = TimeUnit.DAYS), 
                future = @FakeDateFuture(value = 10, unit = TimeUnit.DAYS)
    )
    private LocalDateTime dateOpen;
    ```
    
* ##### @FakeDateNow
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
* ##### @FakeNumberRandom
    Faker method: [Number::randomNumber](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Number.html#randomNumber())
    
    Supported class: **Interger**, **Long**, **Short**, **Float**, **Double**, **Byte**
    ```java
    @FakeRandom(15)
    private Integer updateInterval;
    ```
    
* ##### @FakeNumberDigits
    Faker method: [Number::randomNumber(digits)](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Number.html#randomNumber(int,%20boolean))
    
    Supported class: **Interger**, **Long**, **Short**, **Float**, **Double**, **Byte**
    ```java
    @FakeRandomNumber(digits = 7)
    private Integer phone;
    ```   
 * ##### @FakeNumberBetween
    Faker method: [Number::numberBetween](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Number.html#numberBetween(long,%20long))
    
    Supported class: **Interger**, **Long**, **Short**, **Float**, **Double**, **Byte**
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
 * ##### @FakeEnum
   Will apply specific values if provided or will apply random value from target enum class.
    ```java
    public enum Colors {
        RED,
        WHITE,
        BLACK
    }
    
    @FakeValue({"RED", "BLACK"})
    private Colors colors;
    ```
  * ##### @FakeInclude
    If you want to fake custom classes in your class you can use **@FakeInclude** annotation. **Applying this annotation on self-referenced or bi-directional classes will result in endless recursive process!**
  
  * ##### @FakeCollection
    Allws you to fake **java.util.List** or **java.util.Set** by placing **@Fake**-annotation that match generic type of your collection.
    ```java
    @FakeFaker("gameOfThrones.dragon")
    @FakeCollection(min = 5, max = 20)
    private List<String> dragonNames;
    ```

<a name="custom-faker"><h3>Customization</h3></a>
If you want to fake custom data you should:
1. Create your annotation
    ```java
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FakeLeetNumber {
    }
    ```
2. Create class that will handle your annotation by implementing **AnnotationHandler** interface
    ```java
    public class FakeLeetAnnotationHandler implements AnnotationHandler<FakeLeetNumber.class>
    {
        @Override
        public Object get(FakeLeetNumber annotation, DataFakeContext context) throws Exception {
            return 1337;
        }
    }
    ```
3. Register your handler in DataFakerRegistry
    ```java
    @Configuration
    public class CustomDataFakerConfiguration implements InitializingBean
    {
        @Autowired
        private DataFaker dataFaker;
        
        public void afterPropertiesSet() {
            dataFaker.getRegistry().registerHandler(FakeLeetNumber.class, new FakeLeetAnnotationHandler());
        }   
    }
    ```
4. Use
    ```java
    @FakeLeetNumber
    private Integer leet;
    ```    