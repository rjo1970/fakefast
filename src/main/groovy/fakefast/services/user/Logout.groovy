package fakefast.services.user

import fakefast.Endpoint

new Endpoint(service: "logout",
        url: "/user/logout",
        method: "POST",
        authType: "Bearer").makeAll()
