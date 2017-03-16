package fakefast

class Reporter {
    public static Set<String> endpoints = new LinkedHashSet<>()

    public static addEndpoint(endpoint) {
        endpoints << generateEndpointReport(endpoint)
    }

    private static String generateEndpointReport(Endpoint endpoint) {
        if (endpoint.name != Endpoint.UNAUTHORIZED) {
            "${endpoint.url} => ${endpoint.resultCode} (${endpoint.name})"
        } else {
            "${endpoint.url} => ${endpoint.resultCode}"
        }
    }
}
