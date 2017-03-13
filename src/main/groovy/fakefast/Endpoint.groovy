package fakefast

import org.mockserver.client.server.MockServerClient
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class Endpoint {

    def service
    def url
    def method = "POST"
    def resultCode = 200
    AuthorizationHeader authorizationHeader
    String name

    def makeAll() {
        Users.find().each { name ->
            Endpoint endpoint = new Endpoint()
            copyProperties(this, endpoint)
            endpoint.name = name
            if (name == 'unauthorized') {
                endpoint.authorizationHeader = null
            } else {
                if (endpoint.authorizationHeader) {
                    endpoint.authorizationHeader.name = name
                }
            }
            endpoint.make()
        }
    }

    private copyProperties(source, target) {
        source.properties.each { key, value ->
            if (target.hasProperty(key) && !(key in ['class', 'metaClass']))
                target[key] = value
        }
    }

    def make() {
        Reader reader = new Reader(this)
        if (!reader.doesExist()) {
            return
        }

        def config = new Users(name: name, service: service).readServices()
        resultCode = config["resultCode"]

        def req = request()
                .withMethod(method)
                .withPath(url)

        if (authorizationHeader) {
            req = req.withHeader(authorizationHeader.header())
        }

        def res = response()
                .withStatusCode(resultCode)
                .withBody(reader.text())

        build().when(
            req
        )
        .respond(
            res
        )
    }

    private MockServerClient build() {
        new MockServerClient("127.0.0.1", port())
    }

    private port() {
        try {
            def p = System.getEnv("serverPort")
            return Integer.parseInt(p)
        } catch(Exception e) {
            return 8181
        }
    }

    public reset() {
        build().reset()
    }
}
