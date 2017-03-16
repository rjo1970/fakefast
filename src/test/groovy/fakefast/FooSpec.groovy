package fakefast

import groovyx.net.http.HTTPBuilder
import spock.lang.Specification


class FooSpec extends Specification {
    def "/foo endpoint accepts two parameters"() {
        HTTPBuilder http = new HTTPBuilder('http://localhost:8181')
        def text
        def resultCode
        when:
        http.get(path: '/foo', query: [a: "1", b: "two"]) { resp, reader ->
            text =  reader.text
            resultCode =  resp.statusLine.statusCode
        }
        then:
        text =~ /Some foo goes here./
        resultCode == 200

    }
}