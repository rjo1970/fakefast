package fakefast

import groovyx.net.http.HTTPBuilder
import spock.lang.Specification


class LogoutSpec extends Specification {
    def "Logout works for John with a Bearer certificate"() {
        HTTPBuilder http = new HTTPBuilder("http://localhost:${Endpoint.port()}")
        def text
        def statusCode
        http.setHeaders([Authorization: new AuthorizationHeader(
                name: 'John',
                type: 'Bearer').value()])
        when:
        http.post(path: "/user/logout") { resp, reader ->
            text =  reader.text
            statusCode =  resp.statusLine.statusCode
        }
        then:
        text =~ /goodbye: John/
        statusCode == 200

    }
}
