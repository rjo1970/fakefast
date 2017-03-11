package fakefast

import spock.lang.*

public class EndpointSpec extends Specification {


    def "An endpoint takes a service, url, resultCode"() {
        def result
        when:
        result = new Endpoint(service: "foo", url: "/foo", resultCode: 302)
        then:
        result.resultCode == 302
    }
}
