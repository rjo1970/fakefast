package fakefast.services

import fakefast.Endpoint

new Endpoint(service: "foo", url: '/foo?a=1&b=two', method: "GET").makeAll()
