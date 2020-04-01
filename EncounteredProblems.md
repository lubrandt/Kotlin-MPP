# Encountered Problems during Development
* ~~adding something manual to a generated project causes problems, 
manual things are not recognized or found 
(adding a jvmMain with main and application.conf)~~ solved, #kleinesK

* hot-reload doesn't work (--continuous param in run config), 
Error while compiling, reload prevented, ktor Version: 1.3.2
    * Module not found: Can't resolve 'abort-controller' in ktor-client-core
    * Module not found: Can't resolve 'text-encoding' in ktor-io

