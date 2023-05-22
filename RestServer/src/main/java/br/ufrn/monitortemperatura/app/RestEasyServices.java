package br.ufrn.monitortemperatura.app;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.ufrn.monitortemperatura.restserver.CairoRestService;
import br.ufrn.monitortemperatura.restserver.LondresRestService;
import br.ufrn.monitortemperatura.restserver.NatalRestService;
import br.ufrn.monitortemperatura.restserver.NovaIorqueRestService;
import br.ufrn.monitortemperatura.restserver.OsloRestService;

@ApplicationPath("/monitortemperatura")
public class RestEasyServices extends Application {

    private Set < Object > singletons = new HashSet < Object > ();

    public RestEasyServices() {
        singletons.add(new NatalRestService());
        singletons.add(new LondresRestService());
        singletons.add(new NovaIorqueRestService());
        singletons.add(new CairoRestService());
        singletons.add(new OsloRestService());
    }

    @Override
    public Set < Object > getSingletons() {
        return singletons;
    }
}