package fakefast

import groovyx.net.http.HTTPBuilder
import spock.lang.Specification


class LogoutSpec extends Specification {
    def "Logout works for John with a Bearer certificate"() {
        HTTPBuilder http = new HTTPBuilder('http://localhost:8181')
        def text
        def resultCode
        http.setHeaders([Authorization: new AuthorizationHeader(
                name: 'John',
                type: 'Bearer').value()])
        when:
        http.post(path: "/user/logout") { resp, reader ->
            text =  reader.text
            resultCode =  resp.statusLine.statusCode
        }
        then:
        text =~ /goodbye: John/
        resultCode == 200

    }
}
