package fakefast.services.user

import fakefast.AuthorizationHeader
import fakefast.Endpoint

new Endpoint(service: "login",
        url: "/user/login",
        method: "POST",
        authorizationHeader: new AuthorizationHeader(
                type: "Basic",
                password: "password")).makeAll()

