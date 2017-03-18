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
    def body
    AuthorizationHeader authorizationHeader
    String name = UNAUTHORIZED
    String authType = "Basic"
    String password = "password"

    def makeAll() {
        Users.find().each { name ->
            Endpoint endpoint = new Endpoint()
            copyProperties(this, endpoint)
            endpoint.name = name
            endpoint.make()
        }
    }

    private static void copyProperties(source, target) {
        source.properties.each { key, value ->
            if (target.hasProperty(key) && !(key in ['class', 'metaClass']))
                target[key] = value
        }
    }

    def make() {
        configureAuthorization()
        if (findBody()) {
            splitUrl()
            resultCode = findResultCode()
            createEndpoint(resultCode)
            Reporter.addEndpoint(this)
        }
        this
    }

    private boolean findBody() {
        Reader reader
        if (!body) {
            reader = new Reader(this)
            if (reader.doesExist()) {
                body = reader.text()
                return true
            } else {
                return false
            }
        }
        return true
    }

    private void configureAuthorization() {
        if (name == UNAUTHORIZED) {
            authorizationHeader = null
        } else {
            if (authorizationHeader) {
                authorizationHeader.name = name
            } else {
                authorizationHeader = new AuthorizationHeader(name: name, password: password, type: authType)
            }
        }
    }

    private void createEndpoint(resultCode) {
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
                .withBody(body)

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

    def static port() {
        try {
            def p = System.getenv("port") != null ? System.getenv("port") : "8181"
            return Integer.parseInt(p)
        } catch(Exception e) {
            return 8181
        }
    }

    def hostname() {
        InetAddress.getLocalHost().getCanonicalHostName()
    }

    def authHeader() {
        if (authorizationHeader) {
            return "Authorization: ${authorizationHeader.value()}"
        }
        ""
    }

    public reset() {
        build().reset()
    }
}
