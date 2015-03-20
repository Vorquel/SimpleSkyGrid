package vorquel.mod.simpleskygrid.config;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigDataMap<Entry, Quantity> {

    private HashMap<String, ArrayList<Entry>> entries = new HashMap<>();
    private HashMap<String, ArrayList<Quantity>> quantities = new HashMap<>();

    public void put(String label, Entry prototype, Quantity weight) {
        if(!entries.containsKey(label)) {
            entries.put(label, new ArrayList<Entry>());
            quantities.put(label, new ArrayList<Quantity>());
        }
        entries.get(label).add(prototype);
        quantities.get(label).add(weight);
    }

    public int size() {
        return entries.size();
    }

    public int size(String label) {
        if(entries.containsKey(label))
            return entries.get(label).size();
        else
            return 0;
    }

    public Entry getEntry(String label, int i) {
        return entries.get(label).get(i);
    }

    public Quantity getQuantity(String label, int i) {
        return quantities.get(label).get(i);
    }
}
