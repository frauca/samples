package frauca.hazelcast;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CommandController {

    Map<String,String> map = new HashMap();

    @RequestMapping("/put")
    public CommandResponse put(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
        String oldValue = map.put(key, value);
        return new CommandResponse(oldValue);
    }

    @RequestMapping("/get")
    public CommandResponse get(@RequestParam(value = "key") String key) {
        String value = map.get(key);
        return new CommandResponse(value);
    }

    @RequestMapping("/remove")
    public CommandResponse remove(@RequestParam(value = "key") String key) {
        String value = map.remove(key);
        return new CommandResponse(value);
    }

    @RequestMapping("/size")
    public CommandResponse size() {
        int size = map.size();
        return new CommandResponse(Integer.toString(size));
    }

    @RequestMapping("/info")
    public CommandResponse info() {
        return new CommandResponse("Kubernetes without hazelcast");
    }

    @RequestMapping("/populate")
    public CommandResponse populate() {
        for (int i = 0; i < 1000; i++) {
            String s = Integer.toString(i);
            map.put(s, s);
        }
        return new CommandResponse("1000 entry inserted to the map");
    }

    @RequestMapping("/clear")
    public CommandResponse clear() {
        map.clear();
        return new CommandResponse("Map cleared");
    }
}
