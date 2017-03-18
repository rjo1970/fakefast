package fakefast

import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine

class Reader {
    private Endpoint endpoint
    VelocityEngine ve = new VelocityEngine()
    VelocityContext context = new VelocityContext()
    Properties properties = new Properties()
    boolean exists

    public Reader(Endpoint endpoint) {
        this.endpoint = endpoint
        properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute")
        properties.setProperty("file.resource.loader.path", new File("src/main/resources").absolutePath)
        ve.init(properties)
        context.put("NAME", endpoint.name)
        context.put("OAUTH", "${endpoint.name}OAuth")
        context.put("SERVICE", endpoint.service)
        File file = new Users(name: endpoint.name, service: endpoint.service).toFile()
        exists = file.exists()
    }

    public boolean doesExist() {
        exists
    }

    public String text() {
        if (exists) {
            StringWriter writer = new StringWriter();
            Template template = ve.getTemplate("${endpoint.name}/${endpoint.service}.vm")
            template.merge(context, writer)
            return writer.toString()
        }
        ""
    }
}
