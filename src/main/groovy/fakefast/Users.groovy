package fakefast

import groovy.io.FileType
import groovy.json.JsonSlurper

class Users {
    def name
    def service

    public static find() {
        def baseDir = new File("src/main/resources")
        def users = []
        baseDir.eachFileRecurse( FileType.DIRECTORIES, { dir -> users << dir.toString() })

        users.collect { x -> x.split('/').last() }
    }

    def toFilename() {
        "src/main/resources/${name}/${service}.vm"
    }

    def toFile() {
        new File(toFilename())
    }

    def readServices() {
        def stuff = new File("src/main/resources/${name}/services.json").text
        def body = new JsonSlurper().parseText(stuff)
        def section = body[service]
        if (section == null) {
            return [resultCode: 200]
        }
        section
    }
}
