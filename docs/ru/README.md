# spring-data-faker
Позволяет генерировать фейковые данные для заполнения полей в классах при помощи аннотаций.
Основным датасорсом для данных выступает [java-faker](https://github.com/DiUS/java-faker), 
есть поддержка коллекций, вложенных объектов и перечислений. И все это для Spring Boot, но не самой новой версии.

* [Базовое использование](#usage)  
* [Annotations](#annotations)
* [Customization](#custom-faker)

<a name="usage"><h3>Basic usage</h3></a>
1. Добавляем репозиторий [jitpack.io](https://jitpack.io) в свой проект:
    ```groovy
    repositories {
        maven { url "https://jitpack.io" }
    }
    ```

2. Добавляем зависимость (обязательно убедитесь, что используете последнюю версию)
    ```groovy
    compile 'com.github.vastik:spring-boot-starter-data-faker:1.0.+'
    ```

3. Добавляем аннотации на поля своего класса:
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
     
        // Getters / Setters    
    }
    ```    
4. Генерируем класс с данными при помощи DataFaker
    ```java
    @Autowired
    private DataFaker dataFaker;
    
    public void createSimpleClass() throws Exception {
        SimpleClass simpleClass = dataFaker.fake(SimpleClass.class);
    } 
    ```

<a name="annotations"><h3>Аннотации</h3></a>
* #### @FakeDateFuture
    Метод Faker: [date.future](http://dius.github.io/java-faker/apidocs/com/github/javafaker/DateAndTime.html#between(java.util.Date,%20java.util.Date))
    
    Поддерживаемые типы: **java.lang.Date**, **java.lang.LocalDateTime**
    ```java
    @FakeDateFuture(value = 10, unit = TimeUnit.DAYS)
    private LocalDateTime dateOpen;
    ```
* #### @FakeDatePast
    Метод Faker: [date.past](http://dius.github.io/java-faker/apidocs/com/github/javafaker/DateAndTime.html#past(int,%20java.util.concurrent.TimeUnit))
    
    Поддерживаемые типы: **java.lang.Date**, **java.lang.LocalDateTime**
    ```java
    @FakeDatePast(value = 5, unit = TimeUnit.DAYS)
    private LocalDateTime dateOpen;
    ```
* #### @FakeDateBetween
    Метод Faker: [date.between](http://dius.github.io/java-faker/apidocs/com/github/javafaker/DateAndTime.html#between(java.util.Date,%20java.util.Date))
    
    Поддерживаемые типы: **java.lang.Date**, **java.lang.LocalDateTime**
    
    Notes: Use **@FakePast** and **@FakeFuture** annotations to define time interval.
    ```java
    @FakeDateBetween(
                past = @FakeDatePast(value = 5, unit = TimeUnit.DAYS), 
                future = @FakeDateFuture(value = 10, unit = TimeUnit.DAYS)
    )
    private LocalDateTime dateOpen;
    ```
    
* #### @FakeDateNow
    Вернет текущее значение **LocalDateTime.now()**
    
    Поддерживаемые типы: **java.lang.Date**, **java.lang.LocalDateTime**
    ```java
    @FakeNow
    private LocalDateTime dateOpen;
    ```    
    
* #### @FakeLetterify
    Метод Faker: [letterfiy](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Faker.html#letterify(java.lang.String))
    
    Поддерживаемые типы: **java.lang.String**
    ```java
    @FakeLetterify("12??34")
    private String callerId;
    ```
    
* #### @FakeBothify
    Метод Faker: [bothify](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Faker.html#bothify(java.lang.String))
    
    Поддерживаемые типы: **java.lang.String**
    ```java
    @FakeBothify("###???")
    private String callerId;
    ```

* #### @FakeNumberify
    Метод Faker: [numberify](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Faker.html#numerify(java.lang.String))
    
    Поддерживаемые типы: **java.lang.String**
    ```java
    @FakeBothify("ABC##EFG")
    private String callerId;
    ```
* #### @FakeNumberRandom
    Метод Faker: [Number::randomNumber](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Number.html#randomNumber())
    
    Поддерживаемые типы: **Interger**, **Long**, **Short**, **Float**, **Double**, **Byte**
    ```java
    @FakeRandom(15)
    private Integer updateInterval;
    ```
    
* #### @FakeNumberDigits
    Метод Faker: [Number::randomNumber(digits)](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Number.html#randomNumber(int,%20boolean))
    
    Поддерживаемые типы: **Interger**, **Long**, **Short**, **Float**, **Double**, **Byte**
    ```java
    @FakeRandomNumber(digits = 7)
    private Integer phone;
    ```   
 * #### @FakeNumberBetween
    Метод Faker: [Number::numberBetween](http://dius.github.io/java-faker/apidocs/com/github/javafaker/Number.html#numberBetween(long,%20long))
    
    Поддерживаемые типы: **Interger**, **Long**, **Short**, **Float**, **Double**, **Byte**
    ```java
    @FakeNumberBetween(min = 15, max = 20)
    private Integer phone;
    ```      
  * #### @FakeFaker
    Позволяет вызвать кастомный метод (или цепочку методов) у объекта Faker, например:     
    ```
    @FakeFaker("gameOfThrones.dragon") // аналог new Faker().gameOfThrones().dragon()
    private String name;
    ```
    НО, нельзя вызывать методы, которые принимают параметры (потому что мы не можем их передать)     
 * #### @FakeEnum
   Применит на поле случайное значение из перечисления или случайное из заданных значений в аннотации:
    ```java
    public enum Colors {
        RED,
        WHITE,
        BLACK
    }
    
    @FakeValue({"RED", "BLACK"})
    private Colors colors;
    ```
  * #### @FakeInclude
    Применит генерацию данных для аннотированного класса.
    ```java
    @FakeInclude
    private Contact contact;
    ```    
  * #### @FakeCollection
    Сгенерирует данные для коллекций типа **java.util.List** или **java.util.Set**, 
    если присутствует любая из аннотаций, указанных выше и которая поддерживает тип объекта массива. 
    ```java
    @FakeFaker("gameOfThrones.dragon")
    @FakeCollection(min = 5, max = 20)
    private List<String> dragonNames;
    ```

<a name="custom-faker"><h3>Кастомизация</h3></a>
Вы можете создать свои собственные аннотации и генерировать для них специфичные данные. Для этого надо:
1. Создать свою аннотацию
    ```java
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FakeLeetNumber {
    }
    ```
2. Создать класс, который будет перехватывать процесс генерации данных для этой аннотации, реализовав интерфейс **AnnotationHandler**
    ```java
    public class FakeLeetAnnotationHandler implements AnnotationHandler<FakeLeetNumber>
    {
        @Override
        public Object get(FakeLeetNumber annotation, DataFakeContext context) throws Exception {
            return 1337;
        }
    }
    ```
3. Зарегистрирвоать свой класс в DataFakerRegistry
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
4. Использовать в своем коде!
    ```java
    @FakeLeetNumber
    private Integer leet;
    ```    