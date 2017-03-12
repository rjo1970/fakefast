package fakefast

import org.mockserver.model.Header

class AuthorizationHeader {
    def type
    def name
    def password
    def token

    def value() {
        switch(type) {
            case "Basic":
                return "Basic ${passwordCode()}"
            case "Bearer":
                return "Bearer ${token}"
        }
    }

    def passwordCode() {
       "${name}:${password}".bytes.encodeBase64().toString()
    }

    def header() {
        new Header("Authorization", value())
    }
}
