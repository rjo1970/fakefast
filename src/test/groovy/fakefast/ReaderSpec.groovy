package fakefast

import spock.lang.Specification


class ReaderSpec extends Specification {
    def "can read a file given a user and a service"() {
        def result
        def endpoint = new Endpoint(service: "login", name: "George")
        when:
        result = new Reader(endpoint).text()
        then:
        result =~ /name: George/
    }

    def "does not freak out when a file cannot be found"() {
        def result
        def endpoint = new Endpoint(service: "notAThing", name: "NobodyHere")
        when:
        result = new Reader(endpoint)
        then:
        result.doesExist() == false
        result.text() == ""
    }

    def "interpolates NAME, OAUTH, and SERVICE tags in vm scripts"() {
        def endpoint = new Endpoint(service: "login", name: "John")
        def result
        when:
        result = new Reader(endpoint).text()
        then:
        result.contains("name: \"John\"")
        result.contains("token: \"JohnOAuth\"")
        result.contains("service: \"login\"")
    }
}