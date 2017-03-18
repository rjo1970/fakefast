package fakefast

import org.mockserver.model.Header
import spock.lang.*

class AuthorizationHeaderSpec extends Specification {

  def "a basic AuthorizationHeader consists of a name and a password"() {
      def result
      when:
      result = new AuthorizationHeader(type: "Basic", name: "pants", password: "password")
      then:
      result.value() == "Basic cGFudHM6cGFzc3dvcmQ="
  }

    def "a bearer AuthorizationHeader consists of a provided token"() {
        def result
        when:
        result = new AuthorizationHeader(type: "Bearer", name: "George")
        then:
        result.value() == "Bearer GeorgeOAuth"
    }

    def "can create a full header"() {
        def result
        when:
        result = new AuthorizationHeader(type: "Basic", name: "pants", password: "password").header()
        then:
        result.class == Header.class
        result.name == "Authorization"
        result.values[0] == "Basic cGFudHM6cGFzc3dvcmQ="
    }

}
