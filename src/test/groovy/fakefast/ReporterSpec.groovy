package fakefast

import spock.lang.*


class ReporterSpec extends Specification {

    def "reporter remembers the created endpoint for an unauthorized user"() {
        def endpoint
        given:
        Reporter.endpoints = new LinkedHashSet<>()
        endpoint = new Endpoint(url: "/bar", service: "bar", method: "GET")
        when:
        Reporter.addEndpoint(endpoint)
        then:
        Reporter.endpoints.size() == 1
        Reporter.endpoints[0] == "/bar => 200"
    }

    def "reporter remembers the created endpoint for an authorized user"() {
        def endpoint
        given:
        Reporter.endpoints = new LinkedHashSet<>()
        endpoint = new Endpoint(url: "/bar", service: "bar", method: "GET", name: "Joe")
        when:
        Reporter.addEndpoint(endpoint)
        then:
        Reporter.endpoints.size() == 1
        Reporter.endpoints[0] == "/bar => 200 (Joe)"
    }

}