package fakefast

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import spock.lang.*

class LoginSpec extends Specification {

    def "login works for John"() {
        HTTPBuilder http = new HTTPBuilder('http://localhost:8181')
        def text
        def resultCode
        http.setHeaders([Authorization: new AuthorizationHeader(
                type: 'Basic',
                name: 'John',
                password: 'password').value()])
        when:
        http.post(path: "/user/login") { resp, reader ->
            text =  reader.text
            resultCode =  resp.statusLine.statusCode
        }
        then:
        text =~ /name: John/
        resultCode == 200
    }


    def "login fails for an Unauthorized attempt"() {
        HTTPBuilder http = new HTTPBuilder('http://localhost:8181')
        def text
        def resultCode
        when:
        http.post(path: "/user/login", ) { resp, reader ->
            text =  reader.text
            resultCode =  resp.statusLine.statusCode
        }
        then:
        thrown HttpResponseException
    }
}