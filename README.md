# Kuestion

an Online Survey System build with Full-Stack Kotlin

# Table of Content

* [Scope](#scope)

* [Features](#features)
  
  * [Project Structure](#project-structure)
  * [Ktor](#ktor)
  * [Kotlin Exposed](#kotlin-exposed)
  * [Kotlinx Serialization](#kotlinx-serialization)

* [Starting Kuestion](#starting-kuestion)
  
  * [Startup](#startup)
  * [Configuration](#configuration)
    * [BuildSrc](#buildsrc)
    * [gradle.build.kts](#gradlebuildkts)
    * [Mapping npm Modules](#mapping-npm-modules)

* [Pitfalls during Development](#pitfalls-during-development)
  
  * [common](#common)
  * [js](#js)
  * [jvm](#jvm)
  * [Other](#other)
  * [Related Github Issues](#related-github-issues)

* [Used Libraries](#used-libraries)
  
  * [Frontend/UI/Browser](#frontenduibrowser)
  * [Backend/Server/Database](#backendserverdatabase)

* [Further Reading](#Further-Reading)

* [Based on](#based-on)

## Scope

This projects serves as a Proof of Concept for the Kotlin Multiplatform Project. It tries to use Kotlin wherever possible to determin the Maturity of its use in Server, Browser, Database Connectivity and the common Code Feature.

## Features

* Basic Authorization with local Storage
  
  * need to login before you can create a survey or vote

* Create a Survey with
  
  * A Question
  
  * Answers
  
  * Expiration date & time

* Editing of a Survey
  
  * End the Survey
  
  * Change the properties of it
  
  * Delete it

* Browser is on hotreload (run-config file, `--continuous` as Argument in the Run Configuration, `jsBrowserDevelopmentRun`)

### Project Structure

commonTest & jsTest are only availabile on the fb-tests Branch as they are not working. There is probably a configuration mistake with Karma, not so easy to correct.

+ buildSrc - Versioning & Dependencies. See [buildSrc](#buildsrc)

+ commonMain - shared Codebase, model that is send over the Network, ServerAPI

+ commonTest

+ jsMain - npm mapping, CSS (ComponentStyles.kt), React in Kotlin DSL

+ jsTest

+ jvmMain - Ktor Server Endpoints, Exposed Database Connection

+ jvmTest - Working Endpoint testing

### Ktor

#### Installation Feature

install(Feature){
    Configuration
}

install(ContentNegotiation){
    json()
}

Check the [Official Documentation](https://ktor.io/servers/features.html#installing) for availabile Features and their respective configuration.

#### Authentication Feature

([Official Documentation](https://ktor.io/servers/features/authentication.html))

You install it the usual way and then you can define and configue as many authorizations as you like: 

install(Authentication) {
    basic("NAME") {}
    jwt("NAME2") {}
    oauth{}
}

you the use it like so:

authenticate("NAME"){
    Routes
}

#### Endpoints/Routing

Endpoints are installed within the `routing{}` Lambda. Routing installs itself.

With `route(Path){}` you define the path to your endpoint.

Variants:

route("/hello/world"){
    get{}
    post{}
}

and

route("/hello"){
    get("/world") {}
    post("/world") {}
}

are the same.

#### Kotlin Exposed

This is where and how you manage all your Database related Stuff. The official wiki is [here](https://github.com/JetBrains/Exposed). This Project uses the h2 in-memory Database. it also makes use of the DSL API of Exposed as most examples are written in it, DAOs are available too.

##### Database Connection

To connect to your desired Database you use:
`Database.connect("url","driver")` e.g.:
`Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")`

The `DB_CLOSE_DELAY=-1` is need for the h2 db to keep it alive between transactions/connections. You can assign it a val and specifiy that in your transaction which db you want this transaction to connect to.

##### Tables

To setup your Tables you define an object with Type `Table`. There are a few Tables with predefined primary keys, IntIdTable, LongIdTable and UUIDTable or you define your own with a subclass of IdTable.

`object myTable = IntIdTable(){
    val name = varchar("name", 10)
    val age = integer("age").references(AnotherTable.years)
    val date = datetime("timestamp")
}`

To work with your Table in Kotlin you need to map every Table it to its own data class

`data class myTable(val primKey: Int, val name: String, val age: Int, val date: LocalDateTime)`

using a simple mapping function

`fun mapToMyTable(it: ResultRow) = myTable(
    it[myTable.id].value,
    it[myTable.name],
    it[myTable.age],
    it[myTable.date]
)`

this is type safe.

##### Database Transactions

To now use your database you connect via `transaction{}` calls. Since you can define a logger in every transaction i wrote a custom function where you need to define your logger just once.

`fun <T, K : Table> loggedSchemaUtilsTransaction(  
    db: Database? = null,  
  vararg tables: K,  
  statement: Transaction.() -> T  
): T =  
  transaction(db) {  
  SchemaUtils.create(*tables)  
        addLogger(stdLogger)  
        statement()  
    }`

Inside your calls you execute your `SQL` statements.

Every Database Access in this App is found in the dbAccessor.kt File

CRUD works as follows:

`loggedSchemaUtilsTransaction(db, myTable) {
// Create
    val id = myTable.insertAndGetId {
            it[name] = Bratheringe
            it[age] = 42
            it[date] = LocalDateTime.now()
        }
// Read
    myTable.select {myTable.age = 24}
// Update
    myTable.update ({myTable.age = 42}) {
        it[myTable.name] = Bismarkheringe
    }
    myTable.update ({myTable.date = DATE}) {
        with(SqlExpressionBuilder) {
            it[myTable.age] = myTable.age + 1
        }
    }
// Delete
    myTable.deleteWhere {myTable.age = 43}
}`

#### Kotlinx Serialization

Now, using Kotlinx.Serialization was rather easy. You import the needed packages for the specific platform and add the `@Serialization` Annotation to your data class in common Code. Your HttpClient needs the `ContentType: Application/Json` Header and with Ktor you install `ContentNegotiation` with `json` and you're good to go.

The Datatypes/data classes you want to serialize need to have a Serializer, otherwise it won't work and you need to write your own.

## Starting Kuestion

clone the project and import it into IntelliJ IDEA

### Startup

* for the ui/brower application run `gradle jsBrowserDevelopmentRun`

* for the server run gradle run `gradle run`

### Configuration

#### buildSrc

Versions of Kotlin, React, Ktor, Plugins and more used in build.gradle.kts are defined here. BuildSrc is compiled beforehand therefore these Constants are available in every build.gradle.kts in the project.

#### build.gradle.kts

To use new packages use the following syntax:

* for kotlin: `implementation("package")`

* for node packages: `implementation(npm("package", "version"))`, takes the latest version if none is specified

#### Mapping npm Modules

To use npm modules you might have to write your own wrapper class if you can't find one.

check react-minimal-pie-chart.kt or the fb-handson Branch (implementation of a Kotlin Handson & a SPA example) for examples.

## Pitfalls during Development

### common

#### The Good

* easy to use, you define it here, it's availabile everywhere.

#### The Bad

* Actual/expect methods are limited in functionality or are not straight forward and  need heavy abstraction.

### js

#### The Good

* Kotlin-wrappers so you can use kotlin dsl everywhere
* CSS aka styled Components are easy to use, problem is again writing css in Kotlin DSL

#### The Bad

* React to Kotlin DSL has almost no Documentation and just a few Examples which just show some functionalities. There is no whole React to Kotlin DSL Documentation and you need to know React to know what to do.  Auswikrungen, transitiv Leistung nötig ohne Doku

* Some kotlin-wrappers are heavily out of date

* Some React functionalities need a more complicated approach (e.g. getDerivedStateFromProps)

* Error Handling with Error Boundaries from React. They do not catch async Errors. This is adressed in a experimental Feature in React. So every Component needs to do their own Error Handling.

* Sometimes you need to write your own wrapper for npm packages

* Communicating between browser & server was rather complex. (Propably caused due to inexperience)

### jvm

#### The Good

* Ktor has a very good documentation and quite some examples
* Exposed has a good Documentation

#### The Bad

### Other

#### The Good

* Kotlin DSL everywhere.
* "Plug and Play" of Kotlin Packages.

#### The Bad

* Search queries for kotlin result in a lot of android stuff, as long as no android is used.
* Using the structure with vals as in this project results in IDEA complaining about unused variables

### Related Gitthub Issues

* `Module Not Found`: These Issues do even exist in the official examples, not much you can do here except implementing the needed packages.

* `Warning: changing an uncontrolled Input`: related to kotlinx.html.

## Used Libraries

### Frontend/UI/Browser

- KotlinJS
- Kotlin React ([https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react)
- kotlinx html ([https://github.com/Kotlin/kotlinx.html](https://github.com/Kotlin/kotlinx.html))
- styled components ([https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-styled](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-styled))
- Kotlinx Serialization ([https://github.com/Kotlin/kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization))
- Kotlinx Coroutines ([https://github.com/Kotlin/kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines))

### Backend/Server/Database

- KotlinJVM
- Ktor ([https://github.com/ktorio/ktor](https://github.com/ktorio/ktor))
- Exposed ([https://github.com/JetBrains/Exposed](https://github.com/JetBrains/Exposed))
- Kotlinx Serialization ([https://github.com/Kotlin/kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines))

## Further Reading?

Ausblick was es noch gibt, evtl. libraries zu nutzen. Möglichkeit android/ios App

### Android App

Adding module in intelliJ IDEA?

## Based on

[Wolfgang Wipp/prod-dash](http://gitlab.innosystec.site/wowipp/prod-dash)
