package fakefast.services.user

import fakefast.Endpoint

new Endpoint(service: "login",
        url: "/user/login",
        method: "POST",
        authType: "Basic").makeAll()

