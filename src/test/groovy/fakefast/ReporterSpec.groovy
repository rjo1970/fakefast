package fakefast

import spock.lang.*


class ReporterSpec extends Specification {

    def "reporter remembers each created endpoint"() {
        def endpoint
        when:
        new Endpoint(url: "/foo", service: "foo").make()
        then:
        Reporter.endpoints.size() == 1
        Reporter.endpoints[0] =~ /\/foo => 200/
    }

}