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

* [Further Reading](#further-reading)

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

commonTest & jsTest are only availabile on the fb-tests Branch as they are not working. There is probably a configuration mistake with Karma, not easy to correct.
I have no Idea how to fix that.

+ buildSrc - Versioning & Dependencies. See [buildSrc](#buildsrc)

+ commonMain - shared Codebase, model that is send over the Network, ServerAPI

+ commonTest

+ jsMain - npm mapping, CSS (ComponentStyles.kt), React in Kotlin DSL

+ jsTest

+ jvmMain - Ktor Server Endpoints, Exposed Database Connection

+ jvmTest - Working Endpoint testing

### Ktor

#### Installation Feature

<pre>
install(Feature){
    Configuration
}

install(ContentNegotiation){
    json()
}
</pre>

Check the [Official Documentation](https://ktor.io/servers/features.html#installing) for availabile Features and their respective configuration.

#### Authentication Feature

([Official Documentation](https://ktor.io/servers/features/authentication.html))

You install it the usual way and then you can define and configue as many authorizations as you like: 

<pre>
install(Authentication) {
    basic("NAME") {}
    jwt("NAME2") {}
    oauth{}
}
</pre>

you the use it like so:

<pre>
authenticate("NAME"){
    Routes
}
</pre>

#### Endpoints/Routing

Endpoints are installed within the `routing{}` Lambda. No need for Installation, Routing installs itself.

With `route(Path){}` you define the path to your endpoint.

Variants:

<pre>
route("/hello/world"){
    get{}
    post{}
}
</pre>

and

<pre>
route("/hello"){
    get("/world") {}
    post("/world") {}
}
</pre>

are the same.

You can use extention functions to structure the routes into different files.

<pre>
fun Routing.home() {
    route("/"){
        get {}
        delete{}
        authenticate {
            post{}
        }
    }
}
</pre>

 then just call the function in the base routing lambda.

##### Locations

([Official Documentation](https://ktor.io/servers/features/locations.html)) aka type-safe Routing

The `BasicSurveyRoute.kt` uses a experimental feature of ktor called Locations which offers type-safe Routing. 

<pre>
@Location("/{questionId}")
class question(val questionId: String)

get<question>{ question -> }
</pre>

The Path is define with a `@Location` Annotation and a path specific class which has all the variables as matching name properties. Inside the get Lambda you have access to them.

#### Kotlin Exposed

This is where and how you manage all your Database related Stuff. The official wiki is [here](https://github.com/JetBrains/Exposed). This Project uses the h2 in-memory Database. it also makes use of the DSL API of Exposed as most examples are written in it, DAOs are available too.

##### Database Connection

To connect to your desired Database you use:
`Database.connect("url","driver")` e.g.:
`Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")`

The `DB_CLOSE_DELAY=-1` is need for the h2 db to keep it alive between transactions/connections. You can assign it a val and specifiy in your transaction which db you want this transaction to connect to.

##### Tables

To setup your Tables you define an object with Type `Table`. There are a few Tables with predefined primary keys, IntIdTable, LongIdTable and UUIDTable or you define your own with a subclass of IdTable.

<pre>
object myTable = IntIdTable(){
    val name = varchar("name", 10)
    val age = integer("age").references(AnotherTable.years)
    val date = datetime("timestamp")
}
</pre>

To work with your Table in Kotlin you need to map every Table it to its own data class

`data class myTable(val primKey: Int, val name: String, val age: Int, val date: LocalDateTime)`

using a simple mapping function

<pre>
fun mapToMyTable(it: ResultRow) = myTable(
    it[myTable.id].value,
    it[myTable.name],
    it[myTable.age],
    it[myTable.date]
)
</pre>

this is type safe.

##### Database Transactions

To now use your database you connect via `transaction{}` calls. Since you can define a logger in every transaction i wrote a custom function where you need to define your logger just once. It also executes the `SchemaUtils` call to initialize the tables (I'm still not sure if it is needed every time, it's seems weird to have to use it in every transaction but without it, it doesn't work currently).

<pre>
fun < T, K : Table> loggedSchemaUtilsTransaction(  
    db: Database? = null,  
  vararg tables: K,  
  statement: Transaction.() -> T  
): T =  
  transaction(db) {  
  SchemaUtils.create(*tables)  
        addLogger(stdLogger)  
        statement()  
    }
</pre>

Inside your calls you execute your `SQL` statements.

Every Database Access in this App is found in the `dbAccessor.kt` File which Implements the `DatabaseInterface`

CRUD stuff works as follows:

<pre>
loggedSchemaUtilsTransaction(db, myTable) {
// Create
    val id = myTable.insertAndGetId {
            it[name] = Bratheringe
            it[age] = 42
            it[date] = LocalDateTime.now()
        }
// Read
    myTable.select {myTable.age eq 24}
// Update
    myTable.update ({myTable.age eq 42}) {
        it[myTable.name] = Bismarkheringe
    }
    myTable.update ({myTable.date eq DATE}) {
        with(SqlExpressionBuilder) {
            it[myTable.age] = myTable.age + 1
        }
    }
// Delete
    myTable.deleteWhere {myTable.age eq 43}
}
</pre>

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

To use npm modules you might have to write your own wrapper/mapping if you can't find one.

check `react-minimal-pie-chart.kt` or the `fb-handson` Branch (implementation of a Kotlin Handson & SPA example) for examples.

See [Kotlin Playground: Using packages from NPM](https://play.kotlinlang.org/hands-on/Building%20Web%20Applications%20with%20React%20and%20Kotlin%20JS/07_Using_Packages_From_NPM) for more Infos.

## Pitfalls during Development

### common

#### The Good

* easy to use, you define it here, it's availabile everywhere.

#### The Bad

* Actual/expect methods are limited in functionality or are not straight forward and  need heavy abstraction.

### js

#### The Good

* Kotlin-wrappers so you can use kotlin dsl everywhere
* CSS aka styled Components are easy to use, problem is again writing css in Kotlin DSL (ComponentStyles.kt).

#### The Bad

* React to Kotlin DSL has almost no Documentation and just a few Examples which just show some functionalities. There is no whole React to Kotlin DSL Documentation and you need to know React to know what to do.  Auswikrungen, transitiv Leistung nötig ohne Doku

* Some kotlin-wrappers are heavily out of date

* Some React functionalities need a more complicated approach (e.g. getDerivedStateFromProps)

* Error Handling with Error Boundaries from React. They do not catch async Errors. This is adressed in a experimental Feature in React. So every Component needs to do their own Error Handling.

* Sometimes you need to write your own wrapper for npm packages

* Communicating between a browser & server was rather complex. (Propably caused due to inexperience)

* sometimes acces to properties is direct and sometimes you need to use attrs (confusing).

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
* Using the structure with vals as in this project results in IDEA complaining about unused variables.
* No complete MPP Example with App, js and jvm

### Related Github Issues

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

## Further Reading

+ Kotlinx Flow (https://kotlinlang.org/docs/reference/coroutines/flow.html )
+ Kodein (https://github.com/Kodein-Framework/Kodein-DI )
+ Kotlin Redux (https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react-redux )
+ Kotest (https://github.com/kotest/kotest )
+ Klock (https://github.com/korlibs/klock )

### Articles

+ https://www.youtube.com/watch?v=JnmHqKLgYY4
+ https://play.kotlinlang.org/hands-on/Building%20Web%20Applications%20with%20React%20and%20Kotlin%20JS/01_Introduction
+ https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html

### Mobile App

See branch `fb-android` for a very simple "Hello World" App. This App however is not able to use common code (yet). There seems to be a problem with the project structure as an [official Multiplatform Sample]([GitHub - Kotlin/mpp-example: Example of Kotlin multiplatform project](https://github.com/Kotlin/mpp-example)) & an [Kotlin MPP Example]([GitHub - AAkira/mpp-example: This project is a minimum example of Kotlin Multiplatform Project.](https://github.com/AAkira/mpp-example)) use a different project structure. Resolving this structure was tried in Branch `fb-restructure` but it is unfinished and surprisingly complicated for me.
Also check out the official [handson](https://play.kotlinlang.org/hands-on/Targeting%20iOS%20and%20Android%20with%20Kotlin%20Multiplatform/01_Introduction).

### Used Resources

a list of talks/projects/blogposts used as reference in this project.

+ KtorTalks
  
     https://www.youtube.com/watch?v=V4PS3IjIzlw
     https://www.youtube.com/watch?v=SOPEc8JnFl4
     https://www.youtube.com/watch?v=DGquaQs-Lh0

+ Ktor Samples
  
      https://github.com/ktorio/ktor-samples

+ Awesome-Kotlin
  
      https://github.com/KotlinBy/awesome-kotlin

+ JWT
  
      https://ktor.io/quickstart/guides/api.html#jwt-authentication
      https://ktor.io/servers/features/authentication/jwt.html
      https://www.scottbrady91.com/Kotlin/JSON-Web-Token-Verification-in-Ktor-using-Kotlin-and-Java-JWT
      https://github.com/AndreasVolkmann/ktor-auth-jwt-sample

+ Kotlin/MPP
  
      jsRun not working:
      https://github.com/kotlin-hands-on/intro-kotlin-mutliplatform/issues/2
      complete Project:
      https://github.com/Kotlin/kotlin-full-stack-application-demo
      handson:
      https://play.kotlinlang.org/hands-on/Full%20Stack%20Web%20App%20with%20Kotlin%20Multiplatform/01_Introduction

+ H2-DBS/exposed
  
      https://svlada.com/jwt-token-authentication-with-spring-boot/
      https://dzone.com/articles/working-with-embedded-java-databases-h2-amp-intell
      https://blog.jdriven.com/2019/07/kotlin-exposed-a-lightweight-sql-library/
      https://www.baeldung.com/kotlin-exposed-persistence
      https://github.com/toefel18/kotlin-exposed-blog

+ KotlinConf APP
  
      https://github.com/JetBrains/kotlinconf-app

+ Unit Testing in Kotlin
  
      https://www.youtube.com/watch?v=RX_g65J14H0

+ React Routing/SPA
  
      https://www.kirupa.com/react/creating_single_page_app_react_using_react_router.htm

+ KotlinJS
  
      https://www.raywenderlich.com/201669-web-app-with-kotlin-js-getting-started

+ lifecycle hook React
  
      https://reactjs.org/docs/state-and-lifecycle.html
      https://programmingwithmosh.com/javascript/react-lifecycle-methods/

+ Kotlin&React vs JS&React
  
      https://sudonull.com/post/13208
      https://pastebin.com/dyJgZCqU
      https://reactjs.org/blog/2018/06/07/you-probably-dont-need-derived-state.html
      https://reactjs.org/blog/2018/03/27/update-on-async-rendering.html

+ fetching Data in React
  
      https://www.robinwieruch.de/react-hooks-fetch-data

+ dynamic Lists
  
      https://itnext.io/how-to-build-a-dynamic-controlled-form-with-react-hooks-2019-b39840f75c4f
      http://jasonjl.me/blog/2015/04/18/rendering-list-of-elements-in-react-with-jsx/

+ complete example
  
      https://github.com/gothinkster/realworld

+ React Auth
  
      https://jasonwatmore.com/post/2018/09/11/react-basic-http-authentication-tutorial-example
      https://github.com/strapi/strapi-examples/tree/master/login-react
      https://programmingwithmosh.com/react/localstorage-react/

+ React Tut in Kotlin
  
      https://github.com/rivasdiaz/react-tutorial-kotlin

+ React Error Boundaries
  
      https://medium.com/trabe/catching-asynchronous-errors-in-react-using-error-boundaries-5e8a5fd7b971 // async code
      https://codepen.io/gaearon/pen/wqvxGa?editors=0010

+ React router
  
      https://stackoverflow.com/questions/45089386/what-is-the-best-way-to-redirect-a-page-using-react-router
      https://scotch.io/tutorials/routing-react-apps-the-complete-guide 
          (from 2016 not uptodate eg IndexRoute https://stackoverflow.com/questions/42748727/using-react-indexroute-in-react-router-v4)
      https://ui.dev/react-router-v4-nested-routes/

+ CORS
  
      https://avalanche123.com/blog/2011/10/10/cross-domain-javascript-lessons-learne

## Based on

[Wolfgang Wipp/prod-dash](http://gitlab.innosystec.site/wowipp/prod-dash)
