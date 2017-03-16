package fakefast

import spock.lang.*

public class EndpointSpec extends Specification {


    def "An Endpoint has a service, url, method, resultCode"() {
        def result
        when:
        result = new Endpoint(service: "foo", url: "/foo")
        then:
        result.service == 'foo'
        result.url == '/foo'
        result.method == "POST"
        result.resultCode == 200
    }

    def "An Endpoint can take an authorization header"() {
        def result
        when:
        result = new Endpoint(service: "foo", url: "/foo", authorizationHeader: new AuthorizationHeader(type: "Basic", name: "pants", password: "password"))
        then:
        result.authorizationHeader.value() == "Basic cGFudHM6cGFzc3dvcmQ="
    }

    def "A URL can be given parameters, which will be broken into a parameter list and a base path"() {
        def result
        given:
        result = new Endpoint(service: "foo", url: "/foo?a=1&b=two")
        when:
        result.make()
        then:
        result.path == "/foo"
        result.pathArguments == [a: ["1"], b: ["two"]]
    }

}
