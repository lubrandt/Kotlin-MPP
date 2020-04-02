# Encountered Problems during Development
* ~~adding something manual to a generated project causes problems, 
manual things are not recognized or found 
(adding a jvmMain with main and application.conf)~~ solved, #kleinesK

* ~~hot-reload doesn't work (--continuous param in run config), 
Error while compiling, reload prevented, ktor Version: 1.3.2~~
    * ~~Can't resolve 'abort-controller' in ktor-client-core~~
    * ~~Can't resolve 'text-encoding' in ktor-io~~ 
    * [GitHub issue](https://github.com/ktorio/ktor/issues/961)
    * solved by npm dependency of both but text-encoding is deprecated

* Jodatime/java-time (used in exposed) vs klock (internal Int & Double)
    * time is currently without timezone