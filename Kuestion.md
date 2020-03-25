# Kuestion (Ein Kotlin full-stack Umfragesystem)

## Einführung

In diesem Projekt soll ein Online Umfragesystem entwickelt werden. Es soll folgende Grundfunktionalitäten
bieten:

- Erzeugen und Löschen von Umfragen
- Beantworten von Umfragen unter Angabe eines Namens

Dieses Projekt dient als Reifegradmesser für Kotlin sowohl als Server-, aber vor allem als Browsersprache.
Das Ziel ist es, jedes denkbare Szenario (z.B. Web-Applikation, Applikation-Server, Datenbankzugriff...)
mit Kotlin zu bewältigen.

Folgende Technologien/Libraries sollten hierbei benutzt werden (Punkte in Klammern sind optional/nice to have):

### UI
+ KotlinJS
+ Kotlin React (https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react)
+ kotlinx html (https://github.com/Kotlin/kotlinx.html)
+ styled components (https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-styled)
+ Kotlin Redux (https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react-redux)
+ Kotlinx Coroutines (https://github.com/Kotlin/kotlinx.coroutines)
+ Kotlinx Serialization (https://github.com/Kotlin/kotlinx.serialization)
+ (Kotlinx Flow) (https://kotlinlang.org/docs/reference/coroutines/flow.html)
+ (Kodein) (https://github.com/Kodein-Framework/Kodein-DI)

### Server
+ KotlinJVM
+ KTor (https://github.com/ktorio/ktor)
+ Exposed (https://github.com/JetBrains/Exposed)
+ Kotlinx Serialization (s.o.)
+ (Kotlinx Coroutines) (s.o.)
+ (Kotlinx Flow) (s.o.)
+ (Kodein) (s.o.)

Oben werden manche Libraries sowohl unter UI als auch Server aufgelistet. Kotlin kann Code zwischen verschiedenen Platformzielen teilen solange im teilbaren Code nur Kotlinspezifika verwendet werden (Kotlin Multiplatform). Der Reifegrad dieser Technik soll auch geprüft werden. Deshalb soll das Projekt als Multiplatformprojekt erstellt werden.

Es soll festgestellt werden, wieviel Businesslogik und Datenmodell man hier als SharedCode entwickeln kann/sollte.


### Lesenswertes/Anschauenswertes Material
+ https://www.youtube.com/watch?v=JnmHqKLgYY4
+ https://play.kotlinlang.org/hands-on/Building%20Web%20Applications%20with%20React%20and%20Kotlin%20JS/01_Introduction
+ https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html


## Systemfunktionalität
Das Umfragesystem darf gerne einfach gestrickt sein, der Fokus soll auf den verwendeten Technologien liegen.
Wie schon erwähnt soll es Nutzern möglich sein Umfragen zu erstellen und diese auch wieder zu löschen.
Eine Umfrage besteht hierbei aus einem Fragetext sowie mehreren Antwortmöglichkeiten.
Weitere Nutzer können dann auf diesen Antwortmöglichkeiten abstimmen. 
Nachdem ein Nutzer abgestimmt hat soll ihm die Verteilung der abgegebenen Stimmen angezeigt werden.
Eine Umfrage endet entweder nach einem vom Ersteller vordefinierten Zeitpunkt oder kann manuell vom Ersteller beendet werden. Den Nutzern muss angezeigt werden, wann die Abstimmung endet.
Ist eine Umfrage beendet soll die Umfrage unbearbeiteitbar aber immernoch sichtbar sein.

## Bonuspoints
Wenn Zeit bleibt: Eventuell noch als Android/iOS App?^^
