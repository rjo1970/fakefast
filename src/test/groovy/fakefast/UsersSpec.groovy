package fakefast

import spock.lang.Specification


class UsersSpec extends Specification {
    def "Users can be found"() {
        def result
        when:
        result = Users.find()
        then:
        result.containsAll(['George', 'John', 'Paul', 'Ringo'])
    }

    def "can make a filename"() {
        def result
        when:
        result = new Users(name: "George", service: "login").toFilename()
        then:
        result == "src/main/resources/George/login.vm"
    }

    def "can grab a file handle for a file that exists"() {
        def result
        when:
        result = new Users(name: "George", service: "login").toFile()
        then:
        result.exists() == true
    }

    def "can grab a file handle for a file that does not exist"() {
        def result
        when:
        result = new Users(name: "Missing", service: "notHere").toFile()
        then:
        result.exists() == false
    }

    def "can read an existing services file"() {
        def result
        when:
        result = new Users(name: "unauthorized", service: "login").readServices()
        then:
        result == [resultCode: 401]
    }
}