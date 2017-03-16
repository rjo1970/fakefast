package fakefast

import org.mockserver.client.server.MockServerClient
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class Endpoint {

    public static final String UNAUTHORIZED = "unauthorized"
    def service
    def url
    def path
    def pathArguments
    def method = "POST"
    def resultCode = 200
    AuthorizationHeader authorizationHeader
    String name = UNAUTHORIZED

    def makeAll() {
        Users.find().each { name ->
            Endpoint endpoint = new Endpoint()
            copyProperties(this, endpoint)
            endpoint.name = name
            if (name == UNAUTHORIZED) {
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

        splitUrl()
        resultCode = findResultCode()
        createEndpoint(resultCode, reader)
        Reporter.addEndpoint(this)
    }

    private void createEndpoint(resultCode, Reader reader) {
        def req = request()
                .withMethod(method)
                .withPath(path)

        if (pathArguments.size() > 0) {
            req = req.withQueryStringParameters(pathArguments)
        }

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

    private Object findResultCode() {
        def config = new Users(name: name, service: service).readServices()
        resultCode = config["resultCode"]
        resultCode
    }

    private splitUrl() {
        def split = url.tokenize("?")
        path = split[0]

        if (split[1]) {
            def argpairs = split[1].tokenize("&")
            def argsList = argpairs.collect({ x -> x.tokenize("=") })
            def entries = argsList.collect{ k, v -> [ (k) : [v] ] }
            pathArguments = entries.inject([:]) { m, e -> m + e }
        } else {
            pathArguments = [:]
        }

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
