package fakefast

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import spock.lang.*

class LoginSpec extends Specification {

    def "login works for John"() {
        HTTPBuilder http = new HTTPBuilder("http://localhost:${Endpoint.port()}")
        def text
        def statusCode
        http.setHeaders([Authorization: new AuthorizationHeader(
                type: 'Basic',
                name: 'John',
                password: 'password').value()])
        when:
        http.post(path: "/user/login") { resp, reader ->
            text =  reader.text
            statusCode =  resp.statusLine.statusCode
        }
        then:
        text =~ /name: \"John\"/
        statusCode == 200
    }


    def "login fails for an Unauthorized attempt"() {
        HTTPBuilder http = new HTTPBuilder("http://localhost:${Endpoint.port()}")
        def text
        def statusCode
        when:
        http.post(path: "/user/login", ) { resp, reader ->
            text =  reader.text
            statusCode =  resp.statusLine.statusCode
        }
        then:
        thrown HttpResponseException
    }
}
