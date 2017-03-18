package fakefast

class Reporter {
    public static Set<String> endpoints = new LinkedHashSet<>()

    public static addEndpoint(endpoint) {
        endpoints << generateEndpointReport(endpoint)
    }

    private static String generateEndpointReport(Endpoint endpoint) {
        if (endpoint.name != Endpoint.UNAUTHORIZED) {
            "${endpoint.url} => ${endpoint.resultCode} (${endpoint.name})\n${curlExample(endpoint)}"
        } else {
            "${endpoint.url} => ${endpoint.resultCode}\n${curlExample(endpoint)}"
        }
    }

    static String curlExample(Endpoint endpoint) {
        def header = endpoint.authHeader()
        def headerParam = (header.length() > 0) ? "-H \"${header}\"" : ""

        "curl -X ${endpoint.method} ${headerParam} \"http://${endpoint.hostname()}:${endpoint.port()}${endpoint.url}\""
    }
}
