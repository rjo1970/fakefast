package fakefast

import groovy.io.FileType

new Endpoint().reset()

def baseDir = new File("src/main/groovy/fakefast/services")
baseDir.eachFileRecurse( FileType.FILES, { file -> evaluate file })
