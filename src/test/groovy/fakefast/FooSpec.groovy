package fakefast

import groovyx.net.http.HTTPBuilder
import spock.lang.Specification


class FooSpec extends Specification {
    def "/foo endpoint accepts two parameters"() {
        HTTPBuilder http = new HTTPBuilder("http://localhost:${Endpoint.port()}")
        def text
        def statusCode
        when:
        http.get(path: '/foo', query: [a: "1", b: "two"]) { resp, reader ->
            text =  reader.text
            statusCode =  resp.statusLine.statusCode
        }
        then:
        text =~ /Some foo goes here./
        statusCode == 200

    }
}
